package com.statis.statis.config;

import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class EpgSSHDataSource extends DruidDataSource{
    private static final Logger logger = LoggerFactory.getLogger(EpgSSHDataSource.class);
	private static final long serialVersionUID = 1L;
    private static final int LOCAl_PORT=3307;
    private static final String MYSQL_REMOTE_SERVER="10.1.61.226";
    private static final int REMOTE_PORT=3306;
	@Override
	public void init() throws SQLException {
		// TODO Auto-generated method stub
		if(inited) {
			return;
		}
		connectSession();
		super.init();
		inited = true;
	}

	private void connectSession() {
		// TODO Auto-generated method stub
		String username="chances";
		String password = "YnYs@chances2109";
		String host = "172.31.4.152";
		int port=22;
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(username,host,port);
			session.setPassword(password);
			 Properties config = new Properties();
		     config.put("StrictHostKeyChecking", "no");
		     session.setConfig(config);
			session.connect(1500);
			session.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT); 
		} catch (Exception e) {
			logger.error("出错了"+e);
					}
	}

	
}
