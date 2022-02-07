package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.MSGIRequest;
import com.messanger.engine.uc.message.response.MSGIResponse;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 사용자 메세지 입력 메세지 처리 핸들러.
 * @author skoh
 *
 */
public class MSGIHandler implements MessageHandler<MSGIRequest> {

    IoSessionContext ctx = IoSessionContext.getInstance();
    ReloadableResourceBundleMessageSource messageSource;
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
		
    /**
     * 사용자가 현재 메세지를 입력하고 있다는 이벤트를 전달 받아 단수 혹은 복수의 대화 상대에게 전달.
     */
    public void messageReceived(IoSession session, MSGIRequest request) throws Exception {
    	SessionLog.info(session, "#H-S# InputMsgHandler");
        Locale locale = SessionUtils.getRequestLocale(request);
        MSGIResponse response = new MSGIResponse(request.getType());
        String errMsg = "";
        
        try {
            response.setTransactionId(request.getTransactionId());

            String[] receipt = request.getReceipt();
            /*
            IoSessionContext context = IoSessionContext.getInstance();
            SessionLog.debug(session, context.toString());
            */
            
            for (int i = 0; i < receipt.length; i++) {
                MSGIResponse rcvMsg = new MSGIResponse(Constants.TYPE_SEND_MSGI);
                //SessionLog.info(session, "#H-C#IMH# MSG RECEIVER[" + receipt[i] + "]");
                IoSession targetSession = SessionUtils.getUserSession(receipt[i]);
                
                //세션이 존재하지 않은경우.
                if (targetSession == null) {
                    errMsg += messageSource.getMessage("10021", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    continue;
                }
                        
                //연결이 정상인 경우.
                if (targetSession.isConnected()) {
                    rcvMsg.setTransactionId(request.getTransactionId());
                    rcvMsg.setReceiverUid(receipt[i]);
                    rcvMsg.setSenderUid(request.getSenderUid());
                    rcvMsg.setRoomId(request.getRoomId());
                    rcvMsg.setMSCD(request.getMSCD());
                    targetSession.write(rcvMsg);
                    SessionLog.info(targetSession, "#H-C#IMH# Send To "+receipt[i]+" Success!!");
                    /*
                    WriteFuture future = targetSession.write(rcvMsg);
                    future.join();
                    if( future.isWritten()) {
                        SessionLog.info(targetSession, "#H-C#IMH# Send To "+receipt[i]+" Success!!");
                    } else {
                        errMsg += messageSource.getMessage("10016", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    }
                    */
                }
                //연결이 끊긴경우.
                else {
                    errMsg += messageSource.getMessage("10015", new Object[]{receipt[i]}, locale);
                }
            }            
        } catch (Exception e) {
        	errMsg = e.getMessage();
            SessionLog.error(session, e.getMessage(), e);
        }
        
        response.setResponseMsg(errMsg);
        
        session.write(response);
        SessionLog.info(session, "#H-E# InputMsgHandler");
    }
}
