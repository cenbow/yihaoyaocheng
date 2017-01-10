/**
 * Created By: XI
 * Created On: 2016-11-22 10:24:37
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderReceive extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	异常订单编码
	  */
	private java.lang.String exceptionOrderId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	买家收货人
	  */
	private java.lang.String buyerReceivePerson;

	/**
	  *	买家收货区编码
	  */
	private java.lang.String buyerReceiveRegion;

	/**
	  *	买家收货城市编码
	  */
	private java.lang.String buyerReceiveCity;

	/**
	  *	买家收货省份
	  */
	private java.lang.String buyerReceiveProvince;

	/**
	  *	买家收货详细地址
	  */
	private java.lang.String buyerReceiveAddress;

	/**
	  *	买家收货联系人手机号
	  */
	private java.lang.String buyerReceiveContactPhone;

	/**
	  *	卖家收货人
	  */
	private java.lang.String sellerReceivePerson;

	/**
	  *	卖家收货区编码
	  */
	private java.lang.String sellerReceiveRegion;

	/**
	  *	卖家收货城市编码
	  */
	private java.lang.String sellerReceiveCity;

	/**
	  *	卖家收货省份
	  */
	private java.lang.String sellerReceiveProvince;

	/**
	  *	卖家收货详细地址
	  */
	private java.lang.String sellerReceiveAddress;

	/**
	  *	卖家收货联系人手机号
	  */
	private java.lang.String sellerReceiveContactPhone;

	/**
	  *	
	  */
	private java.lang.String remark;

	/**
	  *	
	  */
	private java.lang.String createUser;

	/**
	  *	记录生成时间
	  */
	private java.lang.String createTime;

	/**
	  *	
	  */
	private java.lang.String updateUser;

	/**
	  *	记录更新时间
	  */
	private java.lang.String updateTime;

	/**
	  *	异常订单编码
	  */
	public java.lang.String getExceptionOrderId() 
	{
		return exceptionOrderId;
	}
	
	/**
	  *	异常订单编码
	  */
	public void setExceptionOrderId(java.lang.String exceptionOrderId) 
	{
		this.exceptionOrderId = exceptionOrderId;
	}
	
	/**
	  *	订单编号
	  */
	public java.lang.String getFlowId() 
	{
		return flowId;
	}
	
	/**
	  *	订单编号
	  */
	public void setFlowId(java.lang.String flowId) 
	{
		this.flowId = flowId;
	}
	
	/**
	  *	买家收货人
	  */
	public java.lang.String getBuyerReceivePerson() 
	{
		return buyerReceivePerson;
	}
	
	/**
	  *	买家收货人
	  */
	public void setBuyerReceivePerson(java.lang.String buyerReceivePerson) 
	{
		this.buyerReceivePerson = buyerReceivePerson;
	}
	
	/**
	  *	买家收货区编码
	  */
	public java.lang.String getBuyerReceiveRegion() 
	{
		return buyerReceiveRegion;
	}
	
	/**
	  *	买家收货区编码
	  */
	public void setBuyerReceiveRegion(java.lang.String buyerReceiveRegion) 
	{
		this.buyerReceiveRegion = buyerReceiveRegion;
	}
	
	/**
	  *	买家收货城市编码
	  */
	public java.lang.String getBuyerReceiveCity() 
	{
		return buyerReceiveCity;
	}
	
	/**
	  *	买家收货城市编码
	  */
	public void setBuyerReceiveCity(java.lang.String buyerReceiveCity) 
	{
		this.buyerReceiveCity = buyerReceiveCity;
	}
	
	/**
	  *	买家收货省份
	  */
	public java.lang.String getBuyerReceiveProvince() 
	{
		return buyerReceiveProvince;
	}
	
	/**
	  *	买家收货省份
	  */
	public void setBuyerReceiveProvince(java.lang.String buyerReceiveProvince) 
	{
		this.buyerReceiveProvince = buyerReceiveProvince;
	}
	
	/**
	  *	买家收货详细地址
	  */
	public java.lang.String getBuyerReceiveAddress() 
	{
		return buyerReceiveAddress;
	}
	
	/**
	  *	买家收货详细地址
	  */
	public void setBuyerReceiveAddress(java.lang.String buyerReceiveAddress) 
	{
		this.buyerReceiveAddress = buyerReceiveAddress;
	}
	
	/**
	  *	买家收货联系人手机号
	  */
	public java.lang.String getBuyerReceiveContactPhone() 
	{
		return buyerReceiveContactPhone;
	}
	
	/**
	  *	买家收货联系人手机号
	  */
	public void setBuyerReceiveContactPhone(java.lang.String buyerReceiveContactPhone) 
	{
		this.buyerReceiveContactPhone = buyerReceiveContactPhone;
	}
	
	/**
	  *	卖家收货人
	  */
	public java.lang.String getSellerReceivePerson() 
	{
		return sellerReceivePerson;
	}
	
	/**
	  *	卖家收货人
	  */
	public void setSellerReceivePerson(java.lang.String sellerReceivePerson) 
	{
		this.sellerReceivePerson = sellerReceivePerson;
	}
	
	/**
	  *	卖家收货区编码
	  */
	public java.lang.String getSellerReceiveRegion() 
	{
		return sellerReceiveRegion;
	}
	
	/**
	  *	卖家收货区编码
	  */
	public void setSellerReceiveRegion(java.lang.String sellerReceiveRegion) 
	{
		this.sellerReceiveRegion = sellerReceiveRegion;
	}
	
	/**
	  *	卖家收货城市编码
	  */
	public java.lang.String getSellerReceiveCity() 
	{
		return sellerReceiveCity;
	}
	
	/**
	  *	卖家收货城市编码
	  */
	public void setSellerReceiveCity(java.lang.String sellerReceiveCity) 
	{
		this.sellerReceiveCity = sellerReceiveCity;
	}
	
	/**
	  *	卖家收货省份
	  */
	public java.lang.String getSellerReceiveProvince() 
	{
		return sellerReceiveProvince;
	}
	
	/**
	  *	卖家收货省份
	  */
	public void setSellerReceiveProvince(java.lang.String sellerReceiveProvince) 
	{
		this.sellerReceiveProvince = sellerReceiveProvince;
	}
	
	/**
	  *	卖家收货详细地址
	  */
	public java.lang.String getSellerReceiveAddress() 
	{
		return sellerReceiveAddress;
	}
	
	/**
	  *	卖家收货详细地址
	  */
	public void setSellerReceiveAddress(java.lang.String sellerReceiveAddress) 
	{
		this.sellerReceiveAddress = sellerReceiveAddress;
	}
	
	/**
	  *	卖家收货联系人手机号
	  */
	public java.lang.String getSellerReceiveContactPhone() 
	{
		return sellerReceiveContactPhone;
	}
	
	/**
	  *	卖家收货联系人手机号
	  */
	public void setSellerReceiveContactPhone(java.lang.String sellerReceiveContactPhone) 
	{
		this.sellerReceiveContactPhone = sellerReceiveContactPhone;
	}
	
	/**
	  *	
	  */
	public java.lang.String getRemark() 
	{
		return remark;
	}
	
	/**
	  *	
	  */
	public void setRemark(java.lang.String remark) 
	{
		this.remark = remark;
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
	  *	记录生成时间
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	记录生成时间
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
	  *	记录更新时间
	  */
	public java.lang.String getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	  *	记录更新时间
	  */
	public void setUpdateTime(java.lang.String updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	public String toString()
	{
		return "OrderReceive [" + 
					"exceptionOrderId=" + exceptionOrderId + 
					", flowId=" + flowId + 
					", buyerReceivePerson=" + buyerReceivePerson + 
					", buyerReceiveRegion=" + buyerReceiveRegion + 
					", buyerReceiveCity=" + buyerReceiveCity + 
					", buyerReceiveProvince=" + buyerReceiveProvince + 
					", buyerReceiveAddress=" + buyerReceiveAddress + 
					", buyerReceiveContactPhone=" + buyerReceiveContactPhone + 
					", sellerReceivePerson=" + sellerReceivePerson + 
					", sellerReceiveRegion=" + sellerReceiveRegion + 
					", sellerReceiveCity=" + sellerReceiveCity + 
					", sellerReceiveProvince=" + sellerReceiveProvince + 
					", sellerReceiveAddress=" + sellerReceiveAddress + 
					", sellerReceiveContactPhone=" + sellerReceiveContactPhone + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

