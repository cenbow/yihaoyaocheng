package com.yyw.yhyc.order.enmu;

/**
 * Created by jiangshuai on 2016/9/12.
 */
public enum SystemOrderPayFlag {
    PlayMoneySuccess(1,"打款成功"),
    PlayMoneyError(2,"打款失败"),
    RefundSuccess(3,"退款成功"),
    RefundError(4,"退款失败");


    private Integer type;
    private String value;

    SystemOrderPayFlag(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(Integer type) {
        for (SystemOrderPayFlag item : SystemOrderPayFlag.values()) {
            if (item.type.equals(type) )
                return item.value;
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
