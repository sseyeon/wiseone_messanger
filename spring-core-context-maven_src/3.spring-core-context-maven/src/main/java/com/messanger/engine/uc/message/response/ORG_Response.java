package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class ORG_Response extends MessageResponse {

    public ORG_Response(String type) {
        super(type);
    }
    
    public final void setOrgList(String data) {
    	setProperty(Constants.PROP_ORG_LIST, data);
    }
}
