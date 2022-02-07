package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class WCIRResponse extends MessageResponse {

	public WCIRResponse(String type) {
		super(type);
	}
	
	public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
		
	public void setOCD(String ocd) {
		setProperty(Constants.PROP_OPTION_CD, ocd);
	}
	
	public void setWID(String wid) {
		setProperty(Constants.PROP_WCROOM_ID, wid);
	}
	
	public void setRoomId(String cid) {
		setProperty(Constants.PROP_ROOM_ID, cid);
	}
}
