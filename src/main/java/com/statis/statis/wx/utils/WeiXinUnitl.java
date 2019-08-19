package com.statis.statis.wx.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class WeiXinUnitl {
	public static WinXinEntity getWinXinEntity(String url) {
		WinXinEntity wx = new WinXinEntity();
		String access_token = getAccessToken();
		String ticket = getTicket(access_token);
		Map<String, String> ret = Sign.sign(ticket, url);
		wx.setTicket(ret.get("jsapi_ticket"));
		wx.setSignature(ret.get("signature"));
		wx.setNoncestr(ret.get("nonceStr"));
		wx.setTimestamp(ret.get("timestamp"));
		return wx;
	}
 
	// 获取token
	private static String getAccessToken() { 
        String access_token = "";  
        String grant_type = "client_credential";
        String AppId="wxe9ac39d6c118387b";
        String secret="da41743893d3d7cc97952832c4f84e97"; 
        
        
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;  //访问链接
        try {  
            URL urlGet = new URL(url);  
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
            http.setRequestMethod("GET"); // 必须是get方式请求  
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
            http.setDoOutput(true);  
            http.setDoInput(true);  
            http.connect();  
            InputStream is = http.getInputStream();  
            int size = is.available();  
            byte[] jsonBytes = new byte[size];  
            is.read(jsonBytes);  
            String message = new String(jsonBytes);  
            JSONObject demoJson = JSONObject.parseObject(message);  
            access_token = demoJson.getString("access_token");  
            is.close();  
        } catch (Exception e) {  
                e.printStackTrace();  
        }  
        return access_token;  
    }
 
	// 获取ticket
	private static String getTicket(String access_token) {
		String ticket = null;
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";// 这个url链接和参数不能变
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			ticket = demoJson.getString("ticket");
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticket;
	}

}
