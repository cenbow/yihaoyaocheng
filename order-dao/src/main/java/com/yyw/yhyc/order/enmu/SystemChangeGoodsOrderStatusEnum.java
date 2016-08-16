package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/8/16.
 */
public enum SystemChangeGoodsOrderStatusEnum {
    WaitingConfirmation("1","买家已申请"),
    Canceled("2","买家已取消"),
    Closed("3","卖家已关闭"),
    WaitingBuyerDelivered("4","卖家已确认"),
    WaitingSellerReceived("5","买家已发货"),
    WaitingSellerDelivered("6","卖家已收货"),
    WaitingBuyerReceived("7","卖家已发货"),
    Finished("8","买家已收货"),
    AutoFinished("9","系统自动确认买家收货");


    private String type;
    private String value;

    SystemChangeGoodsOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemChangeGoodsOrderStatusEnum item : SystemChangeGoodsOrderStatusEnum.values()) {
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
