package com.messanger.engine.uc.message;

/**
 * 프로토콜 메세지 헤더
 * @author skoh
 *
 */
public class MessageHeader {
    
    String type;

    String transactionId;

    int length;

    /**
     * 헤더 타입 조회
     * @return 헤더 타입
     */
    public String getType() {
        return type;
    }

    /**
     * 헤더 타입 설정
     * @param type 헤더 타입
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 트랜잭션 아이디 조회
     * @return 트랜잭션 아이디
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 트랜잭션 아이디 설정
     * @param transactionId 트랜잭션 아이디
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 패킷 길이 조회
     * @return 패킷 길이
     */
    public int getLength() {
        return length;
    }

    /**
     * 패킷 길이 설정
     * @param length 패킷 길이
     */
    public void setLength(String length) {
        this.length = Integer.parseInt(length);
    }
}
