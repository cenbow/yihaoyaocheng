package com.yyw.yhyc.order.dto;

import java.math.BigDecimal;

/**
 * 促销的特价商品活动库存对象
 * @author wangkui01
 *
 */
public class OrderPromotionSpecialProductDto {
	/**
	 * 促销id
	 */
	private Integer promotionId;
	/**
	 * 商品编码
	 */
	private String spuCode;
	/**
	 * 活动价格
	 */
	private BigDecimal promotionPrice;
	/**
	 * 最小起批量
	 */
	private Integer minimumPacking;
	/**
	 * 限购量
	 */
	private Integer limitNum;
	/**
	 * 活动总库存
	 */
	private Integer sumInventory;
	/**
	 * 活动实时库存
	 */
	private Integer currentInventory;
	/**
	 * 优先级
	 */
	private int sort;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "OrderPromotionSpecialProductDto [promotionId=" + promotionId
				+ ", spuCode=" + spuCode + ", promotionPrice=" + promotionPrice
				+ ", minimumPacking=" + minimumPacking + ", limitNum="
				+ limitNum + ", sumInventory=" + sumInventory
				+ ", currentInventory=" + currentInventory + ", sort=" + sort
				+ "]";
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	
	
	
	
	
	

}
