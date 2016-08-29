package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/8/29
 * 在线支付类型枚举
 */
public enum OnlinePayTypeEnum {
    UnionPayB2C(1,"银联B2C支付"),
    UnionPayNoCard(4,"银联无卡支付"),
    MerchantBank(5,"招商银行支付");

    /* 支付方式 */
    private Integer payType;

    /* 支付方式名称 */
    private String payName;

    OnlinePayTypeEnum(Integer payType,String payTypeName){
        this.payType = payType;
        this.payName = payTypeName;
    }

    public static String getPayName(Integer payType) {
        for (OnlinePayTypeEnum onlinePayTypeEnum : OnlinePayTypeEnum.values()) {
            if (onlinePayTypeEnum.payType.equals(payType) )
                return onlinePayTypeEnum.payName;
        }
        return null;
    }

    public Integer getPayType() {
        return payType;
    }
}
