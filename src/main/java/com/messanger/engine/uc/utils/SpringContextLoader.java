package com.messanger.engine.uc.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.messanger.engine.uc.config.Config;

public class SpringContextLoader {
	public static final String SERVER_CONTEXT_FILE  = "ServerContext.xml";
	public static final String XMLWRITER_CONTEXT_FILE  = "OrganizationXmlWriterContext.xml";
//	public static final String XFON_CONTEXT_FILE  = "XFonClientContext.xml";
//	public static final String SAMSUNG_CONTENT_FILE = "Samsung7500Content.xml";
		
	public static ApplicationContext getContext(String configLocation) {
		return new ClassPathXmlApplicationContext(configLocation);
	}
	
	public static ApplicationContext getXmlWriterContext() {
		return new ClassPathXmlApplicationContext(XMLWRITER_CONTEXT_FILE);
	}
	
	public static ApplicationContext getServerContext() {
        return new ClassPathXmlApplicationContext(SERVER_CONTEXT_FILE);
    }	
	
//	public static ApplicationContext getXFonClientContext() {
//		return new ClassPathXmlApplicationContext(XFON_CONTEXT_FILE);
//	}
	
//	public static ApplicationContext getSamsung7500Content() {
//		return new ClassPathXmlApplicationContext(SAMSUNG_CONTENT_FILE);
//	}
	
//	public static ApplicationContext getXfonContext() {
//		Config config = (Config)getServerContext().getBean("config");
//		String contextFileName = config.get("xfon.context.file.name");
//		return new ClassPathXmlApplicationContext(contextFileName);
//	}
}
