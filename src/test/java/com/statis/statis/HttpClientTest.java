package com.statis.statis;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.statis.statis.util.CheckUrl;
import com.statis.statis.util.HttpClientUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpClientTest {
//    @Autowired
//	private HttpClient httpClient;
	
    @Test
    public void testHttpClient() throws ClientProtocolException, IOException {
    	
    	//HttpResponse str=	httpClient.execute(new HttpGet("http://183.224.6.3:7096/pics/content/201906/17/20190617135900431.jpg"));
    	
    	String statuString=CheckUrl.checkValidIP("http://183.224.6.3:7096/pics/biz/201906/05/2019060514212625724186.jpg111");
//    	JSONObject jsonObject1 = JSONObject.parseObject(str);
//    	Object object=jsonObject1.getJSONArray("areaDatas").get(0);
//    	JSONObject areaData=(JSONObject) JSONObject.parse(object.toString());
       	System.out.println(statuString);
    	
//    	HttpResponse response=httpClient.execute(new HttpGet("http://183.224.6.3:7096/pics/biz/201906/05/2019060514212625724186.jpg111"));
//    	
//    	System.out.println("响应状态码:"+response.getStatusLine().getStatusCode());
    }
}
