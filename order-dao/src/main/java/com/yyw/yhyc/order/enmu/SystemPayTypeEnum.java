package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/7/29
 * 支付方式枚举类
 */
public enum  SystemPayTypeEnum {

    PayOnline(1,"在线支付"),
    PayPeriodTerm(2,"账期支付"),
    PayOffline(3,"线下支付"), ;

    /* 支付方式 */
    private Integer payType;

    /* 支付方式名称 */
    private String payTypeName;

    SystemPayTypeEnum(Integer payType,String payTypeName){
        this.payType = payType;
        this.payTypeName = payTypeName;
    }

    public static String getPayTypeName(Integer payType) {
        for (SystemPayTypeEnum systemPayTypeEnum : SystemPayTypeEnum.values()) {
            if (systemPayTypeEnum.payType.equals(payType) )
                return systemPayTypeEnum.payTypeName;
        }
        return null;
    }

    public Integer getPayType() {
        return payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }
}
