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
import java.util.*;

import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.*;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.DateUtils;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.product.bo.ProductInfo;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.mapper.ProductInfoMapper;

import com.yyw.yhyc.utils.ExcelUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;

@Service("orderService")
public class OrderService {

	private Log log = LogFactory.getLog(OrderService.class);

	private OrderMapper	orderMapper;
	private SystemPayTypeService systemPayTypeService;
	private SystemDateMapper systemDateMapper;
	private OrderDetailService orderDetailService;
	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;
	private OrderDeliveryMapper orderDeliveryMapper;
	private OrderTraceMapper orderTraceMapper;
	private OrderPayMapper orderPayMapper;
	private OrderCombinedMapper orderCombinedMapper;
	private ShoppingCartMapper shoppingCartMapper;
	private ProductInfoMapper productInfoMapper;
	private OrderSettlementMapper orderSettlementMapper;
	private UsermanageReceiverAddressMapper receiverAddressMapper;
	private UsermanageEnterpriseMapper enterpriseMapper;


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

	@Autowired
	public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
		this.orderPayMapper = orderPayMapper;
	}

	@Autowired
	public void setOrderCombinedMapper(OrderCombinedMapper orderCombinedMapper) {
		this.orderCombinedMapper = orderCombinedMapper;
	}

	@Autowired
	public void setShoppingCartMapper(ShoppingCartMapper shoppingCartMapper) {
		this.shoppingCartMapper = shoppingCartMapper;
	}

	@Autowired
	public void setProductInfoMapper(ProductInfoMapper productInfoMapper) {
		this.productInfoMapper = productInfoMapper;
	}
	@Autowired
	public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper) {
		this.orderSettlementMapper = orderSettlementMapper;
	}
	@Autowired
	public void setReceiverAddressMapper(UsermanageReceiverAddressMapper receiverAddressMapper) {
		this.receiverAddressMapper = receiverAddressMapper;
	}

	@Autowired
	public void setEnterpriseMapper(UsermanageEnterpriseMapper enterpriseMapper) {
		this.enterpriseMapper = enterpriseMapper;
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
	public List<Order> createOrder(OrderCreateDto orderCreateDto) throws Exception{

		if(UtilHelper.isEmpty(orderCreateDto) || UtilHelper.isEmpty(orderCreateDto.getOrderDeliveryDto())
				|| UtilHelper.isEmpty(orderCreateDto.getOrderDtoList())){
			throw  new Exception("非法参数");
		}

		/* TODO 获取当前登陆用户的企业id */
		Integer currentLoginCustId = 123;

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
			if(null == orderNew) continue;
			orderNewList.add(orderNew);
		}

		/* 创建支付流水号 */
		String payFlowId = RandomUtil.createOrderPayFlowId(systemDateMapper.getSystemDateByformatter("%Y%m%d%H%i%s"),currentLoginCustId);

		/* 插入数据到订单支付表 */
		insertOrderPay(orderNewList,currentLoginCustId,payFlowId);

		/* 插入订单合并表(只有选择在线支付的订单才合成一个单),回写order_combined_id到order表 */
		insertOrderCombined(orderNewList,currentLoginCustId,payFlowId);
        return orderNewList;
    }



	/**
	 * 创建订单相关的所有信息
	 * @param orderDto 订单、商品相关信息
	 * @param orderDeliveryDto 订单发货、配送相关信息
	 * @return
	 */
	private Order createOrderInfo(OrderDto orderDto, OrderDeliveryDto orderDeliveryDto) throws Exception{
		if( null == orderDto || null == orderDeliveryDto){
			return null;
		}

		/* TODO  计算订单相关的价格 */
		orderDto = calculateOrderPrice(orderDto);

		/*  数据插入订单表、订单详情表 */
		Order order = insertOrderAndOrderDetail(orderDto);

		/* 订单配送发货信息表 */
		insertOrderDeliver(order,orderDeliveryDto);

		/* 订单跟踪信息表 */
		insertOrderTrace(order);

		/* 删除购物车 */
		deleteShoppingCart(orderDto);

		//TODO 短信、邮件等通知买家
		//TODO 自动取消订单相关的定时任务

		return order;
	}

	private OrderDto calculateOrderPrice(OrderDto orderDto) {
		if(null == orderDto || null == orderDto.getProductInfoDtoList()) return null;

		BigDecimal orderTotal = new BigDecimal(0);

		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(null == productInfoDto) continue;
			orderTotal = orderTotal.add(productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())));
		}
		/* 计算订单相关的金额 */
		orderDto.setOrderTotal(orderTotal);//订单总金额
		orderDto.setFreight(new BigDecimal(0));//运费
		orderDto.setPreferentialMoney(new BigDecimal(0));//优惠了的金额(如果使用了优惠)
		orderDto.setOrgTotal(orderTotal);//订单优惠后的金额(如果使用了优惠)
		orderDto.setSettlementMoney(orderTotal);//结算金额
		orderDto.setFinalPay(orderTotal);//最后支付金额
		return orderDto;
	}

	/**
	 * 购买成功后，删除购物车中的商品
	 * @param orderDto
     */
	private void deleteShoppingCart(OrderDto orderDto) {
		if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())) return;
		ShoppingCart shoppingCart = null;
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(UtilHelper.isEmpty(productInfoDto)) continue;
			shoppingCart = new ShoppingCart();
			shoppingCart.setCustId(orderDto.getCustId());
			shoppingCart.setProductId(productInfoDto.getId());
			shoppingCart.setSupplyId(orderDto.getSupplyId());
			shoppingCartMapper.deleteByProperty(shoppingCart);
		}
	}


	/**
	 * 插入合并订单表
	 * 只有选择在线支付的订单才合成一个单
	 * @param orderNewList 新生成订单的集合
	 * @param currentLoginCustId  当前登陆人的企业id
	 * @param payFlowId 支付流水号
	 */
	private void insertOrderCombined(List<Order> orderNewList, Integer currentLoginCustId,String payFlowId) {
		if(UtilHelper.isEmpty(orderNewList)) return;
		if(UtilHelper.isEmpty(currentLoginCustId)) return;
		if(UtilHelper.isEmpty(payFlowId)) return;

		/* 在线支付订单的集合 */
		List<Order> payOnlineOrderList = new ArrayList<Order>();

		OrderCombined orderCombined = null;
		for(Order order : orderNewList){
			if(UtilHelper.isEmpty(order)) continue;
			if(!UtilHelper.isEmpty(order.getPayTypeId()) && SystemPayTypeEnum.PayOnline.getPayType().equals(order.getPayTypeId())){
				payOnlineOrderList.add(order);
				continue;
			}
			orderCombined = new OrderCombined();
			orderCombined.setCustId(currentLoginCustId);
			orderCombined.setCreateTime(systemDateMapper.getSystemDate());
			orderCombined.setCombinedNumber(1);//合并订单数
			//todo 价格相关
			orderCombined.setCopeTotal(order.getOrderTotal());//	应付总价
			orderCombined.setPocketTotal(order.getFinalPay());//实付总价
			orderCombined.setFreightPrice(order.getFreight());//运费
			orderCombined.setPayFlowId(payFlowId);
			orderCombinedMapper.save(orderCombined);
			List<OrderCombined> orderCombinedList = orderCombinedMapper.listByProperty(orderCombined);
			if(!UtilHelper.isEmpty(orderCombinedList)){
				orderCombined = orderCombinedList.get(0);
			}
			if(!UtilHelper.isEmpty(orderCombined) && !UtilHelper.isEmpty(orderCombined.getOrderCombinedId())){
				order.setOrderCombinedId(orderCombined.getOrderCombinedId());
				orderMapper.update(order);
			}
		}

		/* 合并在线支付方式的订单 */
		if(!UtilHelper.isEmpty(payOnlineOrderList)){
			BigDecimal allCopeTotal = new BigDecimal(0);   //应付总价
			BigDecimal allPocketTotal = new BigDecimal(0); //实付总价
			BigDecimal allFreightPrice = new BigDecimal(0); //总运费
			for(Order order : payOnlineOrderList){
				//todo 价格相关
				allCopeTotal = allCopeTotal.add(order.getOrderTotal());
				allPocketTotal = allPocketTotal.add(order.getFinalPay());
				allFreightPrice = allFreightPrice.add(order.getFreight());

			}
			orderCombined = new OrderCombined();
			orderCombined.setCustId(currentLoginCustId);
			orderCombined.setCreateTime(systemDateMapper.getSystemDate());
			orderCombined.setCombinedNumber(payOnlineOrderList.size());//合并订单数
			orderCombined.setCopeTotal(allCopeTotal);//	应付总价
			orderCombined.setPocketTotal(allPocketTotal);//实付总价
			orderCombined.setFreightPrice(allFreightPrice);//运费
			orderCombined.setPayFlowId(payFlowId);
			orderCombinedMapper.save(orderCombined);
			List<OrderCombined> orderCombinedList = orderCombinedMapper.listByProperty(orderCombined);
			if(!UtilHelper.isEmpty(orderCombinedList)){
				orderCombined = orderCombinedList.get(0);
			}
			/* 将order_combined_id回写到order表 */
			for(Order order : payOnlineOrderList){
				if(!UtilHelper.isEmpty(orderCombined) && !UtilHelper.isEmpty(orderCombined.getOrderCombinedId())){
					order.setOrderCombinedId(orderCombined.getOrderCombinedId());
					orderMapper.update(order);
				}
			}
		}

	}

	/**
	 * 插入订单支付表
	 * @param orderNewList 新生成订单的集合
	 * @param currentLoginCustId 当前登陆人的企业id
	 * @param payFlowId 支付流水号
	 * @throws Exception
     */
	private void insertOrderPay(List<Order> orderNewList,Integer currentLoginCustId,String payFlowId) throws Exception {
		if(UtilHelper.isEmpty(orderNewList)) return;
		if(UtilHelper.isEmpty(currentLoginCustId)) return;
		if(UtilHelper.isEmpty(payFlowId)) return;
		OrderPay orderPay = null ;
		for(Order order : orderNewList){
			orderPay = new OrderPay();
			orderPay.setOrderId(order.getOrderId());
			orderPay.setFlowId(order.getFlowId());
			orderPay.setPayFlowId(payFlowId);
			orderPay.setPayTypeId(order.getPayTypeId());
			orderPay.setCreateTime(systemDateMapper.getSystemDate());
			orderPay.setPayStatus(OrderPayStatusEnum.UN_PAYED.getPayStatus());
			orderPay.setCreateUser(currentLoginCustId + "");
			orderPayMapper.save(orderPay);
		}
	}

	/**
	 * 插入订单跟踪信息表
	 * @param order
     */
	private void insertOrderTrace(Order order) {
		if(UtilHelper.isEmpty(order)) return;
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(order.getOrderId());
		orderTrace.setOrderStatus(order.getOrderStatus());
		orderTrace.setCreateTime(systemDateMapper.getSystemDate());
		orderTraceMapper.save(orderTrace);
	}

	/**
	 * 插入订单收发货表
	 * @param order
	 * @param orderDeliveryDto
     */
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
		if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
			throw new Exception("非法参数");
		}
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
			order.setOrderStatus(SystemOrderStatusEnum.BuyerOrdered.getType());
			orderFlowIdPrefix = CommonType.ORDER_OFFLINE_PAY_PREFIX;

		/* 在线支付 */
		}else if(SystemPayTypeEnum.PayOnline.getPayType().equals(  systemPayType.getPayType() )){
			order.setOrderStatus(SystemOrderStatusEnum.BuyerOrdered.getType());
			orderFlowIdPrefix = CommonType.ORDER_ONLINE_PAY_PREFIX;

		/* 账期支付 */
		}else if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(  systemPayType.getPayType() )){
			order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
			orderFlowIdPrefix = CommonType.ORDER_PERIOD_TERM_PAY_PREFIX;
		}
		order.setCreateTime(systemDateMapper.getSystemDate());
		order.setCreateUser("");
		order.setTotalCount( orderDto.getProductInfoDtoList().size());

		/* 订单金额相关 */
		order.setOrderTotal(orderDto.getOrderTotal());//订单总金额
		order.setFreight(orderDto.getFreight());//运费
		order.setPreferentialMoney(orderDto.getPreferentialMoney());//优惠了的金额(如果使用了优惠)
		order.setOrgTotal(orderDto.getOrgTotal());//订单优惠后的金额(如果使用了优惠)
		order.setSettlementMoney(orderDto.getSettlementMoney());//结算金额
		order.setFinalPay(orderDto.getFinalPay());//最后支付金额

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
		ProductInfo productInfo = null;
		List<ProductInfoDto> productInfoDtoList = orderDto.getProductInfoDtoList();
		for(ProductInfoDto productInfoDto : productInfoDtoList){
			if(UtilHelper.isEmpty(productInfoDto)){
				continue;
			}
			productInfo = productInfoMapper.getByPK(productInfoDto.getId());
			if(UtilHelper.isEmpty(productInfo)) {
				continue;
			}
			orderDetail = new OrderDetail();
			orderDetail.setOrderId(order.getOrderId());
			orderDetail.setSupplyId(order.getSupplyId());//供应商ID
			orderDetail.setCreateTime(systemDateMapper.getSystemDate());
			orderDetail.setCreateUser("");

			//TODO 商品信息
			orderDetail.setProductPrice(productInfoDto.getProductPrice());
			orderDetail.setProductCount(productInfoDto.getProductCount());
			orderDetail.setProductId(productInfo.getId());
			orderDetail.setProductName(productInfo.getProductName());//商品名称
			orderDetail.setProductCode(productInfo.getProductCode());//商品编码
			orderDetail.setSpecification(productInfo.getSpec());//商品规格
			orderDetail.setBrandName(productInfo.getBrandId() + "");//todo 品牌名称
			orderDetail.setFormOfDrug(productInfo.getDrugformType());//剂型
			orderDetail.setManufactures(productInfo.getFactoryName());//生产厂家
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
	 * @param order
	 * @throws Exception
	 */
	public OrderDetailsDto getOrderDetails(Order order) throws Exception{
		OrderDetailsDto orderDetailsdto=orderMapper.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsdto)){
			return null;
		}
			//加载导入的批号信息，如果有一条失败则状态为失败否则查询成功数据
			OrderDeliveryDetail orderDeliveryDetail=new OrderDeliveryDetail();
			orderDeliveryDetail.setFlowId(order.getFlowId());
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
	 * 采购商订单管理
	 * @param pagination
	 * @param orderDto
     * @return
     */
    public Map<String, Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto) {
        if(UtilHelper.isEmpty(orderDto))
            throw new RuntimeException("参数错误");
		log.debug("request orderDto :"+orderDto.toString());
		if(!UtilHelper.isEmpty(orderDto.getCreateEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderDto.getCreateEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderDto.setCreateEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderDto.getCreateEndTime());
				e.printStackTrace();
				throw new RuntimeException("日期错误");
			}

        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取订单列表
        List<OrderDto> buyerOrderList = orderMapper.listPgBuyerOrder(pagination, orderDto);
        pagination.setResultList(buyerOrderList);

		//获取各订单状态下的订单数量
        List<OrderDto> orderDtoList = orderMapper.findOrderStatusCount(orderDto);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付
        BuyerOrderStatusEnum buyerorderstatusenum;
        if (!UtilHelper.isEmpty(orderDtoList)) {
            for (OrderDto od : orderDtoList) {
				//获取买家视角订单状态
                buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),payType);
                if(buyerorderstatusenum != null){
					//统计买家视角订单数
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
					//获取买家视角订单状态
                    buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
                    if(!UtilHelper.isEmpty(buyerorderstatusenum))
                        od.setOrderStatusName(buyerorderstatusenum.getValue());
                    else
                        od.setOrderStatusName("未知类型");
                }
				//统计订单总额
                if(!UtilHelper.isEmpty(od.getOrderTotal()))
                    orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
				//在线支付 + 未付款订单
				if(!UtilHelper.isEmpty(od.getNowTime()) && 1 == od.getPayType() && SystemOrderStatusEnum.BuyerOrdered.getType().equals(od.getOrderStatus())){
					try {
						time = DateUtils.getSeconds(od.getCreateTime(),od.getNowTime());
						//计算当前时间和支付剩余24小时的剩余秒数
						time = CommonType.PAY_TIME*60*60-time;
						if(time < 0)
							time = 0l;
						od.setResidualTime(time);
					} catch (ParseException e) {
						log.debug("date format error"+od.getCreateTime()+" "+od.getNowTime());
						e.printStackTrace();
						throw new RuntimeException("日期转换错误");
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
        resultMap.put("buyerOrderList", pagination);
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

	/**
	 * 采购商取消订单
	 * @param custId
	 * @param orderId
	 */
	public void  updateOrderStatusForBuyer(Integer custId,Integer orderId){
		if(UtilHelper.isEmpty(custId) || UtilHelper.isEmpty(orderId)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getByPK(orderId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该买家
		if(custId == order.getCustId()){
			if(SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){//已下单订单
				order.setOrderStatus(SystemOrderStatusEnum.BuyerCanceled.getType());//标记订单为用户取消状态
				String now = systemDateMapper.getSystemDate();
				// TODO: 2016/8/1 需获取登录用户信息
				order.setUpdateUser("zhangsan");
				order.setUpdateTime(now);
				int count = orderMapper.update(order);
				if(count == 0){
					log.error("order info :"+order);
					throw new RuntimeException("订单取消失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(order.getOrderId());
				orderTrace.setNodeName("买家取消订单");
				orderTrace.setDealStaff("zhangsan");
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff("zhangsan");
				orderTrace.setOrderStatus(order.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser("zhangsan");
				orderTraceMapper.save(orderTrace);
			}else{
				log.error("order status error ,orderStatus:"+order.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}
		}else{
			log.error("db orderId not equals to request orderId ,orderId:"+orderId+",db orderId:"+order.getOrderId());
			throw new RuntimeException("未找到订单");
		}
	}


	/**
	 * 销售订单查询
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public Map<String, Object> listPgSellerOrder(Pagination<OrderDto> pagination, OrderDto orderDto) {
		if(UtilHelper.isEmpty(orderDto))
			throw new RuntimeException("参数错误");
		log.debug("request orderDto :"+orderDto.toString());
		if(!UtilHelper.isEmpty(orderDto.getCreateEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderDto.getCreateEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderDto.setCreateEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderDto.getCreateEndTime());
				e.printStackTrace();
				throw new RuntimeException("日期错误");
			}

		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//销售订单列表
		List<OrderDto> sellerOrderList = orderMapper.listPgSellerOrder(pagination, orderDto);
		pagination.setResultList(sellerOrderList);

		//获取各订单状态下的订单数量
		List<OrderDto> orderDtoList = orderMapper.findSellerOrderStatusCount(orderDto);
		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付
		SellerOrderStatusEnum sellerOrderStatusEnum;
		if (!UtilHelper.isEmpty(orderDtoList)) {
			for (OrderDto od : orderDtoList) {
				//卖家视角订单状态
				sellerOrderStatusEnum = getSellerOrderStatus(od.getOrderStatus(),payType);
				if(sellerOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(sellerOrderStatusEnum.getType())){
						orderStatusCountMap.put(sellerOrderStatusEnum.getType(),orderStatusCountMap.get(sellerOrderStatusEnum.getType())+od.getOrderCount());
					}else{
						orderStatusCountMap.put(sellerOrderStatusEnum.getType(),od.getOrderCount());
					}
				}
			}
		}

		BigDecimal orderTotalMoney = new BigDecimal(0);
		int orderCount = 0;
		if(!UtilHelper.isEmpty(sellerOrderList)){
			orderCount = sellerOrderList.size();
			for(OrderDto od : sellerOrderList){
				if(!UtilHelper.isEmpty(od.getOrderStatus()) && !UtilHelper.isEmpty(od.getPayType())){
					//卖家视角订单状态
					sellerOrderStatusEnum = getSellerOrderStatus(od.getOrderStatus(),od.getPayType());
					if(!UtilHelper.isEmpty(sellerOrderStatusEnum))
						od.setOrderStatusName(sellerOrderStatusEnum.getValue());
					else
						od.setOrderStatusName("未知类型");
				}
				//统计订单总额
				if(!UtilHelper.isEmpty(od.getOrderTotal()))
					orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
			}
		}

		log.debug("orderStatusCount====>" + orderStatusCountMap);
		log.debug("orderList====>" + orderDtoList);
		log.debug("buyerOrderList====>" + sellerOrderList);
		log.debug("orderCount====>" + orderCount);
		log.debug("orderTotalMoney====>" + orderTotalMoney);

		resultMap.put("orderStatusCount", orderStatusCountMap);
		resultMap.put("buyerOrderList", pagination);
		resultMap.put("orderCount", orderCount);
		resultMap.put("orderTotalMoney", orderTotalMoney);
		return resultMap;
	}

	/**
	 * 采购商取消订单
	 * @param custId
	 * @param orderId
	 */
	public void  updateOrderStatusForSeller(Integer custId,Integer orderId,String cancelResult){
		if(UtilHelper.isEmpty(custId) || UtilHelper.isEmpty(orderId) || UtilHelper.isEmpty(cancelResult)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getByPK(orderId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该卖家
		if(custId == order.getSupplyId()){
			if(SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus()) || SystemOrderStatusEnum.BuyerAlreadyPaid.getType().equals(order.getOrderStatus())){//已下单订单+买家已付款订单
				order.setOrderStatus(SystemOrderStatusEnum.SellerCanceled.getType());//标记订单为卖家取消状态
				String now = systemDateMapper.getSystemDate();
				order.setCancelResult(cancelResult);
				// TODO: 2016/8/1 需获取登录用户信息
				order.setUpdateUser("zhangsan");
				order.setUpdateTime(now);
				int count = orderMapper.update(order);
				if(count == 0){
					log.error("order info :"+order);
					throw new RuntimeException("订单取消失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(order.getOrderId());
				orderTrace.setNodeName("卖家取消订单");
				orderTrace.setDealStaff("zhangsan");
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff("zhangsan");
				orderTrace.setOrderStatus(order.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser("zhangsan");
				orderTraceMapper.save(orderTrace);
			}else{
				log.error("order status error ,orderStatus:"+order.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}
		}else{
			log.error("db orderId not equals to request orderId ,orderId:"+orderId+",db orderId:"+order.getOrderId());
			throw new RuntimeException("未找到订单");
		}
	}

	/**
	 * 检查订单页的数据
	 * @return
     */
	public Map<String,Object> checkOrderPage() throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		//TODO 获取当前登陆用户的的企业id
		Integer currentLoginCustId = 123;

		UsermanageEnterprise enterprise = enterpriseMapper.getByPK(currentLoginCustId);
		if(UtilHelper.isEmpty(enterprise)){
			throw new Exception("非法参数");
		}

		/* 获取买家用户的收货地址列表 */
		UsermanageReceiverAddress receiverAddress = new UsermanageReceiverAddress();
		receiverAddress.setEnterpriseId(enterprise.getEnterpriseId());
		List<UsermanageReceiverAddress> receiverAddressList = receiverAddressMapper.listByProperty(receiverAddress);
		resultMap.put("receiveAddressList",receiverAddressList );


		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(currentLoginCustId);
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);
		resultMap.put("allShoppingCart",allShoppingCart);
		return resultMap;
	}

	/**
	 * 导出销售订单
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public byte[] exportOrder(Pagination<OrderDto> pagination, OrderDto orderDto){
		//销售订单列表
		List<OrderDto> list = orderMapper.listPgSellerOrder(pagination, orderDto);
		String[] headers={};
		List<Object[]> dataset = new ArrayList<Object[]>();
		SellerOrderStatusEnum sellerOrderStatusEnum;
		for(OrderDto order:list) {
			String orderStatusName="未知类型";
			String billTypeName="未知类型";
			if(!UtilHelper.isEmpty(order.getOrderStatus()) && !UtilHelper.isEmpty(order.getPayType())){
				//卖家视角订单状态
				sellerOrderStatusEnum = getSellerOrderStatus(order.getOrderStatus(), order.getPayType());
				if(!UtilHelper.isEmpty(sellerOrderStatusEnum)){
					orderStatusName=sellerOrderStatusEnum.getValue();
				}
			}
			if(!UtilHelper.isEmpty(order.getBillType())){
				if(order.getBillType()==1){
					billTypeName="增值税专用发票";
				}else if(order.getBillType()==2){
					billTypeName="增值税普通发票";
				}
			}
			dataset.add(new Object[]{"下单时间",order.getCreateTime(),"订单号",order.getFlowId(),"订单状态",orderStatusName,"发票类型",billTypeName});
			dataset.add(new Object[]{"采购商",order.getCustName(),"收货人",order.getOrderDelivery().getReceivePerson(),"收货地址",order.getOrderDelivery().getReceiveAddress(),"联系方式",order.getOrderDelivery().getReceiveContactPhone()});
			dataset.add(new Object[]{"商品编码","品名","规格","厂商","单价（元）","数量","金额（元）","促销信息"});
			Double productTotal=0d;
			for (OrderDetail orderDetail : order.getOrderDetailList()) {
				BigDecimal totalPrice = orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductCount() + ""));
				dataset.add(new Object[]{orderDetail.getProductCode(),orderDetail.getProductName(),orderDetail.getSpecification(),orderDetail.getManufactures(),orderDetail.getProductPrice(),orderDetail.getProductCount(),totalPrice.doubleValue(),""});
				productTotal+=totalPrice.doubleValue();
			}
			dataset.add(new Object[]{"商品金额（元）", productTotal, "优惠券（元）", order.getPreferentialMoney(), "订单金额（元）", order.getOrderTotal(), "", ""});
			dataset.add(new Object[]{"买家留言", order.getLeaveMessage(), "", "", "", "", "", ""});
			dataset.add(new Object[]{});
		}
		return  ExcelUtil.exportExcel("订单信息", headers, dataset);
	}

	/**
	 * 系统自动取消订单
	 * 1在线支付订单24小时系统自动取消
	 * 2 线下支付7天后未确认收款系统自动取消
	 * @return
	 */
	public void cancelOrderForNoPay(){
		orderMapper.cancelOrderForNoPay();
	}

	/**
	 * 系统自动取消订单
	 * 订单7个自然日未发货系统自动取消
	 * @return
	 */
	public void cancelOrderFor7DayNoDelivery(){
		List<Order> lo=orderMapper.listOrderFor7DayNoDelivery();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			//根据订单来源进行在线退款 二期对接

			if(true){//退款成功
				cal.add(od.getOrderId());
			}
		}
		//取消订单
		orderMapper. cancelOrderFor7DayNoDelivery(cal);
	}

	/**
	 * 系统自动确认收货
	 * 订单发货后7个自然日后系统自动确认收货
	 * @return
	 */
	public void doneOrderForDeliveryAfter7Day(){
		List<Order> lo=orderMapper.listOrderForDeliveryAfter7Day();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			//根据订单来源进行自动分账 二期对接
			if(true){//分账成功
				cal.add(od.getOrderId());
			}
		}
		//确认收货
		orderMapper. cancelOrderFor7DayNoDelivery(cal);
	}
	
	/**
	 * 收款确认
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public void addForConfirmMoney(Integer custId,OrderSettlement orderSettlement) throws Exception
	{
		Order order = orderMapper.getByPK(orderSettlement.getOrderId());
		if(UtilHelper.isEmpty(order)||!custId.equals(order.getSupplyId())){
			throw new RuntimeException("未找到订单");
		}
		String now = systemDateMapper.getSystemDate();
		orderSettlement.setBusinessType(1);
		orderSettlement.setFlowId(order.getFlowId());
		orderSettlement.setCustId(order.getCustId());
		orderSettlement.setCustName(order.getCustName());
		orderSettlement.setSupplyId(order.getSupplyId());
		orderSettlement.setSupplyName(order.getSupplyName());
		orderSettlement.setConfirmSettlement("1");
		orderSettlement.setPayTypeId(order.getPayTypeId());
		orderSettlement.setSettlementTime(now);
		orderSettlement.setCreateUser(order.getCustName());
		orderSettlement.setCreateTime(now);
		orderSettlementMapper.save(orderSettlement);
		//TODO 订单记录表
		insertOrderTrace(order);
		order.setFinalPay(orderSettlement.getSettlementMoney());
		order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
		order.setConfirmSettlement("1");
		order.setSettlementMoney(orderSettlement.getSettlementMoney());
		order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
		order.setPayTime(now);
		order.setSettlementTime(now);
		order.setUpdateUser(order.getCustName());
		order.setUpdateTime(now);
		orderMapper.update(order);
	}
}
