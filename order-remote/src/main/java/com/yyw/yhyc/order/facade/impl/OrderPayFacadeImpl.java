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

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderPayFacade;
import com.yyw.yhyc.order.service.OrderPayService;

@Service("orderPayFacade")
public class OrderPayFacadeImpl implements OrderPayFacade {

	private OrderPayService orderPayService;
	
	@Autowired
	public void setOrderPayService(OrderPayService orderPayService)
	{
		this.orderPayService = orderPayService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderPay getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderPayService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> list() throws Exception
	{
		return orderPayService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> listByProperty(OrderPay orderPay)
			throws Exception
	{
		return orderPayService.listByProperty(orderPay);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderPay> listPaginationByProperty(Pagination<OrderPay> pagination, OrderPay orderPay)
			throws Exception
	{
		return orderPayService.listPaginationByProperty(pagination, orderPay);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderPayService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderPayService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderPay orderPay) throws Exception
	{
		return orderPayService.deleteByProperty(orderPay);
	}

	/**
	 * 保存记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public void save(OrderPay orderPay) throws Exception
	{
		orderPayService.save(orderPay);
	}

	/**
	 * 更新记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int update(OrderPay orderPay) throws Exception
	{
		return orderPayService.update(orderPay);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderPay orderPay) throws Exception
	{
		return orderPayService.findByCount(orderPay);
	}
}
