package com.enjoybt.data_link.link.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LinkService {
	public List<Map<String, Object>> selectListLayerInfo(Map<String, Object> param) throws Exception;
	
	public List<Map<String, Object>> selectListLayerTestInfo() throws Exception;
	
	public List<Map<String, Object>> emerYearSearch(Map<String, Object> params) throws Exception;

	public Map<String, Object> mapCenterAddress(Map<String, Object> params) throws Exception;
}
