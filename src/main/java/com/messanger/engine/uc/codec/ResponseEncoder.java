package com.messanger.engine.uc.codec;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.messanger.engine.uc.message.response.*;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.util.SessionLog;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;
import com.messanger.engine.uc.utils.StackTracer;

/**
 * output 메세지 encoder
 * @author skoh
 *
 */
public class ResponseEncoder implements MessageEncoder {

private static final Set<Class<?>> TYPES;
    
    static {
        Set<Class<?>> types = new HashSet<Class<?>>();
        
        //로그인 응답
        types.add(EVNTResponse.class);
        types.add(MSGIResponse.class);
        types.add(ORG_Response.class);
        types.add(LGINResponse.class);
        types.add(FILRResponse.class);
        types.add(GRPRResponse.class);        
        types.add(MEMRResponse.class);
        types.add(MSGRResponse.class);
        types.add(FILDResponse.class);
        types.add(FILSResponse.class);
        types.add(GRPSResponse.class);
        types.add(MEMSResponse.class);
        types.add(MSGSResponse.class);
        types.add(USR_Response.class);
        types.add(GWURResponse.class);
        
        types.add(PCIFResponse.class);
        types.add(PDIAResponse.class);
        types.add(PDISResponse.class);
        types.add(PFWDResponse.class);
        types.add(PSTAResponse.class);
        
        types.add(WCIRResponse.class);
        types.add(WCISResponse.class);
        types.add(WCIOResponse.class);
        
        types.add(RCISResponse.class);
        types.add(RCIRResponse.class);
        
        types.add(ONIDResponse.class);
        types.add(PWCHResponse.class);

        types.add(RMSGResponse.class);
        types.add(WHOKResponse.class);

        TYPES = Collections.unmodifiableSet(types);
    }

    /**
     * handler 에서 넘어온 메세지를 프로토콜 타입에 맞게 인코딩하여 전송
     */
    public void encode(IoSession session, Object message, ProtocolEncoderOutput output) 
        throws Exception {       
    	SessionLog.info(session, "#E-S#");        
        
        int packetLen = 0;
        byte[] bBody = null;
        byte[] bHeader = new byte[Constants.HEADER_LENGTH];
        try {
            MessageResponse response = (MessageResponse)message;
            SessionLog.info(session, "#E-C# MSG_TY["+response.getType()+"]");
            Iterator<String> it = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (it = response.getPropertyIterator(); it.hasNext();) {
                String key = it.next();
                String val = response.getProperty(key);
                long intLen = val == null ? 0 : val.getBytes("UTF-8").length;
                String sLen = StringUtils.leftPad(String.valueOf(intLen), Constants.PROP_VAL_LENGTH, '0');
             
                if(key.equals(Constants.PROP_ORG_LIST) == false) {
	                SessionLog.info(session, "#E-C# MSG_CS["+key+"("+sLen+")->"+val+"]");
                } else {
                	SessionLog.info(session, "#E-C# MSG_CS["+key+"("+sLen+")]");
                }
                baos.write(key.getBytes(Constants.DEFAULT_CHARSET));
                baos.write(sLen.getBytes(Constants.DEFAULT_CHARSET));
                if(intLen > 0) {
                	baos.write(val.getBytes(Constants.DEFAULT_CHARSET));
                }
            }
            for (it = response.getBinaryIterator(); it.hasNext();) {
            	String key = it.next();
                byte[] val = response.getBinary(key);
                long intLen = val == null ? 0 : val.length;
                String sLen = StringUtils.leftPad(String.valueOf(intLen), Constants.PROP_VAL_LENGTH, '0');
                SessionLog.info(session, "#E-C# MSG_CB["+key+"("+sLen+")]");
                baos.write(key.getBytes(Constants.DEFAULT_CHARSET));
                baos.write(sLen.getBytes(Constants.DEFAULT_CHARSET));
                if(intLen > 0) {
                	baos.write(val);
                }
            }
            bBody = baos.toByteArray();
            packetLen = Constants.MESSAGE_SOH_BYTES.length+Constants.HEADER_LENGTH+baos.size()+Constants.MESSAGE_EOH_BYTES.length;
            
            StringBuffer headerBuf = new StringBuffer();
            //TYPE
            headerBuf.append(response.getType());
            //TRANSACTION ID
            headerBuf.append(response.getTransactionId());
            //DATA LENGTH
            headerBuf.append(StringUtils.leftPad(String.valueOf(bBody.length), Constants.PROP_VAL_LENGTH, '0'));
            
            bHeader = headerBuf.toString().getBytes(Constants.DEFAULT_CHARSET);
            
            if(response.getType().equals(Constants.TYPE_LIST) == false) {
            	SessionLog.info(session, "#E-C# MSG_HR["+headerBuf.toString()+":"+bBody.length+"]");
            }
        } catch (Exception e) {
            SessionLog.error(session, StackTracer.stackTrace(e));
        }
        ByteBuffer out = ByteBuffer.allocate(packetLen);
        out.put(Constants.MESSAGE_SOH_BYTES);
        out.put(bHeader);
        out.put(bBody);
        out.put(Constants.MESSAGE_EOH_BYTES);
        out.flip();
        output.write(out);
        SessionLog.info(session, "#E-E#");
    }

    /**
     * 등록된 프로토콜 타입 set을 반환
     */
    public Set<Class<?>> getMessageTypes() {
        return TYPES;
    }
}
