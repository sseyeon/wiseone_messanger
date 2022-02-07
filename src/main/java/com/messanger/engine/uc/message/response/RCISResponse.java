package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class RCISResponse extends MessageResponse {

	public RCISResponse(String type) {
		super(type);
	}
	
	public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
	
	public void setAddr(String addr) {
		setProperty(Constants.PROP_ADDRESS, addr);
	}
	
	public void setRoomId(String roomId) {
		setProperty(Constants.PROP_ROOM_ID, roomId);
	}
}
