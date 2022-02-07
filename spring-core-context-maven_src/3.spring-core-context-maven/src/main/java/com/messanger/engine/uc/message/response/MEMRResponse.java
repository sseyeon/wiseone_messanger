package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class MEMRResponse extends MessageResponse {

    public MEMRResponse(String type) {
        super(type);
    }

    public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
    
    public final void setMemo(String memo) {
    	setProperty(Constants.PROP_SEND_MSG, memo);
    }
}
