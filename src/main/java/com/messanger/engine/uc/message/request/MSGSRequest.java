package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.model.SecureType;
import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class MSGSRequest extends MessageRequest {

    public MSGSRequest(String type) {
        super(type);
    }

    public final String getSendMsg() {
    	return getProperty(Constants.PROP_SEND_MSG);
    }   

    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
    
    public final String getRoomId() {
        return getProperty(Constants.PROP_ROOM_ID);
    }
    
    public final String getMsgFont() {
    	return getProperty(Constants.PROP_MSG_FONT);
    }
    
    public final String getMsgEffect() {
    	return getProperty(Constants.PROP_MSG_EFFECT);
    }
    
    public final String getMsgSize() {
    	return getProperty(Constants.PROP_MSG_SIZE);
    }
    
    public final String getMsgColor() {
    	return getProperty(Constants.PROP_MSG_COLOR);
    }

    public SecureType getSecureType() {
        return SecureType.findByCode(getProperty(Constants.PROP_REQUEST_TYPE));
    }
}
