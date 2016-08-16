package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/8/16.
 */
public enum SellerReplenishmentOrderStatusEnum {
    WaitingConfirmation("1","待确认"),
    WaitingDelivered("2","待发货"),
    Closed("3","已关闭"),
    WaitingReceived("4","待收货"),
    Finished("5","已完成");

    private String type;
    private String value;

    SellerReplenishmentOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SellerReplenishmentOrderStatusEnum item : SellerReplenishmentOrderStatusEnum.values()) {
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
