package com.enjoybt.data_link.link.service;

import com.enjoybt.data_link.link.vo.LinkViewVO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LinkdataManageService {

	public void updateLinkData(Integer linkSn, String name, String org, String url, String cron, String text, String schemaName, String tableName) throws SQLException, IOException, InterruptedException;
	
	public void insertLinkData(String name, String org, String url, String cron, String text, String schemaName, String tableName, String type) throws SQLException, IOException, InterruptedException;
	
	public LinkViewVO selectLinkData(Map<String, Object> param) throws SQLException;
	
	public List<LinkViewVO> selectListLinkData(Map<String, Object> param) throws SQLException;
	
	public Integer selectCountLinkData(Map<String, Object> param) throws SQLException;
	
	public Integer selectCountLinkDataLog(Map<String, Object> param) throws SQLException;
	
	public List<Map<String, Object>> selectListLinkDataLog(Map<String, Object> param) throws SQLException;
	
	public List<Map<String, Object>> selectListLinkRowData(Map<String, Object> param) throws SQLException;
	
	public Integer selectCountLinkRowData(Map<String, Object> param) throws SQLException;
	
	public List<Map<String, Object>> selectListLinkDataColumnInfo(Integer linkSn) throws SQLException;
	
	public void updateAutoLinkAT(Integer linkSn, String useAt) throws SQLException, IOException, InterruptedException;
	
	public LinkViewVO selectLinkData(Integer linkSn) throws SQLException;

	public List<List<Map<String, Object>>> linkDataCounts() throws SQLException;

	public String selectLinkDataLastTime(Map<String, Object> param) throws SQLException, NullPointerException;
}
