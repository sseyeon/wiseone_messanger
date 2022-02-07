package com.messanger.engine.uc.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.messanger.engine.uc.Constants;

/**
 * 클라이언트 요청 데이터를 담고 있는 클래스
 * @author skoh
 *
 */
public abstract class MessageRequest extends MessageHeader {
    
	//문자열 기반의 데이터 타입-값 일 경우 저장
    final Map<String, String> propertyMap = new HashMap<String, String>();
    //데이터 값이 바이너리 타입인 경우 저장(파일데이터)
    final Map<String, ArrayList<Byte>> binaryMap = new HashMap<String, ArrayList<Byte>>();
    
    /**
     * 메세지의 헤더 타입을 저장
     * @param type 헤더 타입
     */
    public MessageRequest(String type) {
        this.type = type;
    }
    
    /**
     * 메세지의 데이터를 저장 
     * @param key 데이터 타입
     * @param value 데이터 값
     */
    public final void setProperty(String key, String value) {
        propertyMap.put(key, value);
    }
    
    /**
     * 메세지의 데이터를 불러 옴.
     * @param key 불러올 데이터 타입
     * @return 데이터 값
     */
    public final String getProperty(String key) {
        return propertyMap.get(key);
    }
    
    /**
     * 바이너리 데이터를 저장
     * @param key 데이터 타입
     * @param bytes 데이터 값
     */
    public final void setBinary(String key, byte[] bytes) {
    	ArrayList<Byte> arrBytes = new ArrayList<Byte>();
    	for(int i=0;i<bytes.length;i++) {
    		arrBytes.add(bytes[i]);
    	}
    	binaryMap.put(key, arrBytes);
    }
    
    /**
     * 바이너리 데이터를 불러 옴.
     * @param key 데이터 타입
     * @return 테이터 값
     */
    public final byte[] getBinary(String key) {
    	ArrayList<Byte> arrBytes = binaryMap.get(key);
    	if(arrBytes == null) {
    		return null;
    	}
    	byte[] bytes = new byte[arrBytes.size()];
    	for(int i=0;i<arrBytes.size();i++) {
    		bytes[i] = arrBytes.get(i);
    	}    	
    	return bytes;
    }
    
    /**
     * 요청자의 suid를 조회
     * @return 요청자의 suid
     */
    public final String getSenderUid() {
    	return getProperty(Constants.PROP_SENDER_UID);
    }
    
    public Map test(){
    	return propertyMap;
    }
}
