package com.messanger.engine.uc.handler;

import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.ALCKRequest;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 주기적으로 클라이언트에서 핑 날림
 * @author skoh
 *
 */
public class ALCKHandler implements MessageHandler<ALCKRequest> {
 
    IoSessionContext ctx = IoSessionContext.getInstance();
    
    /**
     * 현재 처리되고 있는 내용 없음.
     */
    public void messageReceived(IoSession session, ALCKRequest request) throws Exception {
        SessionLog.info(session, "#H-S# AliveCheckHandler");
        SessionLog.info(session, "#H-S#ACH# Session[" + SessionUtils.getUserSession(request.getSenderUid()) + "]");
        SessionLog.info(session, "#H-E# AliveCheckHandler");
    }
}
