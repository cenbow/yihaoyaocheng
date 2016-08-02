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

import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderDetailFacade;
import com.yyw.yhyc.order.service.OrderDetailService;

@Service("orderDetailFacade")
public class OrderDetailFacadeImpl implements OrderDetailFacade {

	private OrderDetailService orderDetailService;
	
	@Autowired
	public void setOrderDetailService(OrderDetailService orderDetailService)
	{
		this.orderDetailService = orderDetailService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDetail getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDetailService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDetail> list() throws Exception
	{
		return orderDetailService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDetail> listByProperty(OrderDetail orderDetail)
			throws Exception
	{
		return orderDetailService.listByProperty(orderDetail);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDetail> listPaginationByProperty(Pagination<OrderDetail> pagination, OrderDetail orderDetail)
			throws Exception
	{
		return orderDetailService.listPaginationByProperty(pagination, orderDetail);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDetailService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDetailService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderDetail
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDetail orderDetail) throws Exception
	{
		return orderDetailService.deleteByProperty(orderDetail);
	}

	/**
	 * 保存记录
	 * @param orderDetail
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDetail orderDetail) throws Exception
	{
		orderDetailService.save(orderDetail);
	}

	/**
	 * 更新记录
	 * @param orderDetail
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDetail orderDetail) throws Exception
	{
		return orderDetailService.update(orderDetail);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDetail
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDetail orderDetail) throws Exception
	{
		return orderDetailService.findByCount(orderDetail);
	}
}
