package com.messanger.engine.uc.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.message.request.LGINRequest;
import com.messanger.engine.uc.message.response.LGINResponse;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.Cipher;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 로그인 처리 핸들러
 * @author skoh
 *
 */
public class LGINHandler implements MessageHandler<LGINRequest> {

	protected static final Log LOG = LogFactory.getLog(LGINHandler.class);
    ICommonService commonService; 
    IoSessionContext ctx = IoSessionContext.getInstance();
    ReloadableResourceBundleMessageSource messageSource;
    Config config; 
    private IIptService iptService;
    
    public void setIptService(IIptService iptService) {
		this.iptService = iptService;
	}
    
    /**
     * 
     * @param commonService
     */
    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;        
    }
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    public void setConfig(Config config) {
		this.config = config;
	}
     
	/**
     * 클라이언트 버전 확인, 계정 확인 후 로그인 프로세스를 수행한다.
     * 글로벌 세션 Context에 해당 계정의 세션 및 속성을 저장하고, 읽지 않은 쪽지 개수, 읽지 않은 메일 개수, 처리하지 않은 결제 개수를 조회하여
     * 응답을 준다.
     * 클라이언트의 XFon, WebConference 사용여부는 옵션으로 전달.
     */
    public void messageReceived(IoSession session, LGINRequest request) throws Exception {        
        SessionLog.info(session, "#H-S# LoginHandler");
        LOG.info("messageReceived :============================> call ");
        //사번 로그인 체크 : config
        boolean isEmpnoLogin = Boolean.valueOf(config.get(Constants.LOGIN_IS_EMPNO, "false"));
        LOG.info("isEmpnoLogin :============================>  " + isEmpnoLogin);
        SessionLog.debug(session, " EMPNO Login is " + isEmpnoLogin);
        
        LGINResponse response = new LGINResponse(request.getType());
        response.setTransactionId(request.getTransactionId());
        String errMsg = "";
        
        long startTime = System.currentTimeMillis();
        long tmpTime = 0L;
        try {        	
            String localeString = request.getLocale();
            Locale locale = SessionUtils.getLocale(localeString);
            String uid = request.getSenderUid();
            String userid = request.getUserId();
            String domainName = request.getDomainName();
            String daoName = commonService.findDaoName(domainName);
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("daoName", daoName);
            parameter.put("password", request.getPasswd());
            User userPwd = commonService.getUserByPassWord(parameter);
            String passwd = userPwd.getPassword();
            String version = request.getClientVersion();
            String empno = "" ;
            
            if( isEmpnoLogin ){
            	empno = userid;
            	SessionLog.info(session, request.getCompanyCode());
            	domainName = commonService.findDomainName(request.getCompanyCode());
            	SessionLog.debug(session, "uid + " +uid+ ", findDomainName " + domainName);
            	//사번로그인일 경우 test@domain.com 도메인이 섞여 있으면 안됩니다. 콤보박스 이용.
            	if( request.getSenderUid().indexOf("@") != -1 ){
	            	errMsg = messageSource.getMessage("10029", null, locale);
	            	response.setResponseMsg(errMsg);        
	            	session.write(response);            	
	                return;
            	}
            }
            
            if(version == null) {
            	version = "2.0.0.0";
            }            
            String retVersion = commonService.checkClientVersion(version);
            if(retVersion.compareTo(version) != 0) {
            	SessionLog.warn(session, "client version not match ["+uid+"] "+retVersion+" vs "+ version);
        		errMsg = messageSource.getMessage("10028", new Object[] {version, retVersion}, locale); 
        		response.setResponseMsg(errMsg);        
            	session.write(response);            	
                return;
            }
           
//            String schema = commonService.findScheme(domainName);
//
//            if(schema == null || schema.length() == 0) {
//            	SessionLog.warn(session, "schema not found: "+uid+" at "+domainName);
//            	errMsg = messageSource.getMessage("10003", null, locale);//회사 정보가 존재하지 않음
//            }
//            if(errMsg.length() > 0) {
//            	response.setResponseMsg(errMsg);
//            	session.write(response);
//                return;
//            }
            
            daoName = commonService.findDaoName(domainName);
            
            if(daoName == null || daoName.length() == 0) {            	
            	SessionLog.warn(session, "daoName not found: "+uid+" at "+domainName);
            	errMsg = messageSource.getMessage("10003", null, locale);//회사 정보가 존재하지 않음            	
            }
            if(errMsg.length() > 0) {
            	response.setResponseMsg(errMsg);        
            	session.write(response);
                return;
            }            
    
            
    		Map<String, Object> parameterMap = new HashMap<String, Object>();
//    		parameterMap.put("schema", schema);
    		parameterMap.put("locale", localeString);
    		parameterMap.put("userid", userid);
    		parameterMap.put("email", uid);
//    		parameterMap.put("ableDuplUser", config.get(Constants.DUPLICATE_USER_INFO,"false") );
//    		parameterMap.put("sortUsername", config.get(Constants.SORT_USERNAME,"false") );
    		parameterMap.put("empno", empno);
    		parameterMap.put("isEmpnoLogin", isEmpnoLogin);
    		parameterMap.put("compCode", "00168");
    		parameterMap.put("daoName", daoName);
    		parameterMap.put("isOnlineId", config.get(Constants.ONLINE_ID,"false"));
    		parameterMap.put("domain", domainName);
    		SessionLog.info( session,"[Login LOG] user : " +parameterMap );
//    		User user = commonService.findUser(parameterMap);
    		User user = null;
    		if (isEmpnoLogin) {
    			user = commonService.getUserByEmpNo(parameterMap);
    		} else {
    			user = commonService.getUserByUserId(parameterMap);
    		}
            
            if (user != null) {
            	
//            	String passwordBuilderClassName = commonService.getPasswordBuilderClassName(daoName, config.get(Constants.PASSWORD_BUILDER_WASID, "WD"));
            	
            	if (isAutoLogin(passwd) || user.getPassword().equals(passwd)) {
            		if( isEmpnoLogin )
            			uid = user.getUserid() + "@" + domainName ;
            		
            		//세션정보 저장
		            session.setAttribute(Constants.SESSION_KEY_LOCALE, localeString);
		            session.setAttribute(Constants.SESSION_KEY_UID, uid);
//		            session.setAttribute(Constants.SESSION_KEY_SCHEME, schema);
		            session.setAttribute(Constants.SESSION_KEY_USER_NAME, user.getUserName());
		            session.setAttribute(Constants.SESSION_KEY_STATUS, Constants.STATUS_ONLINE);
		            session.setAttribute(Constants.SESSION_KEY_DOMAIN, domainName);
		            session.setAttribute(Constants.SESSION_KEY_EMAIL, uid);
		            session.setAttribute(Constants.SESSION_KEY_DAONAME, daoName);
		            
            		//이미 접속되어 있는 세션이 있으면 연결을 끊는다.
		            IoSession alreadyConnectedSession = SessionUtils.getUserSession(uid);		            
		            if (alreadyConnectedSession != null && alreadyConnectedSession.isConnected()) {
		                SessionLog.info(session, "#H-C#LGH# ALREADY OPEND SESSION CLOSE!!");
		                alreadyConnectedSession.close();
		            }
		            
		            ctx.putSession(uid, session);
		            SessionLog.info(session, "#H-C#LGH# ["+uid+"] SESSION INFO SAVED!!");
		            
		            int unreadMemoCount = 0;
		            int unreadMailCount = 0;
		            int unreadWFCount = 0;		            
		            try {
//			            //읽지않은 메모갯수
			            unreadMemoCount = commonService.findUnreadMemoCount(uid);
			            SessionLog.info(session, "#H-C#LGH# UNREAD MEMO CNT:" + unreadMemoCount);
//			        	
//			            tmpTime = System.currentTimeMillis();
//			            
//			            //읽지않은 메일갯수
//			            unreadMailCount = commonService.findUnreadMailCount(daoName, schema, user.getUserid(), domainName, localeString);
//			            SessionLog.info(session, "#H-C#LGH# UNREAD MAIL CNT:" + unreadMailCount);
//			            			            
//			        	SessionLog.info(session, "===>MAIL COUNT Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
//			        	tmpTime = System.currentTimeMillis();
//			        	
//			            //처리하지 않은 결재문서 갯수
//			            unreadWFCount = commonService.findUnreadWFCount(daoName, schema, user.getUserid(), domainName, localeString);
//			            SessionLog.info(session, "#H-C#LGH# UNREAD WORKFLOW CNT:" + unreadWFCount);
//			            
//			            SessionLog.info(session, "===>WF COUNT Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));			        	
		            } catch (SQLException e) {
		            	SessionLog.error(session, "Fetch data db fail: "+e.toString());
		            }
		            SessionLog.info(session, "#H-C#LGH# UNREAD BILL CNT:" + unreadWFCount);
		            
		            response.setMemoCount(unreadMemoCount);
		            response.setMailCount(unreadMailCount);
		            response.setWFCount(unreadWFCount);
		            response.setUserName(user.getUserName());
		            
		            // 로그인시 사용자의 empno 를 전달하지 여부. 
		            // nds 전자결제 연동에 의해 추가.
		            
		            if(Boolean.valueOf(commonService.getConf("login.return.empno","false"))){
		            	response.setEmpno(user.getEmpno());
		            }
		             
		            response.setDomain(uid);
		            if(Boolean.getBoolean(commonService.getConf("user.onlineId","false")))
		            	response.setOnid(user.getOnlineId());
		            
		            String opt = Constants.OPTION_REFUSE;
		            
		            if(commonService.useXFon(domainName)) {
		            	opt = Constants.OPTION_ACCEPT;
		            }
		            else {
		            	if(iptService.isIpt(user.getCompPhoneNumber(), config.get("ipt.phonenum.prefix"))) {
			            	opt = Constants.OPTION_ACCEPT;
			            }
		            }
		            response.setXFon(opt);

		            opt = Constants.OPTION_REFUSE;
		            if(commonService.useWCon(domainName)) {
		            	opt = Constants.OPTION_ACCEPT;
		            }
		            response.setWCon(opt);
		            
		            tmpTime = System.currentTimeMillis();
		            SessionLog.info(session, "#H-C#LGH# ["+request.getSenderUid()+"] LOGIN PROCESS SUCCESS!!");
		            
		            // 로그인 현황 입력
//		            if( Boolean.valueOf(config.get("log.login", "false")) ){
//		            	commonService.writeLoginInfo(daoName, user.getUserid(), domainName, user.getUserName()
//		            						, Constants.LOGIN_USER_SUCCESS, "", Constants.LOGIN_USER_STATUS
//		            						, ((InetSocketAddress)session.getRemoteAddress()).getHostName(), "");
//		            	session.setAttribute("", user.getUserName());
//		            }
		            // 로그인 현황 입력
            	} else {
                	errMsg = messageSource.getMessage("10002", null, locale);//패스워드 불일치
            	}
            } else {
            	errMsg = messageSource.getMessage("10001", null, locale);//존재하지 않는 사용자
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	errMsg = e.getMessage();        	
            SessionLog.error(session, e.getMessage(), e);
        }
        response.setResponseMsg(errMsg);
        session.write(response);
        SessionLog.info(session, "#H-E# LoginHandler Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
    }    
    
    private boolean isAutoLogin(String passwd){
    	if( Boolean.valueOf(config.get("autologin","false")) && passwd.equals(Constants.AUTO_LOGIN_PASSWORD) )
    		return true;
    	return false;
    }
}
