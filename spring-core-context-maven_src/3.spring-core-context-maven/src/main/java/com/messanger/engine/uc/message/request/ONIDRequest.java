package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class ONIDRequest extends MessageRequest {

	public ONIDRequest(String type) {
		super(type);
	}

	public final String getDomainName() {
		String senderUid = getSenderUid();
		return senderUid.substring(senderUid.indexOf('@') + 1);
	}

	public final String getUserId() {
		String senderUid = getSenderUid();
		return senderUid.substring(0, senderUid.indexOf('@'));
	}
	
	public final String getU_CD() {
        return getProperty(Constants.PROP_STATUS_CD);
    }
}
