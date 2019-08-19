package com.statis.statis;

import java.io.File;

public class FileTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
      
		
		String path = "D:\\log";
		File file = new File(path);
		String[] fileList = file.list();
		System.out.println(path+"\\"+fileList[0]);
	}

}
