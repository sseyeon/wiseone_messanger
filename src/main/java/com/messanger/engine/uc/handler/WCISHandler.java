package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.WCISRequest;
import com.messanger.engine.uc.message.response.WCISResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 화상회희 초대 메세지 처리 핸들러
 * @author skoh
 *
 */
public class WCISHandler implements MessageHandler<WCISRequest> {

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
     * 단수 혹은 복수의 사용자에게 화상회의 초대 메세지를 전달한다.
     */
	@Override
	public void messageReceived(IoSession session, WCISRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# SendConferenceHandler");
        Locale locale = SessionUtils.getRequestLocale(request);

        String errMsg = "";        
        int i=0, j=0;
        String[] receipt = request.getReceipt();
        
        try {           
            for (i = 0; i < receipt.length; i++) {
            	errMsg = "";
                WCISResponse response = new WCISResponse(request.getType());
                response.setTransactionId(request.getTransactionId());
                response.setRoomId(request.getRoomId());
                response.setReceiverUid(receipt[i]);
                
            	WCISResponse rcvMsg = new WCISResponse(Constants.TYPE_SEND_WC);
            	rcvMsg.setTransactionId(request.getTransactionId());
                IoSession targetSession = SessionUtils.getUserSession(receipt[i]);
                
                //세션이 존재하지 않은경우.
                if (targetSession == null) {
                    errMsg += messageSource.getMessage("10021", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    response.setResponseMsg(errMsg);
                    session.write(response);
                    continue;
                }                        
                //연결이 정상인 경우.
                if (targetSession.isConnected()) {                    
                    rcvMsg.setSenderUid(request.getSenderUid());
                    rcvMsg.setReceiverUid(receipt[i]);                    
                    rcvMsg.setRoomId(request.getRoomId());
                    
                    WriteFuture future = targetSession.write(rcvMsg);
                    future.join();
                    if( future.isWritten()) {
                        SessionLog.info(targetSession, "#H-C#SCH# Send To "+receipt[i]+" Success!!");
                        response.setResponseMsg(errMsg);
                        session.write(response);
                    } else {
                        errMsg = messageSource.getMessage("10016", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                        response.setResponseMsg(errMsg);
                        session.write(response);
                    }
                }
                //연결이 끊긴경우.
                else {
                    errMsg = messageSource.getMessage("10015", new Object[]{receipt[i]}, locale);
                    response.setResponseMsg(errMsg);
                    session.write(response);
                }
            }
        } catch (Exception e) {
            errMsg = e.getMessage();
            SessionLog.error(session, e.getMessage(), e);
            for(j=i;j<receipt.length;j++) {
            	WCISResponse response = new WCISResponse(request.getType());
                response.setTransactionId(request.getTransactionId());
                response.setRoomId(request.getRoomId());
                response.setReceiverUid(receipt[i]);
                response.setResponseMsg(errMsg);
                session.write(response);
            }
        }
        
        SessionLog.info(session, "#H-E# SendConferenceHandler");
	}
}
