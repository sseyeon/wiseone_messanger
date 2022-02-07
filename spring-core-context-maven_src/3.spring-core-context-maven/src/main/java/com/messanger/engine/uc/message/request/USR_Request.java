package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class USR_Request extends MessageRequest {

    public USR_Request(String type) {
        super(type);
    }

    public final String getStatus() {
    	return getProperty(Constants.PROP_STATUS_CD);
    }
}
