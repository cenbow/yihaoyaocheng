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
package com.yyw.yhyc.order.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderIssuedLog;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderIssuedLogFacade;
import com.yyw.yhyc.order.service.OrderIssuedLogService;

@Service("orderIssuedLogFacade")
public class OrderIssuedLogFacadeImpl implements OrderIssuedLogFacade {

	private OrderIssuedLogService orderIssuedLogService;
	
	@Autowired
	public void setOrderIssuedLogService(OrderIssuedLogService orderIssuedLogService)
	{
		this.orderIssuedLogService = orderIssuedLogService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedLog getByPK(Integer primaryKey) throws Exception
	{
		return orderIssuedLogService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedLog> list() throws Exception
	{
		return orderIssuedLogService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedLog> listByProperty(OrderIssuedLog orderIssuedLog)
			throws Exception
	{
		return orderIssuedLogService.listByProperty(orderIssuedLog);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedLog> listPaginationByProperty(Pagination<OrderIssuedLog> pagination, OrderIssuedLog orderIssuedLog)
			throws Exception
	{
		return orderIssuedLogService.listPaginationByProperty(pagination, orderIssuedLog);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return orderIssuedLogService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		orderIssuedLogService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogService.deleteByProperty(orderIssuedLog);
	}

	/**
	 * 保存记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedLog orderIssuedLog) throws Exception
	{
		orderIssuedLogService.save(orderIssuedLog);
	}

	/**
	 * 更新记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogService.update(orderIssuedLog);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogService.findByCount(orderIssuedLog);
	}
}
