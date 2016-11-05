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

    /*商品的本公司编码*/
    private String productCodeCompany;

    /*  是否渠道商品(0否，1是),  */
    private Integer isChannel;

    /*  厂家名称  */
    private String manufactures;

    /*  厂家id  */
    private String manufactureId;

    /* 来源表示字段（0：来自进货单，1：来自极速下单) */
    private Integer fromWhere;


    //    2016-10-31 增加活动商品相关信息 ，对应 t_promotion_product_group表

    /* 活动id */
    private Integer promotionId;

    /* 活动活动名称 */
    private String promotionName;

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

//    2016-10-31 增加活动商品相关信息 ，对应 t_promotion_product_group表


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

    public String getProductCodeCompany() {
        return productCodeCompany;
    }

    public void setProductCodeCompany(String productCodeCompany) {
        this.productCodeCompany = productCodeCompany;
    }

    public Integer getIsChannel() {
        return isChannel;
    }

    public void setIsChannel(Integer isChannel) {
        this.isChannel = isChannel;
    }

    public String getManufactures() {
        return manufactures;
    }

    public void setManufactures(String manufactures) {
        this.manufactures = manufactures;
    }

    public String getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }

    public Integer getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(Integer fromWhere) {
        this.fromWhere = fromWhere;
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

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
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
                ", productCodeCompany='" + productCodeCompany + '\'' +
                ", isChannel=" + isChannel +
                ", manufactures='" + manufactures + '\'' +
                ", manufactureId='" + manufactureId + '\'' +
                ", fromWhere=" + fromWhere +
                ", promotionId=" + promotionId +
                ", promotionName=" + promotionName +
                ", promotionPrice=" + promotionPrice +
                ", promotionMinimumPacking=" + promotionMinimumPacking +
                ", promotionLimitNum=" + promotionLimitNum +
                ", promotionSumInventory=" + promotionSumInventory +
                ", promotionCurrentInventory=" + promotionCurrentInventory +
                "} " + super.toString();
    }
}
