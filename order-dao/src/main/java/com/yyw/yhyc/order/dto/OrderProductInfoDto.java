package com.yyw.yhyc.order.dto;

import java.io.Serializable;

public class OrderProductInfoDto implements Serializable{
	private String spuCode;
	private String sellerCode;
	
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	
	
	
	

}
