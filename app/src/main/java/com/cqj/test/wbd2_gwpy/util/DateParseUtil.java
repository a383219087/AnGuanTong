package com.cqj.test.wbd2_gwpy.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换类
 * 
 * @author xtt
 * 
 */
public class DateParseUtil {

	public static final String FormatStr = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间展示规则：
	 * 
	 * 1、距离24小时之类，显示时间。
	 * 
	 * 2、超过20小时小于3天内，显示天数。
	 * 
	 * 3、超过3天，显示日期。本年度，不显示年份；否则，显示完整年月日。
	 * 
	 * @param date
	 *            服务器返回的时间
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getFormatDate(String date) {
		if (TextUtils.isEmpty(date)) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		try {
			SimpleDateFormat sformat = new SimpleDateFormat(FormatStr);
			Date d = sformat.parse(date);
			calendar.setTime(d);
			int year1 = calendar.get(Calendar.YEAR);
			int month1 = calendar.get(Calendar.MONTH);
			int day1 = calendar.get(Calendar.DAY_OF_MONTH);
			int hours1 = calendar.get(Calendar.HOUR_OF_DAY);
			int minute1 = calendar.get(Calendar.MINUTE);
			calendar = Calendar.getInstance();
			int year2 = calendar.get(Calendar.YEAR);
			int month2 = calendar.get(Calendar.MONTH);
			int day2 = calendar.get(Calendar.DAY_OF_MONTH);
			int hours2 = calendar.get(Calendar.HOUR_OF_DAY);
			int minute2 = calendar.get(Calendar.MINUTE);
			if (year1 < year2) {
				return year1 + "年" + month1 + "月" + day1 + "日";
			} else if (month1 < month2) {
				return month1 + "月" + day1 + "日";
			} else if (day2 - day1 > 3) {
				return month1 + "月" + day1 + "日";
			} else if (day2 - day1 > 0) {
				return day2 - day1 + "天前";
			} else if (hours2 > hours1) {
				return hours2 - hours1 + "小时前";
			} else {
				if (minute2 - minute1 <= 0) {
					return "刚刚";
				} else {
					return minute2 - minute1 + "分钟前";
				}
			}

		} catch (Exception e) {
		}
		return date;
	}

	public static Integer[] compareDate(String dateStr, String serverDate) {
		Integer[] values = new Integer[4];
		try {
			Calendar otherCalendar = Calendar.getInstance();
			SimpleDateFormat sformat = new SimpleDateFormat(FormatStr);
			Date date = sformat.parse(dateStr);
			otherCalendar.setTime(date);
			Calendar nowCalendar = Calendar.getInstance();
			Date serDate = sformat.parse(serverDate);
			nowCalendar.setTime(serDate);
			/**
			 * 0 if the times of the two Calendars are equal, -1 if the time of
			 * this Calendar is before the other one, 1 if the time of this
			 * Calendar is after the other one.
			 */
			int ret = nowCalendar.compareTo(otherCalendar);
			values[0] = ret;
			long millis = nowCalendar.getTimeInMillis()
					- otherCalendar.getTimeInMillis();
			int hour = (int) (millis / (3600 * 1000));
			values[1] = Math.abs(hour);
			int minute = (int) ((millis % (3600 * 1000)) / (60 * 1000));
			values[2] = Math.abs(minute);
			int second = (int) (((millis % (3600 * 1000)) % (60 * 1000)) / 1000);
			values[3] = Math.abs(second);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return values;
	}

}
