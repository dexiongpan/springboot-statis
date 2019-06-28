package com.statis.statis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.statis.statis.util.PagePlugin;
@EnableTransactionManagement
@Configuration
@MapperScan("com.statis.statis.mapper.db*")
public class DataSourceConfig {
 
	    @Autowired
        private PagePlugin pageInterceptor;
	
	    @Bean(name = "db1")
	    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")
	    public DataSource db1() {
	    	 DruidDataSource dataSource = new DruidDataSource();
	        return dataSource;
	    }

	    @Bean(name = "db2")
	    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
	    public DataSource db2() {
	    	 DruidDataSource dataSource = new DruidDataSource();
		        return dataSource;
	    }

	    @Bean(name = "statis")
	    @Primary
	    @ConfigurationProperties(prefix = "spring.datasource.druid.statis")
	    public DataSource statis() {
	    	 DruidDataSource dataSource = new DruidDataSource();
		        return dataSource;
	    }

	    /**
	     * 动态数据源配置
	     *
	     * @return
	     */
	    @Bean
	    public DataSource multipleDataSource(@Qualifier("db1") DataSource db1,
	                                         @Qualifier("db2") DataSource db2,
	                                         @Qualifier("statis") DataSource statis) {
	        DynamicDataSource dynamicDataSource = new DynamicDataSource();
	        Map<Object, Object> targetDataSources = new HashMap<>();
	        targetDataSources.put(DBTypeEnum.db1.getValue(), db1);
	        targetDataSources.put(DBTypeEnum.db2.getValue(), db2);
	        targetDataSources.put(DBTypeEnum.statis.getValue(), statis);
	        dynamicDataSource.setTargetDataSources(targetDataSources);
	        dynamicDataSource.setDefaultTargetDataSource(statis);
	        DbContextHolder.dataSourceKeys.addAll(targetDataSources.keySet());
	        return dynamicDataSource;
	    }

	    @Bean("sqlSessionFactory")
	    public SqlSessionFactory sqlSessionFactory() throws Exception {
	        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
	        sqlSessionFactory.setPlugins(new Interceptor[]{pageInterceptor});
	        sqlSessionFactory.setDataSource(multipleDataSource(db1(), db2(),statis()));
//	        sqlSessionFactory.setVfs(SpringBootVFS.class);
	        sqlSessionFactory.setTypeAliasesPackage("com.statis.statis.model");
	        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));
	        return sqlSessionFactory.getObject();
	    }
	    
	    @Bean
	    public PlatformTransactionManager transactionManager() {
	        return new DataSourceTransactionManager(multipleDataSource(db1(), db2(),statis()));
	    }
}
