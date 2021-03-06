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
package com.yyw.yhyc.order.facade.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.order.facade.OrderIssuedExceptionFacade;
import com.yyw.yhyc.order.service.OrderIssuedExceptionService;

@Service("orderIssuedExceptionFacade")
public class OrderIssuedExceptionFacadeImpl implements OrderIssuedExceptionFacade {

	 private static final Logger logger = LoggerFactory.getLogger(OrderIssuedExceptionFacadeImpl.class);
	 
	private OrderIssuedExceptionService orderIssuedExceptionService;
	 
	@Autowired
	public void setOrderIssuedExceptionService(OrderIssuedExceptionService orderIssuedExceptionService)
	{
		this.orderIssuedExceptionService = orderIssuedExceptionService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedException getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedExceptionService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> list() throws Exception
	{
		return orderIssuedExceptionService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> listByProperty(OrderIssuedException orderIssuedException)
			throws Exception
	{
		return orderIssuedExceptionService.listByProperty(orderIssuedException);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedException> listPaginationByProperty(Pagination<OrderIssuedException> pagination, OrderIssuedException orderIssuedException)
			throws Exception
	{
		return orderIssuedExceptionService.listPaginationByProperty(pagination, orderIssuedException);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedExceptionService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderIssuedExceptionService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionService.deleteByProperty(orderIssuedException);
	}

	/**
	 * 保存记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedException orderIssuedException) throws Exception
	{
		orderIssuedExceptionService.save(orderIssuedException);
	}

	/**
	 * 更新记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionService.update(orderIssuedException);
	}
	 
	/**
	 * 根据条件查询记录条数
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedException orderIssuedException) throws Exception
	{
		return orderIssuedExceptionService.findByCount(orderIssuedException);
	}

	@Override
	public Map<String, String> updateOrderIssued(String flowId,String operator) {
		logger.info("****************调用订单下发接口*******************");
		return orderIssuedExceptionService.updateOrderIssued(flowId,operator);
	}

	@Override
	public Map<String, String> updateOrderMark(String flowId,String operator) {
		logger.info("****************调用订单标记接口*******************");
		return orderIssuedExceptionService.updateOrderMark(flowId,operator);
	}

	@Override
	public Pagination<OrderIssuedExceptionDto> listPaginationByPropertyEx(
			Pagination<OrderIssuedExceptionDto> pagination,
			OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception {
		logger.info("****************调用异常订单查询接口*******************");
		return orderIssuedExceptionService.listPaginationByPropertyEx(pagination, orderIssuedExceptionDto);
	}
}
