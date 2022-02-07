package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class MSGIResponse extends MessageResponse {

    public MSGIResponse(String type) {
        super(type);
    }

    public final void setSenderUid(String senderUid) {
        setProperty(Constants.PROP_SENDER_UID, senderUid);
    }

    public final void setRoomId(String roomId) {
        setProperty(Constants.PROP_ROOM_ID, roomId);
    }
    
    public final void setMSCD(String mscd){
    	setProperty(Constants.PROP_MSCD, mscd);
    }
}
