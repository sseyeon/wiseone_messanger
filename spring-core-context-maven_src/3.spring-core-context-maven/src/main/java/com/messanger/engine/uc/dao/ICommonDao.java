package com.messanger.engine.uc.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.messanger.engine.uc.model.Company;
import com.messanger.engine.uc.model.Dept;
import com.messanger.engine.uc.model.Domain;
import com.messanger.engine.uc.model.Mail;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.model.WorkFlow;

public interface ICommonDao {

	/**
	 * 특정 db의 경우 timeout이 존재하여 연결이 빈번하지 않은 시간대에 db 연결이 끊어짐.
	 * 이를 방지하기 위해 일정 주기로 db에 쿼리를 날림
	 * ServerContext.xml 에 trigger로 등록됨
	 * @return
	 * @throws Exception
	 */
	public int validationQuery() throws Exception;

	/**
	 * db에 저장된 domain list 조회
	 * @return Domain 리스트
	 * @throws Exception
	 */
    public List<Domain> selectDomains( Map<String, String> param ) throws Exception;

    /**
     * db에 저장된 사용자 정보 조회
     * @param locale 조회할 locale
     * @return 사용자 정보 리스트
     * @throws Exception
     */
    public List<User> selectUsers(String locale, boolean isOnlineId, boolean isPluralList, String exclusionSql) throws Exception;

    public List<User> selectUsers(String locale, List<String> compCodes) throws Exception;

    public User selectUserByUserId(Map<String, Object> parameterMap) throws Exception;

    public User selectUserByEmpNo(Map<String, Object> parameterMap) throws Exception;

    /**
     * db에 저장된 회사 정보 조회
     * @param locale 조회할 locale
     * @return 회사 정보 리스트
     * @throws Exception
     */
    public List<Company> selectCompanies(String locale, String exclusionSql) throws Exception;

    /**
     * db에 저장된 부서 정보 조회
     * @param locale 조회할 locale
     * @return 부서 정보 리스트
     * @throws Exception
     */
    public List<Dept> selectDepts(String locale, String exclusionSql) throws Exception;

    List<Dept> selectDepts(String locale, List<String> compCodes) throws Exception;

    /**
     * 읽지 않은 메일 개수 조회
     * @param schema 조회할 스키마
     * @param userid 조회할 사용자 아이디
     * @param domainName 조회할 도메인 명
     * @param locale
     * @return 읽지 않은 메일 갯수
     * @throws Exception
     */
//    public int selectUnreadMailCount(String schema, String userid, String domainName, String locale) throws Exception;

    /**
     * 결제해야 할 결제 건수 조회
     * @param schema 조회할 스키마
     * @param userid 조회할 사용자 아이디
     * @param domainName 조회할 도메인명
     * @param locale
     * @return 결제해야 할 결제 건수
     * @throws Exception
     */
//    public int selectUnreadWFCount(String schema, String userid, String domainName, String locale) throws Exception;

    /**
     * 읽지 않은 메일 정보 조회 (보낸사람, 시간, 제목)
     * @param schema 조회할 스키마
     * @param userid 조회할 사용자 아이디
     * @param domainName 조회할 도메인명
     * @param locale
     * @param maxList 한번에 가져올 리스트 개수
     * @return
     * @throws Exception
     */
//    public List<Mail> selectUnreadMail(String schema, String userid, String domainName, String locale, String maxList) throws Exception;

    /**
     * 결제해야 할 결제 정보 조회(기안자, 시간, 제목)
     * @param schema 조회할 스키마
     * @param userid 조회할 사용자 아이디
     * @param domainName 조회할 도메인명
     * @param locale
     * @param maxList 한번에 가져올 리스트 개수
     * @return
     * @throws Exception
     */
//    public List<WorkFlow> selectUnreadWF(String schema, String userid, String domainName, String locale, String maxList) throws Exception;

    /**
     * 대화명이 등록된 사용자인지 조회
     * @param param
     * @return
     */
    public int selectCountByUserId(Map<String, Object> param) throws SQLException;

    /**
     * 대화명 입력
     * @param param
     * @return
     */
    public int insertOnlineId(Map<String, Object> param) throws SQLException;

    /**
     * 대화명 변경
     * @param param
     * @throws SQLException
     */
    public void updateOnlineId(Map<String, Object> param) throws SQLException;

    public void updatePassword(Map<String, Object> param) throws SQLException;

    /**
     * 로그인 로그 현황
     */
    public void writeLoginInfo(Map<String, Object> param)  throws SQLException;

    /**
     * 도메인과 실제 그룹웨어 주소를 매핑.
     * key : domain
     * value : 그룹웨어 도메인
     */
    public List<Map<String, String>> selectRealGroupwareDomain() throws SQLException;

    public String selectPasswordBuilderClassName(String param) throws Exception;

    public User selectUserByPassWord(Map<String, Object> parameterMap)
			throws Exception;
}
