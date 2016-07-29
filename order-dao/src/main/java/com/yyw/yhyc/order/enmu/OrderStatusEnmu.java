package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/7/29.
 * 订单状态（买卖家视角）
 */
public enum  OrderStatusEnmu {
    PendingPayment("1", "待付款"),
    BackOrder ("2", "待发货"),
    Canceled("4", "已取消"),
    ReceiptOfGoods ("5", "待收货"),
    Finished("6", "已完成"),
    Rejecting("8", "拒收中"),
    Replenishing("9", "补货中");

    private String type;
    private String value;

    OrderStatusEnmu(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (OrderStatusEnmu item : OrderStatusEnmu.values()) {
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
