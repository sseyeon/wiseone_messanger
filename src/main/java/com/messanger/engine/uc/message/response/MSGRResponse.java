package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class MSGRResponse extends MessageResponse {

    public MSGRResponse(String type) {
        super(type);
    }
    
    public final void setReceiveMsg(String msg) {
    	setProperty(Constants.PROP_SEND_MSG, msg);
    }
    
    public final void setSenderUid(String senderUid) {
        setProperty(Constants.PROP_SENDER_UID, senderUid);
    }

    public final void setRoomId(String roomId) {
        setProperty(Constants.PROP_ROOM_ID, roomId);
    }
    
    public final void setMsgFont(String font) {
    	setProperty(Constants.PROP_MSG_FONT, font);
    }
    
    public final void setMsgEffect(String effect) {
    	setProperty(Constants.PROP_MSG_EFFECT, effect);
    }
    
    public final void setMsgSize(String size) {
    	setProperty(Constants.PROP_MSG_SIZE, size);
    }
    
    public final void setMsgColor(String color) {
    	setProperty(Constants.PROP_MSG_COLOR, color);
    }
}
