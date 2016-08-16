package com.yyw.yhyc.order.enmu;

/**
 * 卖家视角退货订单状态
 * Created by zhangqiang on 2016/8/16.
 */
public enum SellerRefundOrderStatusEnum {
    BuyerApplying("1","待确认"),
    Canceled("2","已取消"),
    WaitingBuyerDelivered("3","待买家发货"),
    Closed("4","已关闭"),
    WaitingSellerReceived("5","待卖家收货"),
    refunding("6","退款中"),
    Finished("7","已完成"),;


    private String type;
    private String value;

    SellerRefundOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SellerRefundOrderStatusEnum item : SellerRefundOrderStatusEnum.values()) {
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
