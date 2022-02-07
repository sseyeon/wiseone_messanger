package com.messanger.engine.uc.handler;

import java.util.Iterator;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.ONIDRequest;
import com.messanger.engine.uc.message.response.ONIDResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 
 * @author 김용성
 * @description 대화명 변경 핸들러
 * @since 2010-03-24
 * 
 */
public class ONIDHandler implements MessageHandler<ONIDRequest>{
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
	public void messageReceived(IoSession session, ONIDRequest request)
			throws Exception {
		ONIDResponse response =new ONIDResponse(request.getType());
		String errMsg = "";
		
		final String userid = request.getUserId();
		final String domainName = request.getDomainName();
		final String daoName = commonService.findDaoName(domainName);
		final String schema = commonService.findScheme(domainName);
		final String onlineId = request.getU_CD();
		
		try {
			commonService.modifyOnlineId(SessionUtils.getUid(session), daoName, schema, userid, onlineId);
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, "#H-E# ONIDHandler UPDATE ERROR" + e.getStackTrace());
		}
		
		response.setTransactionId(request.getTransactionId());
		try {
			for (Iterator<Map.Entry<String, IoSession>> it = ctx.iterator(); it.hasNext();) {
				Map.Entry<String, IoSession> entry = it.next();
				String targetId = entry.getKey();
				
				IoSession targetSession = entry.getValue();
				if (targetId.equals(request.getSenderUid()) == false) {
					if(targetSession != null && targetSession.isConnected()) {
						SessionLog.info(targetSession, "#H-C#ONID# Broadcast To ["+targetId+","+request.getSenderUid()+","+userid+"]");
						ONIDResponse changeRequest = new ONIDResponse(request.getType());
						changeRequest.setTransactionId(Constants.TRANSACTION_INIT_CODE);
						changeRequest.setStatusChangedUid(request.getSenderUid());
						changeRequest.setOnlineId(onlineId);
						targetSession.write(changeRequest);
					}
				}
			}
			response.setResponseMsg(Constants.TRANSACTION_SUCCESS, "");
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(), e);
		}
		response.setResponseMsg(errMsg);        
        session.write(response);
        SessionLog.info(session, "#H-E# ONIDHandler");
	}

}
