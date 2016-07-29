/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.enmu.BuyerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.helper.UtilHelper;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.mapper.OrderMapper;

@Service("orderService")
public class OrderService {

	private static final Log log = LogFactory.getLog(OrderService.class);

	private OrderMapper	orderMapper;
	private SystemPayTypeService systemPayTypeService;
	private SystemDateService systemDateService;
	private OrderDetailService orderDetailService;

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper)
	{
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setSystemPayTypeService(SystemPayTypeService systemPayTypeService) {
		this.systemPayTypeService = systemPayTypeService;
	}

	@Autowired
	public void setSystemDateService(SystemDateService systemDateService) {
		this.systemDateService = systemDateService;
	}

	@Autowired
	public void setOrderDetailService(OrderDetailService orderDetailService) {
		this.orderDetailService = orderDetailService;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public Order getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> list() throws Exception
	{
		return orderMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> listByProperty(Order order)
			throws Exception
	{
		return orderMapper.listByProperty(order);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<Order> listPaginationByProperty(Pagination<Order> pagination, Order order) throws Exception
	{
		List<Order> list = orderMapper.listPaginationByProperty(pagination, order);

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
		return orderMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(Order order) throws Exception
	{
		return orderMapper.deleteByProperty(order);
	}

	/**
	 * 保存记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void save(Order order) throws Exception
	{
		orderMapper.save(order);
	}

	/**
	 * 更新记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int update(Order order) throws Exception
	{
		return orderMapper.update(order);
	}

	/**
	 * 根据条件查询记录条数
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int findByCount(Order order) throws Exception
	{
		return orderMapper.findByCount(order);
	}



	/**
	 * 创建订单(PC端、APP端统一入口)
	 * @param orderCreateDto
	 * @throws Exception
	 */
	public OrderCreateDto createOrder(OrderCreateDto orderCreateDto) throws Exception{

		if(UtilHelper.isEmpty(orderCreateDto) || UtilHelper.isEmpty(orderCreateDto.getOrderDelivery())
				|| UtilHelper.isEmpty(orderCreateDto.getOrderDtoList())){
			throw  new Exception("非法参数");
		}

        /* 订单配送信息 */
		OrderDelivery orderDelivery = orderCreateDto.getOrderDelivery();

        /* 订单信息 */
		List<OrderDto> orderDtoList = orderCreateDto.getOrderDtoList();

		List<OrderDto> orderDtoNewList =  new ArrayList<OrderDto>();
		for (OrderDto orderDto : orderDtoList){
			if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
				continue;
			}

            /* 校验所购买商品的合法性 */
			validateProducts(orderDto);

            /* 创建订单相关的所有信息 */
			OrderDto orderDtoNew = createOrderInfo(orderDto,orderDelivery);

			orderDtoNewList.add(orderDtoNew);
		}

		//TODO 生成支付流水，插入数据到订单支付表

		return null;
	}

	/**
	 * 创建订单相关的所有信息
	 * @param orderDto 订单、商品相关信息
	 * @param orderDelivery 订单发货、配送相关信息
	 * @return
	 */
	private OrderDto createOrderInfo(OrderDto orderDto, OrderDelivery orderDelivery) throws Exception{

		/* 计算订单相关的价格 */
//		orderDto = calculateOrderPrice(orderDto);

		/*  数据插入订单表、订单详情表*/
		Order order = insertOrderAndOrderDetail(orderDto);


		//TODO 订单配送发货信息表、订单跟踪信息表

		//TODO 删除购物车

		//TODO 短信、邮件等通知买家

		//TODO 自动取消订单相关的定时任务

		return null;
	}

	/**
	 * 数据插入订单表
	 * @param orderDto
	 * @return
	 * @throws Exception
     */
	private Order insertOrderAndOrderDetail(OrderDto orderDto)throws Exception {
		Order order = new Order();
		order.setCustId(orderDto.getCustId());
		order.setCustName("");//todo
		order.setSupplyId(orderDto.getSupplyId());
		order.setSupplyName("");//todo
		order.setBillType(orderDto.getBillType());
		order.setLeaveMessage(orderDto.getLeaveMessage());
		order.setPayTypeId(orderDto.getPayTypeId());

		SystemPayType systemPayType = systemPayTypeService.getByPK(orderDto.getPayTypeId());
		String orderFlowIdPrefix = "";
		/* 下单后，选择不同支付方式，订单的状态不一样 */
		/* 线下支付 */
		if(SystemPayTypeEnum.PayOffline.getPayType().equals(  systemPayType.getPayType() )){
			order.setOrderStatus(BuyerOrderStatusEnum.PendingPayment.getType());
			orderFlowIdPrefix = CommonType.ORDER_OFFLINE_PAY_PREFIX;

		/* 在线支付 */
		}else if(SystemPayTypeEnum.PayOnline.getPayType().equals(  systemPayType.getPayType() )){
			order.setOrderStatus(BuyerOrderStatusEnum.PendingPayment.getType());
			orderFlowIdPrefix = CommonType.ORDER_ONLINE_PAY_PREFIX;

		/* 账期支付 */
		}else if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(  systemPayType.getPayType() )){
			order.setOrderStatus(BuyerOrderStatusEnum.BackOrder.getType());
			orderFlowIdPrefix = CommonType.ORDER_PERIOD_TERM_PAY_PREFIX;
		}
		order.setCreateTime(systemDateService.getSystemDate());

		order.setCreateUser("");
		orderMapper.save(order);
		log.info("插入数据到订单表：order参数=" + order);
		List<Order> orders = orderMapper.listByProperty(order);
		if (UtilHelper.isEmpty(orders) || orders.size() > 1) {
			throw new Exception("订单不存在");
		}
		order = orders.get(0);
		/* 创建订单编号 */
		String orderFlowId = RandomUtil.createOrderFlowId(systemDateService.getSystemDateByformatter("%Y%m%d%H%i%s"),order.getOrderId() +"", orderFlowIdPrefix);
		order.setFlowId(orderFlowId);
		orderMapper.update(order);
		log.info("更新数据到订单表：order参数=" + order);


		/* 插入订单详情表 */
		OrderDetail orderDetail = null;
		List<ProductInfoDto> productInfoDtoList = orderDto.getProductInfoDtoList();
		for(ProductInfoDto productInfoDto : productInfoDtoList){
			if(UtilHelper.isEmpty(productInfoDto)){
				continue;
			}
			orderDetail = new OrderDetail();
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setSupplyId(order.getSupplyId());
			orderDetail.setProductPrice(productInfoDto.getProductPrice());
			orderDetail.setProductCount(productInfoDto.getProductCount());
			orderDetail.setCreateTime(systemDateService.getSystemDate());
			orderDetail.setCreateUser("");//todo
			orderDetailService.save(orderDetail);
			log.info("更新数据到订单详情表：orderDetail参数=" + orderDetail);
		}
		return order;
	}

	/**
	 * 校验要购买的商品(通用方法)
	 * @param orderDto
	 * @throws Exception
	 */
	public boolean validateProducts(OrderDto orderDto) {
		//TODO 校验要购买的商品

		//TODO 特殊校验(选择账期支付的订单，买家账期、商品账期)
		return false;
	}



	public Map<String,Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		orderDto = new OrderDto();
		orderDto.setCustId(1);
		orderDto.setOrderId(1);
		orderDto.setSupplyName("");
		List<OrderDto> orderDtoList =  orderMapper.findOrderStatusCount(orderDto);
		log.debug(orderDtoList);
		return resultMap;
	}

}