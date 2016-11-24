package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/8/29
 * 在线支付类型枚举
 */
public enum OnlinePayTypeEnum {
    UnionPayB2C(1,"银联B2C支付"),
    UnionPayNoCard(4,"银联无卡支付"),
    MerchantBank(5,"招商银行支付"),
    UnionPayB2B(6,"银联B2B支付"),
    AlipayWeb(7,"支付宝WEB"),
    AlipayApp(8,"支付宝APP"),
    UnionPayMobile(9,"银联手机支付");

    /* 支付方式Id */
    private Integer payTypeId;

    /* 支付方式名称 */
    private String payName;

    OnlinePayTypeEnum(Integer payTypeId,String payTypeName){
        this.payTypeId = payTypeId;
        this.payName = payTypeName;
    }

    public static String getPayName(Integer payTypeId) {
        for (OnlinePayTypeEnum onlinePayTypeEnum : OnlinePayTypeEnum.values()) {
            if (onlinePayTypeEnum.payTypeId.equals(payTypeId) )
                return onlinePayTypeEnum.payName;
        }
        return null;
    }

    public Integer getPayTypeId() {
        return payTypeId;
    }
}
