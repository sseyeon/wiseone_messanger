package com.messanger.engine.uc.ipt.vendor.cisco;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;

import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.ipt.PhoneBook;
import com.messanger.engine.uc.ipt.exception.IllegalIptProtocolException;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.utils.StringUtil;

public class CiscoIptService implements IIptService {
	
	protected static final Log LOG = LogFactory.getLog(CiscoIptService.class);
	private PhoneBook phoneBook = null;
	private IoSession ioSession = null;
	private static final String RESULT_FAIL = "fail";
	private static final String RESULT_SUCCESS = "success";
	private Config config = null;
	private String middleIp = "";
	private int middlePort = 0;
	private static int seqNo = 0; 
	
	@Override
	public void init() {
		middleIp = config.get("ipt.middleware.ip");
		middlePort = Integer.parseInt(config.get("ipt.middleware.port", "9001"));
	}

	@Override
	public void sendConnRequest(String hashKey) throws IOException,
			IllegalIptProtocolException {
		// TODO Auto-generated method stub

	}
	
	public void setConfig(Config config) {
		this.config = config;
	}
	
	@Override
	public void sendDialRequest(String peerNo, String callNo)
			throws IOException, IllegalIptProtocolException {
		String result = "";
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("sendDialRequest start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				LOG.debug("############## peerNo = " + peerNo);
			}
			HashMap hmResult = makeCall(middleIp, middlePort, ++seqNo, peerNo, callNo);
			result = (String)hmResult.get("resultCode");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("@@@@@@@@@@@@@@ sendDialRequest result = " + result);
		}
	}

	@Override
	public void sendHangupRequest(String peerNo) throws IOException,
			IllegalIptProtocolException {
		String result = "";
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("sendHangupRequest start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				LOG.debug("############## peerNo = " + peerNo);
			}
			HashMap hmResult = hangupCall(middleIp, middlePort, ++seqNo, peerNo);
			result = (String)hmResult.get("resultCode");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("@@@@@@@@@@@@@@ sendHangupRequest result = " + result);
		}

	}

	@Override
	public void sendForwardRequest(String peerNo, String callNo)
			throws IOException, IllegalIptProtocolException {
		String result = "";
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("sendForwardRequest start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				LOG.debug("############## peerNo = " + peerNo);
			}
			HashMap hmResult = transferCall(middleIp, middlePort, ++seqNo, peerNo, callNo);
			result = (String)hmResult.get("resultCode");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("@@@@@@@@@@@@@@ sendForwardRequest result = " + result);
		}

	}
	
	@Override
	public boolean isIpt(String phoneNumber, String prefix) {
		boolean ret = true;
		phoneNumber = StringUtil.getReplaceFirst(phoneNumber, prefix);
		try {
			HashMap hmResult = iptState(middleIp, middlePort, ++seqNo, phoneNumber);
//			UcfPhoneInfo phoneInfo = (UcfPhoneInfo)hmResult.get("phoneInfo");
//			String stateCode = String.valueOf(phoneInfo.getPhoneState());
//			if("1201".equals(stateCode)) {
//				ret = false;
//			}
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		} finally {
		}
		if(LOG.isDebugEnabled()) {
			LOG.debug("@@@@@@@@@@@@@@ isIpt ret = " + ret);
		}
		
		return ret;
	}
	
	private HashMap makeCall(String ip, int port, int seqNo, String callerNo, String calleeNo) {
		HashMap hmResult = new HashMap();
		Socket client = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			client = new Socket(ip, port);
			out = client.getOutputStream();

			String writeData = "|CLICK2CALL|" + seqNo + "|" + callerNo + ","
					+ calleeNo;

			int length = writeData.getBytes().length;
			byte[] requestBodyLength = makeIntToBigEndian(length);
			byte[] requestBody = writeData.getBytes();

			out.write(requestBodyLength, 0, requestBodyLength.length);
			out.write(requestBody, 0, requestBody.length);
			out.flush();

			in = client.getInputStream();
			byte[] responseBodyLength = new byte[4];

			int readSize = in.read(responseBodyLength);
			System.out.println("readSize=" + readSize);
			length = readBodyLength(responseBodyLength);
			System.out.println("bodySize=" + length);
			byte[] responseBody = new byte[length];
			readSize = in.read(responseBody);
			System.out.println("readSize=" + readSize);
			System.out.println("read body = " + new String(responseBody));

			StringTokenizer tokenizer = new StringTokenizer(new String(
					responseBody), "|");
			String requestName = tokenizer.nextToken();
			System.out.println("request name=" + requestName);
			String sequenceNumber = tokenizer.nextToken();
			System.out.println("seq =" + sequenceNumber);
			
			String requestResult = tokenizer.nextToken();
			System.out.println("requestResult =" + requestResult);
			hmResult.put("resultCode", requestResult);
			
		} catch (Exception e) {
			hmResult.put("resultCode", RESULT_FAIL);
			e.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e) {}
			if(in != null) try {in.close();} catch(Exception e) {}
			if(client != null) try {client.close();} catch(Exception e) {}
		}
		return hmResult;
	}
	
	private HashMap hangupCall(String ip, int port, int seqNo, String callerNo) {
		HashMap hmResult = new HashMap();
		Socket client = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			client = new Socket(ip, port);
			out = client.getOutputStream();

			String writeData = "|CLICK2HANGUP|" + seqNo + "|" + callerNo;

			int length = writeData.getBytes().length;
			byte[] requestBodyLength = makeIntToBigEndian(length);
			byte[] requestBody = writeData.getBytes();

			out.write(requestBodyLength, 0, requestBodyLength.length);
			out.write(requestBody, 0, requestBody.length);
			out.flush();

			in = client.getInputStream();
			byte[] responseBodyLength = new byte[4];

			int readSize = in.read(responseBodyLength);
			System.out.println("readSize=" + readSize);
			length = readBodyLength(responseBodyLength);
			System.out.println("bodySize=" + length);
			byte[] responseBody = new byte[length];
			readSize = in.read(responseBody);
			System.out.println("readSize=" + readSize);
			System.out.println("read body = " + new String(responseBody));

			StringTokenizer tokenizer = new StringTokenizer(new String(
					responseBody), "|");
			String requestName = tokenizer.nextToken();
			System.out.println("request name=" + requestName);
			String sequenceNumber = tokenizer.nextToken();
			System.out.println("seq =" + sequenceNumber);
			
			String requestResult = tokenizer.nextToken();
			System.out.println("requestResult =" + requestResult);
			hmResult.put("resultCode", requestResult);
			
		} catch (Exception e) {
			hmResult.put("resultCode", RESULT_FAIL);
			e.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e) {}
			if(in != null) try {in.close();} catch(Exception e) {}
			if(client != null) try {client.close();} catch(Exception e) {}
		}
		return hmResult;
	}
	
	private HashMap transferCall(String ip, int port, int seqNo, String callerNo, String calleeNo) {
		HashMap hmResult = new HashMap();
		Socket client = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			client = new Socket(ip, port);
			out = client.getOutputStream();

			String writeData = "|CLICK2TRANSFER|" + seqNo + "|" + callerNo + ","
					+ calleeNo;

			int length = writeData.getBytes().length;
			byte[] requestBodyLength = makeIntToBigEndian(length);
			byte[] requestBody = writeData.getBytes();

			out.write(requestBodyLength, 0, requestBodyLength.length);
			out.write(requestBody, 0, requestBody.length);
			out.flush();

			in = client.getInputStream();
			byte[] responseBodyLength = new byte[4];

			int readSize = in.read(responseBodyLength);
			System.out.println("readSize=" + readSize);
			length = readBodyLength(responseBodyLength);
			System.out.println("bodySize=" + length);
			byte[] responseBody = new byte[length];
			readSize = in.read(responseBody);
			System.out.println("readSize=" + readSize);
			System.out.println("read body = " + new String(responseBody));

			StringTokenizer tokenizer = new StringTokenizer(new String(
					responseBody), "|");
			String requestName = tokenizer.nextToken();
			System.out.println("request name=" + requestName);
			String sequenceNumber = tokenizer.nextToken();
			System.out.println("seq =" + sequenceNumber);
			
			String requestResult = tokenizer.nextToken();
			System.out.println("requestResult =" + requestResult);
			hmResult.put("resultCode", requestResult);
			
		} catch (Exception e) {
			hmResult.put("resultCode", RESULT_FAIL);
			e.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e) {}
			if(in != null) try {in.close();} catch(Exception e) {}
			if(client != null) try {client.close();} catch(Exception e) {}
		}
		return hmResult;
	}
	
	private HashMap iptState(String ip, int port, int seqNo, String callerNo) {
		HashMap hmResult = new HashMap();
		Socket client = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			client = new Socket(ip, port);
			out = client.getOutputStream();

			String writeData = "|IPT_STATE|" + seqNo + "|" + callerNo;

			int length = writeData.getBytes().length;
			byte[] requestBodyLength = makeIntToBigEndian(length);
			byte[] requestBody = writeData.getBytes();

			out.write(requestBodyLength, 0, requestBodyLength.length);
			out.write(requestBody, 0, requestBody.length);
			out.flush();

			in = client.getInputStream();
			byte[] responseBodyLength = new byte[4];

			int readSize = in.read(responseBodyLength);
			System.out.println("readSize=" + readSize);
			length = readBodyLength(responseBodyLength);
			System.out.println("bodySize=" + length);
			byte[] responseBody = new byte[length];
			readSize = in.read(responseBody);
			System.out.println("readSize=" + readSize);
			System.out.println("read body = " + new String(responseBody));

			StringTokenizer tokenizer = new StringTokenizer(new String(responseBody), "|");
			String requestName = tokenizer.nextToken();
			System.out.println("request name=" + requestName);
			String sequenceNumber = tokenizer.nextToken();
			System.out.println("seq =" + sequenceNumber);
			
			String phoneStateInfo = tokenizer.nextToken();
			StringTokenizer tokenizer2 = new StringTokenizer(phoneStateInfo, ",");
			String phoneNo = tokenizer2.nextToken();
			String phoneState = tokenizer2.nextToken();
//			UcfPhoneInfo phoneInfo = new UcfPhoneInfo(seqNo, phoneNo);
//			phoneInfo.setPhoneState(Integer.parseInt(phoneState));
			hmResult.put("resultCode", RESULT_SUCCESS);
//			hmResult.put("phoneInfo", phoneInfo);
			
		} catch (Exception e) {
			hmResult.put("resultCode", RESULT_FAIL);
			e.printStackTrace();
		} finally {
			if(out != null) try {out.close();} catch(Exception e) {}
			if(in != null) try {in.close();} catch(Exception e) {}
			if(client != null) try {client.close();} catch(Exception e) {}
		}
		return hmResult;
	}
	
	private byte[] makeIntToBigEndian(int length) {
		byte[] result = new byte[4];
		result[0] = (byte) ((length >>> 24) & 0xFF);
		result[1] = (byte) ((length >>> 16) & 0xFF);
		result[2] = (byte) ((length >>> 8) & 0xFF);
		result[3] = (byte) ((length >>> 0) & 0xFF);

		return result;
	}

	public static int readBodyLength(byte[] bodyLength) {
		int i0 = 0xFF & bodyLength[0];
		int i1 = 0xFF & bodyLength[1];
		int i2 = 0xFF & bodyLength[2];
		int i3 = 0xFF & bodyLength[3];

		int length = (i0 << 24) + (i1 << 16) + (i2 << 8) + (i3 << 0);

		return length;
	}

	@Override
	public void sendKeepAliveRequest() throws IOException,
			IllegalIptProtocolException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendIngChannelRequest() throws IOException,
			IllegalIptProtocolException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendInnerLineInfoRequest(byte type, String peerNo)
			throws IOException, IllegalIptProtocolException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId(String phoneNo) {
		return phoneBook.getId(phoneNo);
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setConnected(boolean isConnected) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAuth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAuth(boolean isAuth) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPhoneBook(PhoneBook phoneBook) {
		this.phoneBook = phoneBook;

	}

	@Override
	public PhoneBook getPhoneBook() {
		return phoneBook;
	}

	@Override
	public void setSession(IoSession session) {
		this.ioSession = session;

	}

}
