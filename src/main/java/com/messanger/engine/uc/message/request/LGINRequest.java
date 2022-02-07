package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public final class LGINRequest extends MessageRequest {

    public LGINRequest(String type) {
        super(type);
    }

    public final String getLocale() {
        return getProperty(Constants.PROP_LOCALE);
    }
    
    public final String getDomainName() {
        String senderUid = getSenderUid();
        return senderUid.substring(senderUid.indexOf('@') + 1);
    }
    
    public final String getUserId() {
        String senderUid = getSenderUid();
        if( senderUid.indexOf('@') != -1 )
        	return senderUid.substring(0, senderUid.indexOf('@'));
        return senderUid;
    }
    
    public final String getPasswd() {
        return getProperty(Constants.PROP_PASSWD);
    }
    
    public final String getClientVersion() {
    	return getProperty(Constants.PROP_CLIENT_VERSION);
    }
    
    public final String getCompanyCode(){
    	return getProperty(Constants.PROP_COMPANY_CODE);
    	//임시테스트용
    	//String senderUid = getSenderUid();
    	//return senderUid.split("@")[1];
    }
}
