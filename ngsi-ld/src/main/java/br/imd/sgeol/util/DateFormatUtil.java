package br.imd.sgeol.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatUtil {
	
	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");

	static {
		df.setTimeZone(tz);
	}
	
	
	
	public static String format(Date date){
		return df.format(date);
	}
	
	public static Date parse(String date) throws ParseException{
		return df.parse(date);
	}

}
