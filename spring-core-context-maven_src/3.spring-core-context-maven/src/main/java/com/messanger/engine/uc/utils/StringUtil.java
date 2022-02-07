package com.messanger.engine.uc.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class StringUtil {

	public static String getOnlyNumberString(String str) {
		if (str == null)
			return str;

		StringBuffer sb = new StringBuffer();
		int length = str.length();
		for (int i = 0; i < length; i++) {
			char curChar = str.charAt(i);
			if (Character.isDigit(curChar))
				sb.append(curChar);
		}
		return sb.toString();
	}
	
	public static String getReplaceFirst(String src, String prefixs) {
		Set<String> setPrefix = new HashSet<String>();
        String[] strArr = null;
        if(prefixs != null) {
        	strArr = prefixs.split("[|]");
        	for(int i=0;i<strArr.length;i++) {
        		setPrefix.add(strArr[i].trim().toLowerCase());
        	}
        }
        
        src = getOnlyNumberString(src);
		Iterator prefixIter = setPrefix.iterator();
		while(prefixIter.hasNext()) {
			String prefix = (String)prefixIter.next();
			if (src.indexOf(prefix) == 0) {
				src = src.replaceFirst(prefix, "");
				break;
			}
		}
		
		return src;
	}
	
	public static void main(String[] args) {
		String s = "12121212~```~~`1234  1238^^^^^^^';j[;f[j;f'y;f'j;'j45384uj039-----------48538594835#$%^&2121212lsakfaefokasdokfsadf\\\\sadfasdfaf*00";
		String result = StringUtil.getOnlyNumberString(s);
		System.out.println(result);
	}

}
