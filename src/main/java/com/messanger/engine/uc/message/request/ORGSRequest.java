package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class ORGSRequest extends MessageRequest {
    public ORGSRequest(String type) {
        super(type);
    }
    
    public String getCompanyCode(){
    	return getProperty(Constants.PROP_COMPANY_CODE);
    }
    
    public String getDeptCode(){
    	return getProperty(Constants.PROP_DEPT_CODE);
    }
}
