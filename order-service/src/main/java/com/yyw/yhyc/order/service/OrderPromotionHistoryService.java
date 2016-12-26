/**
 * Created By: XI
 * Created On: 2016-12-8 9:28:10
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

import com.yyw.yhyc.order.bo.OrderPromotionHistory;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderPromotionHistoryMapper;

@Service("orderPromotionHistoryService")
public class OrderPromotionHistoryService {

	private OrderPromotionHistoryMapper	orderPromotionHistoryMapper;

	@Autowired
	public void setOrderPromotionHistoryMapper(OrderPromotionHistoryMapper orderPromotionHistoryMapper)
	{
		this.orderPromotionHistoryMapper = orderPromotionHistoryMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderPromotionHistory getByPK(java.lang.Long primaryKey) throws Exception
	{
		return orderPromotionHistoryMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPromotionHistory> list() throws Exception
	{
		return orderPromotionHistoryMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPromotionHistory> listByProperty(OrderPromotionHistory orderPromotionHistory)
			throws Exception
	{
		return orderPromotionHistoryMapper.listByProperty(orderPromotionHistory);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderPromotionHistory> listPaginationByProperty(Pagination<OrderPromotionHistory> pagination, OrderPromotionHistory orderPromotionHistory) throws Exception
	{
		List<OrderPromotionHistory> list = orderPromotionHistoryMapper.listPaginationByProperty(pagination, orderPromotionHistory);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Long primaryKey) throws Exception
	{
		return orderPromotionHistoryMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Long> primaryKeys) throws Exception
	{
		orderPromotionHistoryMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderPromotionHistory
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderPromotionHistory orderPromotionHistory) throws Exception
	{
		return orderPromotionHistoryMapper.deleteByProperty(orderPromotionHistory);
	}

	/**
	 * 保存记录
	 * @param orderPromotionHistory
	 * @return
	 * @throws Exception
	 */
	public void save(OrderPromotionHistory orderPromotionHistory) throws Exception
	{
		orderPromotionHistoryMapper.save(orderPromotionHistory);
	}

	/**
	 * 更新记录
	 * @param orderPromotionHistory
	 * @return
	 * @throws Exception
	 */
	public int update(OrderPromotionHistory orderPromotionHistory) throws Exception
	{
		return orderPromotionHistoryMapper.update(orderPromotionHistory);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderPromotionHistory
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderPromotionHistory orderPromotionHistory) throws Exception
	{
		return orderPromotionHistoryMapper.findByCount(orderPromotionHistory);
	}
}