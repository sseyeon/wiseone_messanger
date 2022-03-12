package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class WHOKResponse extends MessageResponse {
    public WHOKResponse(String type) {
        super(type);
    }
    public void setSenderUid(String senderUid) {
        setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
    public void setStatus(String status) {setProperty(Constants.PROP_STATUS_CD, status);}
    public void setRoomId(String roomId) {
        setProperty(Constants.PROP_ROOM_ID, roomId);
    }
}
