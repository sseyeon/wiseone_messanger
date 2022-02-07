package com.messanger.engine.uc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

public class DebugUtils {

	public static void printMap(Map<String, String> map) {		
		Iterator<String> iter = map.keySet().iterator();
		System.out.println("=====DEBUG MAP START======");
		while(iter.hasNext()) {
			String key = iter.next();
			String val = map.get(key);
			System.out.println(key+"::"+val);
		}
		System.out.println("=====DEBUG MAP END  ======");
	}
	
	public static void stringToFile(String content, String filepath) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		StringReader sr = null;
		BufferedReader br = null;
		try {
			fw = new FileWriter(filepath);
			bw = new BufferedWriter(fw);
			
			sr = new StringReader(content);
			br = new BufferedReader(sr);
			
			char[] buffer = new char[1024];
			int readCnt = -1; 
			while((readCnt = br.read(buffer)) != -1) {
				bw.write(buffer, 0, readCnt);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
