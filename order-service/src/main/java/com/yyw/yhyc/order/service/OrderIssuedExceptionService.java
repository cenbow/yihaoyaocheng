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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderIssuedExceptionMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;

@Service("orderIssuedExceptionService")
public class OrderIssuedExceptionService {
	 private static final Logger logger = LoggerFactory.getLogger(OrderIssuedExceptionService.class);

	private SystemDateMapper systemDateMapper;

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}
	
	private OrderIssuedExceptionMapper	orderIssuedExceptionMapper;

	@Autowired
	public void setOrderIssuedExceptionMapper(OrderIssuedExceptionMapper orderIssuedExceptionMapper)
	{
		this.orderIssuedExceptionMapper = orderIssuedExceptionMapper;
	}
	@Autowired
	private OrderIssuedService orderIssuedService;
	
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
	/**
	 * 订单下发接口，调用此接口更改下发表中issued_count为0
	 * @param flowId
	 * @return
	 */
	public Map<String, String> orderIssued(String flowId, String operator) {
		Map<String, String> result = new HashMap<String, String>(); 
		result.put("statusCode", "1");
		result.put("message", "更新成功");
		
		OrderIssued orderIssued = new OrderIssued();
		orderIssued.setFlowId(flowId);
		orderIssued.setIssuedCount(0);
		orderIssued.setIsScan(0);//如果还是下发失败，会再次扫到异常表
		orderIssued.setUpdateTime(systemDateMapper.getSystemDate());
		try {
			orderIssuedService.updateBySelective(orderIssued);
		} catch (Exception e) {
			logger.error("*********更新下发订单状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更新下发订单状态出错");
		}
		OrderIssuedException orderIssuedException = new OrderIssuedException();
		orderIssuedException.setFlowId(flowId);
		orderIssuedException.setDealStatus(2); 
		orderIssuedException.setOperateTime(systemDateMapper.getSystemDate());
		orderIssuedException.setOperator(operator);
		try {
			this.updateBySelective(orderIssuedException);
		} catch (Exception e) {
			logger.error("*********更改下发订单异常状态为已完成时出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更改下发订单异常状态为已完成时出错");
		}
		return result;
	}
	/**
	 * 订单标记接口，会出现实际下发成功，超时返回或相关问题，或顾客通过其他方式将订单录入到其ERP系统。此处增加一个手工标记成功的机制，标记后则状态变为已完成。仅针对非关联问题的异常订单。
	 * @param flowId
	 * @return
	 */
	public Map<String, String> orderMark(String flowId, String operator) {
		Map<String, String> result = new HashMap<String, String>(); 
		result.put("statusCode", "1");
		result.put("message", "标记下发订单异常状态为已处理");
		
		OrderIssuedException orderIssuedException = new OrderIssuedException();
		orderIssuedException.setFlowId(flowId);
		orderIssuedException.setDealStatus(2);
		orderIssuedException.setOperateTime(systemDateMapper.getSystemDate());
		orderIssuedException.setOperator(operator);
		try {
			this.updateBySelective(orderIssuedException);
		} catch (Exception e) {
			logger.error("*********标记下发订单异常状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "标记下发订单异常状态出错");
		}
		
		OrderIssued orderIssued = new OrderIssued();
		orderIssued.setFlowId(flowId);
		orderIssued.setIssuedStatus("1");//标记为成功
		orderIssued.setUpdateTime(systemDateMapper.getSystemDate());
		try {
			orderIssuedService.updateBySelective(orderIssued);
		} catch (Exception e) {
			logger.error("*********更新下发订单状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更新下发订单状态出错");
		}
		return result;
	}
}