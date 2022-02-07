package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.ORG_Request;
import com.messanger.engine.uc.message.response.ORG_Response;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 조직도 정보 요청 처리 핸들러
 * @author skoh
 *
 */
public class ORG_Handler implements MessageHandler<ORG_Request> {

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
     * 조직도 정보를 읽어 포맷에 맞게 응답한다.
     */
    public void messageReceived(IoSession session, ORG_Request request) throws Exception {      
        SessionLog.info(session, "#H-S# ListHandler");
        ORG_Response response = new ORG_Response(request.getType());
        response.setTransactionId(request.getTransactionId());
        String errMsg = "";
        long startTime = System.currentTimeMillis();
        long tmpTime = 0L;
        try {
        	IoSession userSession = SessionUtils.getUserSession(request.getSenderUid());
        	if (userSession == null)
        	    throw new Exception("USER[" + request.getSenderUid() + "] SESSION NOT FOUND!!");
        	
        	String localeString = SessionUtils.getLocale(userSession);
        	String schema = SessionUtils.getScheme(userSession);
        	
        	tmpTime = System.currentTimeMillis();
            String data = commonService.findAllOrganization(schema, localeString);            
            SessionLog.info(session, "===>ORG Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
        	
            response.setOrgList(data);            
        } catch (Exception e) {
        	errMsg = e.getMessage();
            SessionLog.error(session, e.getMessage(),e);            
        }
        response.setResponseMsg(errMsg);
        
        session.write(response);        
        SessionLog.info(session, "#H-E# ListHandler Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
    }
}
