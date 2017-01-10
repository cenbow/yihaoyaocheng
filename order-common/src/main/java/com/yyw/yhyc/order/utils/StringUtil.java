package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.helper.UtilHelper;

public class StringUtil {
	public static int strToInt(String str ) {
		return UtilHelper.isEmpty(str) ? 0 : Integer.valueOf(str);
	}
}
