package com.messanger.engine.uc.handler;

import java.util.Locale;

import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.request.FILDRequest;
import com.messanger.engine.uc.message.response.FILDResponse;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 파일전송 데이터 처리 핸들러.
 * @author skoh
 *
 */
public class FILDHandler implements MessageHandler<FILDRequest>{

    ReloadableResourceBundleMessageSource messageSource;
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
    
    /**
     * 파일 전송의 데이터 부분을 처리.
     * 수신받은 파일의 바이너리 데이터를 해당 사용자에게 전달
     */
	@Override
	public void messageReceived(IoSession session, FILDRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# SendFileDataHandler");
		FILDResponse response = new FILDResponse(request.getType());
		String transactionId = request.getTransactionId();
		Locale locale = SessionUtils.getRequestLocale(request);
		String errMsg = "";
		
		try {
			response.setTransactionId(transactionId);
			String[] receipts = request.getReceipt();
			
			String suid = request.getSenderUid();			
			String fileKey = request.getFileKey();
			byte[] data = request.getData();
			
			for(int i=0;i<receipts.length;i++) {				
				IoSession rcvSession = SessionUtils.getUserSession(receipts[i]);				
				//SessionLog.info(session, request.getType()+" RECEIVER[" + receipts[i] + "]");
				if(rcvSession == null) {
					errMsg += messageSource.getMessage("10021", new Object[]{receipts[i]}, locale) + Constants.PROP_DELIM;
					continue;
				}				
				
				if(rcvSession.isConnected()) {
					FILDResponse rcvResponse = new FILDResponse(request.getType());
		
					rcvResponse.setTransactionId(transactionId);
					rcvResponse.setSenderUid(suid);
					rcvResponse.setReceiverUid(receipts[i]);					
					rcvResponse.setFileKey(fileKey);
					rcvResponse.setData(data);
					
					WriteFuture future = rcvSession.write(rcvResponse);
					future.join();
	                if( future.isWritten()) {
	                    SessionLog.info(rcvSession, "#H-C#DFH# Send To "+receipts[i]+" Success!!");
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
		SessionLog.info(session, "#H-E# SendFileDataHandler");
	}
}
