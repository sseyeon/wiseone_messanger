package com.messanger.engine.uc.message.request;

import org.apache.commons.lang.StringUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;

public class MSGIRequest extends MessageRequest {

    public MSGIRequest(String type) {
        super(type);
    }

    /**
     * 
     * @return
     */
    public final String[] getReceipt() {
    	return StringUtils.split(getProperty(Constants.PROP_RECEIVER_UID), Constants.PROP_DELIM);
    }
    
    /**
     * 
     * @return
     */
    public final String getRoomId() {
        return getProperty(Constants.PROP_ROOM_ID);
    }
    
    /**
     * MSCD : 0 대화창 글입력 창에 글쓰는 중.
     * MSCD : 1 대화창 글입력 창에 글이 없음.
     * @return
     */
    public final String getMSCD(){
    	return getProperty(Constants.PROP_MSCD);
    }
}
