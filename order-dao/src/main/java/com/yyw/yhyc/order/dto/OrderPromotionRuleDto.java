package com.yyw.yhyc.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderPromotionRuleDto implements Serializable{
	/**
	 * 满多少元（件）
	 */
	private BigDecimal promotionSum;
	/**
	 * 减多少元(折)。送多少积分。送什么赠品
	 */
	private String promotionMinu;
	public BigDecimal getPromotionSum() {
		return promotionSum;
	}
	public void setPromotionSum(BigDecimal promotionSum) {
		this.promotionSum = promotionSum;
	}
	public String getPromotionMinu() {
		return promotionMinu;
	}
	public void setPromotionMinu(String promotionMinu) {
		this.promotionMinu = promotionMinu;
	}
	@Override
	public String toString() {
		return "OrderPromotionRuleBean [promotionSum=" + promotionSum
				+ ", promotionMinu=" + promotionMinu + "]";
	}
	
	
	
	
	

}
