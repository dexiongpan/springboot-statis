package com.statis.statis.cp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.statis.statis.activemq.ActiveMQQueueConst;
import com.statis.statis.activemq.DestinationFactory;

@Component
public class ExtractLogService {
    
	private static Logger logger = LoggerFactory.getLogger(ExtractLogService.class);
	
	 @Autowired
	 private JmsMessagingTemplate jmsMessagingTemplate;
	 
	@Scheduled(cron = "0 0 * * * ?")
	public void extractLog() {
		String path = "D:\\log";
		readFileFromDerectory(path);
	}
	
	private void readFile(String path){
		 long start = System.currentTimeMillis();
		 int bufSize = 1024*1024*5;
		 try {
			 File file = new File(path);
			FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufSize);
			String enterString="\n";
			long len=0l;
			byte[] bs= new byte[bufSize];
			String tempString =null;
			while (fileChannel.read(byteBuffer)!=-1) {
				int rsize = byteBuffer.position();
				byteBuffer.rewind();
				byteBuffer.get(bs);
				byteBuffer.clear();
				tempString = new String(bs,0,bufSize);
				int startIndex =0;
				int endIndex = 0;
				while ((endIndex=tempString.indexOf(enterString,startIndex))!=-1) {
					String line = tempString.substring(startIndex,endIndex);
					//读取文件信息
					System.out.println(line);
					Destination destination = DestinationFactory.getDestination(ActiveMQQueueConst.QUEUE_NAME_EXTRACT_LOG);
			        jmsMessagingTemplate.convertAndSend(destination,line);
					startIndex =endIndex+1;
				}
				
			}
			long end = System.currentTimeMillis();
			logger.info("本次日志读取耗时:"+(end-start)+"ms");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("文件没有发现!"+e.toString());;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("IO异常!"+e.toString());
		}
		 
	 }
	
	//获取文件夹文件路径
	private void readFileFromDerectory(String path) {
		File file= new File(path);
		if(!file.isDirectory()) {
			//直接读取日志文件
			readFile(path);
		}else if(file.isDirectory()) {
			String[]  fileList = file.list();
			for(int i=0;i<fileList.length;i++) {
				String absolutePath = path+"\\"+fileList[i];
				//循环处理
				readFile(absolutePath);
			}
		}
	}
}
