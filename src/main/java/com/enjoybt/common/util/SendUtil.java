package com.enjoybt.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

public class SendUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(SendUtil.class);

	public static String sendText(String rUrl, List<Object> list
								, String USE_BIGDATA_TIMEOUT
								, int BIGDATA_TIMEOUT_SEC
								, String USE_BIGDATA_READ_TIMEOUT
								, int BIGDATA_READ_TIMEOUT_SEC) throws IOException {

		LOGGER.info("BigData_URL : " + rUrl);
		
		String result = "Fail";
		HttpURLConnection conn = null;
		URL url;
		try {
			ObjectMapper om = new ObjectMapper();
			String val = om.writeValueAsString(list);
			url = new URL(rUrl);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setInstanceFollowRedirects(false);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        
	        if(USE_BIGDATA_TIMEOUT.equals("true")) {
	        	conn.setConnectTimeout(BIGDATA_TIMEOUT_SEC);
	        }
	        
	        if(USE_BIGDATA_READ_TIMEOUT.equals("true")) {
	        	conn.setReadTimeout(BIGDATA_READ_TIMEOUT_SEC);
	        }

	        OutputStream os = conn.getOutputStream();
	        os.write(val.getBytes());
	        os.flush();
	        if(conn.getInputStream() != null) {
	        	BufferedReader br = new BufferedReader(new InputStreamReader(
	  	  			  (conn.getInputStream())));	   
	  	        String output;
	  	        LOGGER.info("Output from Server .... \n");
	  	        while ((output = br.readLine()) != null) {
	  	        	LOGGER.info(output);
	  	        }
	  	        result = "Success";
	        }else {
	        	result = "Connection loss";
			}
		} catch (MalformedURLException e) {
			LOGGER.error(e.getMessage());
			result = "Wrong URL Problem";
		} catch (SocketTimeoutException e) {
			LOGGER.error(e.getMessage());
			result = "Timeout Problem";
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			result = "Network Problem";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result = "Other problem";
		} finally {
			conn.disconnect();
		}
        
		return result;
	}

}
