package com.messanger.engine.uc.ipt.service;

import java.io.IOException;

import org.apache.mina.common.IoSession;

import com.messanger.engine.uc.ipt.PhoneBook;

public class DummyIptService implements IIptService {

	@Override
	public String getId(String phoneNo) {
		return null;
	}

	@Override
	public PhoneBook getPhoneBook() {
		return null;
	}

	@Override
	public void init() {
	}

	@Override
	public boolean isAuth() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public void sendConnRequest(String hashKey) throws IOException {
	}

	@Override
	public void sendDialRequest(String peerNo, String callNo)
			throws IOException {
	}

	@Override
	public void sendForwardRequest(String peerNo, String callNo)
			throws IOException {
	}

	@Override
	public void sendHangupRequest(String peerNo) throws IOException {
	}

	@Override
	public void sendIngChannelRequest() throws IOException {
	}

	@Override
	public void sendInnerLineInfoRequest(byte type, String peerNo)
			throws IOException {
	}

	@Override
	public void sendKeepAliveRequest() throws IOException {
	}

	@Override
	public void setAuth(boolean isAuth) {
	}

	@Override
	public void setConnected(boolean isConnected) {
	}

	@Override
	public void setPhoneBook(PhoneBook phoneBook) {
	}

	@Override
	public void setSession(IoSession session) {
	}
	
	@Override
	public boolean isIpt(String phoneNumber, String prefix) {
		return false;
	}
}
