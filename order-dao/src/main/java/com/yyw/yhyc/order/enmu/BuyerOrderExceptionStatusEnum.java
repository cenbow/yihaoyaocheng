package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/8/8.
 */
public enum BuyerOrderExceptionStatusEnum {
    WaitingConfirmation("1","待确认"),
    Refunding("2","退款中"),
    Closed("3","已关闭"),
    Refunded("4","已完成");

    private String type;
    private String value;

    BuyerOrderExceptionStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (BuyerOrderExceptionStatusEnum item : BuyerOrderExceptionStatusEnum.values()) {
            if (item.type.equals(type) )
                return item.value;
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
