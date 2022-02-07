package com.messanger.engine.uc.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.messanger.engine.uc.Constants;

/**
 * 클라이언트 응답 데이터를 담고 있는 클래스
 * @author skoh
 *
 */
public abstract class MessageResponse extends MessageHeader {

	//문자열 기반의 데이터 타입-값 일 경우 저장
    final Map<String, String> dataMap = new HashMap<String, String>();
    //데이터 값이 바이너리 타입인 경우 저장(파일데이터)
    final Map<String, ArrayList<Byte>> binaryMap = new HashMap<String,ArrayList<Byte>>();
    
    /**
     * 응답 타입 설정
     * @param type 메세지 타입
     */
    public MessageResponse(String type) {
        this.type = type;
    }

    /**
     * 수신자 아이디 설정
     * @param receiverUid 수신자 아이디
     */
    public final void setReceiverUid(String receiverUid) {
    	dataMap.put(Constants.PROP_RECEIVER_UID, receiverUid);
    }
    
    /**
     * 응답 데이터 설정
     * @param key 데이터 타입
     * @param value 데이터 값
     */
    public final void setProperty(String key, String value) {
        dataMap.put(key, value);
    }
    
    /**
     * 응답 데이터 설정
     * @param map 응답 데이터 타입-값 리스트
     */
    public final void setProperty(Map<String,String> map) {
    	dataMap.putAll(map);
    }
    
    /**
     * 응답 데이터 타입으로 조회
     * @param key 데이터 타입
     * @return 데이터 값
     */
    public final String getProperty(String key) {
        return dataMap.get(key);
    }
    
    /**
     * 저장되어 있는 데이터 타입의 iterator 요청
     * @return 저장되어 있는 데이터 타입의 iterator
     */
    public final Iterator<String> getPropertyIterator() {
        return dataMap.keySet().iterator();
    }
    
    /**
     * 바이너리 기반의 데이터 타입 - 값 저장
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
     * 바이너리 기반의 데이터 값 조회
     * @param key 데이터 타입
     * @return 데이터 값
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
     * 바이너리 기반의 데이터 타입의 iterator 요청
     * @return 저장되어 있는 데이터 타입의 iterator
     */
    public final Iterator<String> getBinaryIterator() {
    	return binaryMap.keySet().iterator();
    }
    
    /**
     * 응답 코드, 응답 메세지 설정
     * @param code 응답 코드
     * @param msg 응답 메세지
     */
    public final void setResponseMsg(String code, String msg) {
    	dataMap.put(Constants.PROP_RETURN_CODE, code);
    	dataMap.put(Constants.PROP_RETURN_MSG, msg);
    }
    
    /**
     * 응답 메세지 설정. 응답 메세지가 비어 있는 경우 응답 코드 성공으로 설정
     * @param msg 응답 메세지
     */
    public void setResponseMsg(String msg) {
    	if(msg != null && msg.length() > 0) {
    		setResponseMsg(Constants.TRANSACTION_FAIL, msg);
    	} else {
    		setResponseMsg(Constants.TRANSACTION_SUCCESS, msg);
    	}
    }
}
