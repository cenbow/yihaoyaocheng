/**
 * Created By: XI
 * Created On: 2016-8-29 11:23:12
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.bo;

import com.yyw.yhyc.bo.Model;

public class ProductInventory extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键
	  */
	private Integer id;

	/**
	  *	spu 编码
	  */
	private String spuCode;

	/**
	  *	当前库存
	  */
	private Integer currentInventory;

	/**
	  *	预警库存
	  */
	private Integer warningInventory;

	/**
	  *	冻结库存
	  */
	private Integer blockedInventory;

	/**
	  *	前端可售库存
	  */
	private Integer frontInventory;


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
	  *	主键
	  */
	public Integer getId()
	{
		return id;
	}
	
	/**
	  *	主键
	  */
	public void setId(Integer id)
	{
		this.id = id;
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
	  *	当前库存
	  */
	public Integer getCurrentInventory()
	{
		return currentInventory;
	}
	
	/**
	  *	当前库存
	  */
	public void setCurrentInventory(Integer currentInventory)
	{
		this.currentInventory = currentInventory;
	}
	
	/**
	  *	预警库存
	  */
	public Integer getWarningInventory()
	{
		return warningInventory;
	}
	
	/**
	  *	预警库存
	  */
	public void setWarningInventory(Integer warningInventory)
	{
		this.warningInventory = warningInventory;
	}
	
	/**
	  *	冻结库存
	  */
	public Integer getBlockedInventory()
	{
		return blockedInventory;
	}
	
	/**
	  *	冻结库存
	  */
	public void setBlockedInventory(Integer blockedInventory)
	{
		this.blockedInventory = blockedInventory;
	}
	
	/**
	  *	前端可售库存
	  */
	public Integer getFrontInventory()
	{
		return frontInventory;
	}
	
	/**
	  *	前端可售库存
	  */
	public void setFrontInventory(Integer frontInventory)
	{
		this.frontInventory = frontInventory;
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
		return "ProductInventory [" + 
					"id=" + id + 
					", spuCode=" + spuCode + 
					", currentInventory=" + currentInventory + 
					", warningInventory=" + warningInventory + 
					", blockedInventory=" + blockedInventory + 
					", frontInventory=" + frontInventory +
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

