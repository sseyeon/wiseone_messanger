package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class FILRRequest extends MessageRequest {

    public FILRRequest(String type) {
        super(type);
    }

    public String getFileKey() {
    	return getProperty(Constants.PROP_FILE_KEY);
    }
    
    public String getOCD() {
    	return getProperty(Constants.PROP_OPTION_CD);
    }
    
    //2009-11-05 강희동 추가
    public final String getRoomId() {
        return getProperty(Constants.PROP_ROOM_ID);
    }
    
    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
}
