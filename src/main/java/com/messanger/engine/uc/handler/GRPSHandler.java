package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.GRPSRequest;
import com.messanger.engine.uc.message.response.GRPSResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 사용자 정의 그룹 정보 전송 처리 핸들러.
 * @author skoh
 *
 */
public class GRPSHandler implements MessageHandler<GRPSRequest> {

	IoSessionContext ctx = IoSessionContext.getInstance();
	ICommonService commonService;
    
    /**
     * 
     * @param commonService
     */
    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }
    
    /**
     * 사용자 정의 그룹 정보를 수신 받아 서버에 저장한다.
     */
	@Override
	public void messageReceived(IoSession session, GRPSRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# SendGrpHandler");
        GRPSResponse response = new GRPSResponse(request.getType());
		String localeString = SessionUtils.getLocale(session);
		String errMsg = "";
		try {		
			commonService.createGrpInfo(localeString, request.getSenderUid(), request.getGrpInfo());
			response.setTransactionId(request.getTransactionId());
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(),e);
		}		
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# SendGrpHandler");		
	}
}
