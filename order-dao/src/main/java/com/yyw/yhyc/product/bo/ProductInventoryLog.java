/**
 * Created By: XI
 * Created On: 2016-8-29 11:24:18
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.bo;

import com.yyw.yhyc.bo.Model;

public class ProductInventoryLog extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private Integer id;

	/**
	  *	1 添加库存(初始) 2 修改库存 3 冻结  4 扣减  5 释放
	  */
	private Integer logType;

	/**
	  *	spu 编码
	  */
	private String spuCode;

	/**
	  *	
	  */
	private String productName;

	/**
	  *	
	  */
	private Integer productCount;

	/**
	  *	商品供应类型  1 生产厂家 2 供应商
	  */
	private Integer supplyType;

	/**
	  *	供应商ID
	  */
	private Integer supplyId;

	/**
	  *	供应商名称
	  */
	private String supplyName;

	/**
	  *	记录创建者
	  */
	private String createUser;

	/**
	  *	记录生成时间
	  */
	private String createTime;

	/**
	  *	记录更新者
	  */
	private String updateUser;

	/**
	  *	记录更新时间
	  */
	private String updateTime;

	/**
	  *	备注
	  */
	private String remark;

	/**
	  *	
	  */
	public Integer getId()
	{
		return id;
	}
	
	/**
	  *	
	  */
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	/**
	  *	1 冻结  2 扣减  3 释放
	  */
	public Integer getLogType()
	{
		return logType;
	}
	
	/**
	  *	1 冻结  2 扣减  3 释放
	  */
	public void setLogType(Integer logType)
	{
		this.logType = logType;
	}
	
	/**
	  *	spu 编码
	  */
	public String getSpuCode()
	{
		return spuCode;
	}
	
	/**
	  *	spu 编码
	  */
	public void setSpuCode(String spuCode)
	{
		this.spuCode = spuCode;
	}
	
	/**
	  *	
	  */
	public String getProductName()
	{
		return productName;
	}
	
	/**
	  *	
	  */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	/**
	  *	
	  */
	public Integer getProductCount()
	{
		return productCount;
	}
	
	/**
	  *	
	  */
	public void setProductCount(Integer productCount)
	{
		this.productCount = productCount;
	}
	
	/**
	  *	商品供应类型  1 生产厂家 2 供应商
	  */
	public Integer getSupplyType()
	{
		return supplyType;
	}
	
	/**
	  *	商品供应类型  1 生产厂家 2 供应商
	  */
	public void setSupplyType(Integer supplyType)
	{
		this.supplyType = supplyType;
	}
	
	/**
	  *	供应商ID
	  */
	public Integer getSupplyId()
	{
		return supplyId;
	}
	
	/**
	  *	供应商ID
	  */
	public void setSupplyId(Integer supplyId)
	{
		this.supplyId = supplyId;
	}
	
	/**
	  *	供应商名称
	  */
	public String getSupplyName()
	{
		return supplyName;
	}
	
	/**
	  *	供应商名称
	  */
	public void setSupplyName(String supplyName)
	{
		this.supplyName = supplyName;
	}
	
	/**
	  *	记录创建者
	  */
	public String getCreateUser()
	{
		return createUser;
	}
	
	/**
	  *	记录创建者
	  */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	
	/**
	  *	记录生成时间
	  */
	public String getCreateTime()
	{
		return createTime;
	}
	
	/**
	  *	记录生成时间
	  */
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	/**
	  *	记录更新者
	  */
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	/**
	  *	记录更新者
	  */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	记录更新时间
	  */
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	/**
	  *	记录更新时间
	  */
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	备注
	  */
	public String getRemark()
	{
		return remark;
	}
	
	/**
	  *	备注
	  */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	public String toString()
	{
		return "ProductInventoryLog [" + 
					"id=" + id + 
					", logType=" + logType + 
					", spuCode=" + spuCode + 
					", productName=" + productName + 
					", productCount=" + productCount + 
					", supplyType=" + supplyType + 
					", supplyId=" + supplyId + 
					", supplyName=" + supplyName + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
					", remark=" + remark + 
				"]";
	}
}

