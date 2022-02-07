package com.messanger.engine.uc.message.request;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class MEMSRequest extends MessageRequest {

    public MEMSRequest(String type) {
        super(type);
    }

    public final String getMemo() {
    	return getProperty(Constants.PROP_SEND_MSG);
    }
    
    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
    
    public Map getTest(){
    	return super.test();
    }
}
