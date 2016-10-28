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
package com.yyw.yhyc.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderIssuedLog;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderIssuedLogMapper;

@Service("orderIssuedLogService")
public class OrderIssuedLogService {

	private OrderIssuedLogMapper	orderIssuedLogMapper;

	@Autowired
	public void setOrderIssuedLogMapper(OrderIssuedLogMapper orderIssuedLogMapper)
	{
		this.orderIssuedLogMapper = orderIssuedLogMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedLog getByPK(Integer primaryKey) throws Exception
	{
		return orderIssuedLogMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedLog> list() throws Exception
	{
		return orderIssuedLogMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedLog> listByProperty(OrderIssuedLog orderIssuedLog)
			throws Exception
	{
		return orderIssuedLogMapper.listByProperty(orderIssuedLog);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedLog> listPaginationByProperty(Pagination<OrderIssuedLog> pagination, OrderIssuedLog orderIssuedLog) throws Exception
	{
		List<OrderIssuedLog> list = orderIssuedLogMapper.listPaginationByProperty(pagination, orderIssuedLog);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return orderIssuedLogMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		orderIssuedLogMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogMapper.deleteByProperty(orderIssuedLog);
	}

	/**
	 * 保存记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedLog orderIssuedLog) throws Exception
	{
		orderIssuedLogMapper.save(orderIssuedLog);
	}

	/**
	 * 更新记录
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogMapper.update(orderIssuedLog);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderIssuedLog
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedLog orderIssuedLog) throws Exception
	{
		return orderIssuedLogMapper.findByCount(orderIssuedLog);
	}
}