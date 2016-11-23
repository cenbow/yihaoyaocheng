/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
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
import org.springframework.util.StringUtils;

import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.order.dto.OrderLogDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderTraceMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.utils.ContextHolder;

@Service("orderTraceService")
public class OrderTraceService {

	private OrderTraceMapper	orderTraceMapper;
	@Autowired
    private SystemDateMapper systemDateMapper;

	@Autowired
	public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper)
	{
		this.orderTraceMapper = orderTraceMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderTrace getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderTraceMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderTrace> list() throws Exception
	{
		return orderTraceMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderTrace> listByProperty(OrderTrace orderTrace)
			throws Exception
	{
		return orderTraceMapper.listByProperty(orderTrace);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderTrace> listPaginationByProperty(Pagination<OrderTrace> pagination, OrderTrace orderTrace) throws Exception
	{
		List<OrderTrace> list = orderTraceMapper.listPaginationByProperty(pagination, orderTrace);

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
		return orderTraceMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderTraceMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderTrace orderTrace) throws Exception
	{
		return orderTraceMapper.deleteByProperty(orderTrace);
	}
	
	/**
	 * 保存订单相关的日志
	 * @param orderLog
	 */
	public void saveOrderLog(OrderLogDto orderLog){
		String now=this.systemDateMapper.getSystemDate();
		UserDto userDto=ContextHolder.getUserDtoInfo();
		OrderTrace orderTrace=new OrderTrace();
		orderTrace.setOrderId(orderLog.getOrderId());
		orderTrace.setOrderStatus(orderLog.getOrderStatus());
		orderTrace.setNodeName(orderLog.getNodeName());
		orderTrace.setPaymentPlatforReturn(orderLog.getPaymentPlatforReturn());
		String remark=orderLog.getRemark();
		if(StringUtils.hasText(remark)){
			if(remark.length()>5000){
				String subRemark=remark.substring(0,4999);
				orderTrace.setRemark(subRemark);
			}else{
				orderTrace.setRemark(remark);
			}
		}
		
		orderTrace.setCreateUser(userDto.getUserName());
		orderTrace.setCreateTime(now);
		orderTrace.setUpdateTime(now);
		orderTrace.setUpdateUser(userDto.getUserName());
		this.orderTraceMapper.save(orderTrace);
		
		
	}

	/**
	 * 保存记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public void save(OrderTrace orderTrace) throws Exception
	{
		orderTraceMapper.save(orderTrace);
	}

	/**
	 * 更新记录
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int update(OrderTrace orderTrace) throws Exception
	{
		return orderTraceMapper.update(orderTrace);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderTrace
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderTrace orderTrace) throws Exception
	{
		return orderTraceMapper.findByCount(orderTrace);
	}
}