package com.messanger.engine.uc.handler;

import java.util.Iterator;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.USR_Request;
import com.messanger.engine.uc.message.response.USR_Response;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 사용자 상태 변경 처리 핸들러.
 * @author skoh
 *
 */
public class USR_Handler implements MessageHandler<USR_Request> {


	IoSessionContext ctx = IoSessionContext.getInstance();

	/**
	 * 그룹사내의 사용자의 상태가 변경 되면 로그인 되어 있는 다른 사용자에게 해당 사용자의 상태 변경을 알려줌
	 */
	public void messageReceived(IoSession session, USR_Request request) throws Exception {		
		SessionLog.info(session, "#H-S# UserStatusHandler");
		String status = request.getStatus();
		String uid = request.getSenderUid();
		SessionLog.info(session, "#H-C#UCH# CHANGE STATUS ["+uid+":"+status+"]");
		USR_Response response = null;
		String errMsg = "";

		response = new USR_Response(request.getType());
		response.setTransactionId(request.getTransactionId());
		String schema = SessionUtils.getScheme(session);

		try {
			for (Iterator<Map.Entry<String, IoSession>> it = ctx.iterator(); it.hasNext();) {
				Map.Entry<String, IoSession> entry = it.next();
				String targetId = entry.getKey();
				IoSession targetSession = entry.getValue();
				// 동일한 그룹사에만 상태 변경 정보를 전달한다. 2009-06-24
				if(schema.equals(SessionUtils.getScheme(targetSession)) == false) {
					continue;
				}
				if (targetId.equals(uid) == false) {
					if(targetSession != null && targetSession.isConnected()) {
						SessionLog.info(targetSession, "#H-C#UCH# Broadcast To ["+targetId+"]");
						USR_Response changeRequest = new USR_Response(Constants.TYPE_STATUS);
						changeRequest.setTransactionId("00000000");
						changeRequest.setStatusChangedUid(uid);
						changeRequest.setStatus(status);
						targetSession.write(changeRequest);
					}
				}
			}
			session.setAttribute(Constants.SESSION_KEY_STATUS, status);
			response.setResponseMsg(Constants.TRANSACTION_SUCCESS, "");
		} catch (Exception e) {
			SessionLog.error(session, e.getMessage(), e);
		}
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# UserStatusHandler");
	}
}
