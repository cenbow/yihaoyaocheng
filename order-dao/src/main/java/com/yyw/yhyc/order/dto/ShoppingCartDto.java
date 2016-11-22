package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.ShoppingCart;

import java.math.BigDecimal;

/**
 * Created by lizhou on 2016/8/2
 */
public class ShoppingCartDto extends ShoppingCart  {

    private static final long serialVersionUID = 843745988079982509L;

    /* 商品图片url地址 */
    private String productImageUrl;

    /* 是否是账期商品 */
    private boolean periodProduct;

    /* 商品的账期 */
    private int paymentTerm;

    /* 最小包装单位 */
    private String unit;

    /* 最小拆零包装数量 */
    private int minimumPacking;

    /**
     * 商品起售数量
     */
    private Integer saleStart;
    /**
     * 递增数量
     */
    private Integer upStep;

    /* 是否还有商品库存 */
    private boolean existProductInventory;

    /* 商品库存数量 */
    private Integer productInventory;

    /* （客户组）商品上下架状态：t_product_putaway表中的state字段 （上下架状态 0未上架  1上架  2本次下架  3非本次下架 ）*/
    private Integer putawayStatus;

    /*  是否渠道商品(0否，1是),  */
    private Integer isChannel;


//    2016-10-31 增加活动商品相关信息 ，对应 t_promotion_product_group表
    /* 活动价格 */
    private BigDecimal promotionPrice;

    /* 活动最小起批量 */
    private Integer promotionMinimumPacking;

    /* 活动限购数量 */
    private Integer promotionLimitNum;

    /* 活动总库存 */
    private Integer promotionSumInventory;

    /* 活动实时库存 */
    private Integer promotionCurrentInventory;

    /* 活动类型：1表示特价促销活动 */
    private int promotionType;

//    2016-10-31 增加活动商品相关信息 ，对应 t_promotion_product_group表

    public Integer getSaleStart() {
        return saleStart;
    }

    public void setSaleStart(Integer saleStart) {
        this.saleStart = saleStart;
    }

    public Integer getUpStep() {
        return upStep;
    }

    public void setUpStep(Integer upStep) {
        this.upStep = upStep;
    }

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

    public int getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(int paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public int getMinimumPacking() {
        return minimumPacking;
    }

    public void setMinimumPacking(int minimumPacking) {
        this.minimumPacking = minimumPacking;
    }

    public boolean isExistProductInventory() {
        return existProductInventory;
    }

    public void setExistProductInventory(boolean existProductInventory) {
        this.existProductInventory = existProductInventory;
    }

    public Integer getPutawayStatus() {
        return putawayStatus;
    }

    public void setPutawayStatus(Integer putawayStatus) {
        this.putawayStatus = putawayStatus;
    }

    public Integer getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(Integer productInventory) {
        this.productInventory = productInventory;
    }

    public Integer getIsChannel() {
        return isChannel;
    }

    public void setIsChannel(Integer isChannel) {
        this.isChannel = isChannel;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Integer getPromotionMinimumPacking() {
        return promotionMinimumPacking;
    }

    public void setPromotionMinimumPacking(Integer promotionMinimumPacking) {
        this.promotionMinimumPacking = promotionMinimumPacking;
    }

    public Integer getPromotionLimitNum() {
        return promotionLimitNum;
    }

    public void setPromotionLimitNum(Integer promotionLimitNum) {
        this.promotionLimitNum = promotionLimitNum;
    }

    public Integer getPromotionSumInventory() {
        return promotionSumInventory;
    }

    public void setPromotionSumInventory(Integer promotionSumInventory) {
        this.promotionSumInventory = promotionSumInventory;
    }

    public Integer getPromotionCurrentInventory() {
        return promotionCurrentInventory;
    }

    public void setPromotionCurrentInventory(Integer promotionCurrentInventory) {
        this.promotionCurrentInventory = promotionCurrentInventory;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    @Override
    public String toString() {
        return "ShoppingCartDto{" +
                "productImageUrl='" + productImageUrl + '\'' +
                ", periodProduct=" + periodProduct +
                ", paymentTerm=" + paymentTerm +
                ", unit='" + unit + '\'' +
                ", minimumPacking=" + minimumPacking +
                ", saleStart=" + saleStart +
                ", upStep=" + upStep +
                ", existProductInventory=" + existProductInventory +
                ", productInventory=" + productInventory +
                ", putawayStatus=" + putawayStatus +
                ", isChannel=" + isChannel +
                ", promotionPrice=" + promotionPrice +
                ", promotionMinimumPacking=" + promotionMinimumPacking +
                ", promotionLimitNum=" + promotionLimitNum +
                ", promotionSumInventory=" + promotionSumInventory +
                ", promotionCurrentInventory=" + promotionCurrentInventory +
                ", promotionType=" + promotionType +
                "} " + super.toString();
    }
}
