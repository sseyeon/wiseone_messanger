package com.messanger.engine.uc.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.dao.ICommonDao;
import com.messanger.engine.uc.model.Company;
import com.messanger.engine.uc.model.Dept;
import com.messanger.engine.uc.model.Domain;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.utils.LocalXppDriver;
import com.thoughtworks.xstream.XStream;

/**
 * CommonServiceImpl을 상속받아 XML 조직도에서 읽어 오는 부분을 메모리에서 읽어 오게 수정 
 * @author skoh
 *
 */
public class CommonServiceImpl2 extends CommonServiceImpl {

	//public static String ORGAN_COMPANY

	protected Map<String, Domain> mapDomain;
	protected Map<String, Object> mapOrgan;
 
	public void init() {
		super.init();
		loadOrganization();
	}
	
	/**
	 * 조직도를 읽어 메모리에 올리고 검증 목적으로 이전 버전과 동일하게 XML 파일로 남긴다.
	 */
	public void loadOrganization() {		
		LOG.info("Organization load start");
		String root = config.get("files.xml.path");
		
		int maxRetry = 3;
		int retry = 0;
		Map<String, Domain> mapTmpDomain;
		Map<String, Object> mapTmpOrgan;

		Set<String> setKey = commonDaos.keySet();
		Iterator<String> iterKey = setKey.iterator();

		Set<String> setDoneSchema = new HashSet<String>();

		String sKeyCompany;
		String sKeyDept;
		String sKeyUser;
		String sKeyUserDupl;
		
		List<Company> companyList;
		List<Dept> deptList;
		List<User> userList;
		Map<String,User> mapTmpUser;
		List<Domain>  allDomainList = new ArrayList<Domain>();
		
		long startTime = System.currentTimeMillis();
		mapTmpDomain = Collections.synchronizedMap(new HashMap<String, Domain>());
		mapTmpOrgan =  Collections.synchronizedMap(new HashMap<String, Object>());
		
		Map<String, String> param = new HashMap<String, String>();
		
		try {
			XStream stream = new XStream(new LocalXppDriver());
	        stream.processAnnotations(Domain.class);
	        
			while(iterKey.hasNext()) {
				String daoName = iterKey.next();
				ICommonDao commonDao = commonDaos.get(daoName);
				retry = 0;
				while(retry++ < maxRetry) {
					try {            		
						List<Domain> daoDomainList = null ;
			            
						 if( Boolean.valueOf(config.get(Constants.DOMAIN_CHANGE))){
				            	param.put("daoName", daoName);
				            	daoDomainList = commonDao.selectDomains(param);
			            } else daoDomainList = commonDao.selectDomains(param);
						 
						LOG.info("Load From ["+daoName+" Dao] start");
						allDomainList.addAll(daoDomainList);
						for (Iterator<Domain> iterator = daoDomainList.iterator(); iterator.hasNext();) {
							Domain domain = iterator.next();
							domain.setDaoName(daoName);
							LOG.info("Domain: "+domain.getDomain());
							mapTmpDomain.put(domain.getDomain(), domain);
							String schema = domain.getSchema();
							if(setDoneSchema.add(schema) == false) {
								continue;
							}						
							String dir = root + "/" + schema + "/";
							for (String locale : Constants.locales) {
								LOG.info(locale);
								sKeyCompany = domain.getSchema()+"."+locale+".company";
								sKeyDept = domain.getSchema()+"."+locale+".dept";
								sKeyUser = domain.getSchema()+"."+locale+".user";
								sKeyUserDupl = domain.getSchema()+"."+locale+".userdupl";
								
								deptList = new ArrayList();
								userList = new ArrayList();
								mapTmpUser = new HashMap<String,User>();
	
								companyList = commonDao.selectCompanies(locale, domain.getSchema());

								if(!CollectionUtils.isEmpty(companyList)) {
									List<String> compCodes = new ArrayList();
									for(Company company : companyList) {
										compCodes.add(company.getCompanyCode());
									}

									deptList = commonDao.selectDepts(locale, compCodes);
//								List<User> userList = commonDao.selectUsers(schema, locale, Boolean.valueOf(config.get(Constants.ONLINE_ID, "false")), Boolean.valueOf(config.get(Constants.DUPLICATE_USER_INFO,"false")));
									userList = commonDao.selectUsers(locale, compCodes);
									Iterator<User> iter = userList.iterator();
									while(iter.hasNext()) {
										User user = iter.next();
										mapTmpUser.put(user.getEmail(), user);
									}

									//메모리저장
									mapTmpOrgan.put(sKeyCompany, companyList);
									mapTmpOrgan.put(sKeyDept, deptList);
									mapTmpOrgan.put(sKeyUser, mapTmpUser);
									mapTmpOrgan.put(sKeyUserDupl, userList);
									LOG.info(dir + Constants.COMAPNY_PREFIX + locale + ".xml");
									LOG.info(dir + Constants.DEPT_PREFIX + locale + ".xml");
									LOG.info(dir + Constants.USER_PREFIX + locale + ".xml");
								}
								//파일저장
								//회사파일 저장
			                    stream.processAnnotations(Company.class);
			                    FileUtils.writeStringToFile(new File(dir + Constants.COMAPNY_PREFIX + locale + ".xml"), stream.toXML(companyList), "UTF-8");
			                    //부서파일 저장
			                    stream.processAnnotations(Dept.class);
			                    FileUtils.writeStringToFile(new File(dir + Constants.DEPT_PREFIX + locale + ".xml"), stream.toXML(deptList), "UTF-8");
			                    //사용자파일 저장
			                    stream.processAnnotations(User.class);
			                    FileUtils.writeStringToFile(new File(dir + Constants.USER_PREFIX + locale + ".xml"), stream.toXML(userList), "UTF-8");		                    
							}
						}			            
						LOG.info("Load From ["+daoName+" Dao] end");
						break;
					} catch (Exception e) {
						LOG.error("[DAO:"+daoName+"] fail["+retry+"] "+e.toString(), e);
					}
				} //end of while
			} //end of while
			FileUtils.writeStringToFile(new File(root + "/" + Constants.DOMAIN_PREFIX + ".xml"), stream.toXML(allDomainList), "UTF-8");
		} catch(IOException e) {
			LOG.fatal("Organization load fail: "+e.toString());
		}
		mapDomain = mapTmpDomain;
		mapOrgan = mapTmpOrgan;
		 
		LOG.info("Organization load end: "+String.format("%04d", System.currentTimeMillis()-startTime));		
	}
	
	public String findScheme(String domainName) throws Exception {
		Domain domain = mapDomain.get(domainName);
		return domain == null ? null : domain.getSchema();
    }
	
	public String findDaoName(String domainName) throws Exception {
		Domain domain = mapDomain.get(domainName);
		return domain == null ? null : domain.getDaoName();
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public User findUser(String schema, String locale, String email) throws Exception {
		String sKeyUser = schema+"."+locale+".user";
		Map<String,User> mapUser = (Map<String,User>)mapOrgan.get(sKeyUser);
		return mapUser.get(email);
    }
	
	/**
	 * 2010.03.04
	 * 김용성 
	 * 비밀번호 변경시 최대 2시간의 공백이 있기에 기존의 메모리에서 조직도를 참고하여 사용자 정보를 검색하지 않고 DB에서 검색한다.
	 * 암센터 요구사항.
	 */
	@Override
	@Deprecated
	public User findUser(Map<String, Object> parameterMap) throws Exception {
		// + useOnlineId
		// 중복 허용 여부
//		return commonDaos.get(parameterMap.get("daoName").toString()).selectUser(parameterMap);
		return null;
	}
	
	/**
	 *  조직도의 회사 정보를 반환합니다.
	 *  @author 김용성
	 * @param compKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getCompListFromMemory(String compKey ){
		StringBuffer buf = new StringBuffer();
		List<Company> listCompany = (List<Company>)mapOrgan.get(compKey);
		Iterator<Company> iterCompany = listCompany.iterator();
		
		while(iterCompany.hasNext()) {
			buf.append(iterCompany.next().toString());
		}
		return buf.toString();
	}
	
	/**
	 * 조직도의 부서 정보를 반환 합니다.
	 * @param deptKey
	 * @param companyCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getDeptListFromMemory(String deptKey, String companyCode, String deptCode ){
		final boolean isSearch =  companyCode == null && deptCode == null; 
		Dept dept = null ;
		StringBuffer buf = new StringBuffer();
		List<Dept> listDept = (List<Dept>)mapOrgan.get(deptKey);
		Iterator<Dept> iterDept = listDept.iterator();
		String pDeptCode = Constants.ROOT_DEPT_CODE;
		if( deptCode != null && !deptCode.equals(""))
			pDeptCode = deptCode;
		
		while(iterDept.hasNext()) {
			dept = iterDept.next();
			if( isSearch )
				buf.append(dept.toString());
			else if( dept.getPdeptCode().equals(pDeptCode) && dept.getCompanyCode().equals(companyCode))
				buf.append(dept.toString());
		}
		return buf.toString();
	}
	
	/**
	 * 조직도의 사용자 정보를 반환합니다.
	 * @param userKey
	 * @param companyCode
	 * @param deptCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getUserListFromMemory(String userKey, String companyCode, String deptCode ){
		final boolean isSearch =  companyCode == null && deptCode == null;
		StringBuffer buf = new StringBuffer();
		List<User> userList = (List<User>) mapOrgan.get(userKey);
		Iterator<User> iterUserDupl = userList.iterator();
		User user = null;
		
		while(iterUserDupl.hasNext()) {
			//검색이 아닐 경우 윈도우 메신저 클라이언트
			user = iterUserDupl.next();
			if( isSearch )
				buf.append(user.toString(iptService, config));
			else if( user.getCompanyCode().equals(companyCode) && user.getDeptCode().equals(deptCode))
				buf.append(user.toString(iptService, config));
		}
		return buf.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String findAllOrganization(String schema, String locale) throws Exception {
		String sKeyCompany = schema+"."+locale+".company";
		String sKeyDept = schema+"."+locale+".dept";
		//String sKeyUser = schema+"."+locale+".user";
		String sKeyUserDupl = schema+"."+locale+".userdupl";
		
		StringBuffer buf = new StringBuffer();

		long startTime = System.currentTimeMillis();
		long tmpTime = 0L;

		tmpTime = System.currentTimeMillis();
		//회사정보를 읽어온다.	        
		List<Company> listCompany = (List<Company>)mapOrgan.get(sKeyCompany);
		Iterator<Company> iterCompany = listCompany.iterator();
		while(iterCompany.hasNext()) {
			buf.append(iterCompany.next().toString());
		}
		LOG.info("===>LOAD COMP Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));

		tmpTime = System.currentTimeMillis();

		//부서정보를 읽어온다.	        
		List<Dept> listDept = (List<Dept>)mapOrgan.get(sKeyDept);
		Iterator<Dept> iterDept = listDept.iterator();
		while(iterDept.hasNext()) {
			buf.append(iterDept.next().toString());
		}
		LOG.info("===>LOAD DEPT Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));

		tmpTime = System.currentTimeMillis();
		
		//사용자정보를 읽어온다.	        
//		Map<String,User> mapUser = (Map<String,User>)mapOrgan.get(sKeyUser);
//		Iterator<Map.Entry<String,User>> iterUser = mapUser.entrySet().iterator();
//		while(iterUser.hasNext()) {
//			buf.append(iterUser.next().getValue().toString(iptService));
//		}	
		
		//사용자정보를 읽어온다 (중복 포함).
		List<User> userList = (List<User>) mapOrgan.get(sKeyUserDupl);
		Iterator<User> iterUserDupl = userList.iterator();
		while(iterUserDupl.hasNext()) {
			buf.append(iterUserDupl.next().toString(iptService, config));								
		}
		
		LOG.info("===>LOAD USER Elapse: "+String.format("%05d", System.currentTimeMillis()-tmpTime));
		LOG.info("===>LOAD ALL  Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
		LOG.debug(buf.toString());
		return buf.toString();
	}

	/**
	 * @author 김용성
	 * @since 2010-04-13
	 * 로그인시 조직도 데이타를 메모리에서 가져오기 때문에 메모리에 있는 사용자 대화명도 수정해줘야 한다.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void modifyOnlineIdFromMemory(String schema,
			String userId, String onlineId) {
		for (String locale : Constants.locales) {
			final String sKeyUserDupl = schema+"."+locale+".userdupl";
			List<User> userList = (List<User>) mapOrgan.get(sKeyUserDupl);
			for (User user : userList) {
				if(user.getUserid().equals(userId)){
					user.setOnlineId(onlineId);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String findCompany(String domainName, String schema) {
		String companycode = null;
		for (String locale : Constants.locales) {
			final String sKeyCompany = schema+"."+locale+".company";
			List<Company> compList = (List<Company>) mapOrgan.get(sKeyCompany);
			for (Company company : compList) {
				if(company.getDomainName().equals(domainName)){
					companycode = company.getCompanyCode();
					return companycode;
				}
			}
		}
		return null;
	}

	@Override
	public void writeLoginInfo(String daoName, String userId, String domain, String userName,
			String isSuccess, String sessionCode, String accessType,
			String remoteIp1, String remoteIp2)  throws SQLException {
		ICommonDao commonDao = commonDaos.get(daoName);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("domain", domain);
		param.put("pk", "" + System.currentTimeMillis());
		param.put("userName", userName);
		param.put("isSuccess", isSuccess);
		param.put("sessionCode", sessionCode);
		param.put("accessType", accessType);
		param.put("remoteIp1", remoteIp1);
		param.put("remoteIp2", remoteIp2);
		commonDao.writeLoginInfo(param);
	}
	
	/**
	 * 메모리에서 읽기 때문에 조직도 메모리에 올리는 loadOrganization() 와 같은 class 혹은 상속받은 곳에 위치해야함.
	 * 저 변수들을 부모 클래스에 넣고 부모를 호출해야하는게 정상아닌가... 2명 손이 닿은 클래스 교정이 필요함...
	 * 2시간의 딜레이가 있지만 조직도니까... file || DB 보다야 빠르긋지. 꼬우면 DB 퍼포먼스를 올려주시길...
	 * @author 김용성
	 * @JiraVersion 3.1.000729
	 */
	@Override
	public String findOrganization(String schema, String locale, String companyCode, String deptCode) throws Exception {
		final String compKey = schema+"."+locale+".company";
		final String deptKey = schema+"."+locale+".dept";
		final String userKey = schema+"."+locale+".userdupl";
		final boolean ableLoadCompanyInfo= (companyCode == null && deptCode == null) || (companyCode.equals("") && deptCode.equals("")) ;
		
		StringBuffer org = new StringBuffer();
		
		//회사코드와 부서코드가 null 일 경우 초기 데이타 요청입니다.
		//데이타 : 회사정보 전체 요청임.
		//회사의 1depth 하위의 부서&사용자 정보를 보내준다.
		if( ableLoadCompanyInfo )
			org.append(getCompListFromMemory(compKey));
		
		if( !ableLoadCompanyInfo ){
			//부서코드가 null 이 아닌 경우는 회사를 제외한 조직도 실제 데이타를 요청한것임.
			//하위 부서와 사용자 정보를 보내준다.
			org.append(getDeptListFromMemory(deptKey, companyCode, deptCode));
			org.append(getUserListFromMemory(userKey, companyCode, deptCode));
		}
		return org.toString();
	}
}


 













