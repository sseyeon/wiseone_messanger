package com.messanger.engine.uc.ipt.vendor.cisco;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//public class CiscoIptEvent implements UcfPhoneEvent {
public class CiscoIptEvent {
	private Log LOG = LogFactory.getLog(CiscoIptEvent.class);
	/*
	private Config config;

	private PeerEventMsgRetHandler peerEventMsgRetHandler = null;
	private List<String> listAbsence = new LinkedList<String>();
	private PhoneBook phoneBook = null;

	public CiscoIptEvent(Config config) {
		this.config = config;
	}

	public void init() {
		int iListPort = 0;
		try {
			iListPort = Integer.parseInt(config.get("ipt.middleware.listener.port"));
		} catch (Exception e) {
			LOG.error("ipt.middleware.listener.port error!!!");
		}
		UcfListener listener = new UcfListener(iListPort);
		listener.start();
		listener.addObserver(this);
	}
	
	public void setPeerEventMsgRetHandler(PeerEventMsgRetHandler peerEventMsgRetHandler) {
		this.peerEventMsgRetHandler = peerEventMsgRetHandler;
	}
	
	public void setPhoneBook(PhoneBook phoneBook) {
		this.phoneBook = phoneBook;
	}

	@Override
	public synchronized void PhoneEvent(UcfPhoneInfo phoneInfo) {
		int status = phoneInfo.getCallState();

		switch (status) {
			case UcfPhoneInfo.PHONE_CTI_OFF:
				info(phoneInfo, "PHONE_CTI_OFF");
				break;
			case UcfPhoneInfo.PHONE_CTI_ON:
				info(phoneInfo, "PHONE_CTI_ON");
				break;
			case UcfPhoneInfo.PHONE_OFF:
				info(phoneInfo, "PHONE_OFF");
				break;
			case UcfPhoneInfo.PHONE_ON:
				info(phoneInfo, "PHONE_ON");
				break;
			case UcfPhoneInfo.PHONE_FORWARD_OFF:
				info(phoneInfo, "PHONE_FORWARD_OFF");
				break;
			case UcfPhoneInfo.PHONE_FORWARD_ON:
				info(phoneInfo, "PHONE_FORWARD_ON");
				break;
			case UcfPhoneInfo.CALL_START:
				info(phoneInfo, "CALL_START");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_START, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_START_CONSULT:
				info(phoneInfo, "CALL_START_CONSULT");
				break;
			case UcfPhoneInfo.CALL_DIALING:
				info(phoneInfo, "CALL_DIALING");
				break;
			case UcfPhoneInfo.CALL_DIALING_CONSULT:
				info(phoneInfo, "CALL_DIALING_CONSULT");
				break;
			case UcfPhoneInfo.CALL_ALERTING:
				info(phoneInfo, "CALL_ALERTING");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_ALERTING, phoneInfo.getPhoneNo(), "");
				//부재중 처리 1
				addAbsence(phoneInfo);
				break;
			case UcfPhoneInfo.CALL_ALERTING_CONSULT:
				info(phoneInfo, "CALL_ALERTING_CONSULT");
				break;
			case UcfPhoneInfo.CALL_ALERTING_TRANSFER:
				info(phoneInfo, "CALL_ALERTING_TRANSFER");
				break;
			case UcfPhoneInfo.CALL_ALERTING_CONFERENCE:
				info(phoneInfo, "CALL_ALERTING_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_RINGING:
				info(phoneInfo, "CALL_RINGING");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_RINGING, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_RINGING_CONSULT:
				info(phoneInfo, "CALL_RINGING_CONSULT");
				break;
			case UcfPhoneInfo.CALL_RINGING_TRANSFER:
				info(phoneInfo, "CALL_RINGING_TRANSFER");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_RINGING, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_RINGING_CONFERENCE:
				info(phoneInfo, "CALL_RINGING_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_CONNECT:
				info(phoneInfo, "CALL_CONNECT");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_CONNECT, phoneInfo.getPhoneNo(), "");
				//부재중 처리 2
				removeAbsence(phoneInfo);
				break;
			case UcfPhoneInfo.CALL_CONNECT_CONSULT:
				info(phoneInfo, "CALL_CONNECT_CONSULT");
				break;
			case UcfPhoneInfo.CALL_CONNECT_TRANSFER:
				info(phoneInfo, "CALL_CONNECT_TRANSFER");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_CONNECT, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_CONNECT_CONFERENCE:
				info(phoneInfo, "CALL_CONNECT_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_END:
				info(phoneInfo, "CALL_END");
				if(phoneInfo.getCallCount() == 0) {
					onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_END, phoneInfo.getPhoneNo(), "");
				}
				//부재중 처리 3
				if(isAbsence(phoneInfo)) {
					removeAbsence(phoneInfo);
					onPeerDeviceStatusEvent(CiscoConstants.EVT_STATUS_CALL_ABSENCE, phoneInfo.getPhoneNo(), phoneInfo.getTargetPhoneNo());
					String[] recipients = {phoneBook.getId(phoneInfo.getTargetPhoneNo())};
					String subject = "부재중 전화 알림";
					
					User user = phoneBook.getUser(phoneInfo.getPhoneNo());
					String from = user.getEmail();
					String fromName = user.getUserName();
					String message = "발신자 정보\n\n";
					message += "이름 : " + user.getUserName() + "\n";
					message += "직위 : " + user.getPosName() + "\n";
					message += "회사번호 : " + user.getCompPhoneNumber() + "\n";
					message += "휴대폰 : " + user.getMobileNumber() + "\n";
					
					String smtp = config.get("mail.smtp.host");
					
					try {
						MailUtils.postmail(recipients, subject, message, from, fromName, smtp);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case UcfPhoneInfo.CALL_END_CONSULT:
				info(phoneInfo, "CALL_END_CONSULT");
				break;
			case UcfPhoneInfo.CALL_END_TRANSFER:
				info(phoneInfo, "CALL_END_TRANSFER");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_END, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_END_CONFERENCE:
				info(phoneInfo, "CALL_END_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_FAIL:
				info(phoneInfo, "CALL_FAIL");
				onPeerDeviceStatusEventBroadCast(CiscoConstants.EVT_STATUS_CALL_FAIL, phoneInfo.getPhoneNo(), "");
				break;
			case UcfPhoneInfo.CALL_FAIL_CONSULT:
				info(phoneInfo, "CALL_FAIL_CONSULT");
				break;
			case UcfPhoneInfo.CALL_BUSY:
				info(phoneInfo, "CALL_BUSY");
				break;
			case UcfPhoneInfo.CALL_BUSY_CONSULT:
				info(phoneInfo, "CALL_BUSY_CONSULT");
				break;
			case UcfPhoneInfo.CALL_FAIL_ELSE:
				info(phoneInfo, "CALL_FAIL_ELSE");
				break;
			case UcfPhoneInfo.CALL_FAIL_ELSE_CONSULT:
				info(phoneInfo, "CALL_FAIL_ELSE_CONSULT");
				break;
			case UcfPhoneInfo.CALL_HOLD_ON:
				info(phoneInfo, "CALL_HOLD_ON");
				break;
			case UcfPhoneInfo.CALL_HOLD_ON_CONSULT:
				info(phoneInfo, "CALL_HOLD_ON_CONSULT");
				break;
			case UcfPhoneInfo.CALL_HOLD_ON_TRANSFER:
				info(phoneInfo, "CALL_HOLD_ON_TRANSFER");
				break;
			case UcfPhoneInfo.CALL_HOLD_ON_CONFERENCE:
				info(phoneInfo, "CALL_HOLD_ON_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_HOLD_OFF:
				info(phoneInfo, "CALL_HOLD_OFF");
				break;
			case UcfPhoneInfo.CALL_HOLD_OFF_CONSULT:
				info(phoneInfo, "CALL_HOLD_OFF_CONSULT");
				break;
			case UcfPhoneInfo.CALL_HOLD_OFF_TRANSFER:
				info(phoneInfo, "CALL_HOLD_OFF_TRANSFER");
				break;
			case UcfPhoneInfo.CALL_HOLD_OFF_CONFERENCE:
				info(phoneInfo, "CALL_HOLD_OFF_CONFERENCE");
				break;
			case UcfPhoneInfo.CALL_RECORD_ON:
				info(phoneInfo, "CALL_RECORD_ON");
				break;
			case UcfPhoneInfo.CALL_RECORD_OFF:
				info(phoneInfo, "CALL_RECORD_OFF");
				break;
			case UcfPhoneInfo.CALL_NOTICE_RINGING:
				info(phoneInfo, "CALL_NOTICE_RINGING");
				break;
			default:
				info(phoneInfo, String.valueOf(status));
		}

	}
	
	public void addAbsence(UcfPhoneInfo phoneInfo) {
		String element = getDateString("yyyyMMdd") + phoneInfo.getPhoneNo() + phoneInfo.getTargetPhoneNo();
		listAbsence.add(element);
		if(listAbsence.size() > 1000) {
			listAbsence.remove(0);
		}
	}
	
	public void removeAbsence(UcfPhoneInfo phoneInfo) {
		String element = getDateString("yyyyMMdd") + phoneInfo.getPhoneNo() + phoneInfo.getTargetPhoneNo();
		listAbsence.remove(element);
	}
	
	public boolean isAbsence(UcfPhoneInfo phoneInfo) {
		String element = getDateString("yyyyMMdd") + phoneInfo.getPhoneNo() + phoneInfo.getTargetPhoneNo();
		return listAbsence.contains(element);
	}
	
	public String getDateString(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String sDate = formatter.format(cal.getTime());
		return sDate;
	}
	
	public void info(UcfPhoneInfo phoneInfo, String sStatus) {
		if(LOG.isInfoEnabled()) {
			LOG.info(
					" TIME[" + getDateString("yyyy-MM-dd HH:mm:ss") + "] " + 
					" SEQ[" + phoneInfo.getSeqNo() + "] " +
					" PHONE[" + phoneInfo.getPhoneNo() + "] " +
					" TARGET_PHONE[" + phoneInfo.getTargetPhoneNo() + "] " +
					" BEFORE_PHONE[" + phoneInfo.getBeforePhoneNo() + "] " +
					" CALL_COUNT[" + phoneInfo.getCallCount() + "] " +
					" STATUS[" + sStatus + "]");
		}
	}

	private void onPeerDeviceStatusEvent(byte status, String peerNo, String callNo) {
		try {
			peerEventMsgRetHandler.messageSendPeerEvent(status, peerNo, callNo);
		} catch (Exception exception) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("onPeerDeviceStatusEvent - peerNo<->callNO [%s <-> %s], Dispatch Exception = [%s] ",
								peerNo, callNo, exception.getMessage()));
			}
		}
	}

	private void onPeerDeviceStatusEventBroadCast(byte status, String peerNo, String callNo) {
		try {
			peerEventMsgRetHandler.messageReceived(status, peerNo, callNo);
		} catch (Exception exception) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("onPeerDeviceStatusEvent - peerNo<->callNO [%s <-> %s], Dispatch Exception = [%s] ",
								peerNo, callNo, exception.getMessage()));
			}
		}
	}
	*/
}
