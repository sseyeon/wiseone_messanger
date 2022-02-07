package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.ORGSRequest;
import com.messanger.engine.uc.message.response.ORGSResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

public class ORGSHandler implements MessageHandler<ORGSRequest> {

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
    public void messageReceived(IoSession session, ORGSRequest request) throws Exception {      
        SessionLog.info(session, "#H-S# ORGS ListHandler");
        ORGSResponse response = new ORGSResponse(request.getType());
        response.setTransactionId(request.getTransactionId());
        String errMsg = "";
        long startTime = System.currentTimeMillis();
        long tmpTime = 0L;
        try {
        	IoSession userSession = SessionUtils.getUserSession(request.getSenderUid());
        	if (userSession == null)
        	    throw new Exception("USER[" + request.getSenderUid() + "] SESSION NOT FOUND!!");
        	
        	final String localeString = SessionUtils.getLocale(userSession);
        	final String schema = SessionUtils.getScheme(userSession);
        	final String companyCode = request.getCompanyCode();
        	final String deptCode = request.getDeptCode();
        	
        	tmpTime = System.currentTimeMillis();
        	response.setOrgList(commonService.findOrganization(schema, localeString, companyCode, deptCode));
            SessionLog.info(session, "===>ORGS Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
            
        } catch (Exception e) {
        	errMsg = e.getMessage();
            SessionLog.error(session, e.getMessage(),e);            
        }
        response.setResponseMsg(errMsg);
        
        session.write(response);        
        SessionLog.info(session, "#H-E# ORGS ListHandler Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
    }
}
