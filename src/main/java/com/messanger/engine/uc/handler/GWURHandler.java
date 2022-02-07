package com.messanger.engine.uc.handler;

import java.util.Map;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.GWURRequest;
import com.messanger.engine.uc.message.response.GWURResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.DebugUtils;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 그룹웨어에서 요청한 정보를 읽어 처리하는 핸들러
 * @author skoh
 *
 */
public class GWURHandler implements MessageHandler<GWURRequest> {
    
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
     * 그룹웨어의 요청 정보를 읽어 사용자에게 전달
     * 현재는 읽지않은 메일, 결제해야할 결제 문서에 대한 조회만 구현되어 있다.
     */
    public void messageReceived(IoSession session, GWURRequest request) throws Exception {
        SessionLog.info(session, "#H-S# GWUR Handler");
        //String suid = request.getSenderUid();
        String apcd = request.getAPCD();
         
        String errMsg = "";
        
        GWURResponse response = new GWURResponse(request.getType());
        response.setTransactionId(request.getTransactionId());
       
        final String email = session.getAttribute(Constants.SESSION_KEY_EMAIL).toString();
        final String domainName =   session.getAttribute(Constants.SESSION_KEY_DOMAIN).toString();
        String daoName =      commonService.findDaoName(domainName);
        String schema =       commonService.findScheme(domainName);
        String locale =       SessionUtils.getLocale(session);        

        SessionLog.debug(session,"-********************************************************************");
        SessionLog.debug(session,"daoName, schema, email, domainName, locale");
        SessionLog.debug(session, daoName+":"+ schema+":"+ email+":"+ domainName+":"+ locale );
        SessionLog.debug(session,"-********************************************************************");
        
        
        Map<String,String> resultMap = null;
//        if(apcd.compareTo(Constants.APCD_EMAIL) == 0) {
//        	resultMap = commonService.getUnreadMail(daoName, schema, email, domainName, locale);
//        } else if(apcd.compareTo(Constants.APCD_WF) == 0) {
//        	resultMap = commonService.getUnreadWF(daoName, schema, email, domainName, locale);
//        } else {
        	errMsg = messageSource.getMessage("10027", new Object[] {apcd}, SessionUtils.getLocale(locale));
//        }
//        DebugUtils.printMap(resultMap);
        SessionLog.debug(session,"-----result map -----");
        SessionLog.debug(session, resultMap.toString() );
        SessionLog.debug(session,"----------");
        
        if(resultMap == null || resultMap.size() == 0) {
        	// if count is 0 
        	resultMap.put(Constants.PROP_APPCD, apcd);
        	resultMap.put(Constants.PROP_URL1, "");
        	resultMap.put(Constants.PROP_URL2, "");
        	resultMap.put(Constants.PROP_SEND_MSG, "");
        }
        response.setProperty(resultMap);
        
        response.setResponseMsg(errMsg);
        session.write(response);
        SessionLog.info(session, "#H-E# GWUR Handler");
    }
}
