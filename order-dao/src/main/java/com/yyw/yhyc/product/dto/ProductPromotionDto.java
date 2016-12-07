package com.yyw.yhyc.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lizhou on 2016/11/2.
 */
public class ProductPromotionDto implements Serializable {

    private static final long serialVersionUID = -3078126996928280807L;
    /* 促销活动id */
    private Integer promotionId;

    /*商品SPU编码*/
    private String spuCode;

    /* 促销商品的活动价格 */
    private BigDecimal promotionPrice;

    /* 最小起批量 */
    private Integer minimumPacking;

    /* 每人限购量 */
    private Integer limitNum;

    /* 活动总库存 */
    private Integer sumInventory;

    /* 活动实时库存 */
    private Integer currentInventory;

    /* 优先级 */
    private Integer sort;

    /* 活动类型：1表示特价促销活动 */
    private int promotionType;

    /* 活动名称 */
    private String promotionName;

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public Integer getMinimumPacking() {
        return minimumPacking;
    }

    public void setMinimumPacking(Integer minimumPacking) {
        this.minimumPacking = minimumPacking;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getSumInventory() {
        return sumInventory;
    }

    public void setSumInventory(Integer sumInventory) {
        this.sumInventory = sumInventory;
    }

    public Integer getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(Integer currentInventory) {
        this.currentInventory = currentInventory;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @Override
    public String toString() {
        return "ProductPromotionDto{" +
                "promotionId=" + promotionId +
                ", spuCode='" + spuCode + '\'' +
                ", promotionPrice=" + promotionPrice +
                ", minimumPacking=" + minimumPacking +
                ", limitNum=" + limitNum +
                ", sumInventory=" + sumInventory +
                ", currentInventory=" + currentInventory +
                ", sort=" + sort +
                ", promotionType=" + promotionType +
                ", promotionName='" + promotionName + '\'' +
                '}';
    }
}
