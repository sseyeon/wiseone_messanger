package com.messanger.engine.uc.handler;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.message.request.WHOKRequest;
import com.messanger.engine.uc.message.response.WHOKResponse;
import com.messanger.engine.uc.utils.SessionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.apache.mina.util.SessionLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


// 메시져 서버가 WEB 서버에 요청을 전달.
public class WHOKHandler implements MessageHandler<WHOKRequest> {
    protected Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void messageReceived(IoSession session, WHOKRequest request) throws Exception {
        SessionLog.info(session, "#H-S# Webhook Handler");

        String uid = request.getSenderUid();
        WHOKResponse response = new WHOKResponse(request.getType());
        response.setTransactionId(request.getTransactionId());
        String errMsg = "";

        String schema = SessionUtils.getScheme(session);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault(); // HttpClient 생성
            HttpPut putRequest = new HttpPut(config.get("web.server.url")); //GET 메소드 URL 생성
            putRequest.addHeader("api-key", config.get("web.server.api_key")); //KEY 입력
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("type", request.getSecureType().getCode()));
            params.add(new BasicNameValuePair("sender", uid));
            params.add(new BasicNameValuePair("receiver", request.getReceiver()));
            params.add(new BasicNameValuePair("result", request.getRequestAnswer().getCode()));
            putRequest.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse httpResponse = httpClient.execute(putRequest);
            response.setStatus(httpResponse.getStatusLine().getStatusCode() + "");

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));

            String inputLine;
            StringBuffer res = new StringBuffer();

            while ((inputLine = reader.readLine()) != null) {
                res.append(inputLine);
            }

            reader.close();

            //Print result
            System.out.println(res.toString());
            httpClient.close();

            response.setResponseMsg(Constants.TRANSACTION_SUCCESS, "");
        } catch (Exception e) {
            SessionLog.error(session, e.getMessage(), e);
        }

        response.setResponseMsg(errMsg);
        session.write(response);
        SessionLog.info(session, "#H-E# UserStatusHandler");
    }
}
