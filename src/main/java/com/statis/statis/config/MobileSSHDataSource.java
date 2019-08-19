package com.statis.statis.config;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class MobileSSHDataSource extends DruidDataSource{

	private static final long serialVersionUID = 1L;
    
	private static final int LOCAl_PORT=3308;
    private static final String MYSQL_REMOTE_SERVER="10.1.61.222";
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
			session.setConfig("StrictHostKeyChecking","no");
			session.connect();
			session.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT); 
		} catch (Exception e) {
					}
	}

}
