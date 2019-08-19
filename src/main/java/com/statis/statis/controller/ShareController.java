package com.statis.statis.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.statis.statis.util.HttpClientUtil;
import com.statis.statis.wx.utils.CheckoutUtil;
import com.statis.statis.wx.utils.WXUnitl;
import com.statis.statis.wx.utils.WeiXinUnitl;
import com.statis.statis.wx.utils.WinXinEntity;

@CrossOrigin(origins = "*", maxAge = 3600) 
@Controller
public class ShareController {
    
	@Autowired
	private HttpClient httpClient;
	

	
	@RequestMapping("wechat/auth")
	public void auth(HttpServletRequest request, HttpServletResponse response) {
		boolean isGet = request.getMethod().toLowerCase().equals("get");
		PrintWriter print;
		if (isGet) {
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce =request.getParameter("nonce");
			String echostr=request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
	}
	
	
	@RequestMapping("/wx/callback")
	public  void getStartURLToGetCode(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * String takenUrl =
		 * "https://open.weixin.qq.com/connect/oauth2/authorize?appid=""&redirect_uri=""&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
		 */
		String code = request.getParameter("code");

		// 获取 access_token and oppenid
        System.out.println("callback:code:"+code);
		getAccess_token("wxe9ac39d6c118387b", "da41743893d3d7cc97952832c4f84e97", code);
		

	}
   
	//通过code获取网页授权access_token,openid
	public  void getAccess_token(String APPID, String SECRET, String code) {

		String access_token = "";
		String openid = "";

		/*
		 * String aturl =
		 * "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
		 * + APPID + "&secret=" + SECRET;
		 */
		String aturl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + SECRET
				+ "&code=" + code + "&grant_type=authorization_code";
		try {
			HttpGet request = new HttpGet(aturl);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());

				JSONObject jsonResult =  JSONObject.parseObject(strResult);
				access_token = (String) jsonResult.get("access_token");
				openid = (String) jsonResult.get("openid");
				System.out.println("OPENID:"+openid+":access_token:"+access_token);
				getUserInfo(access_token, openid);
			} else {
				System.out.println("error");
			}

		} catch (IOException e) {
			// System.out.println("get请求提交失败:" + access_token_url + e);

		}

	}
	
	
	//获取用户信息
	public void getUserInfo(String access_token,String openid) {
		String userInfoUrl="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String nickname ="";
		String headimgurl="";
		try {
			HttpGet request = new HttpGet(userInfoUrl);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());

				JSONObject jsonResult =  JSONObject.parseObject(strResult);
				nickname = (String) jsonResult.get("nickname");
				headimgurl = (String) jsonResult.get("headimgurl");
				System.out.println("nickname:"+nickname+":headimgurl:"+headimgurl);
			} else {
				System.out.println("error");
			}

		} catch (IOException e) {
			// System.out.println("get请求提交失败:" + access_token_url + e);

		}
	}

   //获取分享签名信息
	@RequestMapping("wx/share/sigture")
	@ResponseBody 
	public Map<String, Object> getSigture(HttpServletRequest request) {
		String strUrl=request.getParameter("url");
		WinXinEntity wx = WeiXinUnitl.getWinXinEntity(strUrl);
		Map<String, Object> map = new HashMap<String, Object>();
		String sgture = WXUnitl.getSignature(wx.getTicket(), wx.getNoncestr(), wx.getTimestamp(), strUrl);
		map.put("sgture", sgture.trim());//签名
		map.put("timestamp", wx.getTimestamp().trim());//时间戳
		map.put("noncestr",  wx.getNoncestr().trim());//随即串
        map.put("appid","wxe9ac39d6c118387b");//appID
		return map;
	}
}
