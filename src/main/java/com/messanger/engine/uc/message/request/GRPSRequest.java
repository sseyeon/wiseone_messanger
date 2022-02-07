package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class GRPSRequest extends MessageRequest {

	public GRPSRequest(String type) {
		super(type);
	}
	
	public String getGrpInfo() {
		return getProperty(Constants.PROP_GRP_INFO);
	}

}
