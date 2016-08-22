package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.ShoppingCart;

/**
 * Created by lizhou on 2016/8/2
 */
public class ShoppingCartDto extends ShoppingCart  {

    private static final long serialVersionUID = 843745988079982509L;

    /* 商品图片url地址 */
    private String productImageUrl;

    /* 是否是账期商品 */
    private boolean periodProduct;

    /* 最小包装单位 */
    private String unit;

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public boolean isPeriodProduct() {
        return periodProduct;
    }

    public void setPeriodProduct(boolean periodProduct) {
        this.periodProduct = periodProduct;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ShoppingCartDto{" +
                "productImageUrl='" + productImageUrl + '\'' +
                ", periodProduct=" + periodProduct +
                ", unit='" + unit + '\'' +
                '}';
    }
}
