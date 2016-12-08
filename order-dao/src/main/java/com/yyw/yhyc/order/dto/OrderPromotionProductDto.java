package com.yyw.yhyc.order.dto;

public class OrderPromotionProductDto {
	/**
	 * 商品spu编码
	 */
	private String spuCode;
	/**
	 * 商品在活动中的状态，0：活动中； 1：已从活动中删除
	 */
	private Integer status;
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "OrderPromotionProductBean [spuCode=" + spuCode + ", status="
				+ status + "]";
	}

	
	
}
