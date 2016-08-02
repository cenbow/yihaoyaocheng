/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
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

import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderCombinedFacade;
import com.yyw.yhyc.order.service.OrderCombinedService;

@Service("orderCombinedFacade")
public class OrderCombinedFacadeImpl implements OrderCombinedFacade {

	private OrderCombinedService orderCombinedService;
	
	@Autowired
	public void setOrderCombinedService(OrderCombinedService orderCombinedService)
	{
		this.orderCombinedService = orderCombinedService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderCombined getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderCombinedService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderCombined> list() throws Exception
	{
		return orderCombinedService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderCombined> listByProperty(OrderCombined orderCombined)
			throws Exception
	{
		return orderCombinedService.listByProperty(orderCombined);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderCombined> listPaginationByProperty(Pagination<OrderCombined> pagination, OrderCombined orderCombined)
			throws Exception
	{
		return orderCombinedService.listPaginationByProperty(pagination, orderCombined);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderCombinedService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderCombinedService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderCombined orderCombined) throws Exception
	{
		return orderCombinedService.deleteByProperty(orderCombined);
	}

	/**
	 * 保存记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public void save(OrderCombined orderCombined) throws Exception
	{
		orderCombinedService.save(orderCombined);
	}

	/**
	 * 更新记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int update(OrderCombined orderCombined) throws Exception
	{
		return orderCombinedService.update(orderCombined);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderCombined orderCombined) throws Exception
	{
		return orderCombinedService.findByCount(orderCombined);
	}
}
