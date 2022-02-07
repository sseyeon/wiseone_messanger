package com.messanger.engine.uc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;
import com.messanger.engine.uc.message.MessageResponseFactory;

public class PushDataUtils {
	private static Log LOG = LogFactory.getLog(PushDataUtils.class);
	
	public static Locale getLocale(String uid) {
		return SessionUtils.getUserLocale(uid);
	}
	
	public static void pushData(String uid, String type, Map<String,String> data) {
		// Make response
		String key;
		try {
			IoSession session = SessionUtils.getUserSession(uid);
			if(session == null) {
				LOG.warn("Session not found["+type+"]"+uid);
				return;
			}
			if(session.isClosing()) {
				LOG.warn("Session closed["+type+"]"+uid);
				return;
			}
			MessageResponse response = MessageResponseFactory.createMessage(type);
			response.setTransactionId(Constants.TRANSACTION_INIT_CODE);
			Iterator<String> iter = data.keySet().iterator();
			while(iter.hasNext()) {
				key = iter.next();
				response.setProperty(key, data.get(key));
			}
			// Send data
			session.write(response);
		} catch (Exception e) {			
			LOG.error("Push data fail["+type+"]"+uid, e);
		}
	}
	
	public static void broadcastData(String uid, String type, Map<String,String> data, boolean includeMe) {
		String schema = SessionUtils.getScheme(uid);
		String key;
		for (Iterator<Map.Entry<String, IoSession>> it = SessionUtils.getIterator(); it.hasNext();) {
			Map.Entry<String, IoSession> entry = it.next();
			String userid = entry.getKey();
			// 동일한 그룹사에만 상태 변경 정보를 전달한다. 2009-06-24
			if(schema == null || schema.equals(SessionUtils.getScheme(userid)) == false) {
				continue;
			}			
			if (userid.equals(uid) && includeMe == false) {
				continue;
			}
			try {
				IoSession targetSession = entry.getValue();
				SessionLog.info(targetSession, "RECEIVER[" + userid + "]");
				
				MessageResponse response = MessageResponseFactory.createMessage(type);
				response.setTransactionId(Constants.TRANSACTION_INIT_CODE);
				Iterator<String> iter = data.keySet().iterator();
				while(iter.hasNext()) {
					key = iter.next();
					
					LOG.debug("BroadCast Data :: ++++++++++++++++++++++++ key=[" + key + "], data=[" + data.get(key) + "] +++++++++++++++++++++++++++");
					
					response.setProperty(key, data.get(key));
				}
				targetSession.write(response);
			} catch (Exception e) {
				LOG.error("Push data fail["+type+"]"+uid, e);
			}
		} // end of for
		LOG.info("Broadcast Msg["+type+"]"+uid);
	}
}
