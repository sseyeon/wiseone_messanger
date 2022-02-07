package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class WCISResponse extends MessageResponse {

	public WCISResponse(String type) {
		super(type);
	}

	public void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
	
	public void setRoomId(String roomId) {
		setProperty(Constants.PROP_ROOM_ID, roomId);
	}	
}
