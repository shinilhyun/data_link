package com.enjoybt.common.util.http;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 *  Class Name : WebUtil.java
 *  Description : 다른 서버와의 연계를 위한 HTTP/HTTPS 프로토콜 연계 기능 관련 클래스
 *  Modification Information
 * 
 *     수정일			수정자				수정내용
 *   ---------------------------------------------------
 *   2018. 3. 15.	장재호				최초 생성
 *
 *  @author 장재호
 *  @since 2018. 3. 15.
 *  @version 1.0
 * 
 *  Copyright (C) 2018 by ㈜제이비티 All right reserved.
 */
@SuppressWarnings("unchecked")
public class WebUtil {

	/* 로거 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebUtil.class);

	/**
	 * Description : HTTP/HTTPS API 연계를 위한 기본 기능 메서드
	 * @param reqUrl(String)				- 연결 URL
	 * @param timeout(Integer)				- 연결 시간 정의(밀리세컨드)
	 * @param method(String)				- HTTP Method 유형(ex : GET, POST)
	 * @param encode(String)				- Connection 인코딩(ex : UTF-8)
	 * @param headers(Map<String, String>)	- Connection연결시 보낼 Header 정보
	 * @param param(Map<String, Object>)	- Connection연결시 보낼 파라미터 정보
	 * @return (String)						- 응답결과
	 * @throws NoSuchAlgorithmException	
	 * @throws KeyManagementException
	 */
	public static String getResponseText(String reqUrl, Integer timeout, String method, String encode, Map<String, String> headers, Map<String, Object> param) throws NoSuchAlgorithmException, KeyManagementException {
		HttpURLConnection httpURLConnection = null;
		InputStream is = null; 
		String result = null;
		try {
			String query = null;
			if(param != null) {
				StringBuffer paramBuffer = new StringBuffer();
				Iterator<String> keys = param.keySet().iterator();
				while(keys.hasNext()){
					String paramKey = keys.next();
					Object val = param.get(paramKey);
					paramBuffer.append(paramKey+"="+val+"&");
				}
				query = paramBuffer.toString();
				query = query.substring(0, query.lastIndexOf("&"));
			}
			if("POST".equals(method.toUpperCase())) {
				is = new ByteArrayInputStream(query.getBytes(encode));
			}else{
				if(query != null) {
					if(reqUrl.indexOf("?") != -1) {
						reqUrl += query.toString();
					}else{
						reqUrl += ("?" +query.toString()); 
					}
				}
			}
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			LOGGER.debug("Request URL : " + reqUrl);
			URL targetURL = new URL(reqUrl);
			httpURLConnection = (HttpURLConnection)targetURL.openConnection();

			if(timeout > 0) {
				httpURLConnection.setConnectTimeout(timeout);
				httpURLConnection.setReadTimeout(timeout);
			}
			
			if(headers != null && !headers.isEmpty()) {
				Iterator<String> iterHeaderKey = headers.keySet().iterator();
				
				while(iterHeaderKey.hasNext()) {
					String key = iterHeaderKey.next();
					httpURLConnection.addRequestProperty(key, headers.get(key));
					LOGGER.debug("HEADER("+key+") : " + headers.get(key));
				}
			}
			httpURLConnection.setRequestMethod(method.toUpperCase());
			if("POST".equals(method.toUpperCase())) {
				httpURLConnection.setDoOutput(true); 
				IOUtils.copy(is, httpURLConnection.getOutputStream());
			}
			
			if(httpURLConnection.getResponseCode() == 200) {
				result = IOUtils.toString(httpURLConnection.getInputStream(), encode);
			}else{
				result = IOUtils.toString(httpURLConnection.getErrorStream(), encode);
			}
			
		} catch (IOException e) {
			LOGGER.error("Request Error", e);
		} finally {
			try { if(is != null) { is.close(); } } catch(IOException e) {
				LOGGER.error("exception", e);
			} 
			if(httpURLConnection != null) { httpURLConnection.disconnect(); }
		}
		
		return result;
	}
	
	/**
	 * Description : 기본 요청 메서드에 연결 시간(10초)을 미리 지정해 놓은 메서드
	 * @param url(String)					- 연결 URL
	 * @param method(String)				- HTTP Method 유형(ex : GET, POST)
	 * @param encode(String)				- Connection 인코딩(ex : UTF-8)
	 * @param headers(Map<String, String>)	- Connection연결시 보낼 Header 정보
	 * @param param(Map<String, Object>)	- Connection연결시 보낼 파라미터 정보
	 * @return (String)						- 응답결과
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getResponseText(String url, String method, String encode, Map<String, String> headers, Map<String, Object> param) throws KeyManagementException, NoSuchAlgorithmException {
		return getResponseText(url, 1000*10, method, encode, headers, param);
	}
	
	/**
	 * Description : 기본 요청 메서드에 연결 시간(10초), 인코딩(UTF-8) 정보를 미리 지정해 놓은 메서드
	 * @param url(String)					- 연결 URL
	 * @param method(String)				- HTTP Method 유형(ex : GET, POST)
	 * @param headers(Map<String, String>)	- Connection연결시 보낼 Header 정보
	 * @param param(Map<String, Object>)	- Connection연결시 보낼 파라미터 정보
	 * @return (String)						- 응답결과
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getResponseText(String url, String method, Map<String, String> headers, Map<String, Object> param) throws KeyManagementException, NoSuchAlgorithmException {
		return getResponseText(url, method, "UTF-8", headers, param);
	}
	
	/**
	 * Description : 기본 요청 메서드에 연결 시간(10초), 인코딩(UTF-8), 헤더(null) 정보를 미리 지정해 놓은 메서드
	 * @param url(String)					- 연결 URL
	 * @param method(String)				- HTTP Method 유형(ex : GET, POST)
	 * @param param(Map<String, Object>)	- Connection연결시 보낼 파라미터 정보
	 * @return (String)						- 응답결과
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getResponseText(String url, String method, Map<String, Object> param) throws KeyManagementException, NoSuchAlgorithmException {
		return getResponseText(url, method, null, param);
	}
	
	/**
	 * Description : 기본 요청 메서드에 연결 시간(10초), 인코딩(UTF-8), 헤더(null), 메서드(GET) 정보를 미리 지정해 놓은 메서드
	 * @param url(String)					- 연결 URL
	 * @param param(Map<String, Object>)	- Connection연결시 보낼 파라미터 정보
	 * @return (String)						- 응답결과
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getResponseText(String url, Map<String, Object> param) throws KeyManagementException, NoSuchAlgorithmException {
		return getResponseText(url, "GET", param);
	}
	
	/**
	 * Description : 기본 요청 메서드에 연결 시간(10초), 인코딩(UTF-8), 헤더(null), 메서드(GET), 파라미터(null) 정보를 미리 지정해 놓은 메서드
	 * @param url(String)					- 연결 URL
	 * @return (String)						- 응답결과
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getResponseText(String url) throws KeyManagementException, NoSuchAlgorithmException {
		return getResponseText(url, null);
	}
	
	/**
	 * Description : 동인근원정책(Same-Origin Policy) 정책을 해결하기 위한 방법으로써, 사용자의 요청/응답을 특정 API로 연결해주는 역할을 수행
	 * @param reqUrl(String)				- 연결 URL
	 * @param encode(String)				- Connection 인코딩(ex : UTF-8)
	 * @param request(HttpServletRequest)	- 사용자 요청 객체
	 * @param response(HttpServletResponse)	- 사용자 응답 객체
	 */
	public static void getProxy(String reqUrl, String encode, HttpServletRequest request, HttpServletResponse response) {
		getProxy(reqUrl, encode, null, request, response);
	}
	
	public static void getProxy(String reqUrl, String encode, Map<String,String> addHeader, HttpServletRequest request, HttpServletResponse response) {
		HttpURLConnection httpURLConnection = null;
		InputStream is = null; 
		try {
			if("POST".equals(request.getMethod().toUpperCase())) {
				Enumeration<String> e = request.getParameterNames();
				StringBuffer param = new StringBuffer();
				while(e.hasMoreElements()){
					String paramKey = e.nextElement();
					param.append(paramKey+"="+(String)request.getParameter(paramKey)+"&");
				}
				String query = param.toString();
				query = query.substring(0, query.lastIndexOf("&"));
				is = new ByteArrayInputStream(query.getBytes(encode));
			}else{
				if(request.getQueryString() != null && !"".equals(request.getQueryString())){
					reqUrl = reqUrl + "?" + request.getQueryString(); 
				}
			}

			URL targetURL = new URL(reqUrl);
			httpURLConnection = (HttpURLConnection)targetURL.openConnection();
			
			Enumeration<String> headerKey = request.getHeaderNames();
			while(headerKey.hasMoreElements()) {
				String key = headerKey.nextElement();
				String value = request.getHeader(key);
				httpURLConnection.addRequestProperty(key, value);
			}
			
			if(addHeader != null) {
				Iterator<String> keys = addHeader.keySet().iterator();
				while(keys.hasNext()) {
					String key = keys.next();
					String value = addHeader.get(key);
					httpURLConnection.addRequestProperty(key, value);
				}
			}

			httpURLConnection.setRequestMethod(request.getMethod().toUpperCase());
			if("POST".equals(request.getMethod().toUpperCase())) {
				httpURLConnection.setDoOutput(true); 
				IOUtils.copy(is, httpURLConnection.getOutputStream());
			}
			
			response.setContentType(httpURLConnection.getContentType());
			
			if(httpURLConnection.getResponseCode() == 200) {
				IOUtils.copy(httpURLConnection.getInputStream(), response.getOutputStream());
			}else{
				IOUtils.copy(httpURLConnection.getErrorStream(), response.getOutputStream());
			}
			
		} catch (IOException e) {
			if(LOGGER.isErrorEnabled()) {
				LOGGER.error("Proxy Error", e);
			}
		} finally {
			try { if(is != null) { is.close(); } } catch(IOException e) {
				if(LOGGER.isErrorEnabled()) {
					LOGGER.error("exception", e);
				}
			} 
			if(httpURLConnection != null) { httpURLConnection.disconnect(); }
		}
	}

	/**
	 * Description : getProxy(reqUrl, encode, request, response)에서 인코딩(UTF-8) 정보를 기본값으로 세팅한 메서드
	 * @param reqUrl(String)				- 연결 URL
	 * @param request(HttpServletRequest)	- 사용자 요청 객체
	 * @param response(HttpServletResponse)	- 사용자 응답 객체
	 */
	public static void getProxy(String reqUrl, HttpServletRequest request, HttpServletResponse response) {
		getProxy(reqUrl, "UTF-8", request, response);
	}
	
}
