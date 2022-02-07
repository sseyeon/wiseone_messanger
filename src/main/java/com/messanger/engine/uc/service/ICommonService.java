package com.messanger.engine.uc.service;

import java.sql.SQLException;
import java.util.Map;

import com.messanger.engine.uc.model.User;

public interface ICommonService {

	public void init();
	
	public String getConf(String key);
	
	public String getConf(String key, String value);
	
	public void validationQuery() throws Exception;
	
	public String findDomainName(String companyCode) throws Exception;
	
    /**
     * 도메인명으로 db 스키마명을 조회
     * @param domainName 스키마명을 조회할 도메인명
     * @return 스키마명
     * @throws Exception
     */
    public String findScheme(String domainName) throws Exception;

    /**
     * 다중 database에 연결되어 있는 경우 도메인 명을 가지고 dao 명을 조회
     * @param domainName dao명을 조회할 도메인명
     * @return dao 명
     * @throws Exception
     */
    public String findDaoName(String domainName) throws Exception;
    
    /**
     * daoName이 서비스 되고 있는 그룹웨어 도메인을 얻어 옴
     * @param daoName 조회할 dao 명
     * @return 그룹웨어가 서비스 되고 있는 도메인명
     * @throws Exception
     */
    public String findServiceDomain(String daoName) throws Exception;
    
    /**
     * 사용자 정보를 조회 
     * @param schema 스키마명
     * @param locale 로케일
     * @param email 이메일 주소
     * @return 사용자 정보
     * @throws Exception
     */
    public User findUser(String schema, String locale, String email) throws Exception;
    
    public User getUserByUserId(Map<String, Object> parameterMap) throws Exception;
    
    public User getUserByEmpNo(Map<String, Object> parameterMap) throws Exception;
    
    public User findUser(Map<String, Object> parameterMap) throws Exception;
    /**
     * 전체 조직도를 정해진 문자열 형식으로 조회한다.
     * @param schema 스키마명
     * @param locale 로케일
     * @return 문자열 형식의 조직도
     */
    public String findAllOrganization(String schema, String locale) throws Exception;
    
    /**
     * 쪽지 수신자가 오프라인인 경우 쪽지를 서버에 저장해둔다.
     * @param uid 사용자 id
     * @param senderUid 송신자 id
     * @param locale 송신자 locale
     * @param memo 쪽지 내용
     * @return
     */
    public void createMemo(String locale, String receiverUid, String senderUid, String memo) throws Exception;
    
    /**
     * 사용자의 저장된 쪽지가 있는지 조회한다.
     * @param uid 사용자 id
     * @return 쪽지 전송 포맷 문자열
     */
    public String findMemo(String uid) throws Exception;
    
    /**
     * 사용자 정의 그룹 구조를 서버에 저장한다.
     * @param locale 송신자 locale
     * @param uid 송신자 id
     * @param data 저장할 그룹 구조 내용
     * @throws Exception
     */
    public void createGrpInfo(String locale, String uid, String data) throws Exception;
    
    /**
     * 사용자 정의 그룹 구조를 조회한다.
     * @param uid 요청 사용자 id
     * @return 저장된 그룹 구조 내용
     * @throws Exception
     */
    public String findGrpInfo(String uid) throws Exception;
    
    /**
     * 읽지 않은 쪽지 개수를 조회
     * @param uid 요청 사용자 id
     * @return 읽지 않은 쪽지 개수
     */
    public int findUnreadMemoCount(String uid) throws Exception;
    
    /**
     * 읽지 않은 메일 개수를 조회 
     * @param daoName dao 명
     * @param schema 스키마 명
     * @param userid 요청 사용자 id
     * @param domainName 도메인 명
     * @param locale 요청 사용자의 locale
     * @return 읽지 않은 메일 개수
     * @throws Exception
     */
//    public int findUnreadMailCount(String daoName, String schema, String userid, String domainName, String locale) throws Exception;
 

    /**
     * 결제해야 할 결제 건수 조회 
     * @param daoName dao 명
     * @param schema 스키마 명
     * @param userid 요청 사용자 id
     * @param domainName 도메인 명
     * @param locale 요청 사용자의 locale
     * @return 결제해야 할 결제 건수
     * @throws Exception
     */
//    public int findUnreadWFCount(String daoName, String schema, String userid, String domainName, String locale) throws Exception;
    
    /**
     * 읽지 않은 메일 정보 조회
     * @param daoName dao 명
     * @param schema 스키마 명
     * @param userid 요청 사용자 id
     * @param domainName 도메인 명
     * @param locale 요청 사용자의 locale
     * @return 조회한 정보를 프로퍼티 맵 형식으로 반환(URL1, APCD, URL2, SMSG), 자세한 정보를 "UC 연동프로토콜.doc" 참고
     * @throws Exception
     */
//    public Map<String,String> getUnreadMail(String daoName, String schema, String userid, String domainName, String locale) throws Exception;
    
    /**
     * 읽지 않은 결제 정보 조회
     * @param daoName dao 명
     * @param schema 스키마 명
     * @param userid 요청 사용자 id
     * @param domainName 도메인 명
     * @param locale 요청 사용자의 locale
     * @return 조회한 정보를 프로퍼티 맵 형식으로 반환(URL1, APCD, URL2, SMSG), 자세한 정보를 "UC 연동프로토콜.doc" 참고
     * @throws Exception
     */
//    public Map<String,String> getUnreadWF(String daoName, String schema, String userid, String domainName, String locale) throws Exception;
    
    /**
	 * 클라이언트 버전이 서버와 호환되는 버전인지 체크한다.
	 * @param version 클라이언트 버전
	 * @return 주어진 버전이 호환되는 버전이면 주어진 버전값을 반환하고 그렇지 않으면 호환성 유지를 위한 최소버전값을 반환한다.
     */
    public String checkClientVersion(String version);
    
    /**
     * 도메인 별로 XFon 사용여부를 조회
     * @param domain 사용자의 도메인
     * @return 0이면 사용하지 않음, 1이면 사용
     */
    public boolean useXFon(String domain);
    
    /**
     * 도메인 별로 화상회의 사용여부를 조회
     * @param domain 사용자의 도메인
     * @return 0이면 사용하지 않음, 1이면 사용
     */
    public boolean useWCon(String domain);
    
    /**
     * 대화명 변경
     * @param userid   대화명 변경할 사용자 아이디
     * @param onlineId 대화명
     * @return boolean
     */
    public boolean modifyOnlineId(String uid, String daoName, String schema, String userid , String onlineId) throws SQLException;
    
    public boolean modifyPassword(String uid, String daoName, String userid , String password) throws SQLException;
    
    /**
     * 로그인 로그 생성
     */
    public void writeLoginInfo(String daoName, String userId,String domain,String userName,String  isSuccess,String  sessionCode,String  accessType,String  remoteIp1,String  remoteIp2)  throws SQLException;
    
    /**
     * 회사 코드와 부서 코드로 하위 부서와 사용자를 검색 합니다.
     * 회사 코드와 부서 코드가 null 일 경우 회사 정보를 리턴 합니다.( 초기화시 사용 )
     * @param schema
     * @param localeString
     * @param companyCode
     * @param deptCode
     * @return
     * @throws Exception
     */
    public String findOrganization(String schema, String localeString, String companyCode, String deptCode) throws Exception ;
    
    public String getPasswordBuilderClassName(String daoName, String wasId) throws Exception;

	public User getUserByPassWord(Map<String, Object> parameter) throws Exception;
}
