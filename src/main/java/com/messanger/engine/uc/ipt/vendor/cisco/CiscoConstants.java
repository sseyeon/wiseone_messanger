package com.messanger.engine.uc.ipt.vendor.cisco;

public class CiscoConstants {
	public static final byte EVT_STATUS_CALL_START = 0x08;	//2010
	public static final byte EVT_STATUS_CALL_ALERTING = 0x01;	//2030 전화 연결중입니다.
	public static final byte EVT_STATUS_CALL_RINGING = 0x02;	//2040 전화가 왔습니다.
	public static final byte EVT_STATUS_CALL_CONNECT = 0x03;	//2050 전화가 연결되었습니다.
	public static final byte EVT_STATUS_CALL_END = 0x04;	//2060 통화가 종료되었습니다.
	public static final byte EVT_STATUS_CALL_FAIL = 0x09;	//2070 전화연결이 실패하였습니다.
	public static final byte EVT_STATUS_CALL_ABSENCE = 0x06;	//부재중알림
}
