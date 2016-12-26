package com.yyw.yhyc.product.dto;

import java.io.Serializable;

public class ProductBeanDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商品编码
	 */
	private String produceCode;
	/**
	 * 发货数量
	 */
	private Integer sendNum;
	
	private String batchNumber; //批次
	
	
	public String getProduceCode() {
		return produceCode;
	}
	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	
	
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	@Override
	public String toString() {
		return "ProductBeanDto [produceCode=" + produceCode + ", sendNum="
				+ sendNum + ", batchNumber=" + batchNumber + "]";
	}
	
	

}
