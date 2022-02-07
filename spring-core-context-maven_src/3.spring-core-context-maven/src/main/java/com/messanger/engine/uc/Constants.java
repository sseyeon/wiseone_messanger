package com.messanger.engine.uc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Constants {

    public static final String   DEFAULT_CONFIGURATION    = "ServerConfiguration.xml";
    public static final String   DEFAULT_BUNDLE           = "";
    public static final String   DEFAULT_CHARSET          = "UTF-8";
    public static final String   ORACLE_QUERY             = "OracleQuery.xml";
    public static final String   XFON_PROTOCOL            = "XFonProtocol.xml";
    public static final String   XXXXXXX_DB_NAME          = "WODB";
    public static final String   SCHEME_TOKEN             = "$scheme$";
    public static final String   SCHEME_PREFIX            = "wo";
    public static final String   DEFAULT_SCHEME           = "wo00003";
    public static final String   NODE_USER_DIRECTORY_PATH = "userDirectoryPath";
    public static final String   FILE_SECRET_KEY          = "secretkey.ser";
    public static final String   SECURITY_ALGORIZM        = "DEC";
    public static final int      SESSION_IDLE             = 60;

    public static final String   SESSION_KEY_LOCALE       = "SESSION_KEY_LOCALE";
    public static final String   SESSION_KEY_UID          = "SESSION_KEY_UID";
    public static final String   SESSION_KEY_UINX         = "SESSION_KEY_UINX";
    public static final String   SESSION_KEY_COMPNAY_NAME = "SESSION_KEY_COMPANY_NAME";
    public static final String   SESSION_KEY_DEPT_NAME    = "SESSION_KEY_DEPT_NAME";
    public static final String   SESSION_KEY_USER_NAME    = "SESSION_KEY_USER_NAME";
    public static final String   SESSION_KEY_SCHEME       = "SESSION_KEY_SCHEME";
    public static final String   SESSION_KEY_STATUS       = "SESSION_KEY_STATUS";
    public static final String   SESSION_KEY_DOMAIN       = "SESSION_KEY_DOMAIN";
    public static final String   SESSION_KEY_EMAIL        = "SESSION_KEY_EMAIL";
    public static final String   SESSION_KEY_DAONAME      = "SESSION_KEY_DAONAME";
    
    public static final String   PROP_DELIM               = "|";
    public static final String   ROW_DELIM                = "|";
    public static final String   COL_DELIM                = "^";
    public static final String   SMSG_DELIM                = "-^";
    public static final String   DATE_PATTERN             = "yyyy/MM/dd k:mm:ss";
    
    public static final char     GWUR_ROW_DELIM           = (char) 0x08;
    public static final char     GWUR_COL_DELIM           = (char) 0x07;

    public static final int      XFON_PACKET_HEADER_LEN   = 8;
    public static final int      HEADER_LENGTH            = 20;    
    public static final int      HEADER_TYPE              = 4;
    public static final int      TRANSACTION_ID           = 8;
    public static final int      DATA_LENGTH              = 8;
    public static final int      PROP_KEY_LENGTH          = 4;
    public static final int      PROP_VAL_LENGTH          = 8;

    public static final byte[]   MESSAGE_SOH_BYTES        = { -30, -108, -93 };
    public static final byte[]   MESSAGE_EOH_BYTES        = { -30, -108, -85 };
    public static final String   MESSAGE_SOH              = "┣";
    public static final String   MESSAGE_EOH              = "┫";
    public static final char     EOF                      = (char) 0x1A;
    
    public static final int      PACKET_REQUIRE_LENGTH    = HEADER_LENGTH + MESSAGE_SOH_BYTES.length + MESSAGE_EOH_BYTES.length;

    public static final String   TYPE_LOGIN               = "LGIN";    
    public static final String   TYPE_LIST                = "ORG_";
    public static final String   TYPE_EVENT               = "EVNT";
    public static final String   TYPE_STATUS              = "USR_";
    public static final String   TYPE_CHECK               = "ALCK";    
    public static final String   TYPE_SEND_MSG            = "MSGS";    
    public static final String   TYPE_SEND_MEMO           = "MEMS";
    public static final String   TYPE_SEND_GRP            = "GRPS";    
    public static final String   TYPE_SEND_FILE           = "FILS";
    public static final String   TYPE_SEND_FILEDATA       = "FILD";
    public static final String   TYPE_SEND_MSGI           = "MSGI";
    public static final String   TYPE_RECEIVE_MSG         = "MSGR";    
    public static final String   TYPE_RECEIVE_MEMO        = "MEMR";
    public static final String   TYPE_RECEIVE_GRP         = "GRPR";
    public static final String   TYPE_RECEIVE_FILE        = "FILR";
    public static final String   TYPE_SEND_REMOTECTL      = "RCIS";
    public static final String   TYPE_RECEIVE_REMOTECTL   = "RCIR";
    public static final String   TYPE_GROUPWARE_UNREAD    = "GWUR";
    public static final String   TYPE_ONLINE_ID    		  = "ONID";
    public static final String   TYPE_PASSWORD_CHANGE	  = "PWCH";
    
    public static final String   TYPE_PHONE_DIAL          = "PDIA";
    public static final String   TYPE_PHONE_HANGUP        = "PDIS";
    public static final String   TYPE_PHONE_FORWARD       = "PFWD";
    public static final String   TYPE_PHONE_STATUS        = "PSTA";
    public static final String   TYPE_PHONE_CALLINFO      = "PCIF";
    public static final String   TYPE_SEND_WC             = "WCIS";
    public static final String   TYPE_RECEIVE_WC          = "WCIR";
    public static final String   TYPE_OPEN_WC             = "WCIO";
    public static final String   TYPE_PONC                = "PONC";

    public static final String   PROP_LOCALE              = "LOCL";
    public static final String   PROP_RETURN_CODE         = "R_CD";
    public static final String   PROP_RETURN_MSG          = "RMSG";
    public static final String   PROP_SENDER_UID          = "SUID";
    public static final String   PROP_RECEIVER_UID        = "RUID";
    public static final String   PROP_PASSWD              = "PASS";
    public static final String   PROP_URL                 = "URL_";
    public static final String   PROP_URL1                = "URL1";
    public static final String   PROP_URL2                = "URL2";
    public static final String   PROP_APPCD               = "APCD";
    public static final String   PROP_MEMO_CNT            = "MEMC";
    public static final String   PROP_MAIL_CNT            = "EMLC";
    public static final String   PROP_WF_CNT              = "WRKC";
    public static final String   PROP_CHAGNED_STATUS_UID  = "USLT";
    public static final String   PROP_ROOM_ID             = "C_ID";
    public static final String   PROP_STATUS_CD           = "U_CD";
    public static final String   PROP_USER_IP             = "U_IP";    
    public static final String   PROP_SEND_MSG            = "SMSG";
    public static final String   PROP_ORG_LIST            = "ORGL";
    public static final String   PROP_GRP_INFO            = "GRPI";    
    public static final String   PROP_FILE_NAME           = "FNAM";
    public static final String   PROP_FILE_SIZE           = "FSZE";
    public static final String   PROP_FILE_KEY            = "FKEY";
    public static final String   PROP_OPTION_CD           = "O_CD";
    public static final String   PROP_SEND_BIN            = "SBIN";
    public static final String   PROP_SENDER_PHONENO      = "SPNO";
    public static final String   PROP_RECEIVER_PHONENO    = "RPNO";
    public static final String   PROP_SECONDS             = "SECS";
    public static final String   PROP_PHONE_CD            = "P_CD";  
    public static final String   PROP_WCROOM_ID           = "W_ID";
    public static final String   PROP_ADDRESS             = "ADDR";
    public static final String   PROP_MSG_FONT            = "MSFT";
    public static final String   PROP_MSG_EFFECT          = "MSEF";
    public static final String   PROP_MSG_SIZE            = "MSSZ";
    public static final String   PROP_MSG_COLOR           = "MSCR";
    public static final String   PROP_CLIENT_VERSION      = "VER_";
    public static final String   PROP_USE_XFON            = "XFON";
    public static final String   PROP_USE_WCON            = "WCON";
    public static final String   PROP_COMPANY_CODE        = "CMPC";
    public static final String   PROP_DEPT_CODE           = "DPTC";
    public static final String   PROP_USER_NAME           = "U_NM";
    public static final String   PROP_DOMAIN              = "DOMN";
    public static final String   PROP_MSCD                = "MSCD";
    public static final String   PROP_EMPN                = "EMPN";
    
    public static final String   APCD_CODE                = "C";
    public static final String   APCD_WF                  = "000001";
    public static final String   APCD_BOARD               = "000002";
    public static final String   APCD_POLL                = "000003";
    public static final String   APCD_COP                 = "000004";
    public static final String   APCD_CONTACT             = "000005";
    public static final String   APCD_BLOG                = "000006";
    public static final String   APCD_KMS                 = "000007";
    public static final String   APCD_CONFIG              = "000008";
    public static final String   APCD_WF_CONFIG           = "000009";
    public static final String   APCD_BOARD_TMP           = "000010";
    public static final String   APCD_EMAIL               = "000011";
    public static final String   APCD_WF_MIGRATION        = "000012";
    public static final String   APCD_MEMO_ANOTHER_SYSTEM = "000090";
    
    public static final String   STATUS_ONLINE            = "01";
    public static final String   STATUS_OFFLINE           = "02";
    public static final String   STATUS_ABSENCE           = "03";
    public static final String   STATUS_BUSY              = "04";
    public static final String   STATUS_SPEAKING          = "05";
    public static final String   STATUS_CONFERENCING      = "06";
    public static final String   STATUS_WORKING           = "07";

	//---------------------------------------------------------- 
	// [ This is used in the XFon ]
	//----------------------------------------------------------
    /*
    public static final String   PHONE_RING               = "00";
    public static final String   PHONE_CALL               = "01";
    public static final String   PHONE_HANGUP             = "02";
    public static final String   PHONE_REGISTER           = "03";
    public static final String   PHONE_UNREGISTER         = "04";
    */

	// [ This is used in the Samsung 7500 PBX ]
    public static final String   PHONE_RING               = "00";
    public static final String   PHONE_CALL               = "01";
    public static final String   PHONE_MISSCALL           = "02";
    public static final String   PHONE_ONCALL			  = "03";
    public static final String   PHONE_HANGUP             = "04";
    public static final String   PHONE_REGISTER           = "05";
    public static final String   PHONE_UNREGISTER         = "06";
    public static final String   PHONE_READY	          = "07";
    public static final String   PHONE_NOREADY		      = "08";
    public static final String   PHONE_FAIL			      = "09";


    public static final String   OPTION_ACCEPT            = "1";
    public static final String   OPTION_REFUSE            = "0";
    
    public static final String   TRANSACTION_SUCCESS      = "1";
    public static final String   TRANSACTION_FAIL         = "0";
    
    public static final String   TRANSACTION_INIT_CODE    = "00000000";
    public static final String   TRANSACTION_TEMP_CODE    = "99999999";
    
    public static final String[] locales                  = { "ko_KR", "en_US", "ja_JP", "zh_CN" };
    public static final String   DOMAIN_PREFIX            = "domain";
    public static final String   COMAPNY_PREFIX           = "company_";
    public static final String   DEPT_PREFIX              = "dept_";
    public static final String   USER_PREFIX              = "user_";
    
    public static final String   USERFILE_MEMO_PREFIX     = "memo_";
    public static final String   USERFILE_GROUP_NAME      = "group.inf";
    
    public static final String   LOGIN_USER_STATUS = "MSG";
    public static final String   LOGOUT_USER_STATUS = "004";
    
    public static final String   LOGIN_USER_SUCCESS = "1";
    public static final String   LOGIN_IS_EMPNO = "isEmpnoLogin";
    public static final String   AUTO_LOGIN_PASSWORD = "123456789";
    public static final String   COLUMN_COMPANY_CODE = "compCode";
    public static final String   SLASH = "/";
    
    public static final String   ROOT_DEPT_CODE = "00000";
    
    // DAO 와 도메인이 1:1 이 아닌 N:1 일일 경우 사용한다.
    public static final String DOMAIN_CHANGE = "user.domain.change";
    
    //그룹웨어의 서비스 도메인이 틀린 경우 사용합니다.
    public static final String SERVICE_REPLACE_DOMAIN = "service.replaceDomain";
    //겸직 사용 여부
//    public static final String DUPLICATE_USER_INFO = "user.plural";
    
    public static final String ONLINE_ID = "user.onlineId";
    public static final String PLURAL_LIST = "plural.list";
    public static final String MOBILE_NOTI = "mobile.noti";
    public static final String EXCLUSION_COMPANY_CODE = "exclusion.company.code";
    
    //사용자 이름 정렬 여부
//    public static final String SORT_USERNAME = "sort.username";
    
    public static final Set<String> BINARY_PROPS;
    
    public static final String USE_APNS ="womo.useAPNS";
    
 // 로그인시 사용자의 empno 를 전달하지 여부. 
    // nds 전자결제 연동에 의해 추가.
    public static final String LOGIN_RETURN_EMPNO = "login.return.empno";
    //databsae 종류
    public static final String DATABASE_BUNDLE = "database.bundle";
    
    public static final String DATABASE_MSSQL = "mssql";
    
    public static final String PASSWORD_BUILDER_WASID = "password.builder.wasid";
    static {
    	Set<String> setProps = new HashSet<String>();
    	setProps.add(PROP_SEND_BIN);
    	BINARY_PROPS = Collections.unmodifiableSet(setProps);
    }
}
