package com.enjoybt.data_link.link.controller;

import com.enjoybt.common.Constants;
import com.enjoybt.common.util.http.WebUtil;
import com.enjoybt.data_link.link.service.LinkdataManageService;
import com.enjoybt.data_link.link.vo.LinkViewVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhncorp.lucy.security.xss.XssPreventer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/manage/linkdata/")
public class LinkDataManageRestController {
	/* 로그 관리 객체 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkDataManageRestController.class);
	
	@Autowired
	private LinkdataManageService linkdataService;
	
//	@Value("#{sysConfig['LINK_MANAGER_PATH']}")
//	private String LINK_MANAGER_PATH;// /home/drght/java/LinkManager.jar
	private String LINK_MANAGER_PATH = "/home/drght/java/LinkManager.jar";

//	@Value("#{sysConfig['SERVER_TYPE']}")
//	private String serverType;
	private String serverType = "";

	
//	@Value("#{sysConfig['LINK_MANAGER_API_PATH']}")
//	private String linkManagerAPIPath;
	private String linkManagerAPIPath = null;

//	@Value("#{config['DROUGHT_API_KEY']}")
//	private String DROUGHT_API_KEY;
	private String DROUGHT_API_KEY = null;

	private Map<String, String> getDroughtAPIHeader() {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		Map<String, String> header = new HashMap<String, String>();
		header.put("apiKey", bpe.encode(DROUGHT_API_KEY));
		return header;
	}
	
	@RequestMapping(value="updateLinkData.do", method={RequestMethod.POST})
	public Map<String, Object> updateLinkData(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestParam(value="name", required=true) String name,
		@RequestParam(value="org", required=true) String org,
		@RequestParam(value="url", required=true) String url,
		@RequestParam(value="cron", required=true) String cron,
		@RequestParam(value="text", required=true) String text,
		@RequestParam(value="schemaName", required=true) String schemaName,
		@RequestParam(value="tableName", required=true) String tableName,
		@RequestParam(value="stn_type", required=false) String stnType
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		try {
			url = XssPreventer.unescape(url);
			text = XssPreventer.unescape(text);
			
			if(serverType.equals("DEV") || serverType.equals("PRODUCTION")){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("link_sn", linkSn);
				param.put("link_nm", name);
				param.put("link_org_nm", org);
				param.put("link_time", text);
				param.put("link_cron", cron);
				param.put("link_url", URLEncoder.encode(url, "UTF-8"));
				param.put("link_table_schema_nm", schemaName);
				param.put("link_table_nm", tableName);

				String resultVal = WebUtil.getResponseText(linkManagerAPIPath+"api/updateLinkData.do", "POST", this.getDroughtAPIHeader(), param);
				result = new ObjectMapper().readValue(resultVal, HashMap.class);
				
				resultStr = (String)result.get("RESULT"); 
			}else{
				linkdataService.updateLinkData(linkSn, name, org, url, cron, text, schemaName, tableName);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}
			
			result.put(Constants.KEY_RESULT, resultStr);
		} catch (KeyManagementException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch (JsonParseException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch (JsonMappingException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch (IOException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch(SQLException e) {
			LOGGER.error("Update LinkData Error!!", e);
		} catch (InterruptedException e) {
			LOGGER.error("Update LinkData Error!!", e);
		}
		
		
		return result;
	}
	
	@RequestMapping(value="insertLinkData.do", method={RequestMethod.POST})
	public Map<String, Object> insertLinkData(
		@RequestParam(value="name", required=true) String name,
		@RequestParam(value="org", required=true) String org,
		@RequestParam(value="url", required=true) String url,
		@RequestParam(value="cron", required=true) String cron,
		@RequestParam(value="text", required=true) String text,
		@RequestParam(value="schemaName", required=true) String schemaName,
		@RequestParam(value="tableName", required=true) String tableName,
		@RequestParam(value="link_type", required=true) String type
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			url = XssPreventer.unescape(url);
			text = XssPreventer.unescape(text);
			
			if(serverType.equals("DEV") || serverType.equals("PRODUCTION")){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("link_nm", name);
				param.put("link_org_nm", org);
				param.put("link_time", text);
				param.put("link_cron", cron);
				param.put("link_url", URLEncoder.encode(url, "UTF-8"));
				param.put("link_table_schema_nm", schemaName);
				param.put("link_table_nm", tableName);
				param.put("link_type", type);
				
				String resultVal = WebUtil.getResponseText(linkManagerAPIPath+"api/insertLinkData.do", "POST", this.getDroughtAPIHeader(), param);
				result = new ObjectMapper().readValue(resultVal, HashMap.class);
				
				resultStr = (String)result.get("RESULT");
			}else{
				linkdataService.insertLinkData(name, org, url, cron, text, schemaName, tableName, type);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}
		}catch(SQLException e) {
			LOGGER.error("InsertLinkData Error!!", e);
		} catch (IOException e) {
			LOGGER.error("InsertLinkData Error!!", e);
		} catch (InterruptedException e) {
			LOGGER.error("InsertLinkData Error!!", e);
		} catch (KeyManagementException e) {
			LOGGER.error("InsertLinkData Error!!", e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("InsertLinkData Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="selectListLinkData.do", method={RequestMethod.POST})
	public Map<String, Object> selectListLinkData(
		@RequestParam(value = "keyword", required=false) String keyword,
		@RequestParam(value = "order_type", required = false, defaultValue = "LINK_NM") String orderType,
		@RequestParam(value = "orderby", required = false, defaultValue = "ASC") String orderby,
		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
		@RequestParam(value = "limit", required = false) Integer limit,
		@RequestParam(value = "link_type", required=true) String type
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			if(!StringUtils.isEmpty(keyword)) {
				param.put("keyword", keyword);
			}
			param.put("orderType", orderType);
			param.put("orderby", orderby);
			param.put("offset", (page-1)*limit);
			param.put("limit", limit);
			param.put("link_type", type);
			
			result.put(Constants.KEY_COUNT, linkdataService.selectCountLinkData(param));
			result.put(Constants.KEY_LIST, linkdataService.selectListLinkData(param));
			result.put(Constants.KEY_DATA, linkdataService.selectLinkDataLastTime(param));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList LinkData Error!!", e);
		} catch (Exception e) {
			LOGGER.error("SelectList LinkData Error!!", e);
		}
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}

	@RequestMapping(value="requestCustomProcess.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> requestCustomProcess(
		@RequestParam(value="linkSn", required=true) Integer linkSn
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			if(serverType.equals("DEV") || serverType.equals("PRODUCTION")){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("link_sn", linkSn);
				
//				String resultVal = WebUtil.getResponseText(linkManagerAPIPath+"api/requestCustomProcess.do", "POST", this.getDroughtAPIHeader(), param);
				String resultVal = WebUtil.getResponseText(linkManagerAPIPath+"api/requestCustomProcess.do", 1000*60*60*3, "POST", "UTF-8", this.getDroughtAPIHeader(), param);
				result = new ObjectMapper().readValue(resultVal, HashMap.class);
				
				resultStr = (String)result.get("RESULT");
			}else{
				LinkViewVO vo = linkdataService.selectLinkData(linkSn);
				//LINK_MANAGER_FILE;//java -jar /home/drght/java/LinkManager.jar #{linkSn} #{url}
				String jarPath = LINK_MANAGER_PATH;
				Process p2 = Runtime.getRuntime().exec(new String[]{"java", "-jar", jarPath, linkSn.toString(), vo.getLink_url()});
				p2.waitFor();
				p2.destroy();
				LOGGER.debug("Request : " + jarPath);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}
		}catch(SQLException e) {
			LOGGER.error("requestCustomProcess Error!!", e);
		} catch (IOException e) {
			LOGGER.error("requestCustomProcess Error!!", e);
		} catch (InterruptedException e) {
			LOGGER.error("requestCustomProcess Error!!", e);
		} catch (KeyManagementException e) {
			LOGGER.error("requestCustomProcess Error!!", e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("requestCustomProcess Error!!", e);
		}

		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="selectListLinkDataLog.do", method={RequestMethod.POST})
	public Map<String, Object> selectListLinkDataLog(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
		@RequestParam(value = "limit", required = false) Integer limit
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("link_sn", linkSn);
			param.put("offset", (page-1)*limit);
			param.put("limit", limit);

			result.put(Constants.KEY_COUNT, linkdataService.selectCountLinkDataLog(param));
			result.put(Constants.KEY_LIST, linkdataService.selectListLinkDataLog(param));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList LinkData Log Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	/*
	to-do 데이터
	@RequestMapping(value="selectListLinkDataColumnInfo.do", method={RequestMethod.POST})
	public Map<String, Object> selectListLinkDataColumnInfo(
			@RequestParam(value="link_sn", required=true) Integer linkSn
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		try{
			result.put(Constants.KEY_LIST, linkdataService.selectListLinkDataColumnInfo(linkSn));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList Link Data Column Info Error!!", e);
		}
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}*/
	
	@RequestMapping(value="updateAutoLinkAT.do", method={RequestMethod.POST})
	public Map<String, Object> updateAutoLinkAT(
		@RequestParam(value="link_sn", required=true) Integer linkSn,
		@RequestParam(value="use_at", required=true) String useAt
	) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			if(serverType.equals("DEV") || serverType.equals("PRODUCTION")){
				Map<String, Object> param = new HashMap<String, Object>();
				
				param.put("link_sn", linkSn);
				param.put("use_at", useAt);

				String resultVal = WebUtil.getResponseText(linkManagerAPIPath+"api/updateAutoLinkAT.do", "POST", this.getDroughtAPIHeader(), param);
				result = new ObjectMapper().readValue(resultVal, HashMap.class);
				
				resultStr = (String)result.get("RESULT");
			}else{
				linkdataService.updateAutoLinkAT(linkSn, useAt);
				resultStr = Constants.VALUE_RESULT_SUCCESS;
			}
		}catch(SQLException e) {
			LOGGER.error("Update UseAt Error!!", e);
		} catch (IOException e) {
			LOGGER.error("Update UseAt Error!!", e);
		} catch (InterruptedException e) {
			LOGGER.error("Update UseAt Error!!", e);
		} catch (KeyManagementException e) {
			LOGGER.error("Update UseAt Error!!", e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Update UseAt Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="selectListLinkRowData.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> selectListLinkRowData(
			@RequestParam(value="link_sn", required=true) Integer linkSn,
			@RequestParam(value="column", required=false) String column,
			@RequestParam(value="columnStyle", required=false) String columnStyle,
			@RequestParam(value="comparison", required=false) String comparison,
			@RequestParam(value="val", required=false) String val,
			@RequestParam(value="page", required=false, defaultValue="1") Integer page,
			@RequestParam(value="rows", required=false, defaultValue="10") Integer pageRow,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		try{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("link_sn", linkSn);
			LinkViewVO vo = (LinkViewVO)linkdataService.selectLinkData(param);
			param.clear();
			param.put("tableName", vo.getLink_table_nm());
			if(!StringUtils.isEmpty(column)) {
				param.put("column", XssPreventer.unescape(column));
			}
			if(!StringUtils.isEmpty(comparison)) {
				param.put("comparison", XssPreventer.unescape(comparison));
			}
			Object value = null;
			if(!StringUtils.isEmpty(columnStyle) && !"like".equals(comparison.toLowerCase())) {
				if(columnStyle.indexOf("double") != -1 || columnStyle.indexOf("numeric") != -1) {
					value = Double.parseDouble(val);
				}else if(columnStyle.indexOf("integer") != -1) {
					value = Integer.parseInt(val);
				}else{
					value = val;
				}
			}else{
				value = val;
			}
			
			if(value instanceof String) {
				value = XssPreventer.unescape((String)value);
			}
			
			param.put("val", value);
			param.put("sidx", sidx);
			param.put("sord", sord);
			param.put("limit", pageRow);
			param.put("offset", pageRow * (page - 1));
			
			result.put(Constants.KEY_COUNT, linkdataService.selectCountLinkRowData(param));
			result.put(Constants.KEY_LIST, linkdataService.selectListLinkRowData(param));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList Link Row Data Error!!", e);
		}
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="linkDataCounts.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> linkDataCounts() throws Exception {
		Map<String, Object> result = new HashMap<>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try {
			result.put(Constants.KEY_LIST, linkdataService.linkDataCounts());
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		} catch (Exception e) {
			LOGGER.error("linkDataCounts Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
}
