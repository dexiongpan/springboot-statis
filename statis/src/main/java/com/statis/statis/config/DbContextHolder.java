package com.statis.statis.config;

import java.util.ArrayList;
import java.util.List;

public class DbContextHolder {
  
	private static final ThreadLocal contextHolder = new ThreadLocal<>();
	public static List<Object> dataSourceKeys = new ArrayList<>();
    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
    
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }
}
