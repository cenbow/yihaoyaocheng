package com.yyw.yhyc.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.yyw.yhyc.order.dto.OrderPromotionDetailDto;
import com.yyw.yhyc.product.bo.ProductInfo;

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

    /* 商品的本公司编码*/
    private String productCodeCompany;

    /* 是否渠道商品(0否，1是),  */
    private Integer isChannel;

    /* 厂家名称  */
    private String manufactures;

    /* 厂家id  */
    private String manufactureId;

    /* 来源表示字段（0：来自进货单，1：来自极速下单) */
    private Integer fromWhere;

    /**
     * 2016-10-31 增加特价活动商品相关信息 ，对应 t_promotion_product_group表
     */
    /* 特价 活动id */
    private Integer promotionId;

    /* 特价 活动名称 */
    private String promotionName;

    /* 特价 活动价格 */
    private BigDecimal promotionPrice;

    /* 特价 活动最小起批量 */
    private Integer promotionMinimumPacking;

    /* 特价 活动限购数量 */
    private Integer promotionLimitNum;

    /* 特价 活动总库存 */
    private Integer promotionSumInventory;

    /* 特价 活动实时库存 */
    private Integer promotionCurrentInventory;
    
    /**
     * 2016-12-08 增加满减、满赠、满 活动商品相关信息 ，对应 t_promotion、t_promotion_rule表
     */
    //private List<ProductPromotionInfo> list;
    
    /** 
	 * 其他活动id，如果有多个则用","拼接，不包括特价活动
	 */
	private String promotionCollectionId;
	
	
    /**
     * 保存商品参加的满减信息，什么类型的满减以及每个满减优惠了多少钱
     */
    private List<OrderPromotionDetailDto> promotionDetailInfoList;
    
    
	

    public List<OrderPromotionDetailDto> getPromotionDetailInfoList() {
		return promotionDetailInfoList;
	}

	public void setPromotionDetailInfoList(
			List<OrderPromotionDetailDto> promotionDetailInfoList) {
		this.promotionDetailInfoList = promotionDetailInfoList;
	}

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
    
    

	public String getPromotionCollectionId() {
		return promotionCollectionId;
	}

	public void setPromotionCollectionId(String promotionCollectionId) {
		this.promotionCollectionId = promotionCollectionId;
	}

	@Override
	public String toString() {
		return "ProductInfoDto [productPrice=" + productPrice
				+ ", productCount=" + productCount + ", totalPrice="
				+ totalPrice + ", imageUrl=" + imageUrl + ", periodProduct="
				+ periodProduct + ", paymentTerm=" + paymentTerm
				+ ", productCodeCompany=" + productCodeCompany + ", isChannel="
				+ isChannel + ", manufactures=" + manufactures
				+ ", manufactureId=" + manufactureId + ", fromWhere="
				+ fromWhere + ", promotionId=" + promotionId
				+ ", promotionName=" + promotionName + ", promotionPrice="
				+ promotionPrice + ", promotionMinimumPacking="
				+ promotionMinimumPacking + ", promotionLimitNum="
				+ promotionLimitNum + ", promotionSumInventory="
				+ promotionSumInventory + ", promotionCurrentInventory="
				+ promotionCurrentInventory + ", promotionCollectionId="
				+ promotionCollectionId + ", promotionDetailInfoList="
				+ promotionDetailInfoList + "]";
	}
}
