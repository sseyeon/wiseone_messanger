package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class ONIDResponse extends MessageResponse {

    public ONIDResponse(String type) {
        super(type);
    }
    
    public final void setStatusChangedUid(String userid) {
    	setProperty(Constants.PROP_CHAGNED_STATUS_UID, userid);
    }
    
    public final void setOnlineId(String onlineId) {
    	setProperty(Constants.PROP_STATUS_CD, onlineId);
    }

}
