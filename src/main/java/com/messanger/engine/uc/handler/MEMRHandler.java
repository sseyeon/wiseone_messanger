package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.MEMRRequest;
import com.messanger.engine.uc.message.response.MEMRResponse;
import com.messanger.engine.uc.service.ICommonService;

/**
 * 저장된 쪽지 요청 처리 핸들러.
 * @author NEPTUNE
 *
 */
public class MEMRHandler implements MessageHandler<MEMRRequest> {

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
     * 오프라인시에 수신받아 서버에 저장되어 있는 쪽지를 읽어 해당 사용자에게 전달
     */
    public void messageReceived(IoSession session, MEMRRequest request) throws Exception {     
        SessionLog.info(session, "#H-S# ReceiveMemoHandler");
        MEMRResponse response = new MEMRResponse(request.getType());
        String errMsg = "";
        try {
            response.setTransactionId(request.getTransactionId());
            response.setReceiverUid(request.getSenderUid());
            response.setMemo(commonService.findMemo(request.getSenderUid()));
        } catch (Exception e) {
        	errMsg = e.getMessage();        	
            SessionLog.error(session, e.getMessage(),e);
        }        
        response.setResponseMsg(errMsg);        
        session.write(response); 
        SessionLog.info(session, "#H-E# ReceiveMemoHandler");
    }
}
