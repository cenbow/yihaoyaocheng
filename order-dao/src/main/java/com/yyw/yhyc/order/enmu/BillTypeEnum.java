package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/7/29
 * 订单发票类型 枚举类
 */
public enum BillTypeEnum {

    BillTypeSpecial(1,"增值税专用发票"),
    BillTypeNormal(2,"增值税普通发票");

    /* 发票类型 */
    private Integer billType;

    /* 发票类型名称 */
    private String billTypeName;

    BillTypeEnum(Integer billType,String payTypeName){
        this.billType = billType;
        this.billTypeName = payTypeName;
    }

    public static String getBillTypeName(Integer billType) {
        for (BillTypeEnum billTypeEnum : BillTypeEnum.values()) {
            if (billTypeEnum.billType.equals(billType) )
                return billTypeEnum.billTypeName;
        }
        return null;
    }

    public Integer getBillType() {
        return billType;
    }

    public String getBillTypeName() {
        return billTypeName;
    }
}
