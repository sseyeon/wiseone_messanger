package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class PDISRequest extends MessageRequest {

	public PDISRequest(String type) {
		super(type);
	}
	
	public String getSenderPhoneNo() {
		return getProperty(Constants.PROP_SENDER_PHONENO);
	}
}
