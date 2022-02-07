package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class WCISRequest extends MessageRequest {

	public WCISRequest(String type) {
		super(type);
	}
	
    /**
     * 
     * @return
     */
    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
    
    /**
     * 
     * @return
     */
    public final String getRoomId() {
        return getProperty(Constants.PROP_ROOM_ID);
    }
}
