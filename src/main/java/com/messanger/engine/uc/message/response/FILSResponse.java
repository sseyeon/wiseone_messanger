package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class FILSResponse extends MessageResponse {

    public FILSResponse(String type) {
        super(type);
    }

    public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
    
    public void setFileName(String name) {
    	setProperty(Constants.PROP_FILE_NAME, name);
    }
    
    public void setFileSize(String size) {
    	setProperty(Constants.PROP_FILE_SIZE, size);
    }
    
    public void setFileKey(String key) {
    	setProperty(Constants.PROP_FILE_KEY, key);
    }
    
    public final void setRoomId(String roomId) {
        setProperty(Constants.PROP_ROOM_ID, roomId);
    }
}
