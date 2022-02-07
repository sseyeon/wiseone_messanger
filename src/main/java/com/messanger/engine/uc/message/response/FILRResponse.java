package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class FILRResponse extends MessageResponse {

	public FILRResponse(String type) {
		super(type);
	}
	
	public final void setSenderUid(String senderUid) {
    	setProperty(Constants.PROP_SENDER_UID, senderUid);
    }
	
	public void setFileKey(String key) {
		setProperty(Constants.PROP_FILE_KEY, key);
	}
	
	public void setOCD(String ocd) {
		setProperty(Constants.PROP_OPTION_CD, ocd);
	}
	
	//2009-11-05 강희동 추가 
	public final void setRoomId(String roomId) {
        setProperty(Constants.PROP_ROOM_ID, roomId);
    }
}
