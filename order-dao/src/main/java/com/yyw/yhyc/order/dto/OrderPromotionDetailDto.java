package com.yyw.yhyc.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderPromotionDetailDto implements Serializable{
	private Integer promotionId;
	private Integer promotionType;
	private BigDecimal shareMoney;
	private String promotionName;
	private OrderPromotionDto promotionDto;
	
	
	public OrderPromotionDto getPromotionDto() {
		return promotionDto;
	}
	public void setPromotionDto(OrderPromotionDto promotionDto) {
		this.promotionDto = promotionDto;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	public Integer getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(Integer promotionType) {
		this.promotionType = promotionType;
	}
	public BigDecimal getShareMoney() {
		return shareMoney;
	}
	public void setShareMoney(BigDecimal shareMoney) {
		this.shareMoney = shareMoney;
	}
	@Override
	public String toString() {
		return "OrderPromotionDetailDto [promotionId=" + promotionId
				+ ", promotionType=" + promotionType + ", shareMoney="
				+ shareMoney + ", promotionName=" + promotionName
				+ ", promotionDto=" + promotionDto + "]";
	}
	

}
