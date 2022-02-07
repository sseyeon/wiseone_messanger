package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.GRPRRequest;
import com.messanger.engine.uc.message.response.GRPRResponse;
import com.messanger.engine.uc.service.ICommonService;

/**
 * 사용자 정의 그룹 데이터 요청 처리 핸들러.
 * @author skoh
 *
 */
public class GRPRHandler implements MessageHandler<GRPRRequest> {

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
     * 서버에 저장되어 있는 사용자 정의 그룹 데이터를 읽어 해당 사용자에게 전달.
     */
	@Override
	public void messageReceived(IoSession session, GRPRRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# ReceiveGrpHandler");        		
		GRPRResponse response = new GRPRResponse(request.getType());
		String errMsg = "";
		try {
			String grpInfo = commonService.findGrpInfo(request.getSenderUid());
			response.setTransactionId(request.getTransactionId());
			response.setGrpList(grpInfo);
		} catch (Exception e) {
			errMsg = e.getMessage();
			SessionLog.error(session, e.getMessage(),e);
		}		
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# ReceiveGrpHandler");		
	}
}
