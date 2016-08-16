package com.yyw.yhyc.order.enmu;

/**
 * 补货订单系统状态
 * Created by zhangqiang on 2016/8/16.
 */
public enum SystemCycleOrderStatusEnum {
    BuyerRejectApplying("1","买家已申请"),
    SellerConfirmed("2","卖家已确认"),
    SellerClosed("3","卖家已关闭"),
    SellerDelivered("4","卖家已发货"),
    BuyerReceived("5","买家已收货"),
    SystemAutoConfirmReceipt("6","系统自动确认收货");

    private String type;
    private String value;

    SystemCycleOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemCycleOrderStatusEnum item : SystemCycleOrderStatusEnum.values()) {
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
