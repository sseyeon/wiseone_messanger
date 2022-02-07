package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;



public class GRPRRequest extends MessageRequest {

	public GRPRRequest(String type) {
		super(type);
	}
	
	public void setGrpInfo(String data) {
		setProperty(Constants.PROP_ORG_LIST, data);
	}
}
