package com.yyw.yhyc.product.dto;

import com.yyw.yhyc.product.bo.ProductInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lizhou on 2016/7/29
 */
public class ProductInfoDto extends ProductInfo implements Serializable {

    private static final long serialVersionUID = -7008651652828282150L;

    /* 商品单价 */
    private BigDecimal productPrice;

    /* 商品数量 */
    private Integer productCount;

    /* 商品总价(单价*数量) */
    private Integer totalPrice;

    /* 商品图片地址 */
    private String imageUrl;

    /* 是否是账期商品 */
    private boolean periodProduct;

    /* 商品的账期 */
    private int paymentTerm;


    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPeriodProduct() {
        return periodProduct;
    }

    public void setPeriodProduct(boolean periodProduct) {
        this.periodProduct = periodProduct;
    }

    public int getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(int paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    @Override
    public String toString() {
        return "ProductInfoDto{" +
                "productPrice=" + productPrice +
                ", productCount=" + productCount +
                ", totalPrice=" + totalPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", periodProduct=" + periodProduct +
                ", paymentTerm=" + paymentTerm +
                '}';
    }
}
