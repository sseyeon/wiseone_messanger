package com.messanger.engine.uc.handler;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.MEMSRequest;
import com.messanger.engine.uc.message.response.MEMRResponse;
import com.messanger.engine.uc.message.response.MEMSResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.DateUtils;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 쪽지 전송 요청 처리 핸들러.
 * @author NEPTUNE
 *
 */
public class MEMSHandler implements MessageHandler<MEMSRequest> {

	ICommonService commonService;
    IoSessionContext ctx = IoSessionContext.getInstance();
    ReloadableResourceBundleMessageSource messageSource;
    
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
    
    /**
     * 송신자에게 전달 받은 쪽지를 수신자(단수 혹은 복수)에게 전달
     * 수신자가 오프라인인 경우 서버에 저장해 두고 해당 사용자가 로그인되어 요청하는 시점에 전달한다.
     */
    public void messageReceived(IoSession session, MEMSRequest request) throws Exception {        
    	SessionLog.info(session, "-------------TEST-----------------");    			
    	Map map = request.getTest();
    	Iterator<String> it = map.keySet().iterator();
    	
    	while(it.hasNext()){
    		String key = it.next();
    		System.out.println(key +":"+request.getProperty(key));
    	}
    	
    	SessionLog.info(session, "------------------------------");
        SessionLog.info(session, "#H-S# SendMemoHandler");
        MEMSResponse response = new MEMSResponse(request.getType());
        String errMsg = "";
        
        try {
        	Locale locale = SessionUtils.getRequestLocale(request);
            response.setTransactionId(request.getTransactionId());
            String[] receipt = request.getReceipt();
            //IoSessionContext context = IoSessionContext.getInstance();
            
            //수신자에게 메세지를 전송한다.
            for (int i = 0; i < receipt.length; i++) {
            	MEMRResponse rcvMemo = new MEMRResponse(Constants.TYPE_RECEIVE_MEMO);
                SessionLog.info(session, "#H-C#SOH# MEMO RECEIVER[" + receipt[i] + "]");
                IoSession targetSession = SessionUtils.getUserSession(receipt[i]);
                rcvMemo.setTransactionId(request.getTransactionId());
                rcvMemo.setReceiverUid(receipt[i]);
                rcvMemo.setSenderUid(request.getSenderUid());
                //송신자|수신시간|쪽지메시지EOF송신자|수신시간|쪽지메시지EOF송신자|수신시간|쪽지메시지
                rcvMemo.setMemo(request.getSenderUid() + Constants.PROP_DELIM 
                        + DateUtils.getSystemCurrentTimeMillis(System.currentTimeMillis(), Constants.DATE_PATTERN) + Constants.PROP_DELIM 
                        + request.getMemo());
                SessionLog.info(session,request.getSenderUid() + Constants.PROP_DELIM 
                        + DateUtils.getSystemCurrentTimeMillis(System.currentTimeMillis(), Constants.DATE_PATTERN) + Constants.PROP_DELIM 
                        + request.getMemo());
                //수신자가 연결된 경우.
                if (targetSession != null && targetSession.isConnected()) {
                    WriteFuture future = targetSession.write(rcvMemo);
                    future.join();
                    if( future.isWritten()) {
                        SessionLog.info(targetSession, "#H-C#SOH# Send To "+receipt[i]+" Success!!");
                    } else {
                        errMsg += messageSource.getMessage("10017", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    }
                }
                //세션이 끊겨있는 경우에 파일시스템에 저장한다.
                else {
                	try {
                		String receiverLocale = SessionUtils.getLocale(session);
                		SessionLog.info(session, receiverLocale+":"+receiverLocale+":"+ receipt[i]+":"+ request.getSenderUid()+":"+ request.getMemo());
                		commonService.createMemo(receiverLocale, receipt[i], request.getSenderUid(), request.getMemo());
                	} catch (Exception e) {
                		e.printStackTrace();
                		errMsg += messageSource.getMessage(
                				"10017", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                		SessionLog.error(session, e.getMessage(),e);
                	}
                }
            }
        } catch (Exception e) {
        	errMsg += e.getMessage();
            SessionLog.error(session, e.getMessage(),e);
        }
        response.setResponseMsg(errMsg);        
        session.write(response);
        SessionLog.info(session, "#H-E# SendMemoHandler");
    }
}
