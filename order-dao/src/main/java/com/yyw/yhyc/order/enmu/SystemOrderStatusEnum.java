package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/7/29.
 * 订单系统状态
 */
public enum SystemOrderStatusEnum {

    BuyerOrdered("1", "买家已下单"),
    BuyerCanceled ("2", "买家已取消"),
    SellerCanceled ("3", "卖家已取消"),
    SystemAutoCanceled("4", "系统自动取消"),
    BuyerAlreadyPaid("5", "买家已付款"),
    SellerDelivered ("6", "卖家已发货"),
    BackgroundCancellation("7", "后台取消"),
    BuyerAllReceived("8", "买家全部收货"),
    Rejecting("9", "拒收中"),
    Replenishing("10", "补货中"),
    SystemAutoConfirmReceipt("11", "系统自动确认收货"),
    BuyerDeferredReceipt("12", "买家延期收货"),
    //PaidException("13", "退款异常"),
    BuyerPartReceived("14", "买家部分收货"),
	RejectAndReplenish("15", "拒收&补货中");
	

    private String type;
    private String value;

    SystemOrderStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemOrderStatusEnum item : SystemOrderStatusEnum.values()) {
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
