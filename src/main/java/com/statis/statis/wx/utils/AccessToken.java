package com.statis.statis.wx.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class AccessToken {
	/**
     * 输入自己的id跟密码，获取微信的安全密令字符串
     * @param APP_ID 
     * @param APPSECRET
     * @return
     */
    public static String getAccess_token( String APP_ID,String APPSECRET) {
        //设置变量 url与返回值其中url使用拼接带入参数APP_ID， APPSECRET
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + APP_ID+ "&secret=" + APPSECRET;
        String accessToken = null;
        try {
            //设置链接
            URL urlGet = new URL(url);
            //设置外网代理链接
//            InetSocketAddress addr = new InetSocketAddress("http://pandx.free.idcfengye.com",80);            
//             Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); 
//             //启动链接
//            HttpURLConnection http = (HttpURLConnection) urlGet .openConnection(proxy);
            HttpURLConnection http= (HttpURLConnection) urlGet.openConnection();
            //设置链接参数与要求
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30�?
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30�?
//            链接
            http.connect();
            //获取返回值json字节流
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            //转化成字符串
            String message = new String(jsonBytes, "UTF-8");
//            转化成json对象然后返回accessToken属性的值
            JSONObject demoJson =JSONObject.parseObject(message);
            accessToken = demoJson.getString("access_token");
            System.out.println(accessToken);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}
