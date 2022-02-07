package com.messanger.engine.uc.ipt;

import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.utils.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 내선번호-사용자아이디의 자료를 가지고 있는 클래스.
 * 조직도와 내선정보 조회 프로토콜을 이용하여 load 한다.
 * @author skoh
 *
 */
public class PhoneBook {
	private Log LOG = LogFactory.getLog(PhoneBook.class);

	private Config config;
	//key - inner phone number, value - id
	private Map<String,User> mapBook = new ConcurrentHashMap<String,User>();
	//key - user id, value - P_CD
	private Map<String,String> mapStatus = new ConcurrentHashMap<String,String>();
	private String prefixPhoneNum;
		
	public PhoneBook(Config config) throws IOException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("PhoneBook Constructor");
		}
		this.config = config;
		prefixPhoneNum = config.get("ipt.phonenum.prefix");		
	}	
	
	public void load() throws IOException {
		String userXmlDir = config.get("files.xml.path");
		if(userXmlDir == null) {
			throw new IOException("\"files.xml.path\" isn't set. Check config file.");
		}		
		File fUserXmlDir = new File(userXmlDir);
		if(fUserXmlDir.exists() == false) {
			throw new IOException(userXmlDir+ " not found");
		}
		
		File[] pathes = fUserXmlDir.listFiles(new WoSchemaDirFilter());
		String locale = config.get("locale", "ko_KR");
		
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(User.class);
		ObjectInputStream userIn = null;
		
		User user = null;
		File fUserXml = null;
		String compPhoneNum = null;
//		String mobileNum = null;
		Map<String,User> tempBook = new ConcurrentHashMap<String,User>();
		
		for(int i=0;i<pathes.length;i++) {
			fUserXml = new File(pathes[i], com.messanger.engine.uc.Constants.USER_PREFIX+locale+".xml");
	        userIn = xstream.createObjectInputStream(new FileInputStream(fUserXml));	        
	        try {
	            while(true) {	            	
	            	user = (User)userIn.readObject();	            	
	                compPhoneNum = user.getCompPhoneNumber();
//	                mobileNum = user.getMobileNumber();
	                
	                if("N/A".equals(compPhoneNum) || compPhoneNum == null) {
	                	continue;
	                }
	                
	                compPhoneNum = StringUtil.getReplaceFirst(compPhoneNum, prefixPhoneNum);
	                
//	                if("N/A".equals(mobileNum) || mobileNum == null) {
//	                	continue;
//	                }
//	                if(isXFonNumber(compPhoneNum)) {
//	                	tempBook.put(stripPhoneNumber(compPhoneNum), user.getEmail());
//	                }
	                
//	                if(compPhoneNum.length() == 4) {
//	                	tempBook.put(compPhoneNum, user.getEmail());
//	                }
	                
	                if(compPhoneNum.length() == 4) {
	                	tempBook.put(compPhoneNum, user);
	                }
	                
//	                //원코드
//	                if(compPhoneNum.length() == 4) {
//	                	compPhoneNum = compPhoneNum.replace("-", "");
//	                	tempBook.put(compPhoneNum, user.getEmail());
//	                }
	                
//	                mobileNum = "9" + mobileNum.replace("-", "");
//	                tempBook.put(mobileNum, user.getEmail());
//	                if (compPhoneNum.equals("5564")) {
//	                	System.out.println("zzzzzzzz");
//	                }
	            }
	        } catch (EOFException e) {
	        } catch (ClassNotFoundException e) {				
				LOG.warn("Read user xml fail: "+fUserXml.getName(), e);
			} finally {
	            if (userIn != null) { 
	            	userIn.close();
	            	userIn = null;
	            }
	        }
		}
		mapBook = tempBook;
		
		LOG.info("Phonebook load successfully");
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public String getId(String phoneNumber) {
//		if(phoneNumber.length() != XFonConstants.LEN_INNER_LINE) {
//			return null;
//		}
		User user = mapBook.get(phoneNumber);
		String email = user != null ? user.getEmail() : null;
		return email;
	}
	
	public User getUser(String phoneNumber) {
		User user = mapBook.get(phoneNumber);
		return user;
	}
	
	/**
	 * 
	 * @param peerNo
	 * @param status
	 */
	public void setPEMStatus(String peerNo, byte status) {
		String PCD = cvtPEMStatusToPCD(status);
		if(PCD == null) {
			LOG.warn("Invalid Phone status: "+peerNo+":"+String.format("0x%02X", status));
			return;
		}
		String uid = getId(peerNo);
		if(uid == null) {
			LOG.warn("Phone user not found: "+peerNo+":"+String.format("0x%02X", status));
			return;
		}
		mapStatus.put(uid, PCD);
	}
	
	/**
	 * 
	 * @param peerNo
	 * @param status
	 */
	public void setINFStatus(String peerNo, byte status) {
		String PCD = cvtINFStatusToPCD(status);
		if(PCD == null) {
			LOG.warn("Invalid Phone status: "+peerNo+":"+String.format("0x%02X", status));
			return;
		}
		String uid = getId(peerNo);
		if(uid == null) {
			LOG.warn("Phone user not found: "+peerNo+":"+String.format("0x%02X", status));
			return;
		}
		mapStatus.put(uid, PCD);
	}
	
	/**
	 * 
	 * @param uid
	 * @return
	 */
	public String getStatus(String uid) {
		String PCD = mapStatus.get(uid);
		if(PCD == null) {
			PCD = Constants.PHONE_UNREGISTER;
		}
		return PCD;
	}
	
	
	
	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public String stripPhoneNumber(String phoneNumber) {
		phoneNumber = phoneNumber.replace("-", "");
		if(isIptNumber(phoneNumber)) { 
        	return phoneNumber.substring(phoneNumber.length() - IptConstants.LEN_INNER_LINE);
        }
        return phoneNumber;
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public boolean isIptNumber(String phoneNumber) {
		phoneNumber = phoneNumber.replace("-", "");
        if(phoneNumber.startsWith(prefixPhoneNum)) { 
        	return true;
        }
        return false;
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public static String cvtPEMStatusToPCD(byte status) {
		String PCD = null;
		switch(status) {
		case IptConstants.EVT_STATUS_RING:
			PCD = Constants.PHONE_RING;
			break;
		case IptConstants.EVT_STATUS_CALL:
			PCD = Constants.PHONE_CALL;
			break;
		case IptConstants.EVT_STATUS_HANGUP:
			PCD = Constants.PHONE_HANGUP;
			break;
		case IptConstants.EVT_STATUS_REGISTER:
			PCD = Constants.PHONE_REGISTER;
			break;
		case IptConstants.EVT_STATUS_UNREGISTER:
			PCD = Constants.PHONE_UNREGISTER;
			break;		
		case IptConstants.EVT_STATUS_NOREADY:
			PCD = Constants.PHONE_NOREADY;
			break;
		case IptConstants.EVT_STATUS_READY:
			PCD = Constants.PHONE_READY;
			break;
		case IptConstants.EVT_STATUS_MISSCALL:
			PCD = Constants.PHONE_MISSCALL;
			break;
		case IptConstants.EVT_STATUS_ONCALL:
			PCD = Constants.PHONE_ONCALL;
			break;
		case IptConstants.EVT_STATUS_FAIL:
			PCD = Constants.PHONE_FAIL;
			break;
		}
		return PCD;
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public static String cvtINFStatusToPCD(byte status) {
		String PCD = null;
		switch(status) {
		case IptConstants.IN_STATUS_DISCONNECT:
			PCD = Constants.PHONE_UNREGISTER;			
			break;
		case IptConstants.IN_STATUS_CONNECT:
			PCD = Constants.PHONE_REGISTER;			
			break;
		case IptConstants.IN_STATUS_SENDING:
		case IptConstants.IN_STATUS_RECEIVING:
			PCD = Constants.PHONE_RING;
			break;
		case IptConstants.IN_STATUS_CALLING:
			PCD = Constants.PHONE_CALL;
			break;		
		}
		return PCD;
	}
	
	/**
	 * 
	 */
	public void printBook() {
		Iterator<Entry<String,User>> iter = mapBook.entrySet().iterator();
		Entry<String,User> entry = null;
		LOG.info("=====PRINT BOOK======================");
		while(iter.hasNext()) {
			entry = iter.next();
			LOG.info(entry.getKey()+":"+entry.getValue());
		}
		LOG.info("=====PRINT BOOK======================");
	}
	
	/**
	 * 
	 */
	public void printStatus() {
		Iterator<Entry<String,String>> iter = mapStatus.entrySet().iterator();
		Entry<String,String> entry = null;
		LOG.info("=====PRINT STATUS======================");
		while(iter.hasNext()) {
			entry = iter.next();
			LOG.info(entry.getKey()+":"+entry.getValue());
		}
		LOG.info("=====PRINT STATUS======================");
	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class WoSchemaDirFilter implements FileFilter {
		
		private final String prefix = "wo";
		private final int    length = 7;

		@Override
		public boolean accept(File file) {
			if(file.isDirectory() == false) {
				return false;
			}
			String filename = file.getName();			
			if(filename.length() == length && filename.startsWith(prefix)) {
				return true;
			}
			return false;
		}
	}
}
