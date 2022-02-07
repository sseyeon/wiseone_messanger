package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class FILDResponse extends MessageResponse {

	public FILDResponse(String type) {
		super(type);
	}
	
	public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
	
	public void setFileKey(String key) {
		setProperty(Constants.PROP_FILE_KEY, key);
	}
	
	public void setData(byte[] data) {
		setBinary(Constants.PROP_SEND_BIN, data);
	}
}
