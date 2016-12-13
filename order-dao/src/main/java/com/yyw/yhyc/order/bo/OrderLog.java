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
	private Long id;

	/**
	  *	登陆客户编码
	  */
	private Integer userid;

	/**
	  *	通用唯一标识符
	  */
	private String uuid;

	/**
	  *	全球唯一编码
	  */
	private String guid;

	/**
	  *	订单编号
	  */
	private String orderCode;

	/**
	  *	请求IP
	  */
	private String clientIp;

	/**
	  *	页面的来源URL
	  */
	private String referer;

	/**
	  *	用户代理
	  */
	private String userAgent;

	/**
	  *	运行硬件平台
	  */
	private String platform;

	/**
	  *	1登录、2下单、3发货、4收货
	  */
	private Integer type;

	/**
	  *	主键
	  */
	public Long getId()
	{
		return id;
	}

	/**
	  *	主键
	  */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	  *	登陆客户编码
	  */
	public Integer getUserid()
	{
		return userid;
	}

	/**
	  *	登陆客户编码
	  */
	public void setUserid(Integer userid)
	{
		this.userid = userid;
	}

	/**
	  *	通用唯一标识符
	  */
	public String getUuid()
	{
		return uuid;
	}

	/**
	  *	通用唯一标识符
	  */
	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	/**
	  *	全球唯一编码
	  */
	public String getGuid()
	{
		return guid;
	}

	/**
	  *	全球唯一编码
	  */
	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	/**
	  *	订单编号
	  */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	  *	订单编号
	  */
	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	  *	请求IP
	  */
	public String getClientIp()
	{
		return clientIp;
	}

	/**
	  *	请求IP
	  */
	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	/**
	  *	页面的来源URL
	  */
	public String getReferer()
	{
		return referer;
	}

	/**
	  *	页面的来源URL
	  */
	public void setReferer(String referer)
	{
		this.referer = referer;
	}

	/**
	  *	用户代理
	  */
	public String getUserAgent()
	{
		return userAgent;
	}

	/**
	  *	用户代理
	  */
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	/**
	  *	运行硬件平台
	  */
	public String getPlatform()
	{
		return platform;
	}

	/**
	  *	运行硬件平台
	  */
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}

	/**
	  *	1登录、2下单、3发货、4收货
	  */
	public Integer getType()
	{
		return type;
	}

	/**
	  *	1登录、2下单、3发货、4收货
	  */
	public void setType(Integer type)
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

