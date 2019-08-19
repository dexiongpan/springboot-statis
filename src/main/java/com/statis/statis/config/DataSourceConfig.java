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
	
	    @Bean(name = "epg")
	    @ConfigurationProperties(prefix = "spring.datasource.druid.epg")
	    public DataSource epg() {
	    	 DruidDataSource dataSource = new EpgSSHDataSource();
	        return dataSource;
	    }

	    @Bean(name = "mobile")
	    @ConfigurationProperties(prefix = "spring.datasource.druid.mobile")
	    public DataSource mobile() {
	    	 DruidDataSource dataSource = new MobileSSHDataSource();
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
	    public DataSource multipleDataSource(@Qualifier("epg") DataSource epg,
	                                         @Qualifier("mobile") DataSource mobile,
	                                         @Qualifier("statis") DataSource statis) {
	        DynamicDataSource dynamicDataSource = new DynamicDataSource();
	        Map<Object, Object> targetDataSources = new HashMap<>();
	        targetDataSources.put(DBTypeEnum.epg.getValue(), epg);
	        targetDataSources.put(DBTypeEnum.mobile.getValue(), mobile);
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
	        sqlSessionFactory.setDataSource(multipleDataSource(epg(), mobile(),statis()));
//	        sqlSessionFactory.setVfs(SpringBootVFS.class);
	        
	        sqlSessionFactory.setTypeAliasesPackage("com.statis.statis.model");
	        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*.xml"));
	        return sqlSessionFactory.getObject();
	    }
	    
	    @Bean
	    public PlatformTransactionManager transactionManager() {
	        return new DataSourceTransactionManager(multipleDataSource(epg(), mobile(),statis()));
	    }
}
