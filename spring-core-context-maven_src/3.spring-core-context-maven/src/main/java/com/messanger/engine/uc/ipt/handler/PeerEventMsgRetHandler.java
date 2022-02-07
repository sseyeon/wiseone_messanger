package com.messanger.engine.uc.ipt.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.ipt.PhoneBook;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.utils.PushDataUtils;

/**
 * peer event 메세지에 대한 처리 핸들러
 * 
 * @author skoh
 * 
 */
public class PeerEventMsgRetHandler {
	private Log LOG = LogFactory.getLog(PeerEventMsgRetHandler.class);

	private IIptService iptService;

	public void setIptService(IIptService iptService) {
		this.iptService = iptService;
	}

//	/**
//	 * 전화 통화, 끊김 등의 이벤트를 전달 받아 모든 사용자에게 알려준다.
//	 */
//	@Override
//	public void messageReceived(IoSession session, PeerEventMsgRetBody message)
//			throws Exception {
//		LOG.info(message.toString());
//		PhoneBook phoneBook = iptService.getPhoneBook();
//		if (phoneBook == null) {
//			return;
//		}
//		phoneBook.setPEMStatus(message.getPeerNo(), message.getStatus());
//
//		String suid = iptService.getId(message.getPeerNo());
//		String ruid = iptService.getId(message.getCallNo());
//		LOG.info(suid + "-->" + ruid);
//
//		if (suid == null) {
//			LOG.warn("UID not found: " + message.getPeerNo());
//			return;
//		}
//		Map<String, String> data = new HashMap<String, String>();
//		data.put(Constants.PROP_CHAGNED_STATUS_UID, suid);
//		data.put(Constants.PROP_SENDER_PHONENO, message.getPeerNo());
//
//		String pCD = PhoneBook.cvtPEMStatusToPCD(message.getStatus());
//		if (pCD == null) {
//			LOG.warn("Unexpected PeerEvent status:" + message.toString());
//			return;
//		}
//		data.put(Constants.PROP_PHONE_CD, pCD);
//		PushDataUtils.broadcastData(suid, Constants.TYPE_PHONE_STATUS, data,
//				true);
//	}
	
	/**
	 * 전화 통화, 끊김 등의 이벤트를 전달 받아 모든 사용자에게 알려준다.
	 */
	public void messageReceived(byte status, String peerNo, String callNo)
			throws Exception {
		PhoneBook phoneBook = iptService.getPhoneBook();
		if (phoneBook == null) {
			return;
		}
		phoneBook.setPEMStatus(peerNo, status);

		String suid = iptService.getId(peerNo);
		String ruid = iptService.getId(callNo);
		LOG.info(suid + "-->" + ruid);

		if (suid == null) {
			LOG.warn("UID not found: " + peerNo);
			return;
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.PROP_CHAGNED_STATUS_UID, suid);
		data.put(Constants.PROP_SENDER_PHONENO, peerNo);

		String pCD = PhoneBook.cvtPEMStatusToPCD(status);
		if (pCD == null) {
			LOG.warn("Unexpected PeerEvent pCD:" + pCD);
			return;
		}
		data.put(Constants.PROP_PHONE_CD, pCD);
		PushDataUtils.broadcastData(suid, Constants.TYPE_PHONE_STATUS, data,
				true);
	}	

	/**
	 * 
	 * @param session
	 * @param message
	 * @throws Exception
	 */
	public void messageSendPeerEvent(byte status, String peerNo, String callNo) throws Exception {
		PhoneBook phoneBook = iptService.getPhoneBook();
		if (phoneBook == null) {
			return;
		}
		phoneBook.setPEMStatus(peerNo, status);

		String suid = iptService.getId(peerNo);
		String ruid = iptService.getId(callNo);
		LOG.info(suid + "-->" + ruid);

		if (suid == null) {
			LOG.warn("UID not found: " + peerNo);
			return;
		}
		Map<String, String> data = new HashMap<String, String>();
		data.put(Constants.PROP_CHAGNED_STATUS_UID, suid);
		data.put(Constants.PROP_SENDER_PHONENO, peerNo);
		data.put(Constants.PROP_RECEIVER_UID, ruid);
		data.put(Constants.PROP_RECEIVER_PHONENO, callNo);

		String pCD = PhoneBook.cvtPEMStatusToPCD(status);
		if (pCD == null) {
			LOG.warn("Unexpected PeerEvent pCD:" + pCD);
			return;
		}
		data.put(Constants.PROP_PHONE_CD, pCD);
		PushDataUtils.pushData(suid, Constants.TYPE_PHONE_STATUS, data);
	}

}
