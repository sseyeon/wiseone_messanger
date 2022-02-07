package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class PFWDRequest extends MessageRequest {

	public PFWDRequest(String type) {
		super(type);	
	}
	
	public String getSenderPhoneNo() {
		return getProperty(Constants.PROP_SENDER_PHONENO);
	}
	
	public String getReceiverPhoneNo() {
		return getProperty(Constants.PROP_RECEIVER_PHONENO);
	}
	
	public String getReceiverUid() {
		return getProperty(Constants.PROP_RECEIVER_UID);
	}
}
