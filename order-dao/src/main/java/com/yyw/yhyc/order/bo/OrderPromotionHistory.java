/**
 * Created By: XI
 * Created On: 2016-12-8 9:28:10
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderPromotionHistory extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Long id;

	/**
	  *	买家id
	  */
	private java.lang.Integer custId;

	/**
	  *	买家名称
	  */
	private java.lang.String custName;

	/**
	  *	促销id
	  */
	private java.lang.Integer promotionId;

	/**
	  *	用户使用该促销的次数
	  */
	private java.lang.Integer useNum;

	/**
	  *	
	  */
	private java.lang.String createTime;

	/**
	  *	
	  */
	private java.lang.String createUser;

	/**
	  *	
	  */
	private java.lang.String updateTime;

	/**
	  *	
	  */
	private java.lang.String updateUser;

	/**
	  *	
	  */
	public java.lang.Long getId() 
	{
		return id;
	}
	
	/**
	  *	
	  */
	public void setId(java.lang.Long id) 
	{
		this.id = id;
	}
	
	/**
	  *	买家id
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	买家id
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
	}
	
	/**
	  *	买家名称
	  */
	public java.lang.String getCustName() 
	{
		return custName;
	}
	
	/**
	  *	买家名称
	  */
	public void setCustName(java.lang.String custName) 
	{
		this.custName = custName;
	}
	
	/**
	  *	促销id
	  */
	public java.lang.Integer getPromotionId() 
	{
		return promotionId;
	}
	
	/**
	  *	促销id
	  */
	public void setPromotionId(java.lang.Integer promotionId) 
	{
		this.promotionId = promotionId;
	}
	
	/**
	  *	用户使用该促销的次数
	  */
	public java.lang.Integer getUseNum() 
	{
		return useNum;
	}
	
	/**
	  *	用户使用该促销的次数
	  */
	public void setUseNum(java.lang.Integer useNum) 
	{
		this.useNum = useNum;
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
	
	public String toString()
	{
		return "OrderPromotionHistory [" + 
					"id=" + id + 
					", custId=" + custId + 
					", custName=" + custName + 
					", promotionId=" + promotionId + 
					", useNum=" + useNum + 
					", createTime=" + createTime + 
					", createUser=" + createUser + 
					", updateTime=" + updateTime + 
					", updateUser=" + updateUser + 
				"]";
	}
}

