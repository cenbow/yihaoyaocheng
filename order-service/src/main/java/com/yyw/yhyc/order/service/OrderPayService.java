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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.mapper.OrderCombinedMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;

@Service("orderPayService")
public class OrderPayService {

	private Log log = LogFactory.getLog(OrderPayService.class);

	private OrderPayMapper	orderPayMapper;

	private SystemDateMapper systemDateMapper;

	private OrderMapper orderMapper;

	private OrderCombinedMapper orderCombinedMapper;

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderPayMapper(OrderPayMapper orderPayMapper)
	{
		this.orderPayMapper = orderPayMapper;
	}

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	@Autowired
	public void setOrderCombinedMapper(OrderCombinedMapper orderCombinedMapper) {
		this.orderCombinedMapper = orderCombinedMapper;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderPay getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderPayMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> list() throws Exception
	{
		return orderPayMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> listByProperty(OrderPay orderPay)
			throws Exception
	{
		return orderPayMapper.listByProperty(orderPay);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderPay> listPaginationByProperty(Pagination<OrderPay> pagination, OrderPay orderPay) throws Exception
	{
		List<OrderPay> list = orderPayMapper.listPaginationByProperty(pagination, orderPay);

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
		return orderPayMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderPayMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.deleteByProperty(orderPay);
	}

	/**
	 * 保存记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public void save(OrderPay orderPay) throws Exception
	{
		orderPayMapper.save(orderPay);
	}

	/**
	 * 更新记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int update(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.update(orderPay);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.findByCount(orderPay);
	}

	/**
	 * 在线支付订单前，预处理订单数据
	 *
	 * @param userDto 当前登陆用户
	 * @param flowIds 订单编号
	 * @param payTypeId 支付方式id
	 * @param payFlowId 支付流水号
     * @return
     */
	public OrderPay preHandler(UserDto userDto, String flowIds, int payTypeId, String payFlowId) throws Exception {

		log.info("在线支付订单前，预处理订单数据:userDto=" + userDto + ",flowIds=" + ",payTypeId=" + payTypeId + ",payFlowId="+payFlowId);

		/* 过滤非法订单参数 */
		if(UtilHelper.isEmpty(userDto)) return null;
		if(UtilHelper.isEmpty(flowIds)) return null;
		if(OnlinePayTypeEnum.getPayName(payTypeId) == null ) return null;
		if(UtilHelper.isEmpty(payFlowId)) return null;

		/* 查询、校验、处理合法订单数据 */
		Order order = null;
		int orderCount = 0;//订单数量
		BigDecimal orderTotal = new BigDecimal(0);//订单金额(应付)
		BigDecimal freightTotal = new BigDecimal(0);//运费
		BigDecimal needToPayTotal = new BigDecimal(0);//需要支付的金额(实付)
		List<Integer> orderIdList = new ArrayList<>();

		String [] flowIdArray = flowIds.split(",");
		for(String s : flowIdArray){
			if(UtilHelper.isEmpty(s)) continue;
			order = orderMapper.getOrderbyFlowId(s.trim());
			if(UtilHelper.isEmpty(order)) continue;
			/* 过滤掉不是在线支付类型的订单 */
			if(OnlinePayTypeEnum.getPayName(order.getPayTypeId()) == null ){
				continue;
			}
			/* 过滤掉不是待付款的订单 */
			if(!SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){
				continue;
			}
			/* 过滤掉不是自己下的单 */
			if(UtilHelper.isEmpty(order.getCustId()) || !order.getCustId().equals(userDto.getCustId())){
				continue;
			}
			orderTotal = orderTotal.add(order.getOrderTotal());
			freightTotal = freightTotal.add(order.getFreight());
			needToPayTotal = needToPayTotal.add(order.getOrgTotal());
			orderCount++;
			orderIdList.add(order.getOrderId());
		}

		if(orderCount == 0 || orderIdList.size() == 0) return null;

		/* 插入 orderCombine表*/
		OrderCombined orderCombined = new OrderCombined();
		orderCombined.setPayTypeId(payTypeId);        //支付方式id
		orderCombined.setCustId(userDto.getCustId());
		orderCombined.setCustName(userDto.getCustName());
		orderCombined.setCombinedNumber(orderCount);  //合并支付的订单数量
		orderCombined.setCopeTotal(orderTotal);       //应付总额
		orderCombined.setPocketTotal(needToPayTotal); //实际支付总额
		orderCombined.setFreightPrice(freightTotal);  //运费
		orderCombined.setPayFlowId(payFlowId);        //支付流水号
		orderCombined.setCreateUser(userDto.getUserName());
		orderCombined.setCreateTime(systemDateMapper.getSystemDate());
		orderCombinedMapper.save(orderCombined);
		log.info("在线支付订单前，预处理订单数据:插入orderCombine数据=" + orderCombined);

		/* 回写order_combine_id到order表  */
		List<OrderCombined> orderCombinedList = orderCombinedMapper.listByProperty(orderCombined);
		if(UtilHelper.isEmpty(orderCombinedList) || orderCombinedList.size() != 1) throw new Exception("服务器异常!");
		orderCombined = orderCombinedList.get(0);
		for(Integer orderId : orderIdList){
			if(orderId <= 0) continue;
			order = new Order();
			order.setOrderId(orderId);
			order.setPayTypeId(payTypeId);
			order.setPayTime(systemDateMapper.getSystemDate());
			order.setOrderCombinedId(orderCombined.getOrderCombinedId());
			orderMapper.update(order);
		}

		/* 插入 orderPay */
		OrderPay orderPay = new OrderPay();
		orderPay.setPayFlowId(payFlowId);
		orderPay.setPayTypeId(payTypeId);
		orderPay.setOrderMoney(needToPayTotal);//（实际上需要支付的）订单金额
		orderPay.setPayStatus(OrderPayStatusEnum.UN_PAYED.getPayStatus()); //支付状态：未支付
		orderPay.setCreateUser(userDto.getUserName());
		orderPay.setCreateTime(systemDateMapper.getSystemDate());
		orderPay.setPayTime(systemDateMapper.getSystemDate());
		log.info("在线支付订单前，预处理订单数据:插入orderPay数据=" + orderPay);
		orderPayMapper.save(orderPay);
		List<OrderPay> orderPayList = orderPayMapper.listByProperty(orderPay);
		if(UtilHelper.isEmpty(orderPayList) || orderPayList.size() != 1) throw new Exception("服务器异常!");
		orderPay = orderPayList.get(0);
		log.info("在线支付订单前，预处理订单数据:处理完成，返回数据=" + orderPay);
		return orderPay;
	}

	public OrderPay getByPayFlowId( String payFlowId){
		return orderPayMapper.getByPayFlowId(payFlowId);
	}

}