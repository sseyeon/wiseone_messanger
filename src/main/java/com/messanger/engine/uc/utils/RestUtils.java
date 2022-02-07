package com.messanger.engine.uc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RestUtils {
	
	protected static final Log LOG = LogFactory.getLog(RestUtils.class);



	public static void test() throws Exception {
		final String TEST_URL = "https://was.lemp.kr/xxxxx/sendNoticexxxxx.xfon";
		URL url = new URL(TEST_URL);
		HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
		httpsCon.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		httpsCon.connect();
		InputStream is = httpsCon.getInputStream();
		int nread = 0;
		byte[] buf = new byte[8192];
		while ((nread = is.read(buf)) != -1) {
			System.out.write(buf, 0, nread);
		}

	}

	public static String sendNoti(String pm_sUrl, String pm_sNoticeType, String pm_sNickname, String pm_sSubject, 
			String pm_sEmail, String pm_sCallbackUrl) throws IOException {
		String lm_sRet = "";
		URL lm_oUrl = new URL(pm_sUrl);
		HttpsURLConnection im_oConn = null;
		BufferedReader lm_oBufReader = null;
		OutputStream lm_oOutStream = null;
		String enc = "UTF-8";
		try {
			im_oConn = (HttpsURLConnection) lm_oUrl.openConnection();
			im_oConn.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			
			// 서버로부터 메세지를 받을 수 있도록 한다. 기본값은 true이다.
			im_oConn.setDoInput(true);
		    // 헤더값을 설정한다.
			im_oConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    // 전달 방식을 설정한다. POST or GET, 기본값은 GET 이다.
			im_oConn.setRequestMethod("POST");
		    // 서버로 데이터를 전송할 수 있도록 한다. GET방식이면 사용될 일이 없으나, true로 설정하면 자동으로 POST로 설정된다. 기본값은 false이다.
			im_oConn.setDoOutput(true);
		    // POST방식이면 서버에 별도의 파라메터값을 넘겨주어야 한다.
		    String param = "noticetype=" + URLEncoder.encode(pm_sNoticeType, enc) +
		    			   "&nickname=" + URLEncoder.encode(pm_sNickname, enc) +
		    			   "&subject=" + URLEncoder.encode(pm_sSubject, enc) +
		    			   "&email=" + URLEncoder.encode(pm_sEmail, enc) +
		    			   "&url=" + URLEncoder.encode(pm_sCallbackUrl, enc);
		    lm_oOutStream = im_oConn.getOutputStream();
		    lm_oOutStream.write(param.getBytes(enc));
		    lm_oOutStream.flush();
		    
			//im_oConn.connect();
			int lm_iResponseCode = im_oConn.getResponseCode();
			if (lm_iResponseCode == HttpURLConnection.HTTP_OK) {
				StringBuffer lm_oBuffer = new StringBuffer();
				lm_oBufReader = new BufferedReader(new InputStreamReader(im_oConn.getInputStream()));
				int lm_iRead = -1;
				char[] lm_charBuffer = new char[1024];
				while ((lm_iRead = lm_oBufReader.read(lm_charBuffer)) != -1) {
					lm_oBuffer.append(lm_charBuffer, 0, lm_iRead);
				}// while
				lm_sRet = lm_oBuffer.toString();
			}
		} catch (IOException e) {
			LOG.error("벤치비 서버 알림연동 오류", e);
		} finally {
			try {lm_oOutStream.close();} catch (Exception e) {};
			try {im_oConn.disconnect();} catch (Exception e) {};
			if (lm_oBufReader != null)
				try {lm_oBufReader.close();} catch (Exception e) {};
		}

		return lm_sRet;
	}

	public static String readContent2(String pm_sUrl) throws IOException {
		URL lm_oUrl = new URL(pm_sUrl);
		HttpsURLConnection im_oConn = (HttpsURLConnection) lm_oUrl.openConnection();
		im_oConn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		BufferedReader lm_oBufReader = null;
		StringBuffer lm_oBuffer = new StringBuffer();
		try {
			lm_oBufReader = new BufferedReader(new InputStreamReader(im_oConn.getInputStream()));
			int lm_iRead = -1;
			char[] lm_charBuffer = new char[1024];
			while ((lm_iRead = lm_oBufReader.read(lm_charBuffer)) != -1) {
				lm_oBuffer.append(lm_charBuffer, 0, lm_iRead);
			}// while
			lm_oBufReader.close();
		} catch (IOException e) {
		} finally {
			im_oConn.disconnect();
			if (lm_oBufReader != null)
				lm_oBufReader.close();
		}

		return lm_oBuffer.toString();
	}

	public static boolean isResponse2(String pm_sUrl) throws IOException {
		boolean lm_bRet = false;
		URL lm_oUrl = new URL(pm_sUrl);
		HttpsURLConnection im_oConn = (HttpsURLConnection) lm_oUrl.openConnection();
		im_oConn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		// im_oConn.connect();
		int lm_iResponseCode = im_oConn.getResponseCode();
		if (lm_iResponseCode == HttpURLConnection.HTTP_OK)
			lm_bRet = true;
		im_oConn.disconnect();
		return lm_bRet;
	}

	public static String readContent(String pm_sUrl) throws IOException {
		URL lm_oUrl = new URL(pm_sUrl);
		HttpURLConnection im_oConn = (HttpURLConnection) lm_oUrl.openConnection();

		BufferedReader lm_oBufReader = null;
		StringBuffer lm_oBuffer = new StringBuffer();
		try {
			lm_oBufReader = new BufferedReader(new InputStreamReader(im_oConn.getInputStream()));
			int lm_iRead = -1;
			char[] lm_charBuffer = new char[1024];
			while ((lm_iRead = lm_oBufReader.read(lm_charBuffer)) != -1) {
				lm_oBuffer.append(lm_charBuffer, 0, lm_iRead);
			}// while
			lm_oBufReader.close();
		} catch (IOException e) {
		} finally {
			im_oConn.disconnect();
			if (lm_oBufReader != null)
				lm_oBufReader.close();
		}

		return lm_oBuffer.toString();
	}

	public static boolean isResponse(String pm_sUrl) throws IOException {
		boolean lm_bRet = false;
		URL lm_oUrl = new URL(pm_sUrl);
		HttpURLConnection im_oConn = (HttpURLConnection) lm_oUrl.openConnection();
		// im_oConn.connect();
		int lm_iResponseCode = im_oConn.getResponseCode();
		if (lm_iResponseCode == HttpURLConnection.HTTP_OK)
			lm_bRet = true;
		im_oConn.disconnect();
		return lm_bRet;
	}
}
