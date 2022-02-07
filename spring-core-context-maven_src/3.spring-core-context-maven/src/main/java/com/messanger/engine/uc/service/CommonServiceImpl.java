package com.messanger.engine.uc.service;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.w3c.dom.Element;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.dao.CommonDaos;
import com.messanger.engine.uc.dao.ICommonDao;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.model.Company;
import com.messanger.engine.uc.model.Dept;
import com.messanger.engine.uc.model.Mail;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.model.WorkFlow;
import com.messanger.engine.uc.utils.DateUtils;
import com.messanger.engine.uc.utils.DigestUtil;
import com.messanger.engine.uc.utils.FileUtils;
import com.messanger.engine.uc.utils.LocalXppDriver;
import com.messanger.engine.uc.utils.PrefixFilenameFilter;
import com.messanger.engine.uc.utils.PushDataUtils;
import com.messanger.engine.uc.utils.SecurityUtils;
import com.messanger.engine.uc.utils.SessionUtils;
import com.messanger.engine.uc.utils.XMLUtils;
import com.thoughtworks.xstream.XStream;

/*
 * ICommonService 구현체. 하위 메소드는 ICommonService 주석 참고
 * @author NEPTUNE
 */
public abstract class CommonServiceImpl implements ICommonService {
	
	protected static final Log LOG = LogFactory.getLog(CommonServiceImpl.class);
	
	protected Config config;    
    
	protected CommonDaos commonDaos;
    
	protected ReloadableResourceBundleMessageSource messageSource;
    
    protected IIptService iptService;
    
    protected Map<String,String> gwDomainMap;
    
    protected String validClientVersion;
    protected int[] iValidClientVersion;
    
    protected Set<String> setXFonDomain;
    protected Set<String> setWConDomain;
    
    /**
     * 도메인과 실제 그룹웨어 주소를 매핑.
     * key : domain
     * value : 그룹웨어 도메인
     */
    protected Map<String, String> realGroupwareDomin;
    
    /**
     * 
     * @param config
     */
    public void setConfig(Config config) {
        this.config = config;
    }

	/**
     * 
     * @param commonDaos
     */
    public void setCommonDaos(CommonDaos commonDaos) {
    	this.commonDaos = commonDaos;
    }
    
    public void setIptService(IIptService iptService) {
    	this.iptService = iptService;
    }
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }

    public void init() {
    	String str = config.get("gw.daobase.urls", "");
    	gwDomainMap = new HashMap<String,String>();
    	String[] rows = str.split("[|]");
    	for(int i=0;i<rows.length;i++) {
    		String[] cols = rows[i].split("[`]");
    		if(cols.length == 2) {
    			gwDomainMap.put(cols[0], cols[1]);
    		}
    	}
    	String[] strArr = null;
        
        validClientVersion = config.get("valid.client.version.min", "2.1.0.1");
        strArr = validClientVersion.split("[\\.]");
        iValidClientVersion = new int [strArr.length];
        for(int i=0;i<strArr.length;i++) {
        	iValidClientVersion[i] = Integer.parseInt(strArr[i]);
        }
        
        String domains = "";
        setXFonDomain = new HashSet<String>();
        domains = config.get("ipt.use.domains", "xxxx.com");
        if(domains != null) {
        	strArr = domains.split("[|]");
        	for(int i=0;i<strArr.length;i++) {
        		setXFonDomain.add(strArr[i].trim().toLowerCase());
        	}
        }
        
        setWConDomain = new HashSet<String>();
        domains = config.get("webconference.use.domains", "xxxx.com");
        if(domains != null) {
        	strArr = domains.split("[|]");
        	for(int i=0;i<strArr.length;i++) {
        		setWConDomain.add(strArr[i].trim().toLowerCase());
        	}
        }
        
        realGroupwareDomin= new HashMap<String, String>();
        
    }

    public void validationQuery() throws Exception {
    	Set<String> setKey = commonDaos.keySet();
        Iterator<String> iterKey = setKey.iterator();       
         
        Set<String> setDoneDao = new HashSet<String>();
        while(iterKey.hasNext()) {
        	String daoName = iterKey.next();
        	if(setDoneDao.add(daoName) == true) {
        		ICommonDao commonDao = commonDaos.get(daoName);
        		commonDao.validationQuery();
        		LOG.info("VALIDATE QUERY: "+daoName);
        	}
        }
    }
    
    public String getConf(String key) {
    	return config.get(key);
    }
    
    @Override
	public String getConf(String key, String defaultValue) {
		return  config.get(key, defaultValue);
	}

	@Override
    public String findScheme(String domainName) throws Exception {
    	String path = config.get("files.xml.path") + "/" + Constants.DOMAIN_PREFIX + ".xml";
        String xpathString = "//list/domain[domainName='" + domainName.trim() + "']/schema/text()";
        String schema = XMLUtils.getString(path, xpathString);
        
        //2010-07-13 mssql 형식은 schema.dbo.table
        //return isMssql() ? schema.trim() + ".dbo" : schema.trim();
        return schema.trim();
    }
    
	@Override
	public String findDaoName(String domainName) throws Exception {
		String path = config.get("files.xml.path") + "/" + Constants.DOMAIN_PREFIX + ".xml";
        String xpathString = "//list/domain[domainName='" + domainName.trim() + "']/daoName/text()";
        String daoName = XMLUtils.getString(path, xpathString);
        
        return daoName == null ? null : daoName.trim();
	}
	
	public String findServiceDomain(String daoName) throws Exception {
		if(gwDomainMap == null) {
			return null;
		}
		return gwDomainMap.get(daoName);
	}
	
	public boolean useReplaceDomain() {
		return (findServiceReplaceDomain() != null && !findServiceReplaceDomain().equals(""));
	}
	
	public String findServiceReplaceDomain(){
		return config.get(Constants.SERVICE_REPLACE_DOMAIN);
	}
	
//    @Override
//    public int findUnreadMailCount(String daoName, String schema, String userid, String domainName, String locale)
//    	throws Exception {
//    	ICommonDao commonDao = commonDaos.get(daoName);
//    	if(commonDao == null) {
//    		throw new Exception("Dao["+daoName+"] not found");
//    	}
//    	return commonDao.selectUnreadMailCount(schema, userid, domainName, locale);
//    }

//    @Override
//	public Map<String, String> getUnreadMail(String daoName, String schema,
//			String userid, String domainName, String locale) throws Exception {
//    	ICommonDao commonDao = commonDaos.get(daoName);
//    	if(commonDao == null) {
//    		throw new Exception("Dao["+daoName+"] not found");
//    	}
//    	String maxList = config.get("gw.list.max", "20");
//    	List<Mail> resultList = commonDao.selectUnreadMail(schema, userid, domainName, locale, maxList);
//    	Map<String,String> resultMap = new HashMap<String,String>();
//    	if(resultList == null || resultList.size() == 0) {
//    		return resultMap;
//    	}
//    	//asp-임시-시작(백업)
////    	MessageFormat url1Form = new MessageFormat(config.get("gw.mail.url1.format", "/servlet/MailServiceSSOController.woext?api=8&ssoFlag=true&userId={0}&userDomain={1}&locale={2}"));
////    	MessageFormat url2Form = new MessageFormat(config.get("gw.mail.url2.format", "&msgId={0}"));
//    	//asp-임시-끝(백업)
//
//    	//asp-임시-시작
//    	MessageFormat url1Form = null;
//    	MessageFormat url2Form = new MessageFormat(config.get("gw.mail.url2.format", "&msgId={0}"));
//    	if("asp".equals(daoName)) {
//    		url1Form = new MessageFormat(config.get("gw.mail.url1.format-asp", "/servlet/MailServiceController.woext?api=8&ssoFlag=true&userId={0}&userDomain={1}&locale={2}"));
//    	}
//    	else {
//    		url1Form = new MessageFormat(config.get("gw.mail.url1.format", "/servlet/MailServiceSSOController.woext?api=8&ssoFlag=true&userId={0}&userDomain={1}&locale={2}"));
//    	}
//    	//asp-임시-끝
//
//    	if(userid.indexOf('@') != -1 ) {
//    		userid = userid.substring(0, userid.indexOf('@'));
//    	}
//
//    	Object[] url1Objs = {
//		    			    URLEncoder.encode(DigestUtil.encodeToBase64(userid), "iso-8859-1"),
//						    URLEncoder.encode(DigestUtil.encodeToBase64(domainName), "iso-8859-1"),
//						    URLEncoder.encode(DigestUtil.encodeToBase64(locale), "iso-8859-1")
//    					    };
//
//    	String gwServiceUrl = null;
//
//    	//asp-임시-시작(백업)
////    	if( useReplaceDomain() ) {
////    		gwServiceUrl = findServiceReplaceDomain();
////    	}
////    	else {
////    		gwServiceUrl = findServiceDomain(daoName) + config.get("webmail.sub.url");
////    	}
//    	//asp-임시-끝(백업)
//
//    	//asp-임시-시작
//    	if( useReplaceDomain() ) {
//    		gwServiceUrl = findServiceReplaceDomain();
//    	}
//    	else {
//    		String subURL = "";
//    		if(!"asp".equals(daoName)) {
//    			subURL = config.get("webmail.sub.url", "/xxxx");
//    		}
////    		gwServiceUrl = findServiceDomain(daoName) + subURL;
//    		gwServiceUrl = subURL;
//    	}
//    	//asp-임시-끝
//
//
//
//    	if(gwServiceUrl == null) {
//    		throw new Exception("gw.daobase.urls property was null.");
//        }
//    	String url1 = gwServiceUrl+url1Form.format(url1Objs);
//
//    	Iterator<Mail> iter = resultList.iterator();
//    	StringBuffer sbUrl2 = new StringBuffer();
//    	StringBuffer sbMsg = new StringBuffer();
//    	int cnt = 0;
//    	while(iter.hasNext()) {
//    		Mail mail = iter.next();
//
//    		/* 특수문자로 인해 클라이언트에서 수신이 안되는 현상 해결 시작 */
//    		String regex = "\\p{Cntrl}";
//    		String sSubject = mail.getSubject();
//
//    		if(sSubject != null) {
//    			mail.setSubject(sSubject.replaceAll(regex, ""));
//    		}
//    		/* 특수문자로 인해 클라이언트에서 수신이 안되는 현상 해결 종료 */
//
//    		Object[] url2Objs = { URLEncoder.encode(mail.getMessageId(), "UTF-8")};
//    		sbMsg.append(mail.getSender()).append(Constants.GWUR_COL_DELIM)
//				 .append(mail.getRecvDate()).append(Constants.GWUR_COL_DELIM)
//				 .append(mail.getSubject());
//
//    		sbUrl2.append(url2Form.format(url2Objs));
//    		if(iter.hasNext()) {
//    			sbMsg.append(Constants.GWUR_ROW_DELIM);
//    			sbUrl2.append(Constants.GWUR_ROW_DELIM);
//    		}
//
//    	}
//    	resultMap.put(Constants.PROP_APPCD, Constants.APCD_EMAIL);
//    	resultMap.put(Constants.PROP_URL1, url1);
//    	resultMap.put(Constants.PROP_URL2, sbUrl2.toString());
//    	resultMap.put(Constants.PROP_SEND_MSG, sbMsg.toString());
//		return resultMap;
//	}

//	@Override
//	public Map<String, String> getUnreadWF(String daoName, String schema,
//			String userid, String domainName, String locale) throws Exception {
//		ICommonDao commonDao = commonDaos.get(daoName);
//    	if(commonDao == null) {
//    		throw new Exception("Dao["+daoName+"] not found");
//    	}
//    	String maxList = config.get("gw.list.max", "20");
//    	List<WorkFlow> resultList = commonDao.selectUnreadWF(schema, userid, domainName, locale, maxList);
//    	Map<String,String> resultMap = new HashMap<String,String>();
//    	if(resultList == null || resultList.size() == 0) {
//    		return resultMap;
//    	}
//
////asp-임시-시작(백업)
////    	MessageFormat url0Form = new MessageFormat(config.get("gw.wf.url0.format", "/ext_ui/modules/workflow/view/workflow_view.jsp?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
////    	//MessageFormat url1Form = new MessageFormat(config.get("gw.wf.url1.format", "/gwtui/wffview/WorkflowFormView.html?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
////    	MessageFormat url1Form = new MessageFormat(config.get("gw.wf.url1.format", "/jsp/workflow/WorkflowSession.jsp?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
////    	//MessageFormat url2Form = new MessageFormat(config.get("gw.wf.url2.format", "&form_id={0}&sanc_id={1}"));
////    	MessageFormat url2Form = new MessageFormat(config.get("gw.wf.url2.format", "&form_id={0}&sanc_id={1}&formVer={2}"));
////asp-임시-끝(백업)
//
////asp-임시-시작
//    	MessageFormat url0Form = new MessageFormat(config.get("gw.wf.url0.format", "/ext_ui/modules/workflow/view/workflow_view.jsp?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
//    	MessageFormat url1Form = null;
//    	MessageFormat url2Form = null;
//    	if("asp".equals(daoName)) {
//    		url1Form = new MessageFormat(config.get("gw.wf.url1.format-asp", "/gwtui/wffview/WorkflowFormView.html?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
//    		url2Form = new MessageFormat(config.get("gw.wf.url2.format-asp", "&form_id={0}&sanc_id={1}"));
//    	}
//    	else {
//    		url1Form = new MessageFormat(config.get("gw.wf.url1.format", "/jsp/workflow/WorkflowSession.jsp?ssoFlag=true&mode=2&userid={0}&domain={1}&locale={2}"));
//    		url2Form = new MessageFormat(config.get("gw.wf.url2.format", "&form_id={0}&sanc_id={1}&formVer={2}"));
//    	}
////asp-임시-끝
//
//    	if(userid.indexOf('@') != -1) {
//    		userid = userid.substring(0, userid.indexOf('@'));
//    	}
//
//    	Object[] url1Objs = {
//			    URLEncoder.encode(DigestUtil.encodeToBase64(userid), "iso-8859-1"),
//			    URLEncoder.encode(DigestUtil.encodeToBase64(domainName), "iso-8859-1"),
//			    URLEncoder.encode(DigestUtil.encodeToBase64(locale), "iso-8859-1")
//			    };
//
//    	String gwServiceUrl = null;
//
//    	//asp-임시-시작(백업)
////    	if( useReplaceDomain() ) {
////    		gwServiceUrl = findServiceReplaceDomain();
////    	}
////    	else {
////    		gwServiceUrl = findServiceDomain(daoName) + config.get("workflow.sub.url", "/");
////    	}
//    	//asp-임시-끝(백업)
//
//    	//asp-임시-시작
//    	if( useReplaceDomain() ) {
//    		gwServiceUrl = findServiceReplaceDomain();
//    	}
//    	else {
//    		String subURL = "";
//    		if(!"asp".equals(daoName)) {
//    			subURL = config.get("workflow.sub.url", "/");
//    		}
////    		gwServiceUrl = findServiceDomain(daoName) + subURL;
//    		gwServiceUrl = subURL;
//    	}
//    	//asp-임시-끝
//
//
//        if(gwServiceUrl == null) {
//        	throw new Exception("gw.daobase.urls property was null.");
//        }
//
//        // 전자결제에 두개의 주소가 존재한다. 한글기안기 외 1
//        String[] urlList = new String[2];
//        urlList[0] = url0Form.format(url1Objs);
//        urlList[1] = url1Form.format(url1Objs);
//
//    	Iterator<WorkFlow> iter = resultList.iterator();
//    	StringBuffer sbUrl2 = new StringBuffer();
//    	StringBuffer sbMsg = new StringBuffer();
//    	boolean isEncodeSancId = Boolean.valueOf(config.get("gw.wf.sancid.encode", "false"));
//    	while(iter.hasNext()) {
//    		int urlArrIndex = 0;
//    		WorkFlow wf = iter.next();
//
////asp-임시-시작(백업)
////    		Object[] objs = {
////					wf.getFormSeq(),
////					isEncodeSancId ? URLEncoder.encode(DigestUtil.encodeToBase64(wf.getSancId()), "iso-8859-1") : wf.getSancId(),
////					wf.getFormVer()
////					};
////asp-임시-끝(종료)
//
////asp-임시-시작
//    		Object[] objs = null;
//    		if("asp".equals(daoName)) {
//    			objs = new Object[3];
//    			objs[0] = wf.getFormSeq();
//    			objs[1] = isEncodeSancId ? URLEncoder.encode(DigestUtil.encodeToBase64(wf.getSancId()), "iso-8859-1") : wf.getSancId();
//    			objs[2] = wf.getFormVer();
//    		}
//    		else {
//    			objs = new Object[2];
//    			objs[0] = wf.getFormSeq();
//    			objs[1] = isEncodeSancId ? URLEncoder.encode(DigestUtil.encodeToBase64(wf.getSancId()), "iso-8859-1") : wf.getSancId();
//    		}
////asp-임시-끝
//
//    		sbMsg.append(wf.getWriter()).append(Constants.GWUR_COL_DELIM)
//				 .append(wf.getCreateDate()).append(Constants.GWUR_COL_DELIM)
//				 .append(wf.getSubject());
//
//    		//0 : 한글기안기
//    		//1~이상 : 웹기안기.
//    		if( wf.getFormVer() > 0 )
//    			urlArrIndex = 1;
//
//    		sbUrl2.append(urlList[urlArrIndex]);
//    		sbUrl2.append(url2Form.format(objs));
//
////    		LOG.warn("form ver : " + wf.getFormVer() + " , urlArrIndex" + urlArrIndex + ", s:"+ wf.getSubject() + " ===>  " + urlList[urlArrIndex]);
//    		if(iter.hasNext()) {
//    			sbMsg.append(Constants.GWUR_ROW_DELIM);
//    			sbUrl2.append(Constants.GWUR_ROW_DELIM);
//    		}
//    	}
//    	resultMap.put(Constants.PROP_APPCD, Constants.APCD_WF);
//    	resultMap.put(Constants.PROP_URL1, gwServiceUrl);
//    	resultMap.put(Constants.PROP_URL2, sbUrl2.toString());
//    	resultMap.put(Constants.PROP_SEND_MSG, sbMsg.toString());
//		return resultMap;
//	}
	
    @Override
    public int findUnreadMemoCount(String uid) throws Exception {

        String userDir = config.get("files.userdir.path") + "/" + uid;
        if (!FileUtils.existDirectory(userDir))
            FileUtils.forceMkdir(userDir);

        File dir = new File(userDir);
        
        if (dir.isDirectory()) {
            File[] fileList = dir.listFiles(new PrefixFilenameFilter(Constants.USERFILE_MEMO_PREFIX));
            return fileList.length;
        }
        
        return 0;
    }

//    @Override
//    public int findUnreadWFCount(String daoName, String schema, String userid, String domainName, String locale)
//    	throws Exception {
//    	ICommonDao commonDao = commonDaos.get(daoName);
//    	if(commonDao == null) {
//    		throw new Exception("Dao["+daoName+"] not found");
//    	}
//    	return commonDao.selectUnreadWFCount(schema, userid, domainName, locale);
//    }

    @Override
    public String findAllOrganization(String schema, String localeString) throws Exception {
        StringBuffer buf = new StringBuffer();
        String prefix = config.get("files.xml.path") + "/" + schema + "/";
        String postfix = localeString + ".xml";
        
        long startTime = System.currentTimeMillis();
        long tmpTime = 0L;
        
        tmpTime = System.currentTimeMillis();
        //회사정보를 읽어온다.
        XStream xstream = new XStream(new LocalXppDriver());
        xstream.processAnnotations(Company.class);
        ObjectInputStream compIn = xstream.createObjectInputStream(new FileInputStream(prefix + Constants.COMAPNY_PREFIX + postfix));
        
        try {
            while(true) {
                Object oComp = compIn.readObject();
                if (oComp == null) break;
                buf.append(((Company)oComp).toString());
            }
        } catch (EOFException e) {
            compIn.close();
        } finally {
            if (compIn != null) compIn.close();
        }
        LOG.info("===>LOAD COMP Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
        
        tmpTime = System.currentTimeMillis();
        //부서정보를 읽어온다.
        xstream.processAnnotations(Dept.class);
        ObjectInputStream deptIn = xstream.createObjectInputStream(new FileInputStream(prefix + Constants.DEPT_PREFIX + postfix));
        
        try {
            while(true) {
                Object oDept = deptIn.readObject();
                if (oDept == null) break;
                buf.append(((Dept)oDept).toString());
            }
        } catch (EOFException e) {
            deptIn.close();
        } finally {
            if (deptIn != null) deptIn.close();
        }
        LOG.info("===>LOAD DEPT Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
        
        tmpTime = System.currentTimeMillis();
        //사용자정보를 읽어온다.
        xstream.processAnnotations(User.class);
        ObjectInputStream userIn = xstream.createObjectInputStream(new FileInputStream(prefix + Constants.USER_PREFIX + postfix));
        
        try {
            while(true) { 
                Object oUser = userIn.readObject();
                ///buf.append(((User)oUser).toString());
                buf.append(((User)oUser).toString(iptService, config));
            }
        } catch (EOFException e) {
            userIn.close();
        } finally {
            if (userIn != null) userIn.close();
        }
        LOG.info("===>LOAD USER Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
        LOG.info("===>LOAD ALL  Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
        return buf.toString();
    }
    
    @Override
    public User findUser(String schema, String locale, String email) throws Exception {
        String path = config.get("files.xml.path") + "/" + schema + "/" + Constants.USER_PREFIX + locale + ".xml";
        String xpathString = "//list/user[email='" + email + "']";
        Element e = XMLUtils.getNode(path, xpathString);
        
        if (e == null) return null;
        
        User user = new User();
        user.setUserindex(e.getElementsByTagName("userindex").item(0).getTextContent());
        user.setUserid(e.getElementsByTagName("userid").item(0).getTextContent());
        user.setPassword(e.getElementsByTagName("password").item(0).getTextContent());
        user.setUserName(e.getElementsByTagName("userName").item(0).getTextContent());
        user.setCompanyCode(e.getElementsByTagName("companyCode").item(0).getTextContent());
        user.setDeptCode(e.getElementsByTagName("deptCode").item(0).getTextContent());
        user.setGradeCode(e.getElementsByTagName("gradeCode").item(0).getTextContent());
        user.setGradeName(e.getElementsByTagName("gradeName").item(0).getTextContent());
        user.setDutyCode(e.getElementsByTagName("dutyCode").item(0).getTextContent());
        user.setDutyName(e.getElementsByTagName("dutyName").item(0).getTextContent());
        user.setSex(e.getElementsByTagName("sex").item(0).getTextContent());
        user.setEmail(email);
        
        return user;
    }
   
	@Override
	public User findUser(Map<String, Object> parameterMap) throws Exception {
		return null;
	}
	
	@Override
	public User getUserByUserId(Map<String, Object> parameterMap) throws Exception {
		
		return commonDaos.get(parameterMap.get("daoName").toString()).selectUserByUserId(parameterMap);
	}
	
	@Override
	public User getUserByEmpNo(Map<String, Object> parameterMap) throws Exception {
		
		return commonDaos.get(parameterMap.get("daoName").toString()).selectUserByEmpNo(parameterMap);
	}	
	
	@Override
	public User getUserByPassWord(Map<String, Object> parameterMap) throws Exception {
		
		return commonDaos.get(parameterMap.get("daoName").toString()).selectUserByPassWord(parameterMap);
	}	
	

	@Override
    public String findMemo(String uid) throws Exception {
    	
        File dir = null;
        File[] files = null;
        StringBuilder buf = new StringBuilder();
        
        try {
            String userDir = config.get("files.userdir.path") + "/" + uid;
            dir = new File(userDir);

            if (dir.isDirectory()) {
                files = dir.listFiles(new PrefixFilenameFilter(Constants.USERFILE_MEMO_PREFIX));
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    byte[] bytes = FileUtils.readFile(file);
                    String str = new String(bytes);
                    String decStr = new SecurityUtils(config.get("files.userdir.path")).decrypt(str);
                    buf.append(decStr);
                    buf.append(Constants.EOF);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        
        return buf.toString(); 
    }

    @Override
    public void createMemo(String localeString, String receiverUid, String senderUid, String memo) 
    	throws Exception {

    	StringBuilder builder = new StringBuilder();
    	Locale locale = SessionUtils.getLocale(localeString);
    	
        try {
            String userDir = config.get("files.userdir.path") + "/" + receiverUid;
            if (!FileUtils.existDirectory(userDir)) {
                FileUtils.forceMkdir(userDir);
            }
            
            File file = new File(userDir, Constants.USERFILE_MEMO_PREFIX + System.currentTimeMillis());
            builder.append(senderUid)
                   .append(Constants.PROP_DELIM)
               	   .append(DateUtils.getSystemCurrentTimeMillis(System.currentTimeMillis(), Constants.DATE_PATTERN))
                   .append(Constants.PROP_DELIM)
                   .append(memo);
            
            String s = new SecurityUtils(config.get("files.userdir.path")).encrypt(builder.toString().getBytes(Charset.forName("UTF-8")));
            FileUtils.writeFile(file, s.getBytes());
        } catch (ConfigurationException e) {
            throw new RuntimeException(messageSource.getMessage("10019", null, locale));
        } catch (IOException e) {
        	throw new RuntimeException(messageSource.getMessage("10020", null, locale));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

	@Override
	public void createGrpInfo(String localeString, String uid, String data)
			throws Exception {
		
		StringBuilder builder = new StringBuilder();
		Locale locale = SessionUtils.getLocale(localeString);
		
		try {
			File userDir = new File(config.get("files.userdir.path"), uid);
			if(!FileUtils.existDirectory(userDir)) {
				FileUtils.forceMkdir(userDir);
			}
			File file = new File(userDir, Constants.USERFILE_GROUP_NAME);
			builder.append(data);
			FileUtils.writeFile(file, builder.toString().getBytes(Charset.forName("UTF-8")));
		} catch (ConfigurationException e) {
            throw new RuntimeException(messageSource.getMessage("10019", null, locale));
        } catch (IOException e) {
        	throw new RuntimeException(messageSource.getMessage("10020", null, locale));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
	}

	@Override
	public String findGrpInfo(String uid) throws Exception {
        File file = null;     
        String data = null;
        try {            
            file = new File(new File(config.get("files.userdir.path"), uid), Constants.USERFILE_GROUP_NAME);
            if(file.exists() == false) {
            	return data;
            }
            byte[] bytes = FileUtils.readFile(file);
            data = new String(bytes, Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return data; 
	}	
	
	/**
	 * 클라이언트 버전이 서버와 호환되는 버전인지 체크한다.
	 * @param version 클라이언트 버전
	 * @return 주어진 버전이 호환되는 버전이면 주어진 버전값을 반환하고 그렇지 않으면 호환성 유지를 위한 최소버전값을 반환한다.
	 */
	public String checkClientVersion(String version) {
    	if(version == null) {
    		return validClientVersion;
    	}
    	String[] clientVersion = version.split("[\\.]");
    	if(clientVersion.length != iValidClientVersion.length) {
    		return validClientVersion;
    	}
        for(int i=0;i<clientVersion.length && i<iValidClientVersion.length;i++) {
        	if(iValidClientVersion[i] > Integer.parseInt(clientVersion[i])) {
        		return validClientVersion;
        	}
        }
    	return version;
    }
	
	/**
	 * IP 전화기 기능을 지원하는 도메인인지 검사한다.
	 * @param domain 도메인명
	 * @return IP 전화기 기능을 지원하는 도메인인 경우 true, 그렇지 않은 경우 false
	 */
	public boolean useXFon(String domain) {
		return setXFonDomain.contains(domain);
	}
	
	/**
	 * 화상회의 기능을 지원하는 도메인인지 검사한다.
	 * @param domain 도메인명
	 * @return 화상회의 기능을 지원하는 도메인인 경우 true, 그렇지 않은 경우 false
	 */
	public boolean useWCon(String domain) {
		return setWConDomain.contains(domain);
	}

//	/**
//	 * @author 김용성
//	 * 사용자의 대화명을 변경한다. DB & Memory
//	 */
//	@Override
//	public boolean modifyOnlineId(String daoName, String schema, String userId, String onlineId) throws SQLException {
//		ICommonDao commonDao = commonDaos.get(daoName);
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("schema", schema);
//		param.put("userId", userId);
//		param.put("onlineId", onlineId);
//		if( commonDao.selectCountByUserId(param) > 0 )
//			commonDao.updateOnlineId(param);
//		else commonDao.insertOnlineId(param);
//		
//		modifyOnlineIdFromMemory(schema, userId, onlineId);
//		
//		return true;
//	}
	
	@Override
	public boolean modifyOnlineId(String uid, String daoName, String schema, String userId, String onlineId) throws SQLException {
		ICommonDao commonDao = commonDaos.get(daoName);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("schema", schema);
		param.put("userId", userId);
		param.put("onlineId", onlineId);
		
		commonDao.updateOnlineId(param);
		modifyOnlineIdFromMemory(schema, userId, onlineId);
		
		return true;
	}	
	
	@Override
	public boolean modifyPassword(String uid, String daoName, String userId, String password) throws SQLException {
		ICommonDao commonDao = commonDaos.get(daoName);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("password", password);
		
		commonDao.updatePassword(param);
		
		return true;
	}	
	
	/**
	 * @author 김용성 
	 * @since 2010-04-13
	 */
	protected abstract void modifyOnlineIdFromMemory(String schema, String userId, String onlineId);
	
	/**
	 * @author 김용성
	 * @since 2010-04-28
	 */
	protected abstract String findCompany(String daoName, String schema);

	@Override
	public String findDomainName(String companyCode) throws Exception {
		String path = config.get("files.xml.path") + "/" + Constants.DOMAIN_PREFIX + ".xml";
        String xpathString = "//list/domain[companyCode='" + companyCode.trim() + "']/domainName/text()";
        String compCode = XMLUtils.getString(path, xpathString);
        
        return compCode == null ? null : compCode.trim();
	}
	
	public boolean isMssql(){
		  return Constants.DATABASE_MSSQL.equals(config.get( Constants.DATABASE_BUNDLE, "oracle"));
	}
	
	@Override
	public String getPasswordBuilderClassName(String daoName, String wasId) throws Exception {
		ICommonDao commonDao = commonDaos.get(daoName);
		return commonDao.selectPasswordBuilderClassName(wasId);
	}	

}
