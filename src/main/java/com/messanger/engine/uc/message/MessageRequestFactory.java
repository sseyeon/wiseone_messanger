package com.messanger.engine.uc.message;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.request.*;

public final class MessageRequestFactory {
private static final Map<String, Class<?>> repository = new HashMap<String, Class<?>>();
    
    static {
    	repository.put(Constants.TYPE_CHECK,         ALCKRequest.class);        
    	repository.put(Constants.TYPE_EVENT,         EVNTRequest.class);
    	repository.put(Constants.TYPE_SEND_MSGI,     MSGIRequest.class);  
    	repository.put(Constants.TYPE_LIST,          ORG_Request.class);
        repository.put(Constants.TYPE_LOGIN,         LGINRequest.class);
        repository.put(Constants.TYPE_RECEIVE_FILE,  FILRRequest.class);
        repository.put(Constants.TYPE_RECEIVE_GRP,   GRPRRequest.class);
        repository.put(Constants.TYPE_RECEIVE_MEMO,  MEMRRequest.class);
        repository.put(Constants.TYPE_RECEIVE_MSG,   MSGRRequest.class);
        repository.put(Constants.TYPE_SEND_FILEDATA, FILDRequest.class);
        repository.put(Constants.TYPE_SEND_FILE,     FILSRequest.class);
        repository.put(Constants.TYPE_SEND_GRP,      GRPSRequest.class);
        repository.put(Constants.TYPE_SEND_MEMO,     MEMSRequest.class);
        repository.put(Constants.TYPE_SEND_MSG,      MSGSRequest.class);        
        repository.put(Constants.TYPE_STATUS,        USR_Request.class);
        repository.put(Constants.TYPE_GROUPWARE_UNREAD,   GWURRequest.class);
        
        repository.put(Constants.TYPE_PHONE_DIAL,    PDIARequest.class);
        repository.put(Constants.TYPE_PHONE_HANGUP,  PDISRequest.class);
        repository.put(Constants.TYPE_PHONE_FORWARD, PFWDRequest.class);
        
        repository.put(Constants.TYPE_SEND_WC,       WCISRequest.class);
        repository.put(Constants.TYPE_RECEIVE_WC,    WCIRRequest.class);
        repository.put(Constants.TYPE_OPEN_WC,       WCIORequest.class);
        
        repository.put(Constants.TYPE_SEND_REMOTECTL,    RCISRequest.class);
        repository.put(Constants.TYPE_RECEIVE_REMOTECTL, RCIRRequest.class);
        
        repository.put(Constants.TYPE_ONLINE_ID, ONIDRequest.class);
        repository.put(Constants.TYPE_PASSWORD_CHANGE, PWCHRequest.class);
        
        repository.put(Constants.TYPE_PONC,         PDIARequest.class);

        repository.put(Constants.TYPE_MAIL,         MAILRequest.class);
        repository.put(Constants.TYPE_GROUP_MESSAGE,         RMSGRequest.class);
        repository.put(Constants.TYPE_WHOK,         WHOKRequest.class);
    }
    
    /**
     * 주어진 헤더 타입의 요청 메세지 인스턴스 생성
     * @param type 헤더 타입
     * @return 주어진 헤더타입의 요청 메시지 인스턴스
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static MessageRequest createMessage(String type) throws Exception {
        Class<?> clazz = repository.get(type);
        if(clazz == null) {
        	throw new RuntimeException("Invalid request message type[" + type + "]");
        }
        Constructor c = clazz.getConstructor(String.class);
        MessageRequest messageRequest = (MessageRequest)c.newInstance(type);
        if (messageRequest == null) {
            throw new RuntimeException("Invalid request message type[" + type + "]");
        }
        
        return (MessageRequest)c.newInstance(type);
    }
    
    /**
     * 주어진 헤더 타입의 요청 메세지 클래스가 존재하는지 검사
     * @param type 헤더 타입
     * @return 주어진 헤더 타입의 요청 메세지 클래스가 존재하면 true, 그렇지 않으면 false
     */
    public static boolean containsKey(String type) {
        return repository.containsKey(type);
    }
    
    /**
     * 
     * @param type
     * @return
     */
    public static String getHandlerName(short type) {
        return repository.get(type).getSimpleName();
    }
}
