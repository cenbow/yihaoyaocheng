/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:56
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
	private java.lang.Integer payType;

	/**
	  *	
	  */
	private java.lang.String payTypeName;

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
	  *	支付类型状态 1-启用;0停用
	  */
	private java.lang.Integer payStates;

	/**
	  *	备注
	  */
	private java.lang.String remark;

	/**
	  *	记录创建者
	  */
	private java.lang.String createUser;

	/**
	  *	记录生成时间
	  */
	private java.lang.String createTime;

	/**
	  *	记录更新者
	  */
	private java.lang.String updateUser;

	/**
	  *	记录更新时间
	  */
	private java.lang.String updateTime;

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
	public java.lang.Integer getPayType() 
	{
		return payType;
	}
	
	/**
	  *	支付类型 1-在线支付; 2-线下支付 3 账期支付
	  */
	public void setPayType(java.lang.Integer payType) 
	{
		this.payType = payType;
	}
	
	/**
	  *	
	  */
	public java.lang.String getPayTypeName() 
	{
		return payTypeName;
	}
	
	/**
	  *	
	  */
	public void setPayTypeName(java.lang.String payTypeName) 
	{
		this.payTypeName = payTypeName;
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
	  *	支付类型状态 1-启用;0停用
	  */
	public java.lang.Integer getPayStates() 
	{
		return payStates;
	}
	
	/**
	  *	支付类型状态 1-启用;0停用
	  */
	public void setPayStates(java.lang.Integer payStates) 
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
	
	/**
	  *	记录创建者
	  */
	public java.lang.String getCreateUser() 
	{
		return createUser;
	}
	
	/**
	  *	记录创建者
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
	  *	记录更新者
	  */
	public java.lang.String getUpdateUser() 
	{
		return updateUser;
	}
	
	/**
	  *	记录更新者
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
		return "SystemPayType [" + 
					"payTypeId=" + payTypeId + 
					", payType=" + payType + 
					", payTypeName=" + payTypeName + 
					", payCode=" + payCode + 
					", payName=" + payName + 
					", payLogo=" + payLogo + 
					", payStates=" + payStates + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

