package com.statis.statis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
   private final static Logger logger= LoggerFactory.getLogger(FileUtils.class);
	
	public static void readFile(String path){
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
	public static void readFileFromDerectory(String path) {
		
		File file= new File(path);
		if(file.isDirectory()) {
			//直接读取日志文件
			
		}else if(file.isDirectory()) {
			String[]  fileList = file.list();
			for(int i=0;i<fileList.length;i++) {
				String absolutePath = path+"\\"+fileList[i];
				//循环处理
				
			}
		}
	}
	
}
