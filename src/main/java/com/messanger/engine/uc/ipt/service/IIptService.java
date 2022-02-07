package com.messanger.engine.uc.ipt.service;

import java.io.IOException;

import org.apache.mina.common.IoSession;

import com.messanger.engine.uc.ipt.PhoneBook;
import com.messanger.engine.uc.ipt.exception.IllegalIptProtocolException;

public interface IIptService {
	
	public void init();	
	
	/**
	 * IPT 프로토콜 규격에 맞게 연결 요청을 한다.
	 * @param hashKey 해쉬키
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendConnRequest(String hashKey) throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 전화걸기 요청을 한다.
	 * @param peerNo 자신의 내선 번호
	 * @param callNo 연결하고자 하는 상대방 전화 번호
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendDialRequest(String peerNo, String callNo) throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 전화끊기 요청을 한다.
	 * @param peerNo 연결을 끊고자 하는 내선 번호
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendHangupRequest(String peerNo) throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 통화중 착신 전환 요청을 한다.
	 * @param peerNo 착신전환을 하려는 내선번호. 현재 통화중인 호에서 수신자측 번호.
	 * @param callNo 새롭게 연결을 하려는 전화번호.
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendForwardRequest(String peerNo, String callNo) throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 keep alive 메세지를 전송한다.
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendKeepAliveRequest() throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 현재 통화중인 목록 요청을 한다.
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendIngChannelRequest() throws IOException, IllegalIptProtocolException;
	
	/**
	 * IPT 프로토콜 규격에 맞게 내선정보 요청을 한다.
	 * @param type 0:전체조회, 1: 특정회선 조회
	 * @param peerNo type이 1인 경우 조회할 내선 번호.
	 * @throws IOException
	 * @throws IllegalIptProtocolException
	 */
	public void sendInnerLineInfoRequest(byte type, String peerNo) throws IOException, IllegalIptProtocolException;
	
	/**
	 * 전화번호로 사용자 아이디를 조회한다.
	 * @param phoneNo 전화번호
	 * @return 사용자 아이디
	 */
	public String    getId(String phoneNo);
	
	/**
	 * IPT 연결중인지 상태를 조회한다.
	 * @return 연결중이면 true, 그렇지 않으면 false
	 */
	public boolean   isConnected();
	
	/**
	 * IPT 연결 상태를 설정한다.
	 * @param isConnected 연결 상태
	 */
	public void      setConnected(boolean isConnected);
	
	/**
	 * IPT 인증되었는지를 조회한다.
	 * @return 인증되었으면 true, 그렇지 않으면 false
	 */
	public boolean   isAuth();
	
	/**
	 * IPT 인증여부를 설정한다.
	 * @param isAuth 인증 여부
	 */
	public void      setAuth(boolean isAuth);
	
	/**
	 * 내선번호-사용자 아이디를 가지고 있는 전화번호부를 설정한다.
	 * @param phoneBook 전화번호부 instance
	 */
	public void      setPhoneBook(PhoneBook phoneBook);
	
	/**
	 * 내선번호-사용자 아이디를 가지고 있는 전화번호부를 받아온다.
	 * @return 전화번호부 instance
	 */
	public PhoneBook getPhoneBook();
	
	/**
	 * IPT 연결 세션을 저장한다.
	 * @param session 연결 세션
	 */
	public void      setSession(IoSession session);	
	
	public boolean isIpt(String phoneNumber, String prefix);
}
