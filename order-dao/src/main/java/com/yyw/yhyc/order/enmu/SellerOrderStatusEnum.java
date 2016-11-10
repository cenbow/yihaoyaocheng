package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/7/29.
 */
public enum SellerOrderStatusEnum {
    PendingPayment("1", "待付款"),
    BackOrder ("2", "待发货"),
    ReceiptOfGoods ("3", "待收货"),
    Rejecting("4", "拒收中"),
    Replenishing("5", "补货中"),
    Canceled("6", "已取消"),
    Finished("7", "已完成"),
    PaidException("8", "退款异常"),
    RejectAndReplenish("9", "拒收&补货中");
	

    private String type;
    private String value;

    SellerOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SellerOrderStatusEnum item : SellerOrderStatusEnum.values()) {
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
