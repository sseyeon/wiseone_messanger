import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.message.MessageResponse;
import com.messanger.engine.uc.model.MsgType;
import com.messanger.engine.uc.model.RequestAnswer;
import com.messanger.engine.uc.model.SecureType;
import com.messanger.engine.uc.utils.StackTracer;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.common.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.apache.mina.util.SessionLog;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;

public class TestClient {
    public static void main(String[] args) throws IOException {
        TestClient cm = new TestClient();
        cm.run();
    }

    void run() {
        //소켓 생성
        SocketConnector connector = new SocketConnector();
        //서버쪽 주소 생성. ip는 localhost, 포트는 18501. 필요에 맞게 바꾸기
        SocketAddress address = new InetSocketAddress("127.0.0.1", 8099);
        SocketConnectorConfig config = new SocketConnectorConfig();
        config.setConnectTimeout(5);//second
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "utf-8" ))));//Set the encoding filter
        connector.getFilterChain().addLast( "logger", new org.apache.mina.filter.LoggingFilter() );
        IoSession session =null;
        ConnectFuture cf = connector.connect(address, new HandlerOne(), config);
        cf.join();

        if (cf.isConnected()) {
            session = cf.getSession();
            System.out.println ("Connection succeeded");
//            loginTest(session);
//            mailTest(session);
//            sendChannelMessage(session);
            webhook(session);
        } else{
            System.out.println("Connection failed!!!");
        }
    }

    // 로그인
    private void loginTest(IoSession session) {
        MessageResponse message = new MessageResponse(Constants.TYPE_LOGIN) {
        };
        message.setTransactionId("00000001");
        message.setProperty("LGIN", "88");
        message.setProperty("LOCL", "ko_KR");
        message.setProperty("PASS", "1234qwer");
        message.setProperty("SUID", "hjpark@unicologx.com");
        message.setProperty("VER_", "2.1.0.1");

        this.send(session, message);
    }

    // 메일발송요청
    private void mailTest(IoSession session) {
        MessageResponse message = new MessageResponse(Constants.TYPE_MAIL) {
        };
        message.setTransactionId("00000001");
        message.setProperty(Constants.PROP_MAIL_RECEIVER, "abc@kakao.com");// 리스트 메일 : 구분자 | 로 구분하여 전달. 그룹메일 : @@그룹코드
        message.setProperty(Constants.PROP_MAIL_CONTENT, "본문에 들어가는 확인");
        message.setProperty(Constants.PROP_TEMPLATE_ID, "00000001");

        this.send(session, message);
    }

    // 한명 또는 복수명에게 메시지를 전달합니다.
    private void sendMessage(IoSession session) {
        MessageResponse message = new MessageResponse(Constants.TYPE_RECEIVE_MEMO) {
        };
    }

    // 특정 체널에 등록된 사용자에게 메시지 전달합니다.
    private void sendChannelMessage(IoSession session) {
        MessageResponse message = new MessageResponse(Constants.TYPE_GROUP_MESSAGE) {
        };
        message.setTransactionId("00000001");
        message.setProperty(Constants.PROP_CHANNEL_ID, "SYS10001");// 리스트 메일 : 구분자 | 로 구분하여 전달. 그룹메일 : @@그룹코드
        message.setProperty(Constants.PROP_TEMPLATE_ID, "00000002");
        message.setProperty(Constants.PROP_SENDER_UID, "hjpark@unicologx.com");
        message.setProperty(Constants.PROP_MESSAGE_TYPE, MsgType.GENERAL.getCode());


        this.send(session, message);
    }

    private void webhook(IoSession session) {
        MessageResponse message = new MessageResponse(Constants.TYPE_WHOK) {
        };
        message.setTransactionId("00000001");
        message.setProperty(Constants.PROP_SENDER_UID, "SYS10001");// 리스트 메일 : 구분자 | 로 구분하여 전달. 그룹메일 : @@그룹코드
        message.setProperty(Constants.PROP_TEMPLATE_ID, "00000002");
        message.setProperty(Constants.PROP_RECEIVER_UID, "hjpark@unicologx.com");
        message.setProperty(Constants.PROP_REQUEST_TYPE, SecureType.USB.getCode());
        message.setProperty(Constants.PROP_REQUEST_ANSWER, RequestAnswer.APPROVAL.getCode());

        this.send(session, message);
    }


    private void send(IoSession session, MessageResponse message) {
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
            }           //LGIN0000000100000088PASS000000081234qwerVER_000000072.1.0.1SUID00000020hjpark@unicologx.comLOCL00000005ko_KR
//            baos.write("LGIN0000000100000088LOCL00000005ko_KRPASS000000081234qwerSUID00000020hjpark@unicologx.comVER_000000072.1.0.1".getBytes(Constants.DEFAULT_CHARSET));
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
        session.write(out);
        SessionLog.info(session, "#E-E#");
    }
}

class HandlerOne extends IoHandlerAdapter {

    public HandlerOne() {   //TODO Auto-generated constructor stub
    }

    @Override  public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {

    }

    @Override  public void messageReceived(IoSession session, Object message)
            throws Exception {
        //TODO Auto-generated method stub
        System.out.println("Received information");
        System.out.println("message :"+message.toString());
    }

    @Override  public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("Send carefully");
    }

    @Override  public void sessionClosed(IoSession session) throws Exception {
        //TODO Auto-generated method stub
        super.sessionClosed(session);
    }

    @Override  public void sessionCreated(IoSession session) throws Exception {
        System.out.println(session.getRemoteAddress().toString() +"---create");

    }

    @Override  public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println(session.getServiceAddress() +"IDS");
    }

    @Override  public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Connection opened:"+session.getLocalAddress());
    }

}