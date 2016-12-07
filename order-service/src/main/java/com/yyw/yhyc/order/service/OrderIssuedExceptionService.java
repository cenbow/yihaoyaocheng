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

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.bo.OrderIssuedLog;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.mapper.OrderIssuedExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderIssuedLogMapper;
import com.yyw.yhyc.order.mapper.OrderIssuedMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;

@Service("orderIssuedExceptionService")
public class OrderIssuedExceptionService {
	private static final Logger logger = LoggerFactory
			.getLogger(OrderIssuedExceptionService.class);

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDeliveryService orderDliveryMapper;
	@Autowired
	private SystemPayTypeService systemPayTypeMapper;
	@Autowired
	private OrderIssuedLogMapper orderIssuedLogMapper;
	private SystemDateMapper systemDateMapper;

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	private OrderIssuedMapper orderIssuedMapper;

	@Autowired
	public void setOrderIssuedMapper(OrderIssuedMapper orderIssuedMapper) {
		this.orderIssuedMapper = orderIssuedMapper;
	}

	private OrderIssuedExceptionMapper orderIssuedExceptionMapper;

	@Autowired
	public void setOrderIssuedExceptionMapper(
			OrderIssuedExceptionMapper orderIssuedExceptionMapper) {
		this.orderIssuedExceptionMapper = orderIssuedExceptionMapper;
	}

	@Autowired
	private OrderIssuedService orderIssuedService;

	/**
	 * 通过主键查询实体对象
	 * 
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedException getByPK(java.lang.Integer primaryKey)
			throws Exception {
		return orderIssuedExceptionMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> list() throws Exception {
		return orderIssuedExceptionMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> listByProperty(
			OrderIssuedException orderIssuedException) throws Exception {
		return orderIssuedExceptionMapper.listByProperty(orderIssuedException);
	}

	/**
	 * 根据查询条件查询分页记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedException> listPaginationByProperty(
			Pagination<OrderIssuedException> pagination,
			OrderIssuedException orderIssuedException) throws Exception {
		List<OrderIssuedException> list = orderIssuedExceptionMapper
				.listPaginationByProperty(pagination, orderIssuedException);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * 
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception {
		return orderIssuedExceptionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * 
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys)
			throws Exception {
		orderIssuedExceptionMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * 
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedException orderIssuedException)
			throws Exception {
		return orderIssuedExceptionMapper
				.deleteByProperty(orderIssuedException);
	}

	/**
	 * 保存记录
	 * 
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedException orderIssuedException)
			throws Exception {
		orderIssuedExceptionMapper.save(orderIssuedException);
	}

	/**
	 * 更新记录
	 * 
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedException orderIssuedException)
			throws Exception {
		return orderIssuedExceptionMapper.update(orderIssuedException);
	}

	/**
	 * 更新记录
	 * 
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int updateBySelective(OrderIssuedException orderIssuedException)
			throws Exception {
		return orderIssuedExceptionMapper
				.updateBySelective(orderIssuedException);
	}

	/**
	 * 根据条件查询记录条数
	 * 
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedException orderIssuedException)
			throws Exception {
		return orderIssuedExceptionMapper.findByCount(orderIssuedException);
	}

	/**
	 * 根据查询条件查询分页记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedExceptionDto> listPaginationByPropertyEx(
			Pagination<OrderIssuedExceptionDto> pagination,
			OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception {
		orderIssuedExceptionDto.setDealStatus(1);//只要处理中的
		List<OrderIssuedExceptionDto> list = orderIssuedExceptionMapper
				.listPaginationByPropertyEx(pagination, orderIssuedExceptionDto);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 订单下发接口，调用此接口更改下发表中issued_count为0
	 * 
	 * @param flowId
	 * @return
	 */
	public Map<String, String> updateOrderIssued(String flowId, String operator) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("statusCode", "1");
		result.put("message", "更新成功");
		String now = systemDateMapper.getSystemDate();
		OrderIssued orderIssued = new OrderIssued();
		orderIssued.setFlowId(flowId);
		orderIssued.setIssuedCount(0);
		orderIssued.setIsScan(0);// 如果还是下发失败，会再次扫到异常表
		orderIssued.setUpdateTime(systemDateMapper.getSystemDate());
		try {
			orderIssuedMapper.updateBySelective(orderIssued);
		} catch (Exception e) {
			logger.error("*********更新下发订单状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更新下发订单状态出错");
		}
		OrderIssuedException orderIssuedException = new OrderIssuedException();
		orderIssuedException.setFlowId(flowId);
		orderIssuedException.setDealStatus(2);
		orderIssuedException.setOperateTime(now);
		orderIssuedException.setOperator(operator);
		try {
			this.updateBySelective(orderIssuedException);
		} catch (Exception e) {
			logger.error("*********更改下发订单异常状态为已完成时出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更改下发订单异常状态为已完成时出错");
		}

		OrderIssuedLog orderIssuedLog = new OrderIssuedLog();
		orderIssuedLog.setFlowId(flowId);
		orderIssuedLog.setOperateName("异常管理中手工下发");
		orderIssuedLog.setOperator(operator);
		orderIssuedLog.setOperateTime(now);
		orderIssuedLogMapper.save(orderIssuedLog);

		return result;
	}

	/**
	 * 订单标记接口，会出现实际下发成功，超时返回或相关问题，或顾客通过其他方式将订单录入到其ERP系统。此处增加一个手工标记成功的机制，
	 * 标记后则状态变为已完成。仅针对非关联问题的异常订单。
	 * 
	 * @param flowId
	 * @return
	 */
	public Map<String, String> updateOrderMark(String flowId, String operator) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("statusCode", "1");
		result.put("message", "标记下发订单异常状态为已处理");
		String now = systemDateMapper.getSystemDate();
		OrderIssuedException orderIssuedException = new OrderIssuedException();
		orderIssuedException.setFlowId(flowId);
		orderIssuedException.setDealStatus(2);
		orderIssuedException.setOperateTime(now);
		orderIssuedException.setOperator(operator);
		try {
			this.updateBySelective(orderIssuedException);
		} catch (Exception e) {
			logger.error("*********标记下发订单异常状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "标记下发订单异常状态出错");
		}

		OrderIssuedLog orderIssuedLog = new OrderIssuedLog();
		orderIssuedLog.setFlowId(flowId);
		orderIssuedLog.setOperateName("异常管理中标记成功");
		orderIssuedLog.setOperator(operator);
		orderIssuedLog.setOperateTime(now);
		orderIssuedLogMapper.save(orderIssuedLog);

		OrderIssued orderIssued = new OrderIssued();
		orderIssued.setFlowId(flowId);
		orderIssued.setIssuedStatus("1");// 标记为成功
		orderIssued.setUpdateTime(now);
		try {
			orderIssuedMapper.updateBySelective(orderIssued);
		} catch (Exception e) {
			logger.error("*********更新下发订单状态出错***********", e);
			e.printStackTrace();
			result.put("statusCode", "0");
			result.put("message", "更新下发订单状态出错");
		}
		return result;
	}

	/**
	 * *****************收集下发表中失败状态的记录插入到Exception表任务开始*****************
	 * 
	 * @throws Exception
	 */
	public void downExceptionJob() throws Exception {
		String now = systemDateMapper.getSystemDate();
		OrderIssued orderIssued = new OrderIssued();
		orderIssued.setIssuedCount(3);
		orderIssued.setIssuedStatus("0");
		orderIssued.setIsScan(0);
		List<OrderIssued> listOrderIssued = orderIssuedMapper
				.listByProperty(orderIssued);
		for (OrderIssued one : listOrderIssued) {
			OrderIssuedException orderIssuedException = new OrderIssuedException();
			Order order = orderMapper.getOrderbyFlowId(one.getFlowId());
			PropertyUtils.copyProperties(orderIssuedException, order);
			orderIssuedException.setOrderCreateTime(order.getCreateTime());

			OrderDelivery orderDelivery = new OrderDelivery();
			orderDelivery.setFlowId(one.getFlowId());
			List<OrderDelivery> orderDeliveryList  = orderDliveryMapper.listByProperty(orderDelivery);
			if(orderDeliveryList == null || orderDeliveryList.size() == 0){
				continue;
			}
			orderDelivery = orderDeliveryList.get(0);
			orderIssuedException.setReceivePerson(orderDelivery
					.getReceivePerson());
			orderIssuedException.setReceiveContactPhone(orderDelivery
					.getReceiveContactPhone());
			orderIssuedException.setReceiveAddress(orderDelivery
					.getReceiveAddress());

			SystemPayType sysPayType = systemPayTypeMapper.getByPK(order
					.getPayTypeId());
			orderIssuedException.setPayType(sysPayType.getPayType());
			orderIssuedException.setPayTypeName(sysPayType.getPayTypeName());

			orderIssuedException.setDealStatus(1); // 待处理
			if (one.getCusRelationship() == 1 && one.getIssuedCount() == 3)
				orderIssuedException.setExceptionType(3);// 下发失败
			else if (one.getCusRelationship() == 1 && one.getIssuedCount() != 3)
				orderIssuedException.setExceptionType(2);// 下发返回错误
			else if (one.getCusRelationship() == 0)
				orderIssuedException.setExceptionType(1);// 无关联企业用户

			orderIssuedException.setOperator("system");
			orderIssuedException.setOperateTime(now);
			try {
				this.save(orderIssuedException);
			} catch (Exception ex) {// 这种情况很少，所以用这种方式，避免每次去做一次查询
				logger.error("插入失败，异常表有此flowId", ex);
				this.updateBySelective(orderIssuedException);
				OrderIssuedLog orderIssuedLog = new OrderIssuedLog();
				orderIssuedLog.setFlowId(one.getFlowId());
				orderIssuedLog.setOperateName("下发异常更新");
				orderIssuedLog.setOperator("system");
				orderIssuedLog.setOperateTime(now);
				orderIssuedLogMapper.save(orderIssuedLog);
			}

			// 将扫描标志设置为 已扫描
			one.setIsScan(1);
			one.setUpdateTime(now);
			orderIssuedMapper.updateBySelective(one);

			OrderIssuedLog orderIssuedLog = new OrderIssuedLog();
			orderIssuedLog.setFlowId(one.getFlowId());
			orderIssuedLog.setOperateName("下发失败记录插入异常表");
			orderIssuedLog.setOperator("system");
			orderIssuedLog.setOperateTime(now);
			orderIssuedLogMapper.save(orderIssuedLog);
		}
	}

	/**
	 * *****************收集没有对码的订单记录插入到issued下发表任务开始****************
	 * 
	 * @throws Exception
	 */
	public void downNoRelationshipJob(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = orderIssuedService
				.findOrderIssuedNoRelationshipList(params);

		for (Map<String, Object> param : list) {
			OrderIssued orderIssued = new OrderIssued();

			orderIssued = orderIssuedMapper.findByFlowId((String) param
					.get("FLOW_ID"));
			if (UtilHelper.isEmpty(orderIssued)) {
				orderIssued = new OrderIssued();
				orderIssued.setFlowId((String) param.get("FLOW_ID"));// 设置订单编号
				orderIssued.setSupplyId((Integer) param.get("SUPPLY_ID"));
				orderIssued.setSupplyName((String) param.get("SUPPLY_NAME"));
				orderIssued.setCreateTime(systemDateMapper.getSystemDate());
				// 这样订单下发时不会扫描出来这些没对码的数据
				orderIssued.setIssuedStatus("0");// 设置下发状态，默认为失败
				orderIssued.setIssuedCount(3);// 设置调用次数，初始化为3
				orderIssued.setCusRelationship(0);// 无客户关联关系
				orderIssued.setIsScan(0);
				orderIssuedMapper.save(orderIssued);
			}
		}
	}
}