/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.order.bo.Model;

public class OrderDelivery extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer deliveryId;

	/**
	  *	订单ID
	  */
	private java.lang.Integer orderId;

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
	  *	收货人联系电话
	  */
	private java.lang.String receiveContactPhone;

	/**
	  *	邮政编码
	  */
	private java.lang.String zipCode;

	/**
	  *	
	  */
	private java.lang.String receiveDate;

	/**
	  *	发货地址
	  */
	private java.lang.String deliveryAddress;

	/**
	  *	发货人电话
	  */
	private java.lang.String deliveryContactPhone;

	/**
	  *	预计送达时间
	  */
	private java.lang.String deliveryDate;

	/**
	  *	
	  */
	private java.lang.String updateDate;

	/**
	  *	
	  */
	private java.lang.String remark;

	/**
	  *	配送方式 1 自有物流 2 第三方物流
	  */
	private java.lang.Integer deliveryMethod;

	/**
	  *	发货联系人或第三方物流公司名称
	  */
	private java.lang.String deliveryContactPerson;

	/**
	  *	第三该物流单号
	  */
	private java.lang.String deliveryExpressNo;

	/**
	  *	
	  */
	public java.lang.Integer getDeliveryId() 
	{
		return deliveryId;
	}
	
	/**
	  *	
	  */
	public void setDeliveryId(java.lang.Integer deliveryId) 
	{
		this.deliveryId = deliveryId;
	}
	
	/**
	  *	订单ID
	  */
	public java.lang.Integer getOrderId() 
	{
		return orderId;
	}
	
	/**
	  *	订单ID
	  */
	public void setOrderId(java.lang.Integer orderId) 
	{
		this.orderId = orderId;
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
	  *	收货人联系电话
	  */
	public java.lang.String getReceiveContactPhone() 
	{
		return receiveContactPhone;
	}
	
	/**
	  *	收货人联系电话
	  */
	public void setReceiveContactPhone(java.lang.String receiveContactPhone) 
	{
		this.receiveContactPhone = receiveContactPhone;
	}
	
	/**
	  *	邮政编码
	  */
	public java.lang.String getZipCode() 
	{
		return zipCode;
	}
	
	/**
	  *	邮政编码
	  */
	public void setZipCode(java.lang.String zipCode) 
	{
		this.zipCode = zipCode;
	}
	
	/**
	  *	
	  */
	public java.lang.String getReceiveDate() 
	{
		return receiveDate;
	}
	
	/**
	  *	
	  */
	public void setReceiveDate(java.lang.String receiveDate) 
	{
		this.receiveDate = receiveDate;
	}
	
	/**
	  *	发货地址
	  */
	public java.lang.String getDeliveryAddress() 
	{
		return deliveryAddress;
	}
	
	/**
	  *	发货地址
	  */
	public void setDeliveryAddress(java.lang.String deliveryAddress) 
	{
		this.deliveryAddress = deliveryAddress;
	}
	
	/**
	  *	发货人电话
	  */
	public java.lang.String getDeliveryContactPhone() 
	{
		return deliveryContactPhone;
	}
	
	/**
	  *	发货人电话
	  */
	public void setDeliveryContactPhone(java.lang.String deliveryContactPhone) 
	{
		this.deliveryContactPhone = deliveryContactPhone;
	}
	
	/**
	  *	预计送达时间
	  */
	public java.lang.String getDeliveryDate() 
	{
		return deliveryDate;
	}
	
	/**
	  *	预计送达时间
	  */
	public void setDeliveryDate(java.lang.String deliveryDate) 
	{
		this.deliveryDate = deliveryDate;
	}
	
	/**
	  *	
	  */
	public java.lang.String getUpdateDate() 
	{
		return updateDate;
	}
	
	/**
	  *	
	  */
	public void setUpdateDate(java.lang.String updateDate) 
	{
		this.updateDate = updateDate;
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
	  *	配送方式 1 自有物流 2 第三方物流
	  */
	public java.lang.Integer getDeliveryMethod() 
	{
		return deliveryMethod;
	}
	
	/**
	  *	配送方式 1 自有物流 2 第三方物流
	  */
	public void setDeliveryMethod(java.lang.Integer deliveryMethod) 
	{
		this.deliveryMethod = deliveryMethod;
	}
	
	/**
	  *	发货联系人或第三方物流公司名称
	  */
	public java.lang.String getDeliveryContactPerson() 
	{
		return deliveryContactPerson;
	}
	
	/**
	  *	发货联系人或第三方物流公司名称
	  */
	public void setDeliveryContactPerson(java.lang.String deliveryContactPerson) 
	{
		this.deliveryContactPerson = deliveryContactPerson;
	}
	
	/**
	  *	第三该物流单号
	  */
	public java.lang.String getDeliveryExpressNo() 
	{
		return deliveryExpressNo;
	}
	
	/**
	  *	第三该物流单号
	  */
	public void setDeliveryExpressNo(java.lang.String deliveryExpressNo) 
	{
		this.deliveryExpressNo = deliveryExpressNo;
	}
	
	public String toString()
	{
		return "OrderDelivery [" + 
					"deliveryId=" + deliveryId + 
					", orderId=" + orderId + 
					", receivePerson=" + receivePerson + 
					", receiveRegion=" + receiveRegion + 
					", receiveCity=" + receiveCity + 
					", receiveProvince=" + receiveProvince + 
					", receiveAddress=" + receiveAddress + 
					", receiveContactPhone=" + receiveContactPhone + 
					", zipCode=" + zipCode + 
					", receiveDate=" + receiveDate + 
					", deliveryAddress=" + deliveryAddress + 
					", deliveryContactPhone=" + deliveryContactPhone + 
					", deliveryDate=" + deliveryDate + 
					", updateDate=" + updateDate + 
					", remark=" + remark + 
					", deliveryMethod=" + deliveryMethod + 
					", deliveryContactPerson=" + deliveryContactPerson + 
					", deliveryExpressNo=" + deliveryExpressNo + 
				"]";
	}
}

