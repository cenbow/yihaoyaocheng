package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderReturn;

import java.math.BigDecimal;

/**
 * Created by liqiang on 2016/8/9.
 */
public class OrderReturnDto extends OrderReturn {

    private Integer productId;              //商品id
    private String productName;             //商品名
    private BigDecimal productPrice;       //商品单价
    private String specification;          //商品规格
    private String manufactures;           //生产厂家
    private String imageUrl;                //商品图片

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManufactures() {
        return manufactures;
    }

    public void setManufactures(String manufactures) {
        this.manufactures = manufactures;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
