package com.metr.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public  class DateFormator {
	final static String FullDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	final static String ShortDateFormat = "dd.MM";
	final static String MonthStringFormat = "MM";
	final static String DayStringFormat = "dd";
	final static String HoursStringFormat = "HH";
	final static String MinutesStringFormat = "mm";

	public static String currentDateGrinvichTime() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(FullDateTimeFormat);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(new Date());
	}

	public static String currentDateDAO() throws ParseException {
		return formatDate(new Date(), FullDateTimeFormat);
	}

	public Date substractDaysFromToday(int days){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		return cal.getTime();
	}

	public String getDate(String date){
		Integer datei = Integer.parseInt(date);
		java.util.Date time=new java.util.Date((long)datei*1000);
		String finalDate = formatDate(time, ShortDateFormat);
		System.out.println(finalDate);
		return finalDate;
	}
	public static String formatDate(Date date, String stringFormat){
		SimpleDateFormat dateFormat = new SimpleDateFormat(stringFormat);
		return dateFormat.format(date);
	}
}