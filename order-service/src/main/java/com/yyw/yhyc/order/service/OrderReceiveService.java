/**
 * Created By: XI
 * Created On: 2016-11-21 10:34:21
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

import com.yyw.yhyc.order.bo.OrderReceive;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderReceiveMapper;

@Service("orderReceiveService")
public class OrderReceiveService {

	private OrderReceiveMapper	orderReceiveMapper;

	@Autowired
	public void setOrderReceiveMapper(OrderReceiveMapper orderReceiveMapper)
	{
		this.orderReceiveMapper = orderReceiveMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderReceive getByPK(java.lang.String primaryKey) throws Exception
	{
		return orderReceiveMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReceive> list() throws Exception
	{
		return orderReceiveMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReceive> listByProperty(OrderReceive orderReceive)
			throws Exception
	{
		return orderReceiveMapper.listByProperty(orderReceive);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderReceive> listPaginationByProperty(Pagination<OrderReceive> pagination, OrderReceive orderReceive) throws Exception
	{
		List<OrderReceive> list = orderReceiveMapper.listPaginationByProperty(pagination, orderReceive);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.String primaryKey) throws Exception
	{
		return orderReceiveMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.String> primaryKeys) throws Exception
	{
		orderReceiveMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderReceive
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderReceive orderReceive) throws Exception
	{
		return orderReceiveMapper.deleteByProperty(orderReceive);
	}

	/**
	 * 保存记录
	 * @param orderReceive
	 * @return
	 * @throws Exception
	 */
	public void save(OrderReceive orderReceive) throws Exception
	{
		orderReceiveMapper.save(orderReceive);
	}

	/**
	 * 更新记录
	 * @param orderReceive
	 * @return
	 * @throws Exception
	 */
	public int update(OrderReceive orderReceive) throws Exception
	{
		return orderReceiveMapper.update(orderReceive);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderReceive
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderReceive orderReceive) throws Exception
	{
		return orderReceiveMapper.findByCount(orderReceive);
	}
}