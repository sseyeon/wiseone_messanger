package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.PWCHRequest;
import com.messanger.engine.uc.message.response.PWCHResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.DigestUtil;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * password 변경
 * @author dhchoi
 */
public class PWCHHandler implements MessageHandler<PWCHRequest>{
	ICommonService commonService;
    IoSessionContext ctx = IoSessionContext.getInstance();
    
    /**
     * 
     * @param commonService
     */
    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;        
    }
    
	@Override
	public void messageReceived(IoSession session, PWCHRequest request) throws Exception {
		PWCHResponse response =new PWCHResponse(request.getType());
		String errMsg = "";
		
		final String userid = request.getUserId();
		final String domainName = request.getDomainName();
		final String daoName = commonService.findDaoName(domainName);
//		final String schema = commonService.findScheme(domainName);
		String password = request.getPasswd();
//		password = DigestUtil.woDigestPassword(password, "SHA");
		try {
			commonService.modifyPassword(SessionUtils.getUid(session), daoName, userid, password);
			response.setResponseMsg(Constants.TRANSACTION_SUCCESS, "");
		} catch (Exception e) {
			errMsg = e.getMessage();
			response.setResponseMsg(errMsg);
			SessionLog.error(session, e.getMessage(), e);
		}
		response.setTransactionId(request.getTransactionId());
        session.write(response);
        SessionLog.info(session, "#H-E# PWCHHandler");
	}

}
