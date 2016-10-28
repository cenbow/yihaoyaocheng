/**
 * Created By: XI
 * Created On: 2016-10-25 10:38:15
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderIssuedLog extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private Integer id;

	/**
	  *	订单编码
	  */
	private String flowId;

	/**
	  *	操作名称
	  */
	private String operateName;

	/**
	  *	操作人
	  */
	private String operator;

	/**
	  *	操作时间
	  */
	private String operateTime;

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
	  *	订单编码
	  */
	public String getFlowId()
	{
		return flowId;
	}
	
	/**
	  *	订单编码
	  */
	public void setFlowId(String flowId)
	{
		this.flowId = flowId;
	}
	
	/**
	  *	操作名称
	  */
	public String getOperateName()
	{
		return operateName;
	}
	
	/**
	  *	操作名称
	  */
	public void setOperateName(String operateName)
	{
		this.operateName = operateName;
	}
	
	/**
	  *	操作人
	  */
	public String getOperator()
	{
		return operator;
	}
	
	/**
	  *	操作人
	  */
	public void setOperator(String operator)
	{
		this.operator = operator;
	}
	
	/**
	  *	操作时间
	  */
	public String getOperateTime()
	{
		return operateTime;
	}
	
	/**
	  *	操作时间
	  */
	public void setOperateTime(String operateTime)
	{
		this.operateTime = operateTime;
	}
	
	public String toString()
	{
		return "OrderIssuedLog [" + 
					"id=" + id + 
					", flowId=" + flowId + 
					", operateName=" + operateName + 
					", operator=" + operator + 
					", operateTime=" + operateTime + 
				"]";
	}
}

