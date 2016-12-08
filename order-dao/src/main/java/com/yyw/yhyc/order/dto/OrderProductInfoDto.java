package com.yyw.yhyc.order.dto;

public class OrderProductInfoDto {
	private String spuCode;
	private String productName;
	private int supplyId;
	private int custId;
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}
	
	
	
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	@Override
	public String toString() {
		return "OrderProductInfoDto [spuCode=" + spuCode + ", productName="
				+ productName + ", supplyId=" + supplyId + ", custId=" + custId
				+ "]";
	}
	
	
	
	

}
