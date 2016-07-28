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

import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderReturnFacade;
import com.yyw.yhyc.order.service.OrderReturnService;

@Service("orderReturnFacade")
public class OrderReturnFacadeImpl implements OrderReturnFacade {

	private OrderReturnService orderReturnService;
	
	@Autowired
	public void setOrderReturnService(OrderReturnService orderReturnService)
	{
		this.orderReturnService = orderReturnService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderReturn getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderReturnService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> list() throws Exception
	{
		return orderReturnService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> listByProperty(OrderReturn orderReturn)
			throws Exception
	{
		return orderReturnService.listByProperty(orderReturn);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderReturn> listPaginationByProperty(Pagination<OrderReturn> pagination, OrderReturn orderReturn)
			throws Exception
	{
		return orderReturnService.listPaginationByProperty(pagination, orderReturn);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderReturnService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderReturnService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderReturn orderReturn) throws Exception
	{
		return orderReturnService.deleteByProperty(orderReturn);
	}

	/**
	 * 保存记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public void save(OrderReturn orderReturn) throws Exception
	{
		orderReturnService.save(orderReturn);
	}

	/**
	 * 更新记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int update(OrderReturn orderReturn) throws Exception
	{
		return orderReturnService.update(orderReturn);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderReturn orderReturn) throws Exception
	{
		return orderReturnService.findByCount(orderReturn);
	}
}
