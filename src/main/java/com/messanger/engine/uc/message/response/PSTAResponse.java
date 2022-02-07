package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class PSTAResponse extends MessageResponse {

	public PSTAResponse(String type) {
		super(type);
	}
	
    public void setStatusChangedUid(String userid) {
    	setProperty(Constants.PROP_CHAGNED_STATUS_UID, userid);
    }
    
    public void setPhoneNo(String phoneNo) {
    	setProperty(Constants.PROP_SENDER_PHONENO, phoneNo);
    }
    
    public final void setStatus(String status) {
    	setProperty(Constants.PROP_PHONE_CD, status);
    }
}
