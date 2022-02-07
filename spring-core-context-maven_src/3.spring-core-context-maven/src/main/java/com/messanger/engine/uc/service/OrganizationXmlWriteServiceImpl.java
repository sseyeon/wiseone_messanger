package com.messanger.engine.uc.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.dao.CommonDaos;
import com.messanger.engine.uc.dao.ICommonDao;
import com.messanger.engine.uc.model.Company;
import com.messanger.engine.uc.model.Dept;
import com.messanger.engine.uc.model.Domain;
import com.messanger.engine.uc.model.User;
import com.messanger.engine.uc.utils.LocalXppDriver;
import com.messanger.engine.uc.utils.SpringContextLoader;
import com.thoughtworks.xstream.XStream;

/**
 * 조직도 정보를 db에서 읽어 파일로 저장하는 클래스
 * 메모리로 캐싱하는 방식으로 수정된 후에는 서버가 처음 설치 되어 최초로 조직도를 xml 파일로 저장하는 경우에만 사용
 * @author skoh
 * @deprecated
 */
public class OrganizationXmlWriteServiceImpl extends QuartzJobBean {

    private static final Log LOG = LogFactory.getLog(OrganizationXmlWriteServiceImpl.class);

    private Config config = null;
    
    @SuppressWarnings({ "unchecked", "unused" })
    private Class jobClass;
    
    private CommonDaos commonDaos = null;    
        
    public OrganizationXmlWriteServiceImpl() {
    	
    }
    
    public OrganizationXmlWriteServiceImpl(Config config, CommonDaos commonDaos) {
        this.config = config;
        this.commonDaos = commonDaos;
    }
    
    @SuppressWarnings("unchecked")
    public void setJobClass(Class clazz) {
        this.jobClass = clazz;
    }

    /**
     * 
     * @param commonDaos
     */
    public void setCommonDaos(CommonDaos commonDaos) {
        this.commonDaos = commonDaos;
    }
    
    /**
     * 
     * @param config
     */
    public void setConfig(Config config) {
        this.config = config;
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String root = config.get("files.xml.path");
        LOG.info("OrganizationXmlWriteService start");
        int maxRetry = 3;
        int retry = 0;
        try {
            XStream stream = new XStream(new LocalXppDriver());
            stream.processAnnotations(Domain.class);
            Set<String> setKey = commonDaos.keySet();
            Iterator<String> iterKey = setKey.iterator();
            
            List<Domain> allDomainList = new ArrayList<Domain>(); 
//            Set<String> setDoneSchema = new HashSet<String>();
            
            Map<String, String> param = new HashMap<String, String>();
            
            while(iterKey.hasNext()) {
            	String daoName = iterKey.next();
            	ICommonDao commonDao = commonDaos.get(daoName);
            	retry = 0;
            	while(retry++ < maxRetry) {
	            	try {            		
			            List<Domain> domainList = null ;
			            
			            if( Boolean.valueOf(config.get(Constants.DOMAIN_CHANGE))){
			            	param.put("daoName", daoName);
			            	domainList = commonDao.selectDomains(param);
			            } else domainList = commonDao.selectDomains(param);
			            
			            LOG.info("Load From ["+daoName+" Dao] start");
			            for (Iterator<Domain> iterator = domainList.iterator(); iterator.hasNext();) {
			                Domain domain = iterator.next();
			                domain.setDaoName(daoName);
//			                String schema = domain.getSchema();
//			                LOG.info("Load From schema ["+schema+" Dao] start");
//			                if(setDoneSchema.add(schema) == false) {
//			                	continue;
//			                }
//			                String dir = root + "/" + schema + "/";
			                String dir = root + "/";
			                LOG.info("Domain: "+domain.getDomain());
			                
			                for (String locale : Constants.locales) {
			                	LOG.info("====================================================");
			                	LOG.info(locale);
			                    List<Company> companyList = commonDao.selectCompanies(locale, config.get(Constants.EXCLUSION_COMPANY_CODE, ""));	
			                    LOG.info("companyList  : " + companyList.size());
			                    List<Dept> deptList = commonDao.selectDepts(locale, config.get(Constants.EXCLUSION_COMPANY_CODE, ""));		
			                    LOG.info("deptList  : " + deptList.size());
//			                    List<User> userList = commonDao.selectUsers(schema, locale, Boolean.valueOf(config.get(Constants.ONLINE_ID, "false")), Boolean.valueOf(config.get(Constants.DUPLICATE_USER_INFO,"false")));
			                    List<User> userList = commonDao.selectUsers(locale, Boolean.valueOf(config.get(Constants.ONLINE_ID, "false")), Boolean.valueOf(config.get(Constants.PLURAL_LIST, "false")), config.get(Constants.EXCLUSION_COMPANY_CODE, ""));
			                    LOG.info("userList  : " + userList.size());
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
			            allDomainList.addAll(domainList);
			            LOG.info("Load From ["+daoName+" Dao] end");
			            break;
	            	} catch (Exception e) {
	            		LOG.error("[DAO:"+daoName+"] fail["+retry+"] "+e.toString(), e);
	            	}
            	} //end of while
            } //end of while
            FileUtils.writeStringToFile(new File(root + "/" + Constants.DOMAIN_PREFIX + ".xml"), stream.toXML(allDomainList), "UTF-8");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        LOG.info("OrganizationXmlWriteService done");
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            ApplicationContext ctx = SpringContextLoader.getXmlWriterContext();
            
            OrganizationXmlWriteServiceImpl o = new OrganizationXmlWriteServiceImpl(
                    (Config)ctx.getBean("config"), (CommonDaos)ctx.getBean("commonDaos"));            
            o.executeInternal(null);
        } catch (Exception e) {
        	LOG.fatal(e);
            e.printStackTrace();
        }
    }
}

    