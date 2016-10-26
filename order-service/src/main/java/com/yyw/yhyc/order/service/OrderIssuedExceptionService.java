/**
 * Created By: XI
 * Created On: 2016-10-24 15:52:00
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

import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderIssuedExceptionMapper;

@Service("orderIssuedExceptionService")
public class OrderIssuedExceptionService {

	private OrderIssuedExceptionMapper	orderIssuedExceptionMapper;

	@Autowired
	public void setOrderIssuedExceptionMapper(OrderIssuedExceptionMapper orderIssuedExceptionMapper)
	{
		this.orderIssuedExceptionMapper = orderIssuedExceptionMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedException getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedExceptionMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> list() throws Exception
	{
		return orderIssuedExceptionMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> listByProperty(OrderIssuedException orderIssuedException)
			throws Exception
	{
		return orderIssuedExceptionMapper.listByProperty(orderIssuedException);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedException> listPaginationByProperty(Pagination<OrderIssuedException> pagination, OrderIssuedException orderIssuedException) throws Exception
	{
		List<OrderIssuedException> list = orderIssuedExceptionMapper.listPaginationByProperty(pagination, orderIssuedException);

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
		return orderIssuedExceptionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderIssuedExceptionMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionMapper.deleteByProperty(orderIssuedException);
	}

	/**
	 * 保存记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedException orderIssuedException) throws Exception
	{
		orderIssuedExceptionMapper.save(orderIssuedException);
	}

	/**
	 * 更新记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionMapper.update(orderIssuedException);
	}
	/**
	 * 更新记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int updateBySelective(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionMapper.updateBySelective(orderIssuedException);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionMapper.findByCount(orderIssuedException);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedExceptionDto> listPaginationByPropertyEx(Pagination<OrderIssuedExceptionDto> pagination, OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception
	{
		List<OrderIssuedExceptionDto> list = orderIssuedExceptionMapper.listPaginationByPropertyEx(pagination, orderIssuedExceptionDto);

		pagination.setResultList(list);

		return pagination;
	}
}