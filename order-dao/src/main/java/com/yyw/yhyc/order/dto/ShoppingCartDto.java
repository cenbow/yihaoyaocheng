package com.yyw.yhyc.order.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.yyw.yhyc.order.bo.ShoppingCart;

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
    
    
    private String spec;

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
    
    //搜索出来的满减活动信息集合
    private Set productPromotionInfos;

    /* 商品状态是否正常*/
    private boolean normalStatus ;

    /* 商品状态是不正常的原因*/
    private String unNormalStatusReason;

    /* 状态枚举值 */
    private int statusEnum ;
    
    /**
     * 如果参加了满减活动那么均摊的钱
     */
    private BigDecimal shareMoney;
    
    /**
     * 如果该商品参加了满减活动，那么该属性有值，且可以参加多个满减
     */
    private List<OrderPromotionDto> fullReductionPromotionList;
    
    /**
     * 如果该商品参加了特价活动，那么该属性有值，且是特价活动的实体
     */
    private OrderPromotionDto specailPromotionDto;
    
    /**
     * 保存商品参加的满减信息，什么类型的满减以及每个满减优惠了多少钱
     */
    private List<OrderPromotionDetailDto> promotionDetailInfoList;
    
    private String rule;
    
    private String groupCode;
    

    public OrderPromotionDto getSpecailPromotionDto() {
		return specailPromotionDto;
	}

	public void setSpecailPromotionDto(OrderPromotionDto specailPromotionDto) {
		this.specailPromotionDto = specailPromotionDto;
	}

	public List<OrderPromotionDto> getFullReductionPromotionList() {
		return fullReductionPromotionList;
	}

	public void setFullReductionPromotionList(
			List<OrderPromotionDto> fullReductionPromotionList) {
		this.fullReductionPromotionList = fullReductionPromotionList;
	}

	public BigDecimal getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(BigDecimal shareMoney) {
		this.shareMoney = shareMoney;
	}

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

    public boolean isNormalStatus() {
        return normalStatus;
    }

   

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setNormalStatus(boolean normalStatus) {
        this.normalStatus = normalStatus;
    }

    public String getUnNormalStatusReason() {
        return unNormalStatusReason;
    }

    public void setUnNormalStatusReason(String unNormalStatusReason) {
        this.unNormalStatusReason = unNormalStatusReason;
    }

    public int getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(int statusEnum) {
        this.statusEnum = statusEnum;
    }
    
    

    public List<OrderPromotionDetailDto> getPromotionDetailInfoList() {
		return promotionDetailInfoList;
	}

	public void setPromotionDetailInfoList(
			List<OrderPromotionDetailDto> promotionDetailInfoList) {
		this.promotionDetailInfoList = promotionDetailInfoList;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
	
	public Set getProductPromotionInfos() {
		return productPromotionInfos;
	}

	public void setProductPromotionInfos(Set productPromotionInfos) {
		this.productPromotionInfos = productPromotionInfos;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Override
	public String toString() {
		return "ShoppingCartDto [productImageUrl=" + productImageUrl
				+ ", periodProduct=" + periodProduct + ", paymentTerm="
				+ paymentTerm + ", unit=" + unit + ", minimumPacking="
				+ minimumPacking + ", saleStart=" + saleStart + ", upStep="
				+ upStep + ", existProductInventory=" + existProductInventory
				+ ", productInventory=" + productInventory + ", putawayStatus="
				+ putawayStatus + ", isChannel=" + isChannel
				+ ", promotionPrice=" + promotionPrice
				+ ", promotionMinimumPacking=" + promotionMinimumPacking
				+ ", promotionLimitNum=" + promotionLimitNum
				+ ", promotionSumInventory=" + promotionSumInventory
				+ ", promotionCurrentInventory=" + promotionCurrentInventory
				+ ", promotionType=" + promotionType + ", normalStatus="
				+ normalStatus + ", unNormalStatusReason="
				+ unNormalStatusReason + ", statusEnum=" + statusEnum
				+ ", shareMoney=" + shareMoney
				+ ", fullReductionPromotionList=" + fullReductionPromotionList
				+ ", specailPromotionDto=" + specailPromotionDto
				+ ", promotionDetailInfoList=" + promotionDetailInfoList + "]";
	}
}
