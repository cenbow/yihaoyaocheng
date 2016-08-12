package com.yyw.yhyc.order.enmu;

/**
 * Created by zhangqiang on 2016/8/8.
 */
public enum SystemOrderExceptionStatusEnum {
    BuyerConfirmed("2","卖家已确认"),
    RejectApplying("1","拒收申请中"),
    SellerClosed("3","卖家已关闭"),
    Refunded("4","已退款");

    private String type;
    private String value;

    SystemOrderExceptionStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemOrderExceptionStatusEnum item : SystemOrderExceptionStatusEnum.values()) {
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

