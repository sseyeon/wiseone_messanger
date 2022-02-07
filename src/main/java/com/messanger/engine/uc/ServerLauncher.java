package com.messanger.engine.uc;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.utils.SpringContextLoader;

public class ServerLauncher {

    private static ApplicationContext applicationContext;
    
    protected static final Log LOG = LogFactory.getLog(ServerLauncher.class);
    
    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
    	
    	try {
	    	if(args.length > 0 && "version".equals(args[0])) {
	    		LOG.info("version: "+Config.version());
	    		System.exit(0);
	    	}
	    	Locale.setDefault(Locale.US);
	        if (System.getProperty("com.sun.management.jmxremote") != null) {
	        	applicationContext = SpringContextLoader.getServerContext();
	        	LOG.info("JMX enabled.");
	        } else {
	        	applicationContext = SpringContextLoader.getServerContext();
	        	LOG.info("JMX disabled. Please set the " + "'com.sun.management.jmxremote' system property to enable JMX.");
	        }        
	        
	        LOG.info("Listening ...");
    	} catch (Exception e) {
    		LOG.fatal(e);
    	}
    }    
    
    public static ApplicationContext getContext() {
        return applicationContext;
    }
}
