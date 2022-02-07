package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class PCIFResponse extends MessageResponse {

	public PCIFResponse(String type) {
		super(type);		
	}
		
	public void setSenderUid(String uid) {
		setProperty(Constants.PROP_SENDER_UID, uid);
	}
	
	public void setSenderPhoneNo(String phoneNo) {
		setProperty(Constants.PROP_SENDER_PHONENO, phoneNo);
	}
	
	public void setReceiverPhoneNo(String phoneNo) {
		setProperty(Constants.PROP_RECEIVER_PHONENO, phoneNo);
	}
	
	public void setCallDuration(String seconds) {
		setProperty(Constants.PROP_SECONDS, seconds);
	}
}
