package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.request.RCISRequest;
import com.messanger.engine.uc.message.response.RCISResponse;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 원격 제어 요청 초대 처리 핸들러
 * @author skoh
 *
 */
public class RCISHandler implements MessageHandler<RCISRequest> {

    ReloadableResourceBundleMessageSource messageSource;
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    /**
     * 원격 제어 요청 메세지를 해당 사용자에게 전달한다. 
     */
	@Override
	public void messageReceived(IoSession session, RCISRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# RCIS");		
		
		String transactionId = request.getTransactionId();
		Locale locale = SessionUtils.getRequestLocale(request);
		String errMsg = "";
		
		int i=0,j;
		String[] receipts = request.getReceipt();
		
		try {
			String suid = request.getSenderUid();
			String addr = request.getAddr();
			
			for(i=0;i<receipts.length;i++) {
				RCISResponse response = new RCISResponse(request.getType());
				response.setTransactionId(transactionId);
				response.setRoomId(request.getRoomId());
				response.setReceiverUid(receipts[i]);
				
				IoSession rcvSession = SessionUtils.getUserSession(receipts[i]);				
				//SessionLog.info(session, request.getType()+" RECEIVER[" + receipts[i] + "]");
				if(rcvSession == null) {
					errMsg += messageSource.getMessage("10021", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;
					response.setResponseMsg(errMsg);
					session.write(response);
					continue;
				}				
				
				if(rcvSession.isConnected()) {
					RCISResponse rcvResponse = new RCISResponse(request.getType());		
					rcvResponse.setTransactionId(transactionId);
					rcvResponse.setSenderUid(suid);
					rcvResponse.setReceiverUid(receipts[i]);					
					if(addr != null) {
						rcvResponse.setAddr(addr);
					}
					
					WriteFuture future = rcvSession.write(rcvResponse);
					future.join();
	                if( future.isWritten()) {
	                    SessionLog.info(session, "H-C#RCIS# Send To "+receipts[i]+" Success!!");
	                } else {
	                    errMsg += messageSource.getMessage("10016", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;
	                    response.setResponseMsg(errMsg);
	            		session.write(response);
	                }
				} else {
					errMsg += messageSource.getMessage("10015", new Object[]{receipts[i]}, locale);
					response.setResponseMsg(errMsg);
					session.write(response);
				}				
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(), e);
			for(j=i;j<receipts.length;j++) {
				RCISResponse response = new RCISResponse(request.getType());
				response.setTransactionId(transactionId);
				response.setRoomId(request.getRoomId());
				response.setReceiverUid(receipts[i]);
				response.setResponseMsg(errMsg);
				session.write(response);
			}
		}		
		
		SessionLog.info(session, "#H-E# RCIS");
	}
}
