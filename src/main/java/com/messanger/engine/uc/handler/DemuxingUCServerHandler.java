package com.messanger.engine.uc.handler;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.DemuxingIoHandler;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.response.USR_Response;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;
import com.messanger.engine.uc.utils.StackTracer;

/**
 * 메세지 타입별로 하위 핸들러로 연결시켜주는 핸들러
 * 자세한 사항은 Apache MINA 문서의 DemuxingIoHandler 참고
 * @author skoh
 *
 */
public class DemuxingUCServerHandler extends DemuxingIoHandler {

    IoSessionContext ctx = IoSessionContext.getInstance();
    ICommonService commonService; 
    Config config; 
    
    protected static final Log log = LogFactory.getLog(DemuxingUCServerHandler.class);
    
    
    public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	/**
     * 
     */
    @SuppressWarnings("unchecked")
    public DemuxingUCServerHandler(String[] requestNames, @SuppressWarnings("rawtypes") MessageHandler[] handlerList) {
        for (int i = 0; i < handlerList.length; i++) {
            try {
                addMessageHandler(Class.forName(requestNames[i]), handlerList[i]);
            } catch (ClassNotFoundException e) {
            	log.fatal(e);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public boolean isLoginUser(String name) {
        return ctx.contains(name);
    }

    /**
     * 
     * @return
     */
    public int getNumberOfSession() {
        return ctx.size();
    }

    /**
     * 
     * @param key
     */
    public void kick(String key) {
        synchronized (ctx) {
            IoSession session = ctx.getSession(key);
            session.close();
        }
    }
    
    /**
     * 
     */
    public void sessionOpened(IoSession session) {
        SessionLog.info(session, "Open Session[" + session.toString() + "]");
        session.setIdleTime(IdleStatus.BOTH_IDLE, Constants.SESSION_IDLE);
    }
    
    /**
     * 세션 연결이 끊어진 경우 처리
     */
    public void sessionClosed(IoSession session) throws Exception {
        String closedUid = SessionUtils.getUid(session);
        if (closedUid != null) {
        	IoSession savedSession = SessionUtils.getUserSession(closedUid);        	
        	
        	if(savedSession == session) {
        		ctx.removeSession(closedUid);
        		SessionLog.info(session, "===>Remove  Session[" + closedUid + "]");
        	} else {
        		SessionLog.info(session, "===>Relogin Session[" + closedUid + "]");
        		return;
        	}
        	String schema = SessionUtils.getScheme(session);
            for (Iterator<Map.Entry<String, IoSession>> it = ctx.iterator(); it.hasNext();) {
                Map.Entry<String, IoSession> entry = it.next();
                String targetId = entry.getKey();
                IoSession targetSession = entry.getValue();
				// 동일한 그룹사에만 상태 변경 정보를 전달한다. 2009-06-24
                if(schema.equals(SessionUtils.getScheme(targetSession)) == false) {
					continue;
				}                
                if (targetId.equals(closedUid) == false) {
                	SessionLog.info(targetSession, "[DemuxHandler] Broadcast To ["+targetId+"]");
                    USR_Response changeRequest = new USR_Response(Constants.TYPE_STATUS);
                    changeRequest.setTransactionId(Constants.TRANSACTION_INIT_CODE);
                    changeRequest.setStatusChangedUid(closedUid);
                    changeRequest.setStatus(Constants.STATUS_OFFLINE);
                    targetSession.write(changeRequest);
                }
            }
            
//            final  String domainName = closedUid.split("@")[1];
//            final String userId = closedUid.split("@")[0];
//            final String daoName = commonService.findDaoName(domainName);
            
//            if( Boolean.valueOf(config.get("log.login", "false"))) {
//            	commonService.writeLoginInfo(daoName, userId, domainName
//            			, session.getAttribute(Constants.SESSION_KEY_USER_NAME).toString()
//						, Constants.LOGIN_USER_SUCCESS, "", Constants.LOGOUT_USER_STATUS
//						, session.getRemoteAddress().toString(), "");
//            }
//            if( Boolean.valueOf(config.get("log.login", "false")) ){
//            	commonService.writeLoginInfo(SessionUtils.getDaoName(session)
//            						,SessionUtils.getUid(session).split("@")[0]
//            						,SessionUtils.getDomainName(session)
//            						,SessionUtils.getUserName(session)
//            						,Constants.LOGIN_USER_SUCCESS, "", Constants.LOGOUT_USER_STATUS
//            						,session.getRemoteAddress().toString(), "");
//            }
            
            SessionLog.info(session, "Login Session[" + closedUid + "] removed...");
            session = null;
        } 
    }
    
    /**
     * 
     */
    public void sessionIdle(IoSession session, IdleStatus status) {
        Object uid = session.getAttribute(Constants.SESSION_KEY_UID);
        
        if (uid == null) {
            session.close();
        }
    }
    
    /**
     * 
     */
    public void exceptionCaught(IoSession session, Throwable cause) {
        SessionLog.error(session, StackTracer.stackTrace(cause));
    }
}
