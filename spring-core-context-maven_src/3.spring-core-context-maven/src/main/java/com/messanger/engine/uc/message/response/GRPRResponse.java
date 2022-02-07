package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class GRPRResponse extends MessageResponse {

	public GRPRResponse(String type) {
		super(type);
	}

	public void setGrpList(String data) {
		setProperty(Constants.PROP_GRP_INFO, data);
	}
}
