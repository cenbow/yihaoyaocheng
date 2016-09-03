/**
 * Created By: XI
 * Created On: 2016-8-30 11:47:20
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.bo;

import com.yyw.yhyc.bo.Model;

public class ProductAudit extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键ID
	  */
	private Integer id;

	/**
	  *	spu编码
	  */
	private String spuCode;

	/**
	  *	企业编码
	  */
	private Integer sellerCode;

	/**
	  *	会员名称
	  */
	private String sellerName;

	/**
	  *	是否渠道商品(0否，1是)
	  */
	private String isChannel;

	/**
	  *	本公司产品编码
	  */
	private String productcodeCompany;

	/**
	  *	最小拆零包装
	  */
	private Integer minimumPacking;

	/**
	  *	大包装
	  */
	private Integer bigPacking;

	/**
	  *	审核状态(0待审核,1审核通过,2审核不通过)
	  */
	private String auditStatus;

	/**
	  *	渠道价
	  */
	private java.math.BigDecimal channelPrice;

	/**
	  *	更新人（操作者）
	  */
	private String updateStaff;

	/**
	  *	更新时间
	  */
	private String updateDate;

	/**
	  *	主键ID
	  */
	public Integer getId()
	{
		return id;
	}
	
	/**
	  *	主键ID
	  */
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	/**
	  *	spu编码
	  */
	public String getSpuCode()
	{
		return spuCode;
	}
	
	/**
	  *	spu编码
	  */
	public void setSpuCode(String spuCode)
	{
		this.spuCode = spuCode;
	}
	
	/**
	  *	企业编码
	  */
	public Integer getSellerCode()
	{
		return sellerCode;
	}
	
	/**
	  *	企业编码
	  */
	public void setSellerCode(Integer sellerCode)
	{
		this.sellerCode = sellerCode;
	}
	
	/**
	  *	会员名称
	  */
	public String getSellerName()
	{
		return sellerName;
	}
	
	/**
	  *	会员名称
	  */
	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}
	
	/**
	  *	是否渠道商品(0否，1是)
	  */
	public String getIsChannel()
	{
		return isChannel;
	}
	
	/**
	  *	是否渠道商品(0否，1是)
	  */
	public void setIsChannel(String isChannel)
	{
		this.isChannel = isChannel;
	}
	
	/**
	  *	本公司产品编码
	  */
	public String getProductcodeCompany()
	{
		return productcodeCompany;
	}
	
	/**
	  *	本公司产品编码
	  */
	public void setProductcodeCompany(String productcodeCompany)
	{
		this.productcodeCompany = productcodeCompany;
	}
	
	/**
	  *	最小拆零包装
	  */
	public Integer getMinimumPacking()
	{
		return minimumPacking;
	}
	
	/**
	  *	最小拆零包装
	  */
	public void setMinimumPacking(Integer minimumPacking)
	{
		this.minimumPacking = minimumPacking;
	}
	
	/**
	  *	大包装
	  */
	public Integer getBigPacking()
	{
		return bigPacking;
	}
	
	/**
	  *	大包装
	  */
	public void setBigPacking(Integer bigPacking)
	{
		this.bigPacking = bigPacking;
	}
	
	/**
	  *	审核状态(0待审核,1审核通过,2审核不通过)
	  */
	public String getAuditStatus()
	{
		return auditStatus;
	}
	
	/**
	  *	审核状态(0待审核,1审核通过,2审核不通过)
	  */
	public void setAuditStatus(String auditStatus)
	{
		this.auditStatus = auditStatus;
	}
	
	/**
	  *	渠道价
	  */
	public java.math.BigDecimal getChannelPrice() 
	{
		return channelPrice;
	}
	
	/**
	  *	渠道价
	  */
	public void setChannelPrice(java.math.BigDecimal channelPrice) 
	{
		this.channelPrice = channelPrice;
	}
	
	/**
	  *	更新人（操作者）
	  */
	public String getUpdateStaff()
	{
		return updateStaff;
	}
	
	/**
	  *	更新人（操作者）
	  */
	public void setUpdateStaff(String updateStaff)
	{
		this.updateStaff = updateStaff;
	}
	
	/**
	  *	更新时间
	  */
	public String getUpdateDate()
	{
		return updateDate;
	}
	
	/**
	  *	更新时间
	  */
	public void setUpdateDate(String updateDate)
	{
		this.updateDate = updateDate;
	}
	
	public String toString()
	{
		return "ProductAudit [" + 
					"id=" + id + 
					", spuCode=" + spuCode + 
					", sellerCode=" + sellerCode + 
					", sellerName=" + sellerName + 
					", isChannel=" + isChannel + 
					", productcodeCompany=" + productcodeCompany + 
					", minimumPacking=" + minimumPacking + 
					", bigPacking=" + bigPacking + 
					", auditStatus=" + auditStatus + 
					", channelPrice=" + channelPrice + 
					", updateStaff=" + updateStaff + 
					", updateDate=" + updateDate + 
				"]";
	}
}

