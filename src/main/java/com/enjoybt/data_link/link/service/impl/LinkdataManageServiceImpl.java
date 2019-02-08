package com.enjoybt.data_link.link.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enjoybt.common.database.dao.CommonDAO;
import com.enjoybt.common.util.OSUtil;
import com.enjoybt.common.util.linux.CronTabUtil;
import com.enjoybt.data_link.link.service.LinkdataManageService;
import com.enjoybt.data_link.link.vo.LinkViewVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@SuppressWarnings("unchecked")
public class LinkdataManageServiceImpl implements LinkdataManageService {

	/* 로그 관리 객체 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LinkdataManageServiceImpl.class);
	
	@Autowired
	private CommonDAO mainDAO;
	
	//CRONTAB_DIR
	/* 서버 유형 */
//	@Value("#{sysConfig['CRONTAB_DIR']}")
//	private String crontabDir;
	private String crontabDir = null;

	
//	@Value("#{sysConfig['LINK_MANAGER_COMMAND']}")
//	private String LINK_MANAGER_COMMAND;
	private String LINK_MANAGER_COMMAND = null;

	@Override
	public void updateLinkData(Integer linkSn, String name, String org, String url, String cron, String text, String schemaName, String tableName) throws SQLException, IOException, InterruptedException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("link_sn", linkSn);
		param.put("link_nm", name);
		param.put("link_org_nm", org);
		param.put("link_time", text);
		param.put("link_cron", cron);
		param.put("link_url", url);
		param.put("link_table_schema_nm", schemaName);
		param.put("link_table_nm", tableName);

		mainDAO.update("linkdata.updateLinkData", param);

		if(OSUtil.isUnix()) {	//TODO Crontab 설정
			LinkViewVO vo = (LinkViewVO)mainDAO.selectObject("linkdata.selectLinkData", param);
			if("Y".equals(vo.getUse_at())) {
				LOGGER.debug("Linux Crontab Setting : " + linkSn + " : " + LINK_MANAGER_COMMAND);
				String command = LINK_MANAGER_COMMAND.replace("#{linkSn}", linkSn + "").replace("#{url}", "\""+url+"\"");
				LOGGER.debug("Remove Crontab : " + crontabDir + " : " + linkSn);
				CronTabUtil.removeCrontab(crontabDir, linkSn + "");
				LOGGER.debug("Set Crontab : " + crontabDir + " : " + linkSn + " : " + command);
				CronTabUtil.setCrontab(crontabDir, "" + linkSn, cron, command);
			}
		}else{
			LOGGER.debug("Linux Crontab Not Setting : " + linkSn);
		}
	}
	
	@Override
	public void insertLinkData(String name, String org, String url, String cron, String text, String schemaName, String tableName, String type) throws SQLException, IOException, InterruptedException {
		LOGGER.debug(name + " : " + org + " : " + url + " : " + cron + " : " + text);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("link_nm", name);
		param.put("link_org_nm", org);
		param.put("link_time", text);
		param.put("link_cron", cron);
		param.put("link_url", url);
		param.put("link_table_schema_nm", schemaName);
		param.put("link_table_nm", tableName);
		param.put("link_type", type);
		
		mainDAO.insert("linkdata.insertLinkData", param);
		if(OSUtil.isUnix()) {	//TODO Crontab 설정
			LOGGER.debug("Linux Crontab Setting : " + param.get("link_sn"));
			String command = LINK_MANAGER_COMMAND.replace("#{linkSn}", param.get("link_sn").toString()).replace("#{url}","\"" + url + "\"");
			CronTabUtil.setCrontab(crontabDir, "" + param.get("link_sn"), cron, command);
			
		}else{
			LOGGER.debug("Linux Crontab Not Setting : " + param.get("link_sn"));
		}
	}
	
	@Override
	public List<LinkViewVO> selectListLinkData(Map<String, Object> param) throws SQLException {
		return (List<LinkViewVO>)mainDAO.selectList("linkdata.selectListLinkData", param);
	}
	
	@Override
	public Integer selectCountLinkData(Map<String, Object> param) throws SQLException {
		return (Integer)mainDAO.selectObject("linkdata.selectCountLinkData", param);
	}
	
	@Override
	public LinkViewVO selectLinkData(Map<String, Object> param) throws SQLException {
		return (LinkViewVO)mainDAO.selectObject("linkdata.selectLinkData", param);
	}
	
	@Override
	public Integer selectCountLinkDataLog(Map<String, Object> param) throws SQLException {
		return (Integer)mainDAO.selectObject("linkdata.selectCountLinkDataLog", param);
	}

	@Override
	public List<Map<String, Object>> selectListLinkDataLog(Map<String, Object> param) throws SQLException {
		return mainDAO.selectList("linkdata.selectListLinkDataLog", param);
	}
	
	@Override
	public List<Map<String, Object>> selectListLinkRowData(Map<String, Object> param) throws SQLException {
		return mainDAO.selectList("linkdata.selectListLinkRowData", param);
	}

	@Override
	public Integer selectCountLinkRowData(Map<String, Object> param) throws SQLException {
		return (Integer)mainDAO.selectObject("linkdata.selectCountLinkRowData", param);
	}
	
	@Override
	public List<Map<String, Object>> selectListLinkDataColumnInfo(Integer linkSn) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("link_sn", linkSn);
		LinkViewVO vo = (LinkViewVO)mainDAO.selectObject("linkdata.selectLinkData", param);
		
		param.clear();
		param.put("schemaName", vo.getLink_table_schema_nm());
		param.put("tableName", vo.getLink_table_nm());
		
		return mainDAO.selectList("linkdata.selectListLinkDataColumnInfo", param);
	}

	@Override
	public void updateAutoLinkAT(Integer linkSn, String useAt) throws SQLException, IOException, InterruptedException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("link_sn", linkSn);
		param.put("use_at", useAt);

		mainDAO.update("linkdata.updateLinkData", param);
		
		if(OSUtil.isUnix()) {//TODO Crontab 삭제
			LOGGER.debug("Linux Crontab Setting : " + linkSn);
			CronTabUtil.removeCrontab(crontabDir, linkSn + "");
			if("Y".equals(useAt)) {
				LinkViewVO vo = (LinkViewVO)mainDAO.selectObject("linkdata.selectLinkData", param);
				String command = LINK_MANAGER_COMMAND.replace("#{linkSn}", linkSn + "").replace("#{url}", "\"" +vo.getLink_url() + "\"");
				CronTabUtil.setCrontab(crontabDir, "" + linkSn, vo.getLink_cron(), command);
			}
		}else{
			LOGGER.debug("Linux Crontab Not Setting : " + linkSn);
		}
	}
	
	@Override
	public LinkViewVO selectLinkData(Integer linkSn) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("link_sn", linkSn);
		return (LinkViewVO)mainDAO.selectObject("linkdata.selectLinkData", param);
	}

	@Override
	public List<List<Map<String, Object>>> linkDataCounts() throws SQLException {
		List<List<Map<String, Object>>> result = new ArrayList<>();
		
		result.add(mainDAO.selectList("linkdata.realTimeLinkData"));
		result.add(mainDAO.selectList("linkdata.agencyLinkCategory"));
		result.add(mainDAO.selectList("linkdata.agencyLinkData"));
		
		return result;
	}

	@Override
	public String selectLinkDataLastTime(Map<String, Object> param) throws SQLException, NullPointerException {
		return (String)mainDAO.selectObject("linkdata.selectLinkDataLastTime", param);
	}
}
