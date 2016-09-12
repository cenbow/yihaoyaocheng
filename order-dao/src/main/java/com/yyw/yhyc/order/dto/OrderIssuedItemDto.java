package com.yyw.yhyc.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liuyang on 2016/9/10.
 */
public class OrderIssuedItemDto implements Serializable {
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;
    private String produceCode;          //1号药城商品对应的ERP系统商品编码

    private Long goodsNum;             //1号药城订单商品数量

    private BigDecimal goodsUnitPrice;     //1号药城订单商品销售单价

    private BigDecimal goodsAmount;        //1号药城订单商品金额

    private Integer productId;          //1号药城商品Id

    public Long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getGoodsUnitPrice() {
        return goodsUnitPrice;
    }

    public void setGoodsUnitPrice(BigDecimal goodsUnitPrice) {
        this.goodsUnitPrice = goodsUnitPrice;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getProduceCode() {

        return produceCode;
    }

    public void setProduceCode(String produceCode) {
        this.produceCode = produceCode;
    }
}
