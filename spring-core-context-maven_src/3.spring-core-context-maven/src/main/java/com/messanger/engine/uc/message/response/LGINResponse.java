package com.messanger.engine.uc.message.response;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;

public class LGINResponse extends MessageResponse {

    public LGINResponse(String type) {
        super(type);
    }
    
    public final void setUserId(String userId) {
    	setProperty(Constants.PROP_SENDER_UID, userId);
    }
    
    public final void setMemoCount(int count) {
    	setProperty(Constants.PROP_MEMO_CNT, String.valueOf(count));
    }
    
    public final void setMailCount(int count) {
    	setProperty(Constants.PROP_MAIL_CNT, String.valueOf(count));
    }
    
    public final void setWFCount(int count) {
    	setProperty(Constants.PROP_WF_CNT, String.valueOf(count));
    }
    
    public final void setXFon(String opt) {
    	setProperty(Constants.PROP_USE_XFON, opt);
    }
    
    public final void setWCon(String opt) {
    	setProperty(Constants.PROP_USE_WCON, opt);
    }
    
    public final void setOnid(String onlineId) {
    	setProperty(Constants.PROP_STATUS_CD, onlineId);
    }
    
    public final void setUserName(String userName) {
    	setProperty(Constants.PROP_USER_NAME, userName);
    }
    
    public final void setDomain(String domain) {
    	setProperty(Constants.PROP_DOMAIN, domain);
    }
    
    public final void setEmpno(String empno) {
    	setProperty(Constants.PROP_EMPN, empno);
    }
}
