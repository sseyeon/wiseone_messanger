package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class GWURRequest extends MessageRequest {

	public GWURRequest(String type) {
		super(type);
	}
	
	public final String getDomainName() {
        String senderUid = getSenderUid();
        return senderUid.substring(senderUid.indexOf('@') + 1);
    }
	
	public String getAPCD() {
		return getProperty(Constants.PROP_APPCD);
	}

}
