/**
 * Created By: XI
 * Created On: 2016-12-9 10:42:31
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderLog extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键
	  */
	private java.lang.Long id;

	/**
	  *	登陆客户编码
	  */
	private java.lang.Integer userid;

	/**
	  *	通用唯一标识符
	  */
	private java.lang.String uuid;

	/**
	  *	全球唯一编码
	  */
	private java.lang.String guid;

	/**
	  *	订单编号
	  */
	private java.lang.String orderCode;

	/**
	  *	请求IP
	  */
	private java.lang.String clientIp;

	/**
	  *	页面的来源URL
	  */
	private java.lang.String referer;

	/**
	  *	用户代理
	  */
	private java.lang.String userAgent;

	/**
	  *	运行硬件平台
	  */
	private java.lang.String platform;

	/**
	  *	1登录、2下单、3发货、4收货
	  */
	private java.lang.Integer type;

	/**
	  *	主键
	  */
	public java.lang.Long getId() 
	{
		return id;
	}
	
	/**
	  *	主键
	  */
	public void setId(java.lang.Long id) 
	{
		this.id = id;
	}
	
	/**
	  *	登陆客户编码
	  */
	public java.lang.Integer getUserid() 
	{
		return userid;
	}
	
	/**
	  *	登陆客户编码
	  */
	public void setUserid(java.lang.Integer userid) 
	{
		this.userid = userid;
	}
	
	/**
	  *	通用唯一标识符
	  */
	public java.lang.String getUuid() 
	{
		return uuid;
	}
	
	/**
	  *	通用唯一标识符
	  */
	public void setUuid(java.lang.String uuid) 
	{
		this.uuid = uuid;
	}
	
	/**
	  *	全球唯一编码
	  */
	public java.lang.String getGuid() 
	{
		return guid;
	}
	
	/**
	  *	全球唯一编码
	  */
	public void setGuid(java.lang.String guid) 
	{
		this.guid = guid;
	}
	
	/**
	  *	订单编号
	  */
	public java.lang.String getOrderCode() 
	{
		return orderCode;
	}
	
	/**
	  *	订单编号
	  */
	public void setOrderCode(java.lang.String orderCode) 
	{
		this.orderCode = orderCode;
	}
	
	/**
	  *	请求IP
	  */
	public java.lang.String getClientIp() 
	{
		return clientIp;
	}
	
	/**
	  *	请求IP
	  */
	public void setClientIp(java.lang.String clientIp) 
	{
		this.clientIp = clientIp;
	}
	
	/**
	  *	页面的来源URL
	  */
	public java.lang.String getReferer() 
	{
		return referer;
	}
	
	/**
	  *	页面的来源URL
	  */
	public void setReferer(java.lang.String referer) 
	{
		this.referer = referer;
	}
	
	/**
	  *	用户代理
	  */
	public java.lang.String getUserAgent() 
	{
		return userAgent;
	}
	
	/**
	  *	用户代理
	  */
	public void setUserAgent(java.lang.String userAgent) 
	{
		this.userAgent = userAgent;
	}
	
	/**
	  *	运行硬件平台
	  */
	public java.lang.String getPlatform() 
	{
		return platform;
	}
	
	/**
	  *	运行硬件平台
	  */
	public void setPlatform(java.lang.String platform) 
	{
		this.platform = platform;
	}
	
	/**
	  *	1登录、2下单、3发货、4收货
	  */
	public java.lang.Integer getType() 
	{
		return type;
	}
	
	/**
	  *	1登录、2下单、3发货、4收货
	  */
	public void setType(java.lang.Integer type) 
	{
		this.type = type;
	}
	
	public String toString()
	{
		return "OrderLog [" + 
					"id=" + id + 
					", userid=" + userid + 
					", uuid=" + uuid + 
					", guid=" + guid + 
					", orderCode=" + orderCode + 
					", clientIp=" + clientIp + 
					", referer=" + referer + 
					", userAgent=" + userAgent + 
					", platform=" + platform + 
					", type=" + type + 
				"]";
	}
}

