package com.messanger.engine.uc.handler;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.dao.ICommonDao;
import com.messanger.engine.uc.message.request.MSGSRequest;
import com.messanger.engine.uc.message.request.RMSGRequest;
import com.messanger.engine.uc.message.response.RMSGResponse;
import com.messanger.engine.uc.model.MessageTemplate;
import com.messanger.engine.uc.model.MsgType;
import com.messanger.engine.uc.utils.SessionUtils;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import java.util.List;
import java.util.Locale;

// 채팅방에 등록된 사원에게 메시지 전달 처리 핸들러.
public class RMSGHandler implements MessageHandler<RMSGRequest> {
    private ICommonDao commonDao;
    IoSessionContext ctx = IoSessionContext.getInstance();
    MSGSHandler msgsHandler;

    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }
    public void setMsgsHandler(MSGSHandler msgsHandler) {
        this.msgsHandler = msgsHandler;
    }

    @Override
    public void messageReceived(IoSession session, RMSGRequest request) throws Exception {
        SessionLog.info(session, "#H-S# InputSystemMsgHandler");

        Locale locale = SessionUtils.getRequestLocale(request);
        RMSGResponse response = new RMSGResponse(request.getType());
        response.setTransactionId(request.getTransactionId());

        //수신자 그룹
        List<String> receiverList = commonDao.selectAllMemberByChannelId(request.getChannelId());

        //메시지
        MessageTemplate messageTemplate = commonDao.selectOneByTemplateIdAndTypeIsMessage(request.getTemplateId());
        MsgType msgType = request.getMessageType();

        MSGSRequest msgsRequest = new MSGSRequest(Constants.TYPE_SEND_MSG);
        msgsRequest.setTransactionId(request.getTransactionId());
        msgsRequest.setProperty(Constants.PROP_RECEIVER_UID, String.join(Constants.PROP_DELIM, receiverList)); // DB에서 조회
        msgsRequest.setProperty(Constants.PROP_SENDER_UID, request.getSenderUid());
        msgsRequest.setProperty(Constants.PROP_SEND_MSG, messageTemplate.getMessageBody());
        msgsRequest.setProperty(Constants.PROP_ROOM_ID, request.getChannelId());
        msgsRequest.setProperty(Constants.PROP_MSG_FONT, msgType.getFont());
        msgsRequest.setProperty(Constants.PROP_MSG_EFFECT, msgType.getEffect());
        msgsRequest.setProperty(Constants.PROP_MSG_SIZE, msgType.getFontSize().toString());
        msgsRequest.setProperty(Constants.PROP_MSG_COLOR, msgType.getColor());

        msgsHandler.messageReceived(session, msgsRequest);
    }


    private void getSystemSession(IoSession session) {

    }
}
