package com.example.ssm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

public class DateFormatUtils {
	/**
	 * EEE, dd MMM yyyy HH:mm:ss Z
	 */
	public static final String pattern1 = "EEE, dd MMM yyyy HH:mm:ss Z";
	/**
	 * yyyy-MM-dd'T'HH:mm:ssZZ
	 */
	public static final String pattern11 = "yyyy-MM-dd'T'HH:mm:ssZZ";
	/**
	 * yyyy-MM-dd'T'HH:mm:ss
	 */
	public static final String pattern12 = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String pattern13 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-M-d H:m:s
	 */
	public static final String pattern18 = "yyyy-M-d H:m:s";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String pattern17 = "yyyy-MM-dd HH:mm";
	/**
	 * yyyy-MM-ddZZ
	 */
	public static final String pattern14 = "yyyy-MM-ddZZ";
	/**
	 * yyyy-MM-dd
	 */
	public static final String pattern15 = "yyyy-MM-dd";
	/**
	 * yyyy-MM
	 */
	public static final String pattern16 = "yyyy-MM";
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String pattern21 = "yyyy/MM/dd HH:mm:ss";
	/**
	 * yyyy/MM/dd HH:mm
	 */
	public static final String pattern22 = "yyyy/MM/dd HH:mm";
	/**
	 * yyyy/MM/dd
	 */
	public static final String pattern23 = "yyyy/MM/dd";
	/**
	 * yyyy/MM
	 */
	public static final String pattern24 = "yyyy/MM";
	/**
	 * yyyy'年'MM'月'dd'日' HH:mm
	 */
	public static final String pattern31 = "yyyy'年'MM'月'dd'日' HH:mm";

	private static SimpleDateFormat df_c;

	public static final String[] patterns = { pattern12, pattern13, pattern14, pattern15, pattern16, pattern17,
			pattern21, pattern22, pattern23, pattern24 };

	public static final String[] patternsWithZone = { pattern1, pattern11 };

	public static Date parse(String str) throws ParseException {
		return DateUtils.parseDate(str, patterns);
	}

	public static Date parse(String str, String pattern) throws ParseException {
		return DateUtils.parseDate(str, new String[] { pattern });
	}

	public static Date parseWithZone(String str) throws ParseException {
		return DateUtils.parseDate(str, patternsWithZone);
	}

	public static Date parseFormChina(String str) throws ParseException {
		Date date = null;
		try {
			date = parseWithZone(str);
		} catch (Exception e) {
			Date tempDate = parse(str);
			String tempStr = format(tempDate, pattern13);
			date = getChinaDateFormat().parse(tempStr);
		}
		return date;
	}

	public static Date parseFormChina(String str, String pattern) throws ParseException {
		Date date = null;
		try {
			date = parseWithZone(str);
		} catch (Exception e) {
			Date tempDate = parse(str, pattern);
			String tempStr = format(tempDate, pattern13);
			date = getChinaDateFormat().parse(tempStr);
		}
		return date;
	}

	public static String formatDateTime(Date date) {
		return format(date, pattern13);
	}

	public static String format(Date date, String pattern) {
		return org.apache.commons.lang3.time.DateFormatUtils.format(date, pattern);
	}

	public static String format(Date date, String pattern, TimeZone timeZone) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setTimeZone(timeZone);
		return df.format(date);
	}

	public static String formatInChina(Date date, String pattern) {
		return format(date, pattern, TimeZone.getTimeZone("GMT+8"));
	}

	public static String formatNowInChina(String pattern) {
		return format(new Date(), pattern, TimeZone.getTimeZone("GMT+8"));
	}

	private static SimpleDateFormat getChinaDateFormat() {
		if (df_c == null) {
			df_c = new SimpleDateFormat(pattern13);
			df_c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		}
		return df_c;
	}

}
