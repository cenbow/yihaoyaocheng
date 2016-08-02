package com.yyw.yhyc.product.bo;

import com.yyw.yhyc.bo.Model;

/**
 * Created by lizhou on 2016/7/29
 */
public class ProductInfo extends Model {

    private static final long serialVersionUID = 4135014915354212647L;

    /* 商品id  */
    private Integer id ;

    /* 商品名 */
    private String productName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                '}';
    }
}
