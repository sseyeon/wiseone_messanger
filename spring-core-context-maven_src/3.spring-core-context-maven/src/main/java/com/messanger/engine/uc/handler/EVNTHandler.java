package com.messanger.engine.uc.handler;

import java.net.URLDecoder;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.EVNTRequest;
import com.messanger.engine.uc.message.request.MEMSRequest;
import com.messanger.engine.uc.message.response.EVNTResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.DigestUtil;
import com.messanger.engine.uc.utils.RestUtils;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 그룹웨어에서 메일 수신, 결제 상신 등의 사용자에게 알려야하는 이벤트가 발생한 경우 메신저 서버로 전달해주는 패킷
 * 현재는 메일 수신, 결제해야 할 결제 상신 이벤트를 처리
 * @author skoh
 *
 */
public class EVNTHandler implements MessageHandler<EVNTRequest> {

    IoSessionContext ctx = IoSessionContext.getInstance();
    protected Config config;  
    
//    MobilePushService mobilePushService;
    ICommonService commonService; 
    ReloadableResourceBundleMessageSource messageSource;
    
    public static final String GW_MAIL_PARAM_USERID = "&userId=";
    public static final String GW_WFLOW_PARAM_USERID = "&userid=";
    protected static final Log LOG = LogFactory.getLog(MessageHandler.class);
    
//    public void setMobilePushService(MobilePushService mobilePushService) {
//		this.mobilePushService = mobilePushService;
//	}
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;        
    }
    
	private String searchReciver(String[] urlList2, String receiver, IoSession session){
    	try{
    	for(String url2 : urlList2){
    		 String _validId = "";
             int st = url2.indexOf(GW_MAIL_PARAM_USERID) >= 0 ? url2.indexOf(GW_MAIL_PARAM_USERID):url2.indexOf(GW_WFLOW_PARAM_USERID);
             if (st >= 0) {
                 int ed = url2.indexOf("&", st+1);
                 _validId = url2.substring(st+8, ed);
                 _validId = DigestUtil.decodeToBase64(URLDecoder.decode(_validId, "UTF-8"));
                 if(receiver.split("@")[0].equals(_validId)){
                    return url2;
                 }  
             }
    	}
    	return null;
    	}catch(Exception e){
    		LOG.fatal(e);
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
    /**
     * 그룹웨어에서 수신받은 이벤트 패킷을 파싱하여 로그인 된 개별 사용자 클라이언트로 전달해 준다.
     */
    public void messageReceived(IoSession session, EVNTRequest request) throws Exception {
        SessionLog.info(session, "#H-S# EventHandler******" + request.getApplicationCode());
        String url1 = request.getURL1();
        String[] url2List = request.getURL2();
        String[] receiverList = request.getReceiverList();
        /*
        int loop = 0;
        SessionLog.error(session, "-------------------------------------------------------------------- url2 validation!");
        for(String url2 : url2List){
        	SessionLog.error(session, "index : "+loop+", value : "+url2);
        }
        
        SessionLog.error(session, "-------------------------------------------------------------------- recv validation!");
        for(String recv : receiverList){
        	SessionLog.error(session, "index : "+loop+", value : "+recv);
        }
        */
        
//        String replaceUrl = url1.replaceAll("/gwtui/wffview/WorkflowFormView.html", config.get("gw.wf.url1.format","/gwtui/wfffview/WorkflowFormFView.html?").split("[?]")[0] );
        String replaceUrl = url1;
        if( request.getApplicationCode().equals(Constants.APCD_MEMO_ANOTHER_SYSTEM) == false){
	        for (String receiver : receiverList) {
	            IoSession targetSession = SessionUtils.getUserSession(receiver);
	            if (targetSession != null && targetSession.isConnected()) {
	                EVNTResponse response = new EVNTResponse(request.getType());
	                response.setTransactionId(Constants.TRANSACTION_INIT_CODE);
	                //url1 /serd/mailc.....
	                //url2 /user=sdd@domo=dsdd
	                
	                //배열인덱스 자체가 잘못되는 오류가 발생하여,
	                //시간은 걸리더라도 한번더 확실하게 검증하는 로직을 추가합니다.
	                String url2 = searchReciver(url2List, receiver,session);
	                
	                //String url2 = url2List[i++];
	                
	                /*
	                String _validId = "";
	                int st = url2.indexOf("&userId=");
	                if (st >= 0) {
	                    int ed = url2.indexOf("&", st+1);
	                    _validId = url2.substring(st+8, ed);
	                    _validId = DigestUtil.decodeToBase64(URLDecoder.decode(_validId, "UTF-8"));
	                    if(!receiver.split("@")[0].equals(_validId)){
	                        SessionLog.error(session, "--------------------------------------------------------------------");
	                        SessionLog.error(session, " i n v a l i d   u s e r i d ");
	                        SessionLog.error(session, "--------------------------------------------------------------------");
	                        SessionLog.error(session, replaceUrl);
	                        SessionLog.error(session, url2);
	                        SessionLog.error(session, "receiver : " + receiver +" , _validId"+_validId);
	                        continue;
	                    }
	                }
	                */                      
	                if(url2 != null){
		                response.setURL(replaceUrl + url2);
		                response.setApplicationCode(request.getApplicationCode());
		                response.setContents(request.getContents() + (request.getApplicationCode().equals(Constants.APCD_WF) ? "결재문서가 도착하였습니다." : ""));
		                targetSession.write(response);
		                SessionLog.info(targetSession, "#H-C#EVH# Send To " + receiver + " Success!!");
		                
	                }else{
	                	//올바르게 검증되지 않은 아이디에겐 알림을 송신하지 않습니다.
	                	SessionLog.error(session, "ERROR Send To " + receiver + " Failed!!");
	                }
	            }
	            String[] mailCont = null;
	            if (request.getApplicationCode().equals(Constants.APCD_EMAIL)) {
	            	String contents = request.getContents();
	            	mailCont = StringUtils.tokenizeToStringArray(contents, "^");
	            }
	            if (Boolean.valueOf(config.get(Constants.MOBILE_NOTI, "false"))) {
		            RestUtils.sendNoti(
		            		config.get("mobile.noti.url"),
	                		request.getApplicationCode().equals(Constants.APCD_WF) ? "2" : "1",
	                		request.getApplicationCode().equals(Constants.APCD_WF) ? "결재알림" : mailCont[0],
	                		request.getApplicationCode().equals(Constants.APCD_WF) ? "결재문서가 도착하였습니다." : mailCont[2],
	                		receiver, config.get("mobile.noti.callback.url"));
	            }
	        }  
        } else {
        	/**
        	 * MS-237 외부 시스템 쪽지 연동
        	 */
        	StringBuffer sb = new StringBuffer();
        	for (int i = 0; i < receiverList.length; i++) {
        		sb.append(receiverList[i]);
        		if(i < receiverList.length-1){
        			sb.append(Constants.PROP_DELIM);
        		}
        	}
        	session.setAttribute(Constants.SESSION_KEY_LOCALE, "ko_KR");
        	MEMSHandler memsHandler = new MEMSHandler();
        	memsHandler.setCommonService(commonService);
        	memsHandler.setMessageSource(messageSource);
        	MEMSRequest memsRequest = new MEMSRequest(Constants.TYPE_SEND_MEMO);
        	memsRequest.setTransactionId(Constants.TRANSACTION_TEMP_CODE);
        	memsRequest.setProperty(Constants.PROP_SENDER_UID, "Admin");
        	memsRequest.setProperty(Constants.PROP_SEND_MSG, request.getContents());
        	memsRequest.setProperty(Constants.PROP_RECEIVER_UID, sb.toString());
        	memsHandler.messageReceived(session, memsRequest);
        }
        
        //MA-963  
        if(Boolean.valueOf(config.get(Constants.USE_APNS, "false"))){
        	String sender = request.getSender();
        	final int str = sender.indexOf("<")+1;
        	final int end = sender.indexOf(">");
        	
        	String domainName = null;
        	
        	if( str != -1 && end != -1){
        		domainName = sender.substring(str, end);
        	
	        	boolean isInMail = sender.indexOf("<") != -1 && sender.indexOf(">") != -1;
	        	SessionLog.info(session,domainName);
	        	SessionLog.info(session,"1isInMail : " + isInMail);
	        	
	        	//내부 메일은 이름만 표기함.
	        	if( isInMail ){
	        		isInMail = sender.indexOf("@") != -1 ;
	        		final String schema = commonService.findScheme(domainName.split("@")[1]);
	        		isInMail = !(schema == null || schema.length() == 0);
	        		SessionLog.info(session,"3isInMail : " + isInMail);
	        		//in mail
	        		if( isInMail && str > 0 ){
	        			sender = sender.substring(0, str-1);
	        		}
	        	}
        	}
        	// 외부 메일은 그대로 보여줌.
        	final String message = appCodeMessage(request.getApplicationCode(), sender, new Locale("ko_KR"));
//        	mobilePushService.push(receiverList, message, Constants.APCD_EMAIL, null);
        }
        SessionLog.info(session, "#H-E# EventHandler");
    }

	public void setConfig(Config config) {
		this.config = config;
	}
    
	public String appCodeMessage(String appCode, String sender, Locale locale){
		return messageSource.getMessage(Constants.APCD_CODE + appCode, new Object[]{sender}, locale);	
	}
}
