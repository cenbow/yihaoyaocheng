/**
 * Created By: XI
 * Created On: 2016-9-10 10:28:42
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderIssuedFacade;
import com.yyw.yhyc.order.service.OrderIssuedService;

@Service("orderIssuedFacade")
public class OrderIssuedFacadeImpl implements OrderIssuedFacade {

	private OrderIssuedService orderIssuedService;
	
	@Autowired
	public void setOrderIssuedService(OrderIssuedService orderIssuedService)
	{
		this.orderIssuedService = orderIssuedService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssued getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> list() throws Exception
	{
		return orderIssuedService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> listByProperty(OrderIssued orderIssued)
			throws Exception
	{
		return orderIssuedService.listByProperty(orderIssued);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssued> listPaginationByProperty(Pagination<OrderIssued> pagination, OrderIssued orderIssued)
			throws Exception
	{
		return orderIssuedService.listPaginationByProperty(pagination, orderIssued);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderIssuedService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedService.deleteByProperty(orderIssued);
	}

	/**
	 * 保存记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssued orderIssued) throws Exception
	{
		orderIssuedService.save(orderIssued);
	}

	/**
	 * 更新记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedService.update(orderIssued);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedService.findByCount(orderIssued);
	}

	public Map<String, Object> findOrderIssuedListBySupplyId(Integer supplyId,String payType) throws Exception {
		return orderIssuedService.editOrderIssuedListBySupplyId(supplyId,  payType);
	}

	public Map<String, Object> updateOrderIssuedStatus(List<String> flowList) throws Exception {
		return orderIssuedService.updateOrderIssuedStatus(flowList);
	}

	@Override
	public Map<String, Object> findOrderIssuedListBySupplyAndOrderDate(
			List<Integer> supplyListIds, String startDate, String endDate,
			String orderIdList,String payType) throws Exception {
	
		return this.orderIssuedService.queryOrderIssuedBySupplyIdAndOrderDate(supplyListIds, startDate, endDate, orderIdList,payType);
	}

	@Override
	public Map<String, Object> updateOrderIssuedForWsdl(
			List<OrderIssued> orderIssuedList) throws Exception {
		return this.orderIssuedService.updateOrderIssuedForWsdl(orderIssuedList);
	}
}
