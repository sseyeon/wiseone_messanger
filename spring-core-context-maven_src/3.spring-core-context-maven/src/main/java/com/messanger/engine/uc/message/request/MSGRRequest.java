package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class MSGRRequest extends MessageResponse {

    public MSGRRequest(String type) {
        super(type);
    }

    public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
    
    public final void setSendMsg(String sendMsg) {
    	setProperty(Constants.PROP_SEND_MSG, sendMsg);
    }
}
