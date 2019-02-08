package com.enjoybt.data_link.link.controller;

import com.enjoybt.common.Constants;
import com.enjoybt.data_link.link.service.LinkdataManageService;
import com.enjoybt.data_link.link.vo.LinkViewVO;
import com.nhncorp.lucy.security.xss.XssPreventer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Profile({"staging","link"})
@RequestMapping(value="/api/")
public class LinkDataAPIManageRestController {

private static final Logger LOGGER = LoggerFactory.getLogger(LinkDataAPIManageRestController.class);
	
	@Autowired
	private LinkdataManageService linkdataService;
	
//	@Value("#{sysConfig['LINK_MANAGER_PATH']}")
//	private String LINK_MANAGER_PATH;// /home/drght/java/LinkManager.jar
	private String LINK_MANAGER_PATH = "/home/drght/java/LinkManager.jar";

//	@Value("#{config['DROUGHT_API_KEY']}")
//	private String DROUGHT_API_KEY;
	private String DROUGHT_API_KEY = "sdlfj";

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@RequestMapping(value="updateLinkData.do", method={RequestMethod.POST})
	public Map<String, Object> updateLinkData(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestParam(value="link_nm", required=true) String name,
		@RequestParam(value="link_org_nm", required=true) String org,
		@RequestParam(value="link_url", required=true) String url,
		@RequestParam(value="link_cron", required=true) String cron,
		@RequestParam(value="link_time", required=true) String text,
		@RequestParam(value="link_table_schema_nm", required=true) String schemaName,
		@RequestParam(value="link_table_nm", required=true) String tableName,
		@RequestParam(value="user_id", required=true) String userId,
		@RequestHeader(value="apiKey", required=true) String apiKey
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		if(passwordEncoder.matches(DROUGHT_API_KEY, apiKey)) {
			try{
				url = XssPreventer.unescape(url);
				text = XssPreventer.unescape(text);
				
				linkdataService.updateLinkData(linkSn, name, org, url, cron, text, schemaName, tableName);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}catch(SQLException e) {
				LOGGER.error("Update LinkData Error!!", e);
			} catch (IOException e) {
				LOGGER.error("Update LinkData Error!!", e);
			} catch (InterruptedException e) {
				LOGGER.error("Update LinkData Error!!", e);
			}
		}else{
			LOGGER.error("Password Matches Failure : " + apiKey);
			result.put(Constants.KEY_MSG, "SERVICE ACCESS DENIED ERROR.");
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="insertLinkData.do", method={RequestMethod.POST})
	public Map<String, Object> insertLinkData(
		@RequestParam(value="link_nm", required=true) String name,
		@RequestParam(value="link_org_nm", required=true) String org,
		@RequestParam(value="link_url", required=true) String url,
		@RequestParam(value="link_cron", required=true) String cron,
		@RequestParam(value="link_time", required=true) String text,
		@RequestParam(value="link_table_schema_nm", required=true) String schemaName,
		@RequestParam(value="link_table_nm", required=true) String tableName,
		@RequestParam(value="user_id", required=true) String userId,
		@RequestParam(value="link_type", required=true) String type,
		@RequestHeader(value="apiKey", required=true) String apiKey
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		if(passwordEncoder.matches(DROUGHT_API_KEY, apiKey)) {
			try{
				url = XssPreventer.unescape(url);
				text = XssPreventer.unescape(text);
				
				linkdataService.insertLinkData(name, org, url, cron, text, schemaName, tableName, type);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}catch(SQLException e) {
				LOGGER.error("InsertLinkData Error!!", e);
			} catch (IOException e) {
				LOGGER.error("InsertLinkData Error!!", e);
			} catch (InterruptedException e) {
				LOGGER.error("InsertLinkData Error!!", e);
			}
		}else{
			LOGGER.error("Password Matches Failure : " + apiKey);
			result.put(Constants.KEY_MSG, "SERVICE ACCESS DENIED ERROR.");
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="requestCustomProcess.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> requestCustomProcess(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestHeader(value="apiKey", required=true) String apiKey
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;

		if(passwordEncoder.matches(DROUGHT_API_KEY, apiKey)) {
			try{
				LinkViewVO vo = linkdataService.selectLinkData(linkSn);
				//LINK_MANAGER_FILE;//java -jar /home/drght/java/LinkManager.jar #{linkSn} #{url}
				String jarPath = LINK_MANAGER_PATH;
				Process p2 = Runtime.getRuntime().exec(new String[]{"java", "-jar", jarPath, linkSn.toString(), vo.getLink_url()});
				p2.waitFor();
				p2.destroy();
				LOGGER.debug("Request : " + jarPath);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}catch(SQLException e) {
				LOGGER.error("requestCustomProcess ERROR", e);
			} catch (IOException e) {
				LOGGER.error("requestCustomProcess ERROR", e);
			} catch (InterruptedException e) {
				LOGGER.error("requestCustomProcess ERROR", e);
			}
		}else{
			LOGGER.error("Password Matches Failure : " + apiKey);
			result.put(Constants.KEY_MSG, "SERVICE ACCESS DENIED ERROR.");
		}

		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="updateAutoLinkAT.do", method={RequestMethod.POST})
	public Map<String, Object> updateAutoLinkAT(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestParam(value="use_at", required=true) String useAt,
		@RequestHeader(value="apiKey", required=true) String apiKey
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;

		if(passwordEncoder.matches(DROUGHT_API_KEY, apiKey)) {
			try{
				linkdataService.updateAutoLinkAT(linkSn, useAt);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}catch(SQLException e) {
				LOGGER.error("Update UseAt Error!!", e);
			} catch (IOException e) {
				LOGGER.error("Update UseAt Error!!", e);
			} catch (InterruptedException e) {
				LOGGER.error("Update UseAt Error!!", e);
			}
		}else{
			LOGGER.error("Password Matches Failure : " + apiKey);
			result.put(Constants.KEY_MSG, "SERVICE ACCESS DENIED ERROR.");
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
}
