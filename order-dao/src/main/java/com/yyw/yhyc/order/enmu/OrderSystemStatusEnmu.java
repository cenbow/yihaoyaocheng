package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/7/29.
 * 订单系统状态
 */
public enum OrderSystemStatusEnmu {

    BuyerOrdered("1", "买家已下单"),
    BuyerCanceled ("2", "买家已取消"),
    SystemAutoCanceled("3", "系统自动取消"),
    BuyerAlreadyPaid("4", "买家已付款"),
    SellerDelivered ("5", "卖家已发货"),
    BackgroundCancellation("6", "后台取消"),
    BuyerAllReceived("7", "买家全部收货"),
    Rejecting("8", "拒收中"),
    Replenishing("9", "补货中"),
    SystemAutoConfirmReceipt("10", "系统自动确认收货"),
    BuyerDeferredReceipt("11", "买家延期收货"),
    PaidException("12", "打款异常");

    private String type;
    private String value;

    OrderSystemStatusEnmu(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (OrderSystemStatusEnmu item : OrderSystemStatusEnmu.values()) {
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
