package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class EVNTRequest extends MessageRequest {

    /**
     * 
     * @param type
     */
    public EVNTRequest(String type) {
        super(type);
    }

    /**
     * 
     * @return
     */
    public final String getURL1() {
        return super.getProperty(Constants.PROP_URL1);   
    }
    
    /** 
     * 
     * @return
     */
    public final String[] getURL2() {
        String tmp = super.getProperty(Constants.PROP_URL2);
        return StringUtils.split(tmp, Constants.PROP_DELIM);
    }
    
    /**
     * 
     * @return
     */
    public final String getApplicationCode() {
        return super.getProperty(Constants.PROP_APPCD);
    }
    
    /**
     * 
     * @return
     */
    public final String getContents() {
        return super.getProperty(Constants.PROP_SEND_MSG);
    }
    
    /**
     * 
     * @return
     */
    public final String[] getReceiverList() {
        String tmp = super.getProperty(Constants.PROP_RECEIVER_UID);
        return StringUtils.split(tmp, Constants.PROP_DELIM);
    }
    
    public final String getSender(){
    	String smsg =  super.getProperty(Constants.PROP_SEND_MSG);
    	int smsgLen = smsg.indexOf(Constants.SMSG_DELIM);
    	if( smsgLen == -1){
    		return new String();
    	}
    	return smsg.substring(0,smsgLen);
    }
    
    public final String getLocale() {
        return getProperty(Constants.PROP_LOCALE);
    }
}
