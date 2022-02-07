package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.request.FILSRequest;
import com.messanger.engine.uc.message.response.FILSResponse;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 파일 전송 요청 처리 핸들러.
 * @author skoh
 *
 */
public class FILSHandler implements MessageHandler<FILSRequest> {
	    
    ReloadableResourceBundleMessageSource messageSource;
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    /**
     * 파일 전송 요청 메세지를 해당 사용자에게 전달한다.
     */
	@Override
	public void messageReceived(IoSession session, FILSRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# SendFileHandler");		
		FILSResponse response = new FILSResponse(request.getType());
		String transactionId = request.getTransactionId();
		Locale locale = SessionUtils.getRequestLocale(request);
		String errMsg = "";
		
		try {
			response.setTransactionId(transactionId);
			String[] receipts = request.getReceipt();
			
			String suid = request.getSenderUid();
			String fileName = request.getFileName();
			String fileSize = request.getFileSize();
			String fileKey = request.getFileKey();
			String roomId = request.getRoomId();
			
			for(int i=0;i<receipts.length;i++) {				
				IoSession rcvSession = SessionUtils.getUserSession(receipts[i]);				
				//SessionLog.info(session, request.getType()+" RECEIVER[" + receipts[i] + "]");
				if(rcvSession == null) {
					errMsg += messageSource.getMessage("10021", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;
					continue;
				}				
				
				if(rcvSession.isConnected()) {
					FILSResponse rcvResponse = new FILSResponse(request.getType());
		
					rcvResponse.setTransactionId(transactionId);
					rcvResponse.setSenderUid(suid);
					rcvResponse.setReceiverUid(receipts[i]);
					rcvResponse.setFileName(fileName);
					rcvResponse.setFileSize(fileSize);
					rcvResponse.setFileKey(fileKey);
					rcvResponse.setRoomId(roomId);
					
					WriteFuture future = rcvSession.write(rcvResponse);
					future.join();
	                if( future.isWritten()) {
	                    SessionLog.info(session, "#H-C#SFH# Send To "+receipts[i]+" SUCCESS!!");
	                } else {
	                    errMsg += messageSource.getMessage("10016", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;
	                }
				} else {
					errMsg += messageSource.getMessage("10015", new Object[]{receipts[i]}, locale);
				}				
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(), e);
		}		
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# SendFileHandler");		
	}
}
