package com.messanger.engine.uc.codec;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageRequest;
import com.messanger.engine.uc.message.MessageRequestFactory;
import com.messanger.engine.uc.utils.StackTracer;

/**
 * input 메세지 decoder
 * @author skoh
 */
public class RequestDecoder extends MessageDecoderAdapter {

    protected static final Log LOG = LogFactory.getLog(RequestDecoder.class);

    
    /**
     * input 버퍼를 검사하여 데이터가 충분한지, 더 필요한지를 검사한다.
     * @return 충분하면 MessageDecoderResult.OK, 그렇지 않으면 MessageDecoderResult.NEED_DATA
     */
    public MessageDecoderResult decodable(IoSession session, ByteBuffer in) {

        try {
            return messageComplete(in) ? MessageDecoderResult.OK : MessageDecoderResult.NEED_DATA;
        } catch (Exception ex) {
            LOG.error(ex);
            ex.printStackTrace();
        }

        return MessageDecoderResult.NOT_OK;
    }   

    /**
     * input 버퍼를 검사하여 메세지 연동 프로토콜 만큼의 데이타가 들어왔는지 검사한다.
     * @param in input buffer
     * @return 연동 프로토콜 만큼의 데이터가 존재하면 true, 그렇지 않으면 false 
     * @throws Exception
     */
    private boolean messageComplete(ByteBuffer in) throws Exception {        
        if (in.remaining() < Constants.PACKET_REQUIRE_LENGTH)
            return false;
        
        if (in.get() == Constants.MESSAGE_SOH_BYTES[0] && 
            in.get() == Constants.MESSAGE_SOH_BYTES[1] &&
            in.get() == Constants.MESSAGE_SOH_BYTES[2]) {
        	byte[] tmp = new byte[Constants.HEADER_LENGTH];
        	in.get(tmp);
        	String header = new String(tmp);
        	
            int bodyLen = 0;
            try {
            	bodyLen = Integer.parseInt(header.substring(12));
            } catch (NumberFormatException e) {
            	return false;
            }
            if(in.remaining() < bodyLen + Constants.MESSAGE_EOH_BYTES.length) {
            	return false;
            }
            tmp = new byte[bodyLen];
            in.get(tmp);
            if (in.get() == Constants.MESSAGE_EOH_BYTES[0] && 
                    in.get() == Constants.MESSAGE_EOH_BYTES[1] &&
                    in.get() == Constants.MESSAGE_EOH_BYTES[2]) {
            	return true;
            }
        }         

        return false;
    }
    
    /**
     * 
     * @param session 소켓 세션
     * @param in input 버퍼
     * @return 클라이언트 연동 프로토콜이 header 문자열
     * @throws IOException
     */
    private String getHeader(IoSession session, ByteBuffer in) throws IOException {    	
        byte[] bSOH = new byte[Constants.MESSAGE_SOH_BYTES.length];
        in.get(bSOH);        
        
        byte[] tmp = new byte[Constants.HEADER_LENGTH];
        in.get(tmp);
        String header = new String(tmp, Constants.DEFAULT_CHARSET);
        return header;
    }
    
    private byte[] getBody(IoSession session, ByteBuffer in, int bodyLen) {
    	byte[] bEOH = new byte[Constants.MESSAGE_EOH_BYTES.length];
    	byte[] body = null;        	        
        if(bodyLen > 0) {	        	
	        body = new byte[bodyLen];
	        in.get(body);		                
	        in.get(bEOH);		        
        }
        return body;
    }
    
    /**
     * input 메세지를 연동 프로토콜 규격에 맞게 decode하여 handler로 전달한다.
     * @param session 소켓 세션
     * @param in input 버퍼
     * @param out
     * @return 프로토콜 규격에 맞으면 MessageDecoderResult.OK, 맞지 않으면 MessageDecoderResult.NOT_OK
     */
    public MessageDecoderResult decode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) 
        throws Exception {
    	
    	//테스트코드
    	ByteBuffer buffer = in.duplicate();
    	byte[] aByte = new byte[buffer.remaining()];
    	buffer.get(aByte);
    	String header = new String(aByte, Constants.DEFAULT_CHARSET);
    	//테스트코드
        
        SessionLog.info(session, "#D-S#");
        MessageRequest request = parseRequest(session, in);
        if (request == null)
            return MessageDecoderResult.NOT_OK;

        out.write(request);
        SessionLog.info(session, "#D-E#");
        return MessageDecoderResult.OK;
    }
    
    /**
     * input 버퍼를 파싱하여 request 메세지 instance를 구성한다.
     * @param session 소켓 세션
     * @param in input 버퍼
     * @return request 메세지 instance
     */
    private MessageRequest parseRequest(IoSession session, ByteBuffer in) {
        MessageRequest request = null;
        
        try {
            String header = getHeader(session, in);
            String type = header.substring(0, 4);
            String transactionId = header.substring(4, 12);
            String bodyLen = header.substring(12);
            int iBodyLen = 0;
            try {
            	iBodyLen = Integer.parseInt(bodyLen);
            } catch (NumberFormatException e) {
            	
            }
            
            byte[] bBody = getBody(session, in, iBodyLen);
            
            SessionLog.info(session, "#D-C# MSG_HR[" + header + "]");                        
            
            request = MessageRequestFactory.createMessage(type);
            request.setType(type);
            request.setTransactionId(transactionId);
            request.setLength(bodyLen);
            
            SessionLog.info(session, "#D-C# MSG_HP["+request.getType()+"|"+request.getTransactionId()+"|"+request.getLength()+"]");
            
            byte[] bProp = new byte[4];
            byte[] bLen = new byte[8];
            byte[] bVal = null;
            
            String prop = "";
            int len = 0;
            for (int pos=0; pos<bBody.length;) {            	
            	System.arraycopy(bBody, pos, bProp, 0, Constants.PROP_KEY_LENGTH);
            	pos += Constants.PROP_KEY_LENGTH;
            	prop = new String(bProp, Constants.DEFAULT_CHARSET);
            	
            	System.arraycopy(bBody, pos, bLen, 0, Constants.PROP_VAL_LENGTH);
            	pos += Constants.PROP_VAL_LENGTH;
                len = Integer.parseInt(new String(bLen, Constants.DEFAULT_CHARSET));                
                
                bVal = new byte[len];                
                System.arraycopy(bBody, pos, bVal, 0, len);
                pos += len;
                
                if(Constants.BINARY_PROPS.contains(prop) == false) {
                	String val = new String(bVal, Constants.DEFAULT_CHARSET);
                    request.setProperty(prop, val);                    
                    SessionLog.info(session, "#D-C# MSG_CS["+prop+":("+len+")"+val+"]");	
                } else {
                	request.setBinary(prop, bVal);
                	SessionLog.info(session, "#D-C# MSG_CB["+prop+":("+len+")]");
                }
            }
        } catch (Exception e) {
            SessionLog.error(session, StackTracer.stackTrace(e));
            return null;
        }
        
        return request;
    }      
}
