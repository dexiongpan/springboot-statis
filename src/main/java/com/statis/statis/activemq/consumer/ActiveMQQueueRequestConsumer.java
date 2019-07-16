package com.statis.statis.activemq.consumer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.producer.ActiveMQQueueProducer;
import com.statis.statis.config.TypeEnum;
import com.statis.statis.service.CronLogicService;
import com.statis.statis.util.EmptyUtil;
import com.statis.statis.util.HttpClientUtil;
import com.statis.statis.vo.MessageData;
import com.statis.statis.vo.ResultData;

@Component
public class ActiveMQQueueRequestConsumer {
	private final static Logger logger = LoggerFactory.getLogger(ActiveMQQueueRequestConsumer.class);
	@Autowired
    private ActiveMQQueueProducer activeMQQueueProducer;
	
	@Autowired
	private HttpClientUtil HttpClientUtil;

    @JmsListener(destination = ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, containerFactory = ActiveMQQueueConst.BEAN_NAME_JMSLISTENERCONTAINERFACTORY)
    public void receiveQueueRequestMsg(MessageData message) throws ParseException, ClientProtocolException, IOException {
    	logger.info("消费了");
        String itemType = message.getItemType();
        String dataLink = message.getDataLink();
        if(activeMQQueueProducer.getPathMap().containsKey(dataLink)) {
        	return;
        }
        activeMQQueueProducer.getPathMap().put(dataLink, dataLink);
        
        ExecutorService service = Executors.newFixedThreadPool(10);
    	service.execute(new Runnable() {
			@Override
			public void run() {
				 try {
					handlerRequest(message,itemType,dataLink);
					 Thread.sleep(20000);
				} catch (ParseException | IOException e) {
					// TODO Auto-generated catch block
					logger.debug(e.toString());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.debug(e.toString());
				}
				}
			});
       
        
        
    }
    
    private void handlerRequest(MessageData message,String itemType,String dataLink) throws ParseException, ClientProtocolException, IOException {
    	Map<String, Object>responseMap=HttpClientUtil.getObject(dataLink);
		String status=String.valueOf(responseMap.get("status"));
		String vImg ="";
        switch (itemType) {
		case "biz":
			//判断响应状态
			if(!status.equals("200")) {
				//放到db队列处理错误信息
				sendDbMsg(message,dataLink,status);
				break;
			}
			JSONObject jsonObject =(JSONObject)responseMap.get("jsonObject");
			//获取backgroundUrl
			String backGroundUrl=jsonObject.getString("background");
			String title=jsonObject.getString("title");
			String code=jsonObject.getString("code");
			
			message.setItemTitle(title);
			message.setItemCode(code);
			if(EmptyUtil.isNotEmpty(backGroundUrl)) {
			//放到验证队列处理
			message.setDataLink(backGroundUrl);
			sendVerifyMsg(message);
			}
			//获取到areaDatas-->items
			JSONArray areaDatas=jsonObject.getJSONArray("areaDatas");
			if(areaDatas.size()>0) {
				for(int i =0 ;i<areaDatas.size();i++) {
					Object object=areaDatas.get(i);
			    	JSONObject areaData=(JSONObject) JSONObject.parse(object.toString());
			       	JSONArray jsonArray=areaData.getJSONArray("items");
			       	ResultData<JSONArray> resultData = new ResultData<JSONArray>();
			       	if (EmptyUtil.isNotEmpty(jsonArray)&&jsonArray.size()>0) {
			       		resultData.setData(jsonArray);
				       	resultData.setType(TypeEnum.BIZ.getValue());
				       	activeMQQueueProducer.sendMsg(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, resultData);	
					}
				}
			}

			break;
		case "series":
			//判断响应状态
			if(!status.equals("200")) {
				//放到db队列处理错误信息
				sendDbMsg(message,dataLink,status);
				break;
			}
			JSONObject seriesObject =(JSONObject)responseMap.get("jsonObject");
			vImg=seriesObject.getJSONObject("series").getString("vImg");
			//该vImg 放到验证队列验证
			if(EmptyUtil.isNotEmpty(vImg)) {
			message.setDataLink(vImg);
			sendVerifyMsg(message);
			}
			JSONArray seriesArray = seriesObject.getJSONArray("episodes");
			ResultData<JSONArray> seriesData = new ResultData<JSONArray>();
			if(EmptyUtil.isNotEmpty(seriesArray)&&seriesArray.size()>0){
				seriesData.setData(seriesArray);
				seriesData.setType(TypeEnum.SERIES.getValue());
				//放到请求解析队列处理
		       	activeMQQueueProducer.sendMsg(ActiveMQQueueConst.QUEUE_NAME_REQUEST_TEST, seriesData);	
			}
			break;
		case "vod":
			//判断响应状态
			if(!status.equals("200")) {
				//放到db队列处理错误信息
				sendDbMsg(message,dataLink,status);
				break;
			}
			JSONObject vodObject =(JSONObject)responseMap.get("jsonObject");
			 vImg=vodObject.getJSONObject("vod").getString("vImg");
			 //放到验证队验证
			 if(EmptyUtil.isNotEmpty(vImg)) {
				 message.setDataLink(vImg);
				 sendVerifyMsg(message); 
			 }
			break;
		case "channel":
			if(!status.equals("200")) {
				//放到db队列处理错误信息
				sendDbMsg(message,dataLink,status);
				break;
			}
			JSONObject channelObject =(JSONObject)responseMap.get("jsonObject");
			vImg=channelObject.getJSONObject("channel").getString("icon");
			 if(EmptyUtil.isNotEmpty(vImg)) {
				 message.setDataLink(vImg);
				 sendVerifyMsg(message); 
			 }
			break;
		default:
			break;
		}
    }
    
    
    private void sendDbMsg(MessageData message,String dataLink,String status) {
    	message.setStatus(status);
		message.setDataLink(dataLink);
		activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_DBOPERATE, message);
    }
    
 private void sendVerifyMsg(MessageData message) {
	 activeMQQueueProducer.sendMsgData(ActiveMQQueueConst.QUEUE_NAME_VERIFY, message);	
    }
}
