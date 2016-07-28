/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderRefund;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderRefundFacade;
import com.yyw.yhyc.order.service.OrderRefundService;

@Service("orderRefundFacade")
public class OrderRefundFacadeImpl implements OrderRefundFacade {

	private OrderRefundService orderRefundService;
	
	@Autowired
	public void setOrderRefundService(OrderRefundService orderRefundService)
	{
		this.orderRefundService = orderRefundService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderRefund getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderRefundService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderRefund> list() throws Exception
	{
		return orderRefundService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderRefund> listByProperty(OrderRefund orderRefund)
			throws Exception
	{
		return orderRefundService.listByProperty(orderRefund);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderRefund> listPaginationByProperty(Pagination<OrderRefund> pagination, OrderRefund orderRefund)
			throws Exception
	{
		return orderRefundService.listPaginationByProperty(pagination, orderRefund);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderRefundService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderRefundService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderRefund
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderRefund orderRefund) throws Exception
	{
		return orderRefundService.deleteByProperty(orderRefund);
	}

	/**
	 * 保存记录
	 * @param orderRefund
	 * @return
	 * @throws Exception
	 */
	public void save(OrderRefund orderRefund) throws Exception
	{
		orderRefundService.save(orderRefund);
	}

	/**
	 * 更新记录
	 * @param orderRefund
	 * @return
	 * @throws Exception
	 */
	public int update(OrderRefund orderRefund) throws Exception
	{
		return orderRefundService.update(orderRefund);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderRefund
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderRefund orderRefund) throws Exception
	{
		return orderRefundService.findByCount(orderRefund);
	}
}
