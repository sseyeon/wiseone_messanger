package com.messanger.engine.uc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.messanger.engine.uc.model.Company;
import com.messanger.engine.uc.model.Dept;
import com.messanger.engine.uc.model.Domain;
import com.messanger.engine.uc.model.Mail;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.model.WorkFlow;
import com.messanger.engine.uc.service.OrganizationXmlWriteServiceImpl;
import com.messanger.engine.uc.utils.DigestUtil;

/**
 * wo db dao
 * 세부 주석은 interface인 ICommonDao 참고
 * @author skoh
 *
 */
public class CommonDaoImpl extends SqlMapClientDaoSupport implements ICommonDao {

	private static final Log LOG = LogFactory.getLog(CommonDaoImpl.class);
	
    @SuppressWarnings("unchecked")
    public List<Domain> selectDomains(Map<String, String> param) throws Exception {
    	LOG.info("CommonDaoImpl : " + param);
        return getSqlMapClientTemplate().queryForList("selectDomains", param);
    }
    
    public int validationQuery() throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("validationQuery");
    }
    
    @SuppressWarnings("unchecked")
    public List<User> selectUsers(String locale, boolean isOnlineId, boolean isPluralList, String companyCodes) throws Exception {
    	String[] sCompanyCodes = null;
    	if (companyCodes != null && companyCodes.equals("") == false) sCompanyCodes = companyCodes.split("[,]");
    	String exclusionSql = "";
    	if (sCompanyCodes != null) {
    		exclusionSql = "AND company_code NOT IN (";
	    	for (int i = 0; i < sCompanyCodes.length; i++) {
	    		exclusionSql += "'" + sCompanyCodes[i] + "'";
	    		if (i + 1 < sCompanyCodes.length) exclusionSql += ",";
	    	}
	    	exclusionSql += ")";
    	}	    	
        Map<String, String> parameterMap = new HashMap<String, String>();
//        parameterMap.put("schema", schema);
        parameterMap.put("locale", locale);
        parameterMap.put("isOnlineId", String.valueOf(isOnlineId));
        parameterMap.put("isPluralList", String.valueOf(isPluralList));
        parameterMap.put("exclusion_sql", exclusionSql);
//        parameterMap.put("ableDuplUser", String.valueOf(ableDuplUser));
        return getSqlMapClientTemplate().queryForList("selectUsers", parameterMap);
    }

    @Override
	public User selectUserByUserId(Map<String, Object> parameterMap)
			throws Exception {
    	User user = null;
    	List<User> list = getSqlMapClientTemplate().queryForList("selectUserByUserId", parameterMap);
    	if (list.size() > 0) {
    		user = list.get(0);
    	}
    	user.setPassword(DigestUtil.woDigestPassword(user.getPassword(),"SHA"));
		return user;
	}
    
    @Override
	public User selectUserByEmpNo(Map<String, Object> parameterMap)
			throws Exception {
		User user = null;
    	List<User> list = getSqlMapClientTemplate().queryForList("selectUserByEmpNo", parameterMap);
    	if (list.size() > 0) {
    		user = list.get(0);
    	}
		return user;
	}    

	@SuppressWarnings("unchecked")
    public List<Dept> selectDepts(String locale, String companyCodes) throws Exception {
    	String[] sCompanyCodes = null;
    	if (companyCodes != null && companyCodes.equals("") == false) sCompanyCodes = companyCodes.split("[,]");
    	String exclusionSql = "";
    	if (sCompanyCodes != null) {
    		exclusionSql = "AND company_code NOT IN (";
	    	for (int i = 0; i < sCompanyCodes.length; i++) {
	    		exclusionSql += "'" + sCompanyCodes[i] + "'";
	    		if (i + 1 < sCompanyCodes.length) exclusionSql += ",";
	    	}
	    	exclusionSql += ")";
    	}		
        Map<String, String> parameterMap = new HashMap<String, String>();
//        parameterMap.put("schema", schema);
        parameterMap.put("locale", locale);
        parameterMap.put("exclusion_sql", exclusionSql);
        return getSqlMapClientTemplate().queryForList("selectDepts", parameterMap);
    }

    @SuppressWarnings("unchecked")
    public List<Company> selectCompanies(String locale, String companyCodes) throws Exception {
    	String[] sCompanyCodes = null;
    	if (companyCodes != null && companyCodes.equals("") == false) sCompanyCodes = companyCodes.split("[,]");
    	String exclusionSql = "";
    	if (sCompanyCodes != null) {
    		exclusionSql = "AND company_code NOT IN (";
	    	for (int i = 0; i < sCompanyCodes.length; i++) {
	    		exclusionSql += "'" + sCompanyCodes[i].trim() + "'";
	    		if (i + 1 < sCompanyCodes.length) exclusionSql += ",";
	    	}
	    	exclusionSql += ")";
    	}
        Map<String, Object> parameterMap = new HashMap<String, Object>();
//        parameterMap.put("schema", schema);
        parameterMap.put("locale", locale);
        parameterMap.put("exclusion_sql", exclusionSql);
        return getSqlMapClientTemplate().queryForList("selectCompanies", parameterMap);
    }

    public int selectUnreadMailCount(String schema, String userid, String domainName, String locale) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("schema", schema);
        parameterMap.put("userid", userid);
        parameterMap.put("domain_name", domainName);
        parameterMap.put("locale", locale);
        return (Integer)getSqlMapClientTemplate().queryForObject("selectUnreadMailCount", parameterMap);
    }

    public int selectUnreadWFCount(String schema, String userid, String domainName, String locale) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("schema", schema);
        parameterMap.put("userid", userid);
        parameterMap.put("domain_name", domainName);
        parameterMap.put("locale", locale);
        return (Integer)getSqlMapClientTemplate().queryForObject("selectUnreadWFCount", parameterMap);
    }
    
    @SuppressWarnings("unchecked")
    public List<Mail> selectUnreadMail(String schema, String userid, String domainName, String locale, String maxList)
    throws Exception {
    	Map<String, String> parameterMap = new HashMap<String, String>();
    	parameterMap.put("schema", schema);
        parameterMap.put("userid", userid);
        parameterMap.put("domain_name", domainName);
        parameterMap.put("locale", locale);
        parameterMap.put("query_cnt", maxList);
    	return getSqlMapClientTemplate().queryForList("selectUnreadMail", parameterMap);
    }
    
    @SuppressWarnings("unchecked")
    public List<WorkFlow> selectUnreadWF(String schema, String userid, String domainName, String locale, String maxList)
    throws Exception {
    	Map<String, String> parameterMap = new HashMap<String, String>();
    	parameterMap.put("schema", schema);
        parameterMap.put("userid", userid);
        parameterMap.put("domain_name", domainName);
        parameterMap.put("locale", locale);
        parameterMap.put("query_cnt", maxList);
    	return getSqlMapClientTemplate().queryForList("selectUnreadWF", parameterMap);
    }

	@Override
	public int insertOnlineId(Map<String, Object> param) throws SQLException{
		return (Integer)getSqlMapClientTemplate().getSqlMapClient().insert("insertOnlineId",param);
	}

	@Override
	public int selectCountByUserId(Map<String, Object> param) throws SQLException{
		return (Integer)getSqlMapClientTemplate().getSqlMapClient().queryForObject("selectCountByUserId", param);
	}

	@Override
	public void updateOnlineId(Map<String, Object> param) throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().update("updateOnlineId", param);
	}
	
	@Override
	public void updatePassword(Map<String, Object> param) throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().update("updatePassword", param);
	}

	@Override
	public void writeLoginInfo(Map<String, Object> param) throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().insert("insert-login-info-new", param);
	}

	@Override
	public List<Map<String, String>> selectRealGroupwareDomain() throws SQLException {
		return getSqlMapClientTemplate().getSqlMapClient().queryForList("selectRealGroupwareDomain");
	}

	@Override
	public String selectPasswordBuilderClassName(String wasId) throws Exception {
		return (String)getSqlMapClientTemplate().queryForObject("selectPasswordBuilderClassName", wasId);
	}
	
	
	
}
