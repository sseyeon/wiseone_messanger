package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.request.WCIRRequest;
import com.messanger.engine.uc.message.response.WCIRResponse;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 화상회의 초대 응답 메세지 처리 핸들러
 * @author skoh
 *
 */
public class WCIRHandler implements MessageHandler<WCIRRequest> {

    ReloadableResourceBundleMessageSource messageSource;
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    /**
     * 화상회의 초대에 대한 응답 메세지를 전달
     */
	@Override
	public void messageReceived(IoSession session, WCIRRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# ReceiveConferenceHandler");		
		String transactionId = request.getTransactionId();
		Locale locale = SessionUtils.getRequestLocale(request);
		String errMsg = "";
		
		int i=0, j;
		String[] receipts = request.getReceipt();
		
		try {
			String suid = request.getSenderUid();
			String ocd = request.getOCD();
			String cid = request.getRoomId();
			String wid = request.getWID();
			
			for(i=0;i<receipts.length;i++) {
				errMsg = "";
				WCIRResponse response = new WCIRResponse(request.getType());				
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
					WCIRResponse rcvResponse = new WCIRResponse(request.getType());
		
					rcvResponse.setTransactionId(transactionId);
					rcvResponse.setSenderUid(suid);
					rcvResponse.setReceiverUid(receipts[i]);
					rcvResponse.setOCD(ocd);
					rcvResponse.setRoomId(cid);
					if(wid != null) {
						rcvResponse.setWID(wid);
					}
					
					WriteFuture future = rcvSession.write(rcvResponse);
					future.join();
	                if( future.isWritten()) {
	                    SessionLog.info(session, "H-C#RCH# Send To "+receipts[i]+" Success!!");
	                    response.setResponseMsg(errMsg);
	                    session.write(response);
	                } else {
	                    errMsg = messageSource.getMessage("10016", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;	                    
	                    response.setResponseMsg(errMsg);
	                    session.write(response);
	                }
				} else {
					errMsg = messageSource.getMessage("10015", new Object[]{receipts[i]}, locale);					
					response.setResponseMsg(errMsg);
                    session.write(response);
				}				
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(), e);
			for(j=i;j<receipts.length;j++) {
				WCIRResponse response = new WCIRResponse(request.getType());
                response.setTransactionId(request.getTransactionId());
                response.setRoomId(request.getRoomId());
                response.setReceiverUid(receipts[i]);
                response.setResponseMsg(errMsg);
                session.write(response);
            }
		}
		SessionLog.info(session, "#H-E# ReceiveConferenceHandler");
	}
}
