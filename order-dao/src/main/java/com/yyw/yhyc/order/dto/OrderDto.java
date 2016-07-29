package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.product.dto.ProductInfoDto;

import java.util.List;

/**
 * Created by lizhou on 2016/7/29
 */
public class OrderDto extends Order {

    private static final long serialVersionUID = -3856718893790194219L;

    /* 商品信息集合 */
    private List<ProductInfoDto> productInfoDtoList;

    public List<ProductInfoDto> getProductInfoDtoList() {
        return productInfoDtoList;
    }

    public void setProductInfoDtoList(List<ProductInfoDto> productInfoDtoList) {
        this.productInfoDtoList = productInfoDtoList;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "productInfoDtoList=" + productInfoDtoList +
                '}';
    }
}
