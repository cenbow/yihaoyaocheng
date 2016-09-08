package com.yyw.yhyc.order.enmu;

/**
 * Created by jiangshuai on 2016/8/31.
 */
public enum SystemRefundPayStatusEnum {

    refundStatusIng("1","退款中"),
    refundStatusOk("2","退款成功"),
    refundStatusFail("3","退款失败");

    private String type;
    private String value;

    SystemRefundPayStatusEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(String type) {
        for (SystemRefundPayStatusEnum item : SystemRefundPayStatusEnum.values()) {
            if (item.type.equals(type) )
                return item.value;
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
