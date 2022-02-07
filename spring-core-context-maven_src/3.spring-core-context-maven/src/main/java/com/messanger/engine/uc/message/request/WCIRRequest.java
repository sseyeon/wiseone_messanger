package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class WCIRRequest extends MessageRequest {

	public WCIRRequest(String type) {
		super(type);
	}
	
    /**
     * 
     * @return
     */
    public String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }    
    
    public String getOCD() {
    	return getProperty(Constants.PROP_OPTION_CD);
    }
    
    public String getRoomId() {
    	return getProperty(Constants.PROP_ROOM_ID);
    }
    
    public String getWID() {
    	return getProperty(Constants.PROP_WCROOM_ID);
    }
}
