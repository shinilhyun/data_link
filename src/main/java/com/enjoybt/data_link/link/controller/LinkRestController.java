package com.enjoybt.data_link.link.controller;

import com.enjoybt.common.Constants;
import com.enjoybt.data_link.link.service.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/link/")
public class LinkRestController {
	/* 로그 관리 객체 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRestController.class);
	
	@Autowired
	private LinkService linkService;
	
//	@Value("#{sysConfig['LINK_MANAGER_PATH']}")
//	private String LINK_MANAGER_PATH;// /home/drght/java/LinkManager.jar

	@RequestMapping(value="selectListLayerInfo.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> selectListLayerInfo(
			@RequestParam(value="page", required=true) String page
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			Map<String, Object> param = new HashMap<>();
			param.put("page", page);
			
			result.put(Constants.KEY_LIST, linkService.selectListLayerInfo(param));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(Exception e) {
			LOGGER.error("SelectList Layer Info Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}
	
	@RequestMapping(value="selectListLayerTestInfo.do", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> selectListLayerTestInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try{
			result.put(Constants.KEY_LIST, linkService.selectListLayerTestInfo());
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(Exception e) {
			LOGGER.error("SelectList Layer Info Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		return result;
	}

	@RequestMapping(value="mapCenterAddress.do", method={RequestMethod.POST})
	public Map<String, Object> mapCenterAddress(
			@RequestParam(value="x") float lon,
			@RequestParam(value="y") float lat
			) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("lon", lon);
			params.put("lat", lat);
			
			result.put(Constants.KEY_DATA, linkService.mapCenterAddress(params));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList Link Row Data Error!!", e);
		}

		result.put(Constants.KEY_RESULT, resultStr);
		
		return result;
	}
	
	@RequestMapping(value="emerYearSearch.do", method={RequestMethod.POST})
	public Map<String, Object> emerYearSearch(
			@RequestParam(value="emerType") String emerType
			) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String resultStr = Constants.VALUE_RESULT_FAILURE;
		
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("emerType", emerType);
			
			result.put(Constants.KEY_LIST, linkService.emerYearSearch(params));
			resultStr = Constants.VALUE_RESULT_SUCCESS;
		}catch(SQLException e) {
			LOGGER.error("SelectList Link Row Data Error!!", e);
		}
		
		result.put(Constants.KEY_RESULT, resultStr);
		
		return result;
	}
}
