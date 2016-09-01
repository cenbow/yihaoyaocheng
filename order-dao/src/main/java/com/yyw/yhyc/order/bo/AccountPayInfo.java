/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class AccountPayInfo extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer accountInfoId;

	/**
	  *	
	  */
	private java.lang.Integer custId;

	/**
	  *	支付类型表ID主键
	  */
	private java.lang.Integer payTypeId;

	/**
	  *	
	  */
	private java.lang.String accountType;

	/**
	  *	付款账号
	  */
	private java.lang.String payAccountNo;

	/**
	  *	付款账号名称
	  */
	private java.lang.String payAccountName;

	/**
	  *	收款账号
	  */
	private java.lang.String receiveAccountNo;

	/**
	  *	收款款账号名称
	  */
	private java.lang.String receiveAccountName;

	/**
	  *	0 停用 1 启用 
	  */
	private java.lang.String accountStatus;

	/**
	  *	
	  */
	private java.lang.String createUser;

	/**
	  *	
	  */
	private java.lang.String createTime;

	/**
	  *	
	  */
	private java.lang.String updateUser;

	/**
	  *	
	  */
	private java.lang.String updateTime;

	/**
	  *	收款开户地区
	  */
	private java.lang.String accountArea;

	/**
	  *	收款开户支行
	  */
	private java.lang.String subbankName;

	/**
	  *	付款开户地区
	  */
	private java.lang.String payAccountArea;

	/**
	  *	付款开户支行
	  */
	private java.lang.String paySubbankName;

	/**
	  *	备注
	  */
	private java.lang.String remark;


	/**
	 *	省名称
	 */
	private String provinceName;

	/**
	 *	市名称
	 */
	private String cityName;

	/**
	 *	区名称
	 */
	private String districtName;

	/**
	  *	
	  */
	public java.lang.Integer getAccountInfoId() 
	{
		return accountInfoId;
	}
	
	/**
	  *	
	  */
	public void setAccountInfoId(java.lang.Integer accountInfoId) 
	{
		this.accountInfoId = accountInfoId;
	}
	
	/**
	  *	
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
	}
	
	/**
	  *	支付类型表ID主键
	  */
	public java.lang.Integer getPayTypeId() 
	{
		return payTypeId;
	}
	
	/**
	  *	支付类型表ID主键
	  */
	public void setPayTypeId(java.lang.Integer payTypeId) 
	{
		this.payTypeId = payTypeId;
	}
	
	/**
	  *	
	  */
	public java.lang.String getAccountType() 
	{
		return accountType;
	}
	
	/**
	  *	
	  */
	public void setAccountType(java.lang.String accountType) 
	{
		this.accountType = accountType;
	}
	
	/**
	  *	付款账号
	  */
	public java.lang.String getPayAccountNo() 
	{
		return payAccountNo;
	}
	
	/**
	  *	付款账号
	  */
	public void setPayAccountNo(java.lang.String payAccountNo) 
	{
		this.payAccountNo = payAccountNo;
	}
	
	/**
	  *	付款账号名称
	  */
	public java.lang.String getPayAccountName() 
	{
		return payAccountName;
	}
	
	/**
	  *	付款账号名称
	  */
	public void setPayAccountName(java.lang.String payAccountName) 
	{
		this.payAccountName = payAccountName;
	}
	
	/**
	  *	收款账号
	  */
	public java.lang.String getReceiveAccountNo() 
	{
		return receiveAccountNo;
	}
	
	/**
	  *	收款账号
	  */
	public void setReceiveAccountNo(java.lang.String receiveAccountNo) 
	{
		this.receiveAccountNo = receiveAccountNo;
	}
	
	/**
	  *	收款款账号名称
	  */
	public java.lang.String getReceiveAccountName() 
	{
		return receiveAccountName;
	}
	
	/**
	  *	收款款账号名称
	  */
	public void setReceiveAccountName(java.lang.String receiveAccountName) 
	{
		this.receiveAccountName = receiveAccountName;
	}
	
	/**
	  *	0 停用 1 启用 
	  */
	public java.lang.String getAccountStatus() 
	{
		return accountStatus;
	}
	
	/**
	  *	0 停用 1 启用 
	  */
	public void setAccountStatus(java.lang.String accountStatus) 
	{
		this.accountStatus = accountStatus;
	}
	
	/**
	  *	
	  */
	public java.lang.String getCreateUser() 
	{
		return createUser;
	}
	
	/**
	  *	
	  */
	public void setCreateUser(java.lang.String createUser) 
	{
		this.createUser = createUser;
	}
	
	/**
	  *	
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	
	  */
	public void setCreateTime(java.lang.String createTime) 
	{
		this.createTime = createTime;
	}
	
	/**
	  *	
	  */
	public java.lang.String getUpdateUser() 
	{
		return updateUser;
	}
	
	/**
	  *	
	  */
	public void setUpdateUser(java.lang.String updateUser) 
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	
	  */
	public java.lang.String getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	  *	
	  */
	public void setUpdateTime(java.lang.String updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	收款开户地区
	  */
	public java.lang.String getAccountArea() 
	{
		return accountArea;
	}
	
	/**
	  *	收款开户地区
	  */
	public void setAccountArea(java.lang.String accountArea) 
	{
		this.accountArea = accountArea;
	}
	
	/**
	  *	收款开户支行
	  */
	public java.lang.String getSubbankName() 
	{
		return subbankName;
	}
	
	/**
	  *	收款开户支行
	  */
	public void setSubbankName(java.lang.String subbankName) 
	{
		this.subbankName = subbankName;
	}
	
	/**
	  *	付款开户地区
	  */
	public java.lang.String getPayAccountArea() 
	{
		return payAccountArea;
	}
	
	/**
	  *	付款开户地区
	  */
	public void setPayAccountArea(java.lang.String payAccountArea) 
	{
		this.payAccountArea = payAccountArea;
	}
	
	/**
	  *	付款开户支行
	  */
	public java.lang.String getPaySubbankName() 
	{
		return paySubbankName;
	}
	
	/**
	  *	付款开户支行
	  */
	public void setPaySubbankName(java.lang.String paySubbankName) 
	{
		this.paySubbankName = paySubbankName;
	}
	
	/**
	  *	备注
	  */
	public java.lang.String getRemark() 
	{
		return remark;
	}
	
	/**
	  *	备注
	  */
	public void setRemark(java.lang.String remark) 
	{
		this.remark = remark;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	@Override
	public String toString() {
		return "AccountPayInfo{" +
				"accountInfoId=" + accountInfoId +
				", custId=" + custId +
				", payTypeId=" + payTypeId +
				", accountType='" + accountType + '\'' +
				", payAccountNo='" + payAccountNo + '\'' +
				", payAccountName='" + payAccountName + '\'' +
				", receiveAccountNo='" + receiveAccountNo + '\'' +
				", receiveAccountName='" + receiveAccountName + '\'' +
				", accountStatus='" + accountStatus + '\'' +
				", createUser='" + createUser + '\'' +
				", createTime='" + createTime + '\'' +
				", updateUser='" + updateUser + '\'' +
				", updateTime='" + updateTime + '\'' +
				", accountArea='" + accountArea + '\'' +
				", subbankName='" + subbankName + '\'' +
				", payAccountArea='" + payAccountArea + '\'' +
				", paySubbankName='" + paySubbankName + '\'' +
				", remark='" + remark + '\'' +
				", provinceName='" + provinceName + '\'' +
				", cityName='" + cityName + '\'' +
				", districtName='" + districtName + '\'' +
				'}';
	}
}

