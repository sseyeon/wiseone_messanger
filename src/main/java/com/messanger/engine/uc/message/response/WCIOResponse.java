package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class WCIOResponse extends MessageResponse {

	public WCIOResponse(String type) {
		super(type);
	}
	
	public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
	
	public void setWID(String wid) {
		setProperty(Constants.PROP_WCROOM_ID, wid);
	}
	
	public void setRoomId(String cid) {
		setProperty(Constants.PROP_ROOM_ID, cid);
	}
}
