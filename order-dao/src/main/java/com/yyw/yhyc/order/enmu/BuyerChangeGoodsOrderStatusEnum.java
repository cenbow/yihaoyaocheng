package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/8/16.
 */
public enum BuyerChangeGoodsOrderStatusEnum {
    WaitingConfirmation("1","待确认"),
    Canceled("2","已取消"),
    Closed("3","已关闭"),
    WaitingBuyerDelivered("4","待买家发货"),
    WaitingSellerReceived("5","待卖家收货"),
    WaitingSellerDelivered("6","待卖家发货"),
    WaitingBuyerReceived("7","待买家收货"),
    Finished("8","已完成");


    private String type;
    private String value;

    BuyerChangeGoodsOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (BuyerChangeGoodsOrderStatusEnum item : BuyerChangeGoodsOrderStatusEnum.values()) {
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
