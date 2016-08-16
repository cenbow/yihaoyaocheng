package com.yyw.yhyc.order.enmu;

/**
 * 退货订单系统状态
 * Created by zhangqiang on 2016/8/16.
 */
public enum SystemRefundOrderStatusEnum {
    BuyerApplying("1","买家已申请"),
    BuyerCanceled("2","买家已取消"),
    SellerConfirmed("3","卖家已确认"),
    SellerClosed("4","卖家已关闭"),
    BuyerDelivered("5","买家已发货"),
    SellerReceived("6","卖家已收货"),
    SystemAutoConfirmReceipt("7","系统自动确认收货"),
    Refunded("8","已退款");


    private String type;
    private String value;

    SystemRefundOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemRefundOrderStatusEnum item : SystemRefundOrderStatusEnum.values()) {
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
