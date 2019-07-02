package com.statis.statis.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(value = -100)
@Slf4j
@Aspect
public class DataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    /**
     * 切换数据源
     *
     * @param point
     * @param targetDataSource
     */
    @Before("@annotation(targetDataSource))")
    public void switchDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        if (!DbContextHolder.containDataSourceKey(targetDataSource.value())) {
            logger.error("DataSource [{}] doesn't exist, use default DataSource [{}]", targetDataSource.value());
        } else {
            // 切换数据源
        	DbContextHolder.setDbType(targetDataSource.value());
            logger.info("Switch DataSource to [{}] in Method [{}]",
            		DbContextHolder.getDbType(), point.getSignature());
            System.out.println("datasource:"+DbContextHolder.getDbType());
        }
    }

    /**
     * 重置为默认数据源
     *
     * @param point
     * @param targetDataSource
     */
    @After("@annotation(targetDataSource))")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        // 将数据源置为默认数据源
    	DbContextHolder.clearDbType();
        logger.info("Restore DataSource to [{}] in Method [{}]",
        		DbContextHolder.getDbType(), point.getSignature());
    }
}
