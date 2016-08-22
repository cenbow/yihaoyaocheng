package com.yyw.yhyc.order.enmu;

/**
 * 补货订单系统状态
 * Created by zhangqiang on 2016/8/16.
 */
public enum SystemReplenishmentOrderStatusEnum {
    SellerConfirmed("2","卖家已确认"),
    BuyerRejectApplying("1","补货申请中"),
    SellerClosed("3","卖家已关闭"),
    BuyerReceived("5","买家已收货"),
    SellerDelivered("4","卖家已发货"),
    SystemAutoConfirmReceipt("6","系统自动确认收货");

    private String type;
    private String value;

    SystemReplenishmentOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemReplenishmentOrderStatusEnum item : SystemReplenishmentOrderStatusEnum.values()) {
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
