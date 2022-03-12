package com.messanger.engine.uc.context;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.common.IoSession;

/**
 * 서버에 연결된 session에 대해 로그인 이메일 주소를 키값으로 세션을 저장하는 클래스
 * @author skoh
 *
 */
public class IoSessionContext {

    final Map<String, IoSession> repository = new ConcurrentHashMap<String, IoSession>();
    
    static final IoSessionContext instance = new IoSessionContext();
    
    /**
     * 
     */
    private IoSessionContext() {
    }
    
    /**
     * 
     * @return
     */
    public static IoSessionContext getInstance() {
        return instance;
    }
    
    /**
     * 
     * @param uid
     * @param session
     */
    public void putSession(String key, IoSession session) {
        repository.put(key, session);
    }
    
    /**
     * 
     * @param key
     */
    public void removeSession(String key) {
        repository.remove(key);
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public IoSession getSession(String key) {
        return repository.get(key);
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return repository.containsKey(key);
    }
    
    /**
     * 
     * @return
     */
    public Iterator<Map.Entry<String, IoSession>> iterator() {
        return repository.entrySet().iterator();
    }
    
    /**
     * 
     * @return
     */
    public int size() {
        return repository.size();
    }
    
    /**
     * 
     */
    public String toString() {
        return "IoSessionContext:" + repository;
    }
}
