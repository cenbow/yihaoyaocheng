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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.enmu.BuyerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SellerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.exception.ServiceException;
import com.yyw.yhyc.order.helper.UtilHelper;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.utils.DateUtils;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.Pagination;

@Service("orderService")
public class OrderService {

	Log log = LogFactory.getLog(OrderService.class);

	private OrderMapper	orderMapper;
	private SystemPayTypeService systemPayTypeService;
	private SystemDateMapper systemDateMapper;
	private OrderDetailService orderDetailService;
	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;
	private OrderDeliveryMapper orderDeliveryMapper;
	private OrderTraceMapper orderTraceMapper;

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper)
	{
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper) {
		this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
	}


	@Autowired
	public void setSystemPayTypeService(SystemPayTypeService systemPayTypeService) {
		this.systemPayTypeService = systemPayTypeService;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderDetailService(OrderDetailService orderDetailService) {
		this.orderDetailService = orderDetailService;
	}

	@Autowired
	public void setOrderDeliveryMapper(OrderDeliveryMapper orderDeliveryMapper) {
		this.orderDeliveryMapper = orderDeliveryMapper;
	}

	@Autowired
	public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
		this.orderTraceMapper = orderTraceMapper;
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

		if(UtilHelper.isEmpty(orderCreateDto) || UtilHelper.isEmpty(orderCreateDto.getOrderDeliveryDto())
				|| UtilHelper.isEmpty(orderCreateDto.getOrderDtoList())){
			throw  new Exception("非法参数");
		}

        /* 订单配送信息 */
		OrderDeliveryDto orderDeliveryDto = orderCreateDto.getOrderDeliveryDto();

        /* 订单信息 */
		List<OrderDto> orderDtoList = orderCreateDto.getOrderDtoList();

		List<Order> orderNewList =  new ArrayList<Order>();
		for (OrderDto orderDto : orderDtoList){
			if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
				continue;
			}

            /* 校验所购买商品的合法性 */
			validateProducts(orderDto);

            /* 创建订单相关的所有信息 */
			Order orderNew = createOrderInfo(orderDto,orderDeliveryDto);

			orderNewList.add(orderNew);
		}

		/* 生成支付流水，插入数据到订单支付表 */
		insertOrderPay(orderNewList);

        return null;
    }



	/**
	 * 创建订单相关的所有信息
	 * @param orderDto 订单、商品相关信息
	 * @param orderDeliveryDto 订单发货、配送相关信息
	 * @return
	 */
	private Order createOrderInfo(OrderDto orderDto, OrderDeliveryDto orderDeliveryDto) throws Exception{

		/* 计算订单相关的价格 */
//		orderDto = calculateOrderPrice(orderDto);

		/*  数据插入订单表、订单详情表 */
		Order order = insertOrderAndOrderDetail(orderDto);

		/* 订单配送发货信息表 */
		insertOrderDeliver(order,orderDeliveryDto);

		/* 订单跟踪信息表 */
		insertOrderTrace(order);


		//TODO 删除购物车

		//TODO 短信、邮件等通知买家

		//TODO 自动取消订单相关的定时任务

		return order;
	}


	private void insertOrderPay(List<Order> orderNewList) throws Exception {
		if(UtilHelper.isEmpty(orderNewList)) {
			throw new Exception("生成订单异常");
		}
		for(Order order : orderNewList){
			//todo
		}
	}

	private void insertOrderTrace(Order order) {
		if(UtilHelper.isEmpty(order)) return;
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(order.getOrderId());
		orderTrace.setOrderStatus(order.getOrderStatus());
		orderTrace.setCreateTime(systemDateMapper.getSystemDate());
		orderTraceMapper.save(orderTrace);
	}

	private void insertOrderDeliver(Order order, OrderDeliveryDto orderDeliveryDto) {
		if(UtilHelper.isEmpty(order) || UtilHelper.isEmpty(orderDeliveryDto)){
			return ;
		}
		OrderDelivery  orderDelivery = new OrderDelivery();
		orderDelivery.setOrderId(order.getOrderId());
		orderDelivery.setFlowId(order.getFlowId());
		orderDelivery.setReceivePerson(orderDeliveryDto.getReceivePerson());
		orderDelivery.setReceiveProvince(orderDeliveryDto.getReceiveProvince());
		orderDelivery.setReceiveCity(orderDeliveryDto.getReceiveCity());
		orderDelivery.setReceiveRegion(orderDeliveryDto.getReceiveRegion());
		orderDelivery.setReceiveAddress(orderDeliveryDto.getReceiveAddress());
		orderDelivery.setReceiveContactPhone(orderDeliveryDto.getReceiveContactPhone());
		orderDelivery.setZipCode(orderDeliveryDto.getZipCode());
		orderDelivery.setCreateTime(systemDateMapper.getSystemDate());
		orderDeliveryMapper.save(orderDelivery);

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
		order.setCreateTime(systemDateMapper.getSystemDate());

		order.setCreateUser("");
		orderMapper.save(order);
		log.info("插入数据到订单表：order参数=" + order);
		List<Order> orders = orderMapper.listByProperty(order);
		if (UtilHelper.isEmpty(orders) || orders.size() > 1) {
			throw new Exception("订单不存在");
		}
		order = orders.get(0);
		/* 创建订单编号 */
		String orderFlowId = RandomUtil.createOrderFlowId(systemDateMapper.getSystemDateByformatter("%Y%m%d%H%i%s"),order.getOrderId() +"", orderFlowIdPrefix);
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
			orderDetail.setCreateTime(systemDateMapper.getSystemDate());
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

		//TODO 特殊校验(选择账期支付方式的订单，校验买家账期、商品账期)
		return false;
	}



	/**
	 * 根据订单号查询订单详情
	 * @param flowId
	 * @throws Exception
	 */
	public OrderDetailsDto getOrderDetails(String flowId) throws Exception{
		OrderDetailsDto orderDetailsdto=orderMapper.getOrderDetails(flowId);
			//加载导入的批号信息，如果有一条失败则状态为失败否则查询成功数据
			OrderDeliveryDetail orderDeliveryDetail=new OrderDeliveryDetail();
			orderDeliveryDetail.setFlowId(flowId);
			orderDeliveryDetail.setDeliveryStatus(0);
			List<OrderDeliveryDetail> list=orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
			if(list.size()>0){
				orderDetailsdto.setOrderDeliveryDetail(list.get(0));
			}else{
				orderDeliveryDetail.setDeliveryStatus(1);
				List<OrderDeliveryDetail> listDeliveryDetai=orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
				if(listDeliveryDetai.size()>0){
					orderDetailsdto.setOrderDeliveryDetail(listDeliveryDetai.get(0));
				}
			}
		return orderDetailsdto;
	}

    /**
     * 校验要购买的商品(通用方法)
     * @param productInfoDtoList
     * @throws Exception
     */
    public boolean validateProducts(List<ProductInfoDto> productInfoDtoList) {
        //todo
        return false;
    }

    public Map<String, Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto) {
        if(UtilHelper.isEmpty(orderDto))
            throw new ServiceException("参数错误");
		log.debug("request orderDto :"+orderDto.toString());
		if(!UtilHelper.isEmpty(orderDto.getCreateEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderDto.getCreateEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderDto.setCreateEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderDto.getCreateEndTime());
				e.printStackTrace();
				throw new ServiceException("日期错误");
			}

        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<OrderDto> buyerOrderList = orderMapper.listPgBuyerOrder(pagination, orderDto);
        pagination.setResultList(buyerOrderList);

        List<OrderDto> orderDtoList = orderMapper.findOrderStatusCount(orderDto);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付
        BuyerOrderStatusEnum buyerorderstatusenum;
        if (!UtilHelper.isEmpty(orderDtoList)) {
            for (OrderDto od : orderDtoList) {
                buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),payType);
                if(buyerorderstatusenum != null){
                    if(orderStatusCountMap.containsKey(buyerorderstatusenum.getType())){
                        orderStatusCountMap.put(buyerorderstatusenum.getType(),orderStatusCountMap.get(buyerorderstatusenum.getType())+od.getOrderCount());
                    }else{
                        orderStatusCountMap.put(buyerorderstatusenum.getType(),od.getOrderCount());
                    }
                }
            }
        }

        BigDecimal orderTotalMoney = new BigDecimal(0);
        int orderCount = 0;
		long time = 0l;
        if(!UtilHelper.isEmpty(buyerOrderList)){
            orderCount = buyerOrderList.size();
            for(OrderDto od : buyerOrderList){
                if(!UtilHelper.isEmpty(od.getOrderStatus()) && !UtilHelper.isEmpty(od.getPayType())){
                    buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
                    if(!UtilHelper.isEmpty(buyerorderstatusenum))
                        od.setOrderStatusName(buyerorderstatusenum.getValue());
                    else
                        od.setOrderStatusName("未知类型");
                }
                if(!UtilHelper.isEmpty(od.getOrderTotal()))
                    orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
				if(!UtilHelper.isEmpty(od.getNowTime()) && 1 == od.getPayType() && SystemOrderStatusEnum.BuyerOrdered.getType().equals(od.getOrderStatus())){//在线支付 + 未付款订单
					try {
						time = DateUtils.getSeconds(od.getCreateTime(),od.getNowTime());
						time = CommonType.PAY_TIME*60*60-time;
						if(time < 0)
							time = 0l;
						od.setResidualTime(time);
					} catch (ParseException e) {
						log.debug("date format error"+od.getCreateTime()+" "+od.getNowTime());
						e.printStackTrace();
						throw new ServiceException("日期转换错误");
					}

				}
            }
        }

		log.debug("orderStatusCount====>" + orderStatusCountMap);
        log.debug("orderList====>" + orderDtoList);
        log.debug("buyerOrderList====>" + buyerOrderList);
		log.debug("orderCount====>" + orderCount);
		log.debug("orderTotalMoney====>" + orderTotalMoney);

        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("buyerOrderList", buyerOrderList);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney);
        return resultMap;
    }

    /**
     * 根据订单系统状态和支付类型获取买家视角订单状态
     * @param systemOrderStatus
     * @param payType
     * @return
     */
    BuyerOrderStatusEnum getBuyerOrderStatus(String systemOrderStatus,int payType){
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerOrdered.getType())) {//买家已下单
            if (payType == 2) {
                return BuyerOrderStatusEnum.BackOrder;//待发货
            } else {
                return BuyerOrderStatusEnum.PendingPayment;//待付款
            }
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAlreadyPaid.getType())) {//买家已付款
            return BuyerOrderStatusEnum.BackOrder;//待发货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.SellerDelivered.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerDeferredReceipt.getType())) {//卖家已发货+买家延期收货
            return BuyerOrderStatusEnum.ReceiptOfGoods;//待收货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Rejecting.getType())) {//拒收中
            return BuyerOrderStatusEnum.Rejecting;//拒收中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Replenishing.getType())) {//补货中
            return BuyerOrderStatusEnum.Replenishing;//补货中
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())) {//买家已取消+系统自动取消+后台取消
            return BuyerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType())) {// 买家全部收货+系统自动确认收货
            return BuyerOrderStatusEnum.Finished;//补货中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return BuyerOrderStatusEnum.PaidException;//打款异常
        }
        return null;
    }

    /**
     * 根据订单系统状态和支付类型获取买家视角订单状态
     * @param systemOrderStatus
     * @param payType
     * @return
     */
    SellerOrderStatusEnum getSellerOrderStatus(String systemOrderStatus, int payType){
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerOrdered.getType())) {//买家已下单
            if (payType == 2) {
                return SellerOrderStatusEnum.BackOrder;//待发货
            } else {
                return SellerOrderStatusEnum.PendingPayment;//待付款
            }
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAlreadyPaid.getType())) {//买家已付款
            return SellerOrderStatusEnum.BackOrder;//待发货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.SellerDelivered.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerDeferredReceipt.getType())) {//卖家已发货+买家延期收货
            return SellerOrderStatusEnum.ReceiptOfGoods;//待收货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Rejecting.getType())) {//拒收中
            return SellerOrderStatusEnum.Rejecting;//拒收中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Replenishing.getType())) {//补货中
            return SellerOrderStatusEnum.Replenishing;//补货中
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())) {//买家已取消+系统自动取消+后台取消
            return SellerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType())) {// 买家全部收货+系统自动确认收货
            return SellerOrderStatusEnum.Finished;//补货中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return SellerOrderStatusEnum.PaidException;//打款异常
        }
        return null;
    }

}