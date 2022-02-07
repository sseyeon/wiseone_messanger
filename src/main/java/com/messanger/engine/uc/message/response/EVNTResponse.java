package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class EVNTResponse extends MessageResponse {

    public EVNTResponse(String type) {
        super(type);
    }
    
    /**
     * 
     * @param receiverUid
     */
    public final void setReceiver(String receiverUid) {
        super.setProperty(Constants.PROP_RECEIVER_UID, receiverUid);
    }
    
    /**
     * 
     * @param url
     */
    public final void setURL(String url) {
        super.setProperty(Constants.PROP_URL, url);
    }
    
    /**
     * 
     * @param applicationCode
     */
    public final void setApplicationCode(String applicationCode) {
        super.setProperty(Constants.PROP_APPCD, applicationCode);
    }
    
    /**
     * 
     * @param contents
     */
    public final void setContents(String contents) {
        super.setProperty(Constants.PROP_SEND_MSG, contents);
    }
}
