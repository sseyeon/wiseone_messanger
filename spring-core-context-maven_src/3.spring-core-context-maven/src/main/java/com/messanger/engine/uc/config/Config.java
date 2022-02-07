package com.messanger.engine.uc.config;

import java.util.Properties;

/**
 * conf/Config.properties 내용을 메모리에 가지고 있는 클래스
 * @author skoh
 *
 */
public class Config {	
	private final static String VERSION = "1.0.1";
	
    private Properties prop;

    public String get(String key) {
    	return this.prop.getProperty(key);
    }
    
    public String get(String key, String defaultValue) {
    	return this.prop.getProperty(key, defaultValue).trim();
    }
    
    public void setProperties(Properties prop) {
    		this.prop = prop;
    }
    
    public static String version() {
    	return VERSION;
    }
}
