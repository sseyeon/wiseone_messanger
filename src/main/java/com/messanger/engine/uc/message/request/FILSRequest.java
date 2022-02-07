package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class FILSRequest extends MessageRequest {

    public FILSRequest(String type) {
        super(type);
    }

    public String getFileName() {
    	return getProperty(Constants.PROP_FILE_NAME);
    }
    
    public String getFileSize() {
    	return getProperty(Constants.PROP_FILE_SIZE);
    }
    
    public String getFileKey() {
    	return getProperty(Constants.PROP_FILE_KEY);
    }
    
    public final String getRoomId() {
        return getProperty(Constants.PROP_ROOM_ID);
    }
    
    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
}
