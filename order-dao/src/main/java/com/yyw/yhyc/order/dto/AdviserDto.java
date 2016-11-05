package com.yyw.yhyc.order.dto;

import java.io.Serializable;

public class AdviserDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 795301129006916467L;

	/**
	 * 销售顾问编码
	 */
	private String adviserCode;
	
	/**
	 * 销售顾问姓名
	 */
	private String adviserName;
	
	/**
	 * 销售顾问手机号码
	 */
	private String adviserPhoneNumber; 
	
	/**
	 * 销售顾问备注
	 */
	private String adviserRemark;

	/**
	 * @return the adviserCode
	 */
	public String getAdviserCode() {
		return adviserCode;
	}

	/**
	 * @param adviserCode the adviserCode to set
	 */
	public void setAdviserCode(String adviserCode) {
		this.adviserCode = adviserCode;
	}

	/**
	 * @return the adviserName
	 */
	public String getAdviserName() {
		return adviserName;
	}

	/**
	 * @param adviserName the adviserName to set
	 */
	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	/**
	 * @return the adviserPhoneNumber
	 */
	public String getAdviserPhoneNumber() {
		return adviserPhoneNumber;
	}

	/**
	 * @param adviserPhoneNumber the adviserPhoneNumber to set
	 */
	public void setAdviserPhoneNumber(String adviserPhoneNumber) {
		this.adviserPhoneNumber = adviserPhoneNumber;
	}

	/**
	 * @return the adviserRemark
	 */
	public String getAdviserRemark() {
		return adviserRemark;
	}

	/**
	 * @param adviserRemark the adviserRemark to set
	 */
	public void setAdviserRemark(String adviserRemark) {
		this.adviserRemark = adviserRemark;
	}
}
