package com.statis.statis;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.statis.statis.wx.utils.AccessToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccessTokenTest {
	String appID="wx024ac2bcf06564c1";
	String appsecret ="b113e398a934d29cf7300807d5f46375";
	@Test
    public void getAccessToken() {
		String access_token= AccessToken.getAccess_token(appID, appsecret);
		System.out.println("access_token:"+access_token);
	}
}
