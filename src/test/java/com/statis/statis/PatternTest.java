package com.statis.statis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatternTest {
	 @Test
	public void test() {
		 String str = "http://183.224.6.2:7095/epg/api/vod/35100001000000011560418896619209.json";
		 Pattern p = Pattern.compile(".*/series/.*");
		 Matcher m = p.matcher(str);
		 boolean isValid = m.matches();
		 System.out.println(isValid);
	}
}
