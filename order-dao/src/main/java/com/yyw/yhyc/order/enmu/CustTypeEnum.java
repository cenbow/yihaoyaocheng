package com.yyw.yhyc.order.enmu;

/**
 * Created by shiyongxi on 2016/8/12.
 */
public enum CustTypeEnum {
    buyer(1, "买家"),
    seller(2, "卖家"),
    buyerAndSeller(3, "买卖家"),
    other(0, "其他未知");

    private int type;
    private String value;

    CustTypeEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(int type) {
        for (CustTypeEnum item : CustTypeEnum.values()) {
            if (item.type == type)
                return item.value;
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
