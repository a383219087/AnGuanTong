package com.cqj.test.wbd2_gwpy.util;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class StringUtil {

	public static final String SPACE = "\u3000";
	public static final String ENTER = "\n";

	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0) || str.equals("null");
	}

	/**
	 * 设置listview高度
	 * 
	 * @param ada
	 * @param lv
	 */
	public static void setHeight(BaseAdapter ada, ListView lv) {
		int listViewHeight = 0;
		int adaptCount = ada.getCount();
		for (int i = 0; i < adaptCount; i++) {
			View temp = ada.getView(i, null, lv);
			temp.measure(0, 0);
			listViewHeight += temp.getMeasuredHeight();
		}
		LayoutParams layoutParams = lv.getLayoutParams();
		layoutParams.width = LayoutParams.FILL_PARENT;
		layoutParams.height = listViewHeight;
		lv.setLayoutParams(layoutParams);
	}

	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String noNull(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static String integerNoNull(Object obj) {
		if (obj == null) {
			return "0";
		} else {
			return obj.toString();
		}
	}

	public static String parseNumberByLevel(int level, int code) {
		String value = "";
		switch (level) {
		case 1:
			value = parseNumber1(code);
			break;
		case 2:
			value = parseNumber2(code);
			break;
		case 3:
			value = parseNumber3(code);
			break;
		case 4:
			value = parseNumber4(code);
			break;
		default:
			break;
		}
		return value;
	}

	public static String parseNumber1(int code) {
		String ret = "";
		if (code > 10) {
			int x1 = code / 10;
			int x2 = code % 10;
			if(x1==1){
				return "十"+ parseNumber1(x2);
			}
			return parseNumber1(x1) +"十"+ parseNumber1(x2);
		}
		switch (code) {
		case 1:
			ret = "一";
			break;
		case 2:
			ret = "二";
			break;
		case 3:
			ret = "三";
			break;
		case 4:
			ret = "四";
			break;
		case 5:
			ret = "五";
			break;
		case 6:
			ret = "六";
			break;
		case 7:
			ret = "七";
			break;
		case 8:
			ret = "八";
			break;
		case 9:
			ret = "九";
			break;
		case 10:
			ret = "十";
			break;

		default:
			ret = code + "";
			break;
		}
		return ret;
	}

	public static String parseNumber2(int code) {
		return "<" + parseNumber1(code) + ">";
	}

	public static String parseNumber3(int code) {
		return code + "";
	}

	public static String parseNumber4(int code) {
		return "(" + code + ")";
	}

	public static String parseError(int code) {
		String ret = "";
		switch (code) {
		case 1:
			ret = "用户名不存在";
			break;

		case 2:
			ret = "手机号码不存在";
			break;
		case 3:
			ret = "电子邮件不存在";
			break;
		case 4:
			ret = "密码不正确";
			break;
		case 5:
			ret = "用户名格式不正确";
			break;

		default:
			break;
		}
		return ret;
	}
}
