package com.statis.statis.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class HttpClientUtil {
  
	@Autowired
	private HttpClient httpClient;
	
	public Map<String, Object> getObject(String url) throws ParseException, ClientProtocolException, IOException {
		Map<String, Object> replayObject = new HashMap<String, Object>();
    	HttpResponse response=httpClient.execute(new HttpGet(url));
		String jsonStr=EntityUtils.toString(response.getEntity());
    	JSONObject jsonObject=JSONObject.parseObject(jsonStr);
    	replayObject.put("jsonObject", jsonObject);
    	replayObject.put("status", String.valueOf(response.getStatusLine().getStatusCode()));
    	return replayObject;
	}
	
}
