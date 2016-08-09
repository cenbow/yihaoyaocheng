/**
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.List;

import com.yyw.yhyc.order.dto.OrderExceptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;

@Service("orderExceptionService")
public class OrderExceptionService {

	private OrderExceptionMapper	orderExceptionMapper;

	@Autowired
	public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper)
	{
		this.orderExceptionMapper = orderExceptionMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderException getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderExceptionMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> list() throws Exception
	{
		return orderExceptionMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> listByProperty(OrderException orderException)
			throws Exception
	{
		return orderExceptionMapper.listByProperty(orderException);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException) throws Exception
	{
		List<OrderException> list = orderExceptionMapper.listPaginationByProperty(pagination, orderException);

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
		return orderExceptionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderExceptionMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.deleteByProperty(orderException);
	}

	/**
	 * 保存记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderException orderException) throws Exception
	{
		orderExceptionMapper.save(orderException);
	}

	/**
	 * 更新记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.update(orderException);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.findByCount(orderException);
	}

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		return orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
	}
}