/**
 * Created By: XI
 * Created On: 2016-11-21 10:34:21
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
	  *	收货人
	  */
	private java.lang.String receivePerson;

	/**
	  *	收货区编码
	  */
	private java.lang.String receiveRegion;

	/**
	  *	收货城市编码
	  */
	private java.lang.String receiveCity;

	/**
	  *	收货省份
	  */
	private java.lang.String receiveProvince;

	/**
	  *	收货详细地址
	  */
	private java.lang.String receiveAddress;

	/**
	  *	
	  */
	private java.lang.String receiveContactPhone;

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
	  *	收货人
	  */
	public java.lang.String getReceivePerson() 
	{
		return receivePerson;
	}
	
	/**
	  *	收货人
	  */
	public void setReceivePerson(java.lang.String receivePerson) 
	{
		this.receivePerson = receivePerson;
	}
	
	/**
	  *	收货区编码
	  */
	public java.lang.String getReceiveRegion() 
	{
		return receiveRegion;
	}
	
	/**
	  *	收货区编码
	  */
	public void setReceiveRegion(java.lang.String receiveRegion) 
	{
		this.receiveRegion = receiveRegion;
	}
	
	/**
	  *	收货城市编码
	  */
	public java.lang.String getReceiveCity() 
	{
		return receiveCity;
	}
	
	/**
	  *	收货城市编码
	  */
	public void setReceiveCity(java.lang.String receiveCity) 
	{
		this.receiveCity = receiveCity;
	}
	
	/**
	  *	收货省份
	  */
	public java.lang.String getReceiveProvince() 
	{
		return receiveProvince;
	}
	
	/**
	  *	收货省份
	  */
	public void setReceiveProvince(java.lang.String receiveProvince) 
	{
		this.receiveProvince = receiveProvince;
	}
	
	/**
	  *	收货详细地址
	  */
	public java.lang.String getReceiveAddress() 
	{
		return receiveAddress;
	}
	
	/**
	  *	收货详细地址
	  */
	public void setReceiveAddress(java.lang.String receiveAddress) 
	{
		this.receiveAddress = receiveAddress;
	}
	
	/**
	  *	
	  */
	public java.lang.String getReceiveContactPhone() 
	{
		return receiveContactPhone;
	}
	
	/**
	  *	
	  */
	public void setReceiveContactPhone(java.lang.String receiveContactPhone) 
	{
		this.receiveContactPhone = receiveContactPhone;
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
					", receivePerson=" + receivePerson + 
					", receiveRegion=" + receiveRegion + 
					", receiveCity=" + receiveCity + 
					", receiveProvince=" + receiveProvince + 
					", receiveAddress=" + receiveAddress + 
					", receiveContactPhone=" + receiveContactPhone + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

