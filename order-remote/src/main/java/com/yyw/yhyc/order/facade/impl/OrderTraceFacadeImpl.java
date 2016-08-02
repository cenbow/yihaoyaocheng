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

import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderTraceFacade;
import com.yyw.yhyc.order.service.OrderTraceService;

@Service("orderTraceFacade")
public class OrderTraceFacadeImpl implements OrderTraceFacade {

	private OrderTraceService orderTraceService;
	
	@Autowired
	public void setOrderTraceService(OrderTraceService orderTraceService)
	{
		this.orderTraceService = orderTraceService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderTrace getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderTraceService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderTrace> list() throws Exception
	{
		return orderTraceService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderTrace> listByProperty(OrderTrace orderTrace)
			throws Exception
	{
		return orderTraceService.listByProperty(orderTrace);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderTrace> listPaginationByProperty(Pagination<OrderTrace> pagination, OrderTrace orderTrace)
			throws Exception
	{
		return orderTraceService.listPaginationByProperty(pagination, orderTrace);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderTraceService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderTraceService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderTrace orderTrace) throws Exception
	{
		return orderTraceService.deleteByProperty(orderTrace);
	}

	/**
	 * 保存记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public void save(OrderTrace orderTrace) throws Exception
	{
		orderTraceService.save(orderTrace);
	}

	/**
	 * 更新记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int update(OrderTrace orderTrace) throws Exception
	{
		return orderTraceService.update(orderTrace);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderTrace orderTrace) throws Exception
	{
		return orderTraceService.findByCount(orderTrace);
	}
}
