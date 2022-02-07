package com.messanger.engine.uc.handler;

import java.io.IOException;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.ipt.exception.IllegalIptProtocolException;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.message.request.PDISRequest;
import com.messanger.engine.uc.message.response.PDISResponse;
import com.messanger.engine.uc.utils.SessionUtils;
import com.messanger.engine.uc.utils.StringUtil;
import com.messanger.engine.uc.utils.Validator;

/**
 * 전화 끊기 처리 핸들러
 * @author skoh
 *
 */
public class PDISHandler implements MessageHandler<PDISRequest> {
	private Config config;
	private String prefixPhoneNum;
	private String localExchangeNumber;
	private IIptService iptService;
	private ReloadableResourceBundleMessageSource messageSource;
	
	public void setConfig(Config config) {
		this.config = config;
		prefixPhoneNum = config.get("ipt.phonenum.prefix");
	}
	
	public void setIptService(IIptService scoreBoard) {
		this.iptService = scoreBoard;
	}
	
	public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }

	/**
	 * 전화 끊기 요청 메세지를 전달
	 */
	@Override
	public void messageReceived(IoSession session,
			PDISRequest request) throws Exception {		
		SessionLog.info(session, "#H-S# PhoneDialHandler");
		String errMsg = "";
		PDISResponse response = new PDISResponse(request.getType());
		response.setTransactionId(request.getTransactionId());
		
		if (!Validator.isValidPhoneNumber(request.getSenderPhoneNo())) {
			SessionLog.warn(session, "#H-S#PSH# SPNO is required");			
			errMsg = messageSource.getMessage("11005", null, SessionUtils.getSessionLocale(session));			
		} else {
			try {
				String senderPhoneNo = request.getSenderPhoneNo();
				if(senderPhoneNo != null) {
					senderPhoneNo = StringUtil.getReplaceFirst(senderPhoneNo, prefixPhoneNum);
				}
				
				iptService.setSession(session);
				iptService.sendHangupRequest(senderPhoneNo);
			} catch (IllegalIptProtocolException e) {
				errMsg = messageSource.getMessage(e.getMessage(), new Object[] {request.getSenderPhoneNo()}, SessionUtils.getSessionLocale(session));
				SessionLog.error(session, errMsg);
			} catch (IOException e) {								
				errMsg = messageSource.getMessage("11001", null, SessionUtils.getSessionLocale(session));
				SessionLog.error(session, e.getMessage());
			}			
		}
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# PhoneDisconnectHandler");
	}
}
