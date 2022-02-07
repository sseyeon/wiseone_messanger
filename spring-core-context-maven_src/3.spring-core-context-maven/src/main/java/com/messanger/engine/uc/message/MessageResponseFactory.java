package com.messanger.engine.uc.message;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.response.EVNTResponse;
import com.messanger.engine.uc.message.response.FILDResponse;
import com.messanger.engine.uc.message.response.FILRResponse;
import com.messanger.engine.uc.message.response.FILSResponse;
import com.messanger.engine.uc.message.response.GRPRResponse;
import com.messanger.engine.uc.message.response.GRPSResponse;
import com.messanger.engine.uc.message.response.GWURResponse;
import com.messanger.engine.uc.message.response.LGINResponse;
import com.messanger.engine.uc.message.response.MEMRResponse;
import com.messanger.engine.uc.message.response.MEMSResponse;
import com.messanger.engine.uc.message.response.MSGIResponse;
import com.messanger.engine.uc.message.response.MSGRResponse;
import com.messanger.engine.uc.message.response.MSGSResponse;
import com.messanger.engine.uc.message.response.ONIDResponse;
import com.messanger.engine.uc.message.response.ORG_Response;
import com.messanger.engine.uc.message.response.PCIFResponse;
import com.messanger.engine.uc.message.response.PDIAResponse;
import com.messanger.engine.uc.message.response.PDISResponse;
import com.messanger.engine.uc.message.response.PFWDResponse;
import com.messanger.engine.uc.message.response.PSTAResponse;
import com.messanger.engine.uc.message.response.PWCHResponse;
import com.messanger.engine.uc.message.response.RCIRResponse;
import com.messanger.engine.uc.message.response.RCISResponse;
import com.messanger.engine.uc.message.response.USR_Response;
import com.messanger.engine.uc.message.response.WCIOResponse;
import com.messanger.engine.uc.message.response.WCIRResponse;
import com.messanger.engine.uc.message.response.WCISResponse;

public class MessageResponseFactory {
private static final Map<String, Class<?>> repository = new HashMap<String, Class<?>>();
    
    static {    	        
    	repository.put(Constants.TYPE_EVENT,          EVNTResponse.class);
    	repository.put(Constants.TYPE_SEND_MSGI,      MSGIResponse.class);  
    	repository.put(Constants.TYPE_LIST,           ORG_Response.class);
        repository.put(Constants.TYPE_LOGIN,          LGINResponse.class);
        
        repository.put(Constants.TYPE_RECEIVE_FILE,   FILRResponse.class);
        repository.put(Constants.TYPE_RECEIVE_GRP,    GRPRResponse.class);
        repository.put(Constants.TYPE_RECEIVE_MEMO,   MEMRResponse.class);
        repository.put(Constants.TYPE_RECEIVE_MSG,    MSGRResponse.class);
        repository.put(Constants.TYPE_SEND_FILEDATA,  FILDResponse.class);
        repository.put(Constants.TYPE_SEND_FILE,      FILSResponse.class);
        repository.put(Constants.TYPE_SEND_GRP,       GRPSResponse.class);
        repository.put(Constants.TYPE_SEND_MEMO,      MEMSResponse.class);
        repository.put(Constants.TYPE_SEND_MSG,       MSGSResponse.class);        
        repository.put(Constants.TYPE_STATUS,         USR_Response.class);
        repository.put(Constants.TYPE_GROUPWARE_UNREAD, GWURResponse.class);
        
        repository.put(Constants.TYPE_PHONE_CALLINFO, PCIFResponse.class);
        repository.put(Constants.TYPE_PHONE_DIAL,     PDIAResponse.class);
        repository.put(Constants.TYPE_PHONE_HANGUP,   PDISResponse.class);
        repository.put(Constants.TYPE_PHONE_FORWARD,  PFWDResponse.class);   
        repository.put(Constants.TYPE_PHONE_STATUS,   PSTAResponse.class);
        
        repository.put(Constants.TYPE_SEND_WC,        WCISResponse.class);
        repository.put(Constants.TYPE_RECEIVE_WC,     WCIRResponse.class);
        repository.put(Constants.TYPE_OPEN_WC,  	  WCIOResponse.class);
        
        repository.put(Constants.TYPE_SEND_REMOTECTL,    RCISResponse.class);
        repository.put(Constants.TYPE_RECEIVE_REMOTECTL, RCIRResponse.class);
        
        repository.put(Constants.TYPE_ONLINE_ID, ONIDResponse.class);
        repository.put(Constants.TYPE_PASSWORD_CHANGE, PWCHResponse.class);
    }
    
    /**
     * 주어진 헤더타입의 응답 메세지 인스턴스 생성
     * @param type 헤더 타입
     * @return 헤더 타입의 응답 메세지 인스턴스
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static MessageResponse createMessage(String type) throws Exception {
        Class<?> clazz = repository.get(type);
        Constructor c = clazz.getConstructor(String.class);
        MessageResponse messageResponse = (MessageResponse)c.newInstance(type);
        if (messageResponse == null)
            throw new RuntimeException("Invalid response message type[" + type + "]");
        
        return (MessageResponse)c.newInstance(type);
    }
    
    /**
     * 주어진 헤더 타입의 응답 메세지 클래스가 존재하는지 검사
     * @param type 헤더 타입
     * @return 헤더 타입의 응답 메세지 클래스가 존재하면 true, 그렇지 않으면 false
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
