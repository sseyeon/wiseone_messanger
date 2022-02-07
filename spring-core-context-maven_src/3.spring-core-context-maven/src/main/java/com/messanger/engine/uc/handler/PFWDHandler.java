package com.messanger.engine.uc.handler;

import java.io.IOException;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.ipt.exception.IllegalIptProtocolException;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.message.request.PFWDRequest;
import com.messanger.engine.uc.message.response.PFWDResponse;
import com.messanger.engine.uc.utils.SessionUtils;
import com.messanger.engine.uc.utils.StringUtil;
import com.messanger.engine.uc.utils.Validator;

/**
 * 착신전화 전달 처리 핸들러
 * @author skoh
 *
 */
public class PFWDHandler implements MessageHandler<PFWDRequest> {
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
	 * 착신전화 전달 요청 메세지를 전달
	 */
	@Override
	public void messageReceived(IoSession session, PFWDRequest request)
			throws Exception {
		SessionLog.info(session, "#H-S# PhoneForwardHandler");
		String errMsg = "";
		PFWDResponse response = new PFWDResponse(request.getType());
		response.setTransactionId(request.getTransactionId());
		
		if (Validator.isValidPhoneNumber(request.getSenderPhoneNo()) == false
				|| Validator.isValidPhoneNumber(request.getReceiverPhoneNo()) == false) {
			SessionLog.warn(session, "#H-S#PFH# SPNO or RPNO is required");			
			errMsg = messageSource.getMessage("11005", null, SessionUtils.getSessionLocale(session));        	
		} else {
			try {
				String senderPhoneNo = request.getSenderPhoneNo();
				if(senderPhoneNo != null) {
					senderPhoneNo = StringUtil.getReplaceFirst(senderPhoneNo, prefixPhoneNum);
				}
				
				String receiverPhoneNo = request.getReceiverPhoneNo();
				if(receiverPhoneNo != null) {
					receiverPhoneNo = StringUtil.getReplaceFirst(receiverPhoneNo, prefixPhoneNum);
				}
				
				iptService.setSession(session);
				iptService.sendForwardRequest(senderPhoneNo, receiverPhoneNo);
			} catch (IllegalIptProtocolException e) {
				errMsg = messageSource.getMessage(e.getMessage(), new Object[] {request.getSenderPhoneNo()}, SessionUtils.getSessionLocale(session));
				SessionLog.error(session, errMsg);
			} catch (IOException e) {
				response.setResponseMsg(messageSource.getMessage("11001", null, SessionUtils.getSessionLocale(session)));
				SessionLog.error(session, e.getMessage());
			}
		}
		response.setResponseMsg(errMsg);
		session.write(response);
		SessionLog.info(session, "#H-E# PhoneForwardHandler");
	}
}
