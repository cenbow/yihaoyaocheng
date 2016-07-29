package com.yyw.yhyc.product.dto;

import com.yyw.yhyc.product.bo.ProductInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lizhou on 2016/7/29
 */
public class ProductInfoDto extends ProductInfo implements Serializable {

    private static final long serialVersionUID = 6799891469310083830L;

    /* 商品单价 */
    private BigDecimal productPrice;

    /* 商品数量 */
    private Integer productCount;


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

    @Override
    public String toString() {
        return "ProductInfoDto{" +
                ", productPrice=" + productPrice +
                ", productCount=" + productCount +
                '}';
    }
}
