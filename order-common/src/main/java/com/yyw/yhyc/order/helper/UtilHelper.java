package com.yyw.yhyc.order.helper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class UtilHelper {
    public static String toTrimLowerCase(String value) {
        return value == null?null:value.trim().toLowerCase();
    }

    public static String toTrimUpperCase(String value) {
        return value == null?null:value.trim().toUpperCase();
    }

    public static String trim(String value) {
        return value == null?null:value.trim();
    }

    public static String eliminateSpecialCharacter(String str) {
        return str == null?null:str.replaceAll("[^A-Za-z0-9]", "");
    }

    public static boolean isEmpty(Object object) {
        return object == null;
    }

    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static boolean isEmpty(Collection<?> o) {
        return o == null || o.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> o) {
        return o == null || o.isEmpty();
    }

    public static List<String> getList(String[] strArray) {
        return Arrays.asList(strArray);
    }

    public static String toChineseDigits(Double value) {
        if(value.doubleValue() == 0.0D) {
            return null;
        } else {
            StringBuffer returnValue = new StringBuffer();
            String[] chineseDigits = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String[] chineseUnits = new String[]{"圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
            BigDecimal bd = new BigDecimal(value.doubleValue());
            String tmp = bd.toString();
            String[] valueStrs = tmp.split("\\.");
            String integerValue = isEmpty((Object[])valueStrs)?tmp:valueStrs[0];
            String decimalStr = valueStrs.length > 1?valueStrs[1]:null;
            System.out.println("decimalStr = " + decimalStr);
            if(integerValue != null) {
                int[] integers = new int[integerValue.length()];

                int length;
                for(length = 0; length < integerValue.length(); ++length) {
                    integers[length] = Integer.parseInt(integerValue.substring(length, length + 1));
                }

                length = integers.length;

                for(int i = 0; i < length; ++i) {
                    returnValue.append(chineseDigits[integers[i]]).append(chineseUnits[length - 1 - i]);
                }
            }

            return returnValue.toString();
        }
    }
}
