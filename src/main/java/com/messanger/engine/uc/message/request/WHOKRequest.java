package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;
import com.messanger.engine.uc.model.RequestAnswer;
import com.messanger.engine.uc.model.SecureType;

public class WHOKRequest extends MessageRequest {
    /**
     * 메세지의 헤더 타입을 저장
     *
     * @param type 헤더 타입
     */
    public WHOKRequest(String type) {
        super(type);
    }

    public String getSender(){
        return getProperty(Constants.PROP_SENDER_UID);
    }

    public String getReceiver() {
        return getProperty(Constants.PROP_RECEIVER_UID);
    }
    public SecureType getSecureType() {
        return SecureType.findByCode(getProperty(Constants.PROP_REQUEST_TYPE));
    }
    public RequestAnswer getRequestAnswer() {return RequestAnswer.findByCode(getProperty(Constants.PROP_REQUEST_ANSWER));}
    public String getDescription() {
        return getProperty(Constants.PROP_MESSAGE_TYPE);
    }
}
