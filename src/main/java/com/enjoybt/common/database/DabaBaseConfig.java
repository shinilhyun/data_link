package com.enjoybt.common.database;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DabaBaseConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean(name="datasource")
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory() throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:config/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:com/enjoybt/**/mapper/**/*_mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "main")
    public SqlSessionTemplate getSqlSessionTemplate() throws Exception
    {
        return new SqlSessionTemplate(getSqlSessionFactory());
    }

    @Bean
    public SqlSessionFactory getSqlBatchSessionFactory() throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:config/mybatis-batch-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:com/enjoybt/**/mapper/**/*_batch_mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "batch")
    public SqlSessionTemplate getSqlBatchSessionTemplate() throws Exception
    {
        return new SqlSessionTemplate(getSqlBatchSessionFactory());
    }
}
