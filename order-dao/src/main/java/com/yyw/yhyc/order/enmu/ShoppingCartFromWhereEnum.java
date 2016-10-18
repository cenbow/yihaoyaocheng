package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/10/17.
 */
public enum ShoppingCartFromWhereEnum {

    SHOPPING_CART(0,"进货单"),
    FAST_ORDER(1,"极速下单");


    /* 来源 */
    private int fromWhere;

    /* 来源名称 */
    private String fromWhereName;

    ShoppingCartFromWhereEnum(int fromWhere, String fromWhereName) {
        this.fromWhere = fromWhere;
        this.fromWhereName = fromWhereName;
    }

    public int getFromWhere() {
        return fromWhere;
    }

    public String getFromWhereName() {
        return fromWhereName;
    }

    public static String getFromWhereName(int fromWhere) {
        for (ShoppingCartFromWhereEnum shoppingCartFromWhereEnum : ShoppingCartFromWhereEnum.values()) {
            if (shoppingCartFromWhereEnum.fromWhere == fromWhere )
                return shoppingCartFromWhereEnum.fromWhereName;
        }
        return null;
    }

}
