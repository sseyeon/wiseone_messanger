package com.messanger.engine.uc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.mina.common.IoSession;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.MessageRequest;

public class SessionUtils {

    private static IoSessionContext ctx = IoSessionContext.getInstance();
    
    public static Iterator<Map.Entry<String, IoSession>> getIterator() {
    	return ctx.iterator();
    }
    
    /**
     * 
     * @param uid
     * @return
     */
    public static IoSession getUserSession(String uid) {
    	return ctx.getSession(uid);
    }
    
    /**
     * 
     * @param session
     * @return
     */
    public static String getScheme(IoSession session) {
        return session.getAttribute(Constants.SESSION_KEY_SCHEME) == null ? null : 
            (String)session.getAttribute(Constants.SESSION_KEY_SCHEME);
    }
    
    public static String getScheme(String uid) {
    	IoSession session = getUserSession(uid);  
    	if(session == null) {
    		return null;
    	}
    	return getScheme(session);
    }
    /**
     * 
     * @param session
     * @return
     */
    public static String getLocale(IoSession session) {
        return session.getAttribute(Constants.SESSION_KEY_LOCALE) == null ? null : 
            (String)session.getAttribute(Constants.SESSION_KEY_LOCALE);
    }
    
    /**
     * 
     * @param session
     * @return
     */
    public static String getUid(IoSession session) {
        return session.getAttribute(Constants.SESSION_KEY_UID) == null ? null : 
            (String)session.getAttribute(Constants.SESSION_KEY_UID); 
    }
    
    /**
     * 
     * @param uid
     * @return
     * @throws Exception
     */
    public static String getStatus(String uid) {
        String st = Constants.STATUS_ONLINE;
        IoSession session = getUserSession(uid);
        
        if (session == null) { 
            return Constants.STATUS_OFFLINE;
        }
        
        if (!session.isConnected()) {
            session.close();
            return Constants.STATUS_OFFLINE;
        }
        
        st = session.getAttribute(Constants.SESSION_KEY_STATUS) == null ? null :
            (String)session.getAttribute(Constants.SESSION_KEY_STATUS);
        
        if (st == null) st = Constants.STATUS_ONLINE;
        
        return st;
    }
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static Locale getRequestLocale(MessageRequest request) {
		String uid = request.getSenderUid();
		return getUserLocale(uid);
	}
	
	public static Locale getSessionLocale(IoSession session) {
		if(session == null) {
			return Locale.getDefault();
		}
		return getLocale(getLocale(session));
	}
	/**
	 * 
	 * @param uid
	 * @return
	 */
	public static Locale getUserLocale(String uid) {
		IoSession userSession = getUserSession(uid);
		if(userSession == null) {
			return Locale.getDefault();
		}
		String localeString = (String)userSession.getAttribute(Constants.SESSION_KEY_LOCALE);
		return getLocale(localeString);
	}
	
	/**
	 * 
	 * @param localeString
	 * @return
	 */
	public static Locale getLocale(String localeString) {
		if (localeString.indexOf('_') > 0 && localeString.length() > 4) {
			String language = localeString.substring(0, localeString.indexOf('_'));
			return new Locale(language);
		}
		return Locale.getDefault();
	}
	
	/**
	 * 
	 * @param daoName
	 * @return
	 */
	public static String getDaoName(IoSession session){
		final Object daoName = session.getAttribute(Constants.SESSION_KEY_DAONAME);
		return daoName == null ? null : (String)daoName;
	}
	
	public static String getDomainName(IoSession session){
		final Object domainName = session.getAttribute(Constants.SESSION_KEY_DOMAIN);
		return domainName == null ? null : (String)domainName;
	}
	
	public static String getUserName(IoSession session){
		final Object userName = session.getAttribute(Constants.SESSION_KEY_USER_NAME);
		return userName == null ? null : (String)userName;
	}
}
