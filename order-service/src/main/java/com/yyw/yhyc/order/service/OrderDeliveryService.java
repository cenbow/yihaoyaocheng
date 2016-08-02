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
package com.yyw.yhyc.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;

@Service("orderDeliveryService")
public class OrderDeliveryService {

	private OrderDeliveryMapper	orderDeliveryMapper;

	@Autowired
	public void setOrderDeliveryMapper(OrderDeliveryMapper orderDeliveryMapper)
	{
		this.orderDeliveryMapper = orderDeliveryMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDelivery getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> list() throws Exception
	{
		return orderDeliveryMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> listByProperty(OrderDelivery orderDelivery)
			throws Exception
	{
		return orderDeliveryMapper.listByProperty(orderDelivery);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDelivery> listPaginationByProperty(Pagination<OrderDelivery> pagination, OrderDelivery orderDelivery) throws Exception
	{
		List<OrderDelivery> list = orderDeliveryMapper.listPaginationByProperty(pagination, orderDelivery);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDeliveryMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.deleteByProperty(orderDelivery);
	}

	/**
	 * 保存记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDelivery orderDelivery) throws Exception
	{
		orderDeliveryMapper.save(orderDelivery);
	}

	/**
	 * 更新记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.update(orderDelivery);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.findByCount(orderDelivery);
	}
}