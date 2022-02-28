package com.messanger.engine.uc.message.request;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;
import lombok.Getter;

@Getter
public class MAILRequest extends MessageRequest {

    /**
     * 메세지의 헤더 타입을 저장
     *
     * @param type 헤더 타입
     */
    public MAILRequest(String type) {
        super(type);
    }

    private String toAddr;
    private String mailTemplate;
    private String content;

    public String getToAddr() {
        return getProperty(Constants.PROP_MAIL_RECEIVER);
    }

    public final String getContent() {
        return getProperty(Constants.PROP_MAIL_CONTENT);
    }

    public final String getMailTemplate() {
        return getProperty(Constants.PROP_MAIL_TEMPLATE);
    }
}
