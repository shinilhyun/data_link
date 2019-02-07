package com.enjoybt.common.database.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CommonBatchDAO extends DaoSupport {

   /* 로그 관리 객체 */
   private static final Logger LOGGER = LoggerFactory.getLogger(CommonBatchDAO.class);

   /* SqlSession 객체 */
    @Resource(name = "batch")
   private SqlSession sqlBatchSession;

   /**
    * Description :  SqlSession 조회 메서드
    * @return (SqlSession)									- 사용중인 SqlSession 객체
    */
   public final SqlSession getSqlBatchSession() {
       return this.sqlBatchSession;
   }

   /* (non-Javadoc)
    * @see org.springframework.dao.support.DaoSupport#checkDaoConfig()
    */
   protected void checkDaoConfig() {
       Assert.notNull(this.sqlBatchSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
   }

   /**
    * Description : 동시 여러개 데이터를 Insert 하기 위한 메서드
    * @param mapperId(String)					- 적용할 SQL Mapper ID
    * @param paramList(List<Object> paramList	- Insert할 데이터
    * @return (Integer)						- 데이터 건수
    * @throws SQLException
    */
   public int insertBatch(String mapperId, List<Object> paramList) throws SQLException {
       int total = paramList.size();
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Insert Batch : Total - " + total);
       }
       int output = 0;
       int checkPer = 0;
       for(Object parameter : paramList) {
           output++;
           this.sqlBatchSession.insert(mapperId, parameter);
           int persent = (int)(((float)output / total) * 100);
//			if(persent != checkPer && LOGGER.isDebugEnabled()) {
           if(persent != checkPer && persent % 25 == 0 && LOGGER.isDebugEnabled()) {
               checkPer = persent;
               LOGGER.debug("Insert Batch("+output+") : " + persent + "% Complete");
           }
       }
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Insert Batch End");
       }
       return output;
   }

   /**
    * Description : 동시 여러개 데이터를 Update 하기 위한 메서드
    * @param mapperId(String)					- 적용할 SQL Mapper ID
    * @param paramList(List<Object> paramList	- Update할 데이터
    * @return (Integer)						- 데이터 건수
    * @throws SQLException
    */
   public int updateBatch(String mapperId, List<Object> paramList) throws SQLException {
       int total = paramList.size();
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Update Batch : Total - " + total);
       }
       int output = 0;
       int checkPer = 0;
       for(Object parameter : paramList) {
           output++;
           this.sqlBatchSession.update(mapperId, parameter);
           int persent = (int)(((float)output / total) * 100);
//			if(persent != checkPer && LOGGER.isDebugEnabled()) {
           if(persent != checkPer && persent % 25 == 0 && LOGGER.isDebugEnabled()) {
               checkPer = persent;
               LOGGER.debug("Update Batch("+output+") : " + persent + "% Complete");
           }
       }
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Update Batch End");
       }
       return output;
   }

   /**
    * Description : 동시에 여러개 데이터를 Delete 하기 위한 메서드
    * @param mapperId(String)					- 적용할 SQL Mapper ID
    * @param paramList(List<Object> paramList	- Delete할 데이터
    * @return (Integer)						- 데이터 건수
    * @throws SQLException
    */
   public int deleteBatch(String mapperId, List<Object> paramList) throws SQLException {
       int total = paramList.size();
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Delete Batch : Total - " + total);
       }
       int output = 0;
       int checkPer = 0;
       for(Object parameter : paramList) {
           output++;
           this.sqlBatchSession.delete(mapperId, parameter);
           int persent = (int)(((float)output / total) * 100);
//			if(persent != checkPer && LOGGER.isDebugEnabled()) {
           if(persent != checkPer && persent % 25 == 0 && LOGGER.isDebugEnabled()) {
               checkPer = persent;
               LOGGER.debug("Delete Batch("+output+") : " + persent + "% Complete");
           }
       }
       if(LOGGER.isDebugEnabled()) {
           LOGGER.debug("Delete Batch End");
       }
       return output;
   }
}
