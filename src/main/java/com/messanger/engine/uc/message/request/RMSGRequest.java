package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;
import com.messanger.engine.uc.model.MsgType;

public class RMSGRequest extends MessageRequest {
    /**
     * 메세지의 헤더 타입을 저장
     *
     * @param type 헤더 타입
     */
    public RMSGRequest(String type) {
        super(type);
    }

    public String getChannelId(){
        return getProperty(Constants.PROP_CHANNEL_ID);
    }

    public String getTemplateId() {
        return getProperty(Constants.PROP_TEMPLATE_ID);
    }
    public MsgType getMessageType() {
        return MsgType.findByCode(getProperty(Constants.PROP_MESSAGE_TYPE));
    }
}
