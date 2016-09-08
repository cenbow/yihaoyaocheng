package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/8/1
 * 订单支付状态枚举类
 */
public enum OrderPayStatusEnum {

    PAYED("1","已支付"),
    UN_PAYED("0","未支付");


    /* 支付状态 */
    private String payStatus;

    /* 支付状态(名称) */
    private String payStatusName;

    OrderPayStatusEnum(String payStatus, String payStatusName) {
        this.payStatus = payStatus;
        this.payStatusName = payStatusName;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public String getPayStatusName() {
        return payStatusName;
    }

    public static String getPayStatusName(String payStatus) {
        for (OrderPayStatusEnum orderPayStatusEnum : OrderPayStatusEnum.values()) {
            if (orderPayStatusEnum.payStatus.equals(payStatus) )
                return orderPayStatusEnum.payStatusName;
        }
        return null;
    }
}
