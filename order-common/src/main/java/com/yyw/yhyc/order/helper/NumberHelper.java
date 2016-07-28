package com.yyw.yhyc.order.helper;

import java.math.BigDecimal;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class NumberHelper {
    public static final BigDecimal BLANK_DECIMAL = new BigDecimal(-99999);

    public NumberHelper() {
    }

    public static BigDecimal round(BigDecimal aValue, int aScale) {
        if(aValue == null) {
            return null;
        } else {
            aValue = aValue.setScale(aScale, 4);
            return aValue;
        }
    }

    public static BigDecimal getBigDecimal(String aInput) {
        return getBigDecimal(aInput, BLANK_DECIMAL);
    }

    public static BigDecimal getBigDecimal(String aInput, BigDecimal aDefaultValue) {
        try {
            return new BigDecimal(aInput);
        } catch (Exception e) {
            return aDefaultValue;
        }
    }

    public static BigDecimal add(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.add(aValue2):null;
    }

    public static BigDecimal add2Abs(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.add(aValue2).abs():null;
    }

    public static BigDecimal subtract(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.subtract(aValue2):null;
    }

    public static BigDecimal subtract2Abs(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.subtract(aValue2).abs():null;
    }

    public static BigDecimal multiply(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.multiply(aValue2):null;
    }

    public static BigDecimal multiply(BigDecimal aValue1, BigDecimal aValue2, int sacle, int roundingMode) {
        return aValue1 != null && aValue2 != null?aValue1.multiply(aValue2).setScale(sacle, roundingMode):null;
    }

    public static BigDecimal divide(BigDecimal aValue1, BigDecimal aValue2, int scale) {
        return aValue1 != null && aValue2 != null?aValue1.divide(aValue2, scale, 4):null;
    }

    public static BigDecimal remainder(BigDecimal aValue1, BigDecimal aValue2) {
        return aValue1 != null && aValue2 != null?aValue1.remainder(aValue2):null;
    }
}