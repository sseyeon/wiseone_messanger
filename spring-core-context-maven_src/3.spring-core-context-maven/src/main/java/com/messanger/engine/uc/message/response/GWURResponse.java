package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class GWURResponse extends MessageResponse {

	public GWURResponse(String type) {
		super(type);
	}
	
	public void setUrl1(String url) {
		setProperty(Constants.PROP_URL1, url);
	}
	
	public void setUrl2(String url) {
		setProperty(Constants.PROP_URL2, url);
	}
	
	public void setAPCD(String apcd) {
		setProperty(Constants.PROP_APPCD, apcd);
	}
	
	public void setSendMsg(String smsg) {
		setProperty(Constants.PROP_SEND_MSG, smsg);
	}
}
