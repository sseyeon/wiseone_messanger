package com.messanger.engine.uc.handler;

import com.messanger.engine.uc.message.request.MAILRequest;
import com.messanger.engine.uc.message.response.MAILResponse;
import com.messanger.engine.uc.service.SendMailService;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import java.util.Collections;

@Setter
public class MAILHandler implements MessageHandler<MAILRequest> {

    protected static final Log LOG = LogFactory.getLog(MAILHandler.class);
    private SendMailService mailService;

    public void init() {
        mailService.init();
    }

    @Override
    public void messageReceived(IoSession session, MAILRequest request) throws Exception {
        SessionLog.info(session, "#H-S# MailHandler");
        LOG.info("messageReceived :============================> call ");
        MAILResponse response = new MAILResponse(request.getType());
        response.setTransactionId(request.getTransactionId());
        String errMsg = "";

        long startTime = System.currentTimeMillis();
        mailService.send(request.getToAddr(), request.getTemplateId(), Collections.singletonMap("content", request.getContent()));
        response.setResponseMsg(errMsg);
        session.write(response);
        SessionLog.info(session, "#H-E# MailHandler Elapse: "+String.format("%05d", System.currentTimeMillis()-startTime));
    }
}
