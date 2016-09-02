package com.yyw.yhyc.product.enmu;

/**
 * Created by liqiang on 2016/9/1.
 */
public enum ProductInventoryLogTypeEnum {
    addto(1,"添加(初始)"),
    modify(2, "修改"),
    frozen(3,"冻结"),
    deduction(4,"扣减"),
    release(5,"释放");

    private int type;
    private String value;

    ProductInventoryLogTypeEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static String getName(int type) {
        for (ProductInventoryLogTypeEnum item : ProductInventoryLogTypeEnum.values()) {
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
