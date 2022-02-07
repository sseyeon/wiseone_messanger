package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class USR_Response extends MessageResponse {

    public USR_Response(String type) {
        super(type);
    }

    public final void setStatusChangedUid(String userid) {
    	setProperty(Constants.PROP_CHAGNED_STATUS_UID, userid);
    }
    
    public final void setStatus(String status) {
    	setProperty(Constants.PROP_STATUS_CD, status);
    }
}
