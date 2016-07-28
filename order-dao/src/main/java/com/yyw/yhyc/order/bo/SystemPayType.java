/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.order.bo.Model;

public class SystemPayType extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键ID
	  */
	private java.lang.Integer payTypeId;

	/**
	  *	支付类型 1-在线支付; 2-线下支付 3 账期支付
	  */
	private java.lang.String payType;

	/**
	  *	支付类型编号
	  */
	private java.lang.String payCode;

	/**
	  *	支付类型名称
	  */
	private java.lang.String payName;

	/**
	  *	支付类型图标
	  */
	private java.lang.String payLogo;

	/**
	  *	支付类型状态 1-启用;-1停用
	  */
	private java.lang.String payStates;

	/**
	  *	备注
	  */
	private java.lang.String remark;

	/**
	  *	主键ID
	  */
	public java.lang.Integer getPayTypeId() 
	{
		return payTypeId;
	}
	
	/**
	  *	主键ID
	  */
	public void setPayTypeId(java.lang.Integer payTypeId) 
	{
		this.payTypeId = payTypeId;
	}
	
	/**
	  *	支付类型 1-在线支付; 2-线下支付 3 账期支付
	  */
	public java.lang.String getPayType() 
	{
		return payType;
	}
	
	/**
	  *	支付类型 1-在线支付; 2-线下支付 3 账期支付
	  */
	public void setPayType(java.lang.String payType) 
	{
		this.payType = payType;
	}
	
	/**
	  *	支付类型编号
	  */
	public java.lang.String getPayCode() 
	{
		return payCode;
	}
	
	/**
	  *	支付类型编号
	  */
	public void setPayCode(java.lang.String payCode) 
	{
		this.payCode = payCode;
	}
	
	/**
	  *	支付类型名称
	  */
	public java.lang.String getPayName() 
	{
		return payName;
	}
	
	/**
	  *	支付类型名称
	  */
	public void setPayName(java.lang.String payName) 
	{
		this.payName = payName;
	}
	
	/**
	  *	支付类型图标
	  */
	public java.lang.String getPayLogo() 
	{
		return payLogo;
	}
	
	/**
	  *	支付类型图标
	  */
	public void setPayLogo(java.lang.String payLogo) 
	{
		this.payLogo = payLogo;
	}
	
	/**
	  *	支付类型状态 1-启用;-1停用
	  */
	public java.lang.String getPayStates() 
	{
		return payStates;
	}
	
	/**
	  *	支付类型状态 1-启用;-1停用
	  */
	public void setPayStates(java.lang.String payStates) 
	{
		this.payStates = payStates;
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
	
	public String toString()
	{
		return "SystemPayType [" + 
					"payTypeId=" + payTypeId + 
					", payType=" + payType + 
					", payCode=" + payCode + 
					", payName=" + payName + 
					", payLogo=" + payLogo + 
					", payStates=" + payStates + 
					", remark=" + remark + 
				"]";
	}
}

