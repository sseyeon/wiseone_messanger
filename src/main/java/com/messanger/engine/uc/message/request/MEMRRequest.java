package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class MEMRRequest extends MessageRequest {

    public MEMRRequest(String type) {
        super(type);
    }
    
    public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
    
    public final void setSendMemo(String sendMemo) {
    	setProperty(Constants.PROP_SEND_MSG, sendMemo);
    }

}
