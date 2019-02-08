package com.enjoybt.data_link.link.service.impl;

import com.enjoybt.common.database.dao.CommonDAO;
import com.enjoybt.data_link.link.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class LinkServiceImpl implements LinkService {
	
	@Autowired
	private CommonDAO mainDAO;
	
	//CRONTAB_DIR
	/* 서버 유형 */
//	@Value("#{sysConfig['CRONTAB_DIR']}")
//	private String crontabDir;

	
//	@Value("#{sysConfig['LINK_MANAGER_COMMAND']}")
//	private String LINK_MANAGER_COMMAND;//java -jar /home/drght/java/LinkManager.jar #{linkSn} #{url}

	@Override
	public List<Map<String, Object>> selectListLayerInfo(Map<String, Object> param) throws Exception {
		return mainDAO.selectList("link.selectListLayerInfo", param);
	}

	@Override
	public List<Map<String, Object>> selectListLayerTestInfo() throws Exception {
		return mainDAO.selectList("link.selectListLayerTestInfo");
	}
	
	@Override
	public List<Map<String, Object>> emerYearSearch(Map<String, Object> params) throws Exception {
		return mainDAO.selectList("link.emerYearSearch", params);
	}

	@Override
	public Map<String, Object> mapCenterAddress(Map<String, Object> params) throws Exception {
		return (Map<String, Object>) mainDAO.selectObject("link.mapCenterAddress", params);
	}

}
