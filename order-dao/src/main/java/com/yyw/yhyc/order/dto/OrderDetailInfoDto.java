package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDetail;

/**
 * Created by jiangshuai on 2016/7/29.
 */
public class OrderDetailInfoDto extends OrderDetail {

    //商品名称
    private String productName;
    //规格
    private String spec;
    //生产厂商
    private String factoryName;

    private Integer productId;



    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
