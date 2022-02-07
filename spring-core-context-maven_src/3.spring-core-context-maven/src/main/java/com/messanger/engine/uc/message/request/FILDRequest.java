package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class FILDRequest extends MessageRequest {

	public FILDRequest(String type) {
		super(type);
	}
	
	public String getFileKey() {
		return getProperty(Constants.PROP_FILE_KEY);
	}
	
	public byte[] getData() {
		return getBinary(Constants.PROP_SEND_BIN);
	}
	
	public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
}
