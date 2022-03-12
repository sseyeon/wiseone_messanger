package com.messanger.engine.uc.handler;

import java.util.Locale;

import com.messanger.engine.uc.message.request.MEMSRequest;
import com.messanger.engine.uc.model.SecureType;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.MSGSRequest;
import com.messanger.engine.uc.message.response.MSGRResponse;
import com.messanger.engine.uc.message.response.MSGSResponse;
import com.messanger.engine.uc.service.ICommonService;
import com.messanger.engine.uc.utils.SessionUtils;

/**
 * 대화 전달 처리 핸들러.
 * @author skoh
 *
 */
public class MSGSHandler implements MessageHandler<MSGSRequest> {

	ICommonService commonService;
    IoSessionContext ctx = IoSessionContext.getInstance();
    ReloadableResourceBundleMessageSource messageSource;

    MEMSHandler memsHandler;
    
    /**
     * 
     * @param commonService
     */
    public void setCommonService(ICommonService commonService) {
        this.commonService = commonService;
    }
    
    public void setMemsHandler(MEMSHandler memsHandler) {
        this.memsHandler = memsHandler;
    }
    
    /**
     * 
     * @param messageSource
     */
    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
    	this.messageSource = messageSource;
    }
		
    /**
     * 사용자가 대화상대에게 입력한 메세지를 전체 대화 상대에게 전달
     */
    public void messageReceived(IoSession session, MSGSRequest request) throws Exception {        
        SessionLog.info(session, "#H-S# SendMsgHandler");
        
        Locale locale = SessionUtils.getRequestLocale(request);
        MSGSResponse response = new MSGSResponse(request.getType());
        String errMsg = "";
        
        try {
            response.setTransactionId(request.getTransactionId());

            String[] receipt = request.getReceipt();
            String msgFont = request.getMsgFont();
            String msgEffect = request.getMsgEffect();
            String msgSize = request.getMsgSize();
            String msgColor = request.getMsgColor();

            SecureType secureType = request.getSecureType();
            
            /*
            IoSessionContext context = IoSessionContext.getInstance();
            SessionLog.debug(session, context.toString());
            */
            
            for (int i = 0; i < receipt.length; i++) {
                MSGRResponse rcvMsg = new MSGRResponse(Constants.TYPE_RECEIVE_MSG);
                //SessionLog.info(session, "#H-C#SMH# MSG RECEIVER[" + receipt[i] + "]");
                IoSession targetSession = SessionUtils.getUserSession(receipt[i]);
                
                //세션이 존재하지 않은경우.
                if (targetSession == null) {
                    errMsg += messageSource.getMessage("10021", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    continue;
                }
                        
                //연결이 정상인 경우.
                if (targetSession.isConnected()) {
                    rcvMsg.setTransactionId(request.getTransactionId());
                    rcvMsg.setReceiverUid(receipt[i]);
                    rcvMsg.setSenderUid(request.getSenderUid());
                    rcvMsg.setReceiveMsg(request.getSendMsg());
                    rcvMsg.setRoomId(request.getRoomId());
                    if(msgFont != null) {
                    	rcvMsg.setMsgFont(msgFont);
                    }
                    if(msgEffect != null) {
                    	rcvMsg.setMsgEffect(msgEffect);
                    }
                    if(msgSize != null) {
                    	rcvMsg.setMsgSize(msgSize);
                    }
                    if(msgColor != null) {
                    	rcvMsg.setMsgColor(msgColor);
                    }

                    if(secureType != null) {
                        rcvMsg.setSecureType(secureType);
                    }
                    
                    WriteFuture future = targetSession.write(rcvMsg);
                    future.join();
                    if( future.isWritten()) {
                        SessionLog.info(targetSession, "#H-C#SMH# Send To "+receipt[i]+" Success!!");
                    } else {
                        errMsg += messageSource.getMessage("10016", new Object[]{receipt[i]}, locale) + Constants.PROP_DELIM;
                    }
                }
                //연결이 끊긴경우.
                else {
                    MEMSRequest memsRequest = new MEMSRequest(Constants.TYPE_SEND_MEMO);
                    memsRequest.setTransactionId(request.getTransactionId());
                    memsRequest.setProperty(Constants.PROP_SENDER_UID, request.getSenderUid());
                    memsRequest.setProperty(Constants.PROP_RECEIVER_UID, receipt[i]);
                    memsRequest.setProperty(Constants.PROP_SEND_MSG, request.getSendMsg());

                    memsHandler.messageReceived(session, memsRequest);
                    errMsg += messageSource.getMessage("10015", new Object[]{receipt[i]}, locale);
                }
            }
        } catch (Exception e) {
            errMsg = e.getMessage();
            SessionLog.error(session, e.getMessage(), e);
        }
        response.setResponseMsg(errMsg);        
        session.write(response);
        SessionLog.info(session, "#H-E# SendMsgHandler");
    }
}
