package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lizhou on 2016/8/2
 */
public class ShoppingCartListDto implements Serializable{

    private static final long serialVersionUID = 4186154289532275138L;

    /* 买家企业信息 */
    private UsermanageEnterprise buyer;

    /* 供应商企业信息 */
    private UsermanageEnterprise seller;

    /* 买家在供应商中买的商品(购物车条目集合) */
    private List<ShoppingCartDto> shoppingCartDtoList;

    /* 购买当前供应商下所有商品的总金额*/
    private BigDecimal productPriceCount;

    /* 购买当前供应商下(设置了账期的)商品的总金额*/
    private BigDecimal periodProductPriceCount;

    /* 购买当前供应商下(没有设置账期的)商品的总金额*/
    private BigDecimal nonPeriodProductPriceCount;

    /* 供应商对采供商设置的账期，即客户账期 */
    private Integer paymentTermCus;

    public UsermanageEnterprise getBuyer() {
        return buyer;
    }

    public void setBuyer(UsermanageEnterprise buyer) {
        this.buyer = buyer;
    }

    public UsermanageEnterprise getSeller() {
        return seller;
    }

    public void setSeller(UsermanageEnterprise seller) {
        this.seller = seller;
    }

    public List<ShoppingCartDto> getShoppingCartDtoList() {
        return shoppingCartDtoList;
    }

    public void setShoppingCartDtoList(List<ShoppingCartDto> shoppingCartDtoList) {
        this.shoppingCartDtoList = shoppingCartDtoList;
    }

    public BigDecimal getProductPriceCount() {
        return productPriceCount;
    }

    public void setProductPriceCount(BigDecimal productPriceCount) {
        this.productPriceCount = productPriceCount;
    }

    public BigDecimal getPeriodProductPriceCount() {
        return periodProductPriceCount;
    }

    public void setPeriodProductPriceCount(BigDecimal periodProductPriceCount) {
        this.periodProductPriceCount = periodProductPriceCount;
    }

    public BigDecimal getNonPeriodProductPriceCount() {
        return nonPeriodProductPriceCount;
    }

    public void setNonPeriodProductPriceCount(BigDecimal nonPeriodProductPriceCount) {
        this.nonPeriodProductPriceCount = nonPeriodProductPriceCount;
    }

    public Integer getPaymentTermCus() {
        return paymentTermCus;
    }

    public void setPaymentTermCus(Integer paymentTermCus) {
        this.paymentTermCus = paymentTermCus;
    }

    @Override
    public String toString() {
        return "ShoppingCartListDto{" +
                "buyer=" + buyer +
                ", seller=" + seller +
                ", shoppingCartDtoList=" + shoppingCartDtoList +
                ", productPriceCount=" + productPriceCount +
                ", periodProductPriceCount=" + periodProductPriceCount +
                ", nonPeriodProductPriceCount=" + nonPeriodProductPriceCount +
                ", paymentTermCus=" + paymentTermCus +
                '}';
    }
}
