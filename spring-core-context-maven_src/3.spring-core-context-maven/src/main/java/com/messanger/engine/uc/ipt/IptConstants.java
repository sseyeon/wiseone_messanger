package com.messanger.engine.uc.ipt;

public class IptConstants {
	public static final String DEFAULT_CHARSET = "US-ASCII";
	public static final char   CMD_ECHO        = 0x01; // ECHO for test
	public static final char   CMD_CONNECT     = 0x0A; // Connect
	public static final char   CMD_CALLCONTROL = 0x0B; // Call Control
	public static final char   CMD_MANAGER     = 0x0C; // PBX Manager
	public static final char   CMD_EVENT       = 0x0D; // PBX Event

	public static final char   MSG_10          = 0x0A;
	public static final char   MSG_11          = 0x0B;
	public static final char   MSG_12          = 0x0C;
	public static final char   MSG_13          = 0x0D;
	public static final char   MSG_14          = 0x0E;
	
	public static final int    PROTOCOL_VERSION = 1;
	
	public static final byte   TRUNK_SIP              = 0x00;
	public static final byte   TRUNK_FXO              = 0x01;
	public static final byte   TRUNK_PRI              = 0x02;
	public static final byte   TRUNK_IAX2             = 0x03;
	public static final byte   TRUNK_H323             = 0x04;
	
	//---------------------------------------------------------- 
	// [ This is used in the XFon ]
	//----------------------------------------------------------
	/*
	public static final byte   EVT_STATUS_RING        = 0x00;
	public static final byte   EVT_STATUS_CALL        = 0x01;
	public static final byte   EVT_STATUS_HANGUP      = 0x02;
	public static final byte   EVT_STATUS_REGISTER    = 0x03;
	public static final byte   EVT_STATUS_UNREGISTER  = 0x04;
	*/
	
	// [ This is used in the Samsung 7500 PBX ]
	public static final byte   EVT_STATUS_RING        = 0x00;  //연결중
	public static final byte   EVT_STATUS_CALL        = 0x01;  // 호연결상태
	public static final byte   EVT_STATUS_MISSCALL 	  = 0x02;  // 전활를 걸었는 안 받을때 또는 통화중
	public static final byte   EVT_STATUS_ONCALL	  = 0x03;  // 
	public static final byte   EVT_STATUS_HANGUP      = 0x04;  // 수화긴 든거
	public static final byte   EVT_STATUS_REGISTER    = 0x05;  //
	public static final byte   EVT_STATUS_UNREGISTER  = 0x06;	
	public static final byte   EVT_STATUS_READY	  	  = 0x07;
	public static final byte   EVT_STATUS_NOREADY  	  = 0x08;
	public static final byte   EVT_STATUS_FAIL  	  = 0x09;
	
	
	
	
	public static final byte   IN_TYPE_ALL            = 0x00;
	public static final byte   IN_TYPE_ONE            = 0x01;
	
	public static final byte   IN_STATUS_DISCONNECT   = 0x00;
	public static final byte   IN_STATUS_CONNECT      = 0x01;
	public static final byte   IN_STATUS_SENDING      = 0x02;
	public static final byte   IN_STATUS_RECEIVING    = 0x03;
	public static final byte   IN_STATUS_CALLING      = 0x04;
	
	public static final byte   INOUT_SEND             = 0x00;
	public static final byte   INOUT_RECEIVE          = 0x01;
	public static final byte   INOUT_INNER            = 0x02;
	
	public static final int    LEN_UCHAR              = 1;
	public static final int    LEN_USHORT             = 2;
	public static final int    LEN_UINT               = 4;
	
	public static final int    LEN_INNER_LINE         = 4;
}
