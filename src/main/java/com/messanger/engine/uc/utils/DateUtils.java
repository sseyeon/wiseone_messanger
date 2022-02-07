package com.messanger.engine.uc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**]
	 * 
	 * @param timeMillis
	 * @param localeString
	 * @return
	 * @throws Exception
	 */
	public static String getSystemCurrentTimeMillis(long timeMillis, String pattern) throws Exception {
//		String language = localeString.substring(0, localeString.indexOf('_'));
//		String country = localeString.substring(localeString.indexOf('_') + 1);
//		Locale locale = new Locale(language, country);
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date(timeMillis));
	}
	
}
