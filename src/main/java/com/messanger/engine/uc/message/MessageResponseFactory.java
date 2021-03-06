package com.messanger.engine.uc.message;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.response.*;

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

        repository.put(Constants.TYPE_MAIL,         MAILResponse.class);
        repository.put(Constants.TYPE_GROUP_MESSAGE,         RMSGResponse.class);
        repository.put(Constants.TYPE_WHOK,         WHOKResponse.class);
    }
    
    /**
     * ????????? ??????????????? ?????? ????????? ???????????? ??????
     * @param type ?????? ??????
     * @return ?????? ????????? ?????? ????????? ????????????
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
     * ????????? ?????? ????????? ?????? ????????? ???????????? ??????????????? ??????
     * @param type ?????? ??????
     * @return ?????? ????????? ?????? ????????? ???????????? ???????????? true, ????????? ????????? false
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
