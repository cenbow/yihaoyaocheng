package com.yyw.yhyc.order.enmu;

/**
 * Created by lvhongjie on 2016/7/29
 * 导常订单类型 枚举类
 */
public enum OrderExceptionTypeEnum {

    RETURN("1","退货"),
    CHANGE("2","换货"),
    REPLENISHMENT("3","补货"),
    REJECT("4","拒收");

    private String Type;

    private String typeName;

    OrderExceptionTypeEnum(String Type, String typeName){
        this.Type = Type;
        this.typeName = typeName;
    }



    public String getType() {
        return Type;
    }

    public String getTypeName() {
        return typeName;
    }
}
