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
	private OrderExceptionMapper  orderExceptionMapper;

    @Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private OrderExceptionService OrderExceptionService;

	@Autowired
	public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper)
	{
		this.orderExceptionMapper = orderExceptionMapper;
	}

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
	 * 拆单逻辑：
	 * 		  按供应商为单位，拆成一个单 （一期需求）
	 * 		  按供应商为单位，且对该供应商下的每个商品进行检查。如果是合法的账期商品，则该商品就生成一个订单，其余商品和非法账期商品则合成一个订单 （二期需求）
	 *
	 * @param orderCreateDto
	 * @throws Exception
	 */
	public Map<String,Object> createOrder(OrderCreateDto orderCreateDto) throws Exception{

		if(UtilHelper.isEmpty(orderCreateDto) || UtilHelper.isEmpty(orderCreateDto.getReceiveAddressId()) || UtilHelper.isEmpty(orderCreateDto.getBillType())
				|| UtilHelper.isEmpty(orderCreateDto.getOrderDtoList()) || UtilHelper.isEmpty(orderCreateDto.getUserDto())){
			throw  new Exception("非法参数");
		}
		log.info("创建订单接口-请求参数orderCreateDto：" + orderCreateDto);

		/* 获取当前登陆用户的企业id */
		Integer currentLoginEnterpriseId = orderCreateDto.getUserDto().getCustId();
		if(UtilHelper.isEmpty(currentLoginEnterpriseId)) throw new Exception("非法参数");
		UsermanageEnterprise enterprise = enterpriseMapper.getByEnterpriseId(currentLoginEnterpriseId + "");
		log.info("创建订单接口-当前登陆用户信息enterprise：" + enterprise);
		if(UtilHelper.isEmpty(enterprise)){
			throw new Exception("用户不存在");
		}

        /* 订单配送信息 */
		OrderDelivery orderDelivery = handlerOrderDelivery(enterprise,orderCreateDto);
		log.info("创建订单接口-订单配送信息orderDelivery ：" + orderDelivery);

		/* 创建支付流水号 */
		String payFlowId = RandomUtil.createOrderPayFlowId(systemDateMapper.getSystemDateByformatter("%Y%m%d%H%i%s"),currentLoginEnterpriseId);
		log.info("创建订单接口-创建支付流水号,payFlowId = " + payFlowId);

		List<Order> orderNewList =  new ArrayList<Order>();
		/* 遍历订单数据中的各个供应商,即按供应商为单位拆单(一期需求) */
		for (OrderDto orderDto : orderCreateDto.getOrderDtoList()){
			if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
				continue;
			}

            /* 校验所购买商品的合法性 */
			validateProducts(currentLoginEnterpriseId,orderDto);

            /* 创建订单相关的所有信息 */
			if(UtilHelper.isEmpty(orderDto.getBillType())){
				orderDto.setBillType(orderCreateDto.getBillType());
			}

			/* 如果含有合法的账期商品，则跳过该商品，继续生成订单 */
			if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(orderDto.getPayTypeId())){
				if(orderDto.getPaymentTerm() == null || orderDto.getPaymentTerm() <= 0 ||  orderDto.getAccountAmount() <= 0){
					/* 选择账期支付的前提条件：既要资信有额度 又要设置客户账期 */
					throw new Exception("非法订单!");
				}
				orderDto = removePeriodTermOrderDto(orderDto);
				if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
					continue;
				}
			}
			Order orderNew = createOrderInfo(orderDto,orderDelivery,orderCreateDto.getUserDto(),payFlowId);
			if(null == orderNew) continue;
			orderNewList.add(orderNew);
		}
		log.info("创建订单接口-生成的订单数据(以供应商为单位拆单),orderNewList = " + orderNewList);
		Map<String,Object> resultMap = new HashMap<>();



		/* 从原始订单信息中筛选出合法的账期商品信息 */
		List<OrderDto> periodTermOrderDtoList = getPeriodTermOrderDtoList(orderCreateDto.getOrderDtoList());
		/* 创建账期订单 */
		List<Order> periodTermOrderList =  createPeriodTermOrder(periodTermOrderDtoList,orderDelivery,orderCreateDto.getUserDto());
		log.info("创建订单接口-生成的订单数据(以账期商品为单位拆单),periodTermOrderList = " + periodTermOrderList);
		resultMap.put("periodTermOrderList",periodTermOrderList);

		/* 合并生成的订单数据 */
		if (!UtilHelper.isEmpty(periodTermOrderList)){
			orderNewList.addAll(periodTermOrderList);
		}
		log.info("创建订单接口-完成，返回数据,orderNewList = " + orderNewList);
		resultMap.put("orderNewList",orderNewList);
        return resultMap;
    }

	/**
	 * 筛选出选择账期支付方式的商品信息
	 * @param orderDtoList 确认订单页面中 组装的订单商品数据（页面原始数据）
	 * @return 符合账期支付订单规则的数据(新的数据)
     */
	private List<OrderDto> getPeriodTermOrderDtoList(List<OrderDto> orderDtoList) {
		if(UtilHelper.isEmpty(orderDtoList)) return null;

		List<OrderDto> periodTermOrderDtoList = new ArrayList<OrderDto>();
		OrderDto  periodTermOrderDto = null;


		for (OrderDto orderDto : orderDtoList) {
			if(UtilHelper.isEmpty(orderDto)) continue;

			List<ProductInfoDto> productInfoDtoList = new ArrayList<ProductInfoDto>();
			/* 遍历该供商下的所有商品 */
			for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
				if(UtilHelper.isEmpty(productInfoDto)) continue;

				/* 校验该商品是否符合账期订单的规则 */
				if (validateRulesOfPeriodTermOrder(orderDto,productInfoDto)) {
					productInfoDtoList.add(productInfoDto);
				}
			}
			if(!UtilHelper.isEmpty(productInfoDtoList)){
				periodTermOrderDto = new OrderDto();
				periodTermOrderDto.setCustId(orderDto.getCustId());
				periodTermOrderDto.setCustName(orderDto.getCustName());
				periodTermOrderDto.setSupplyId(orderDto.getSupplyId());
				periodTermOrderDto.setSupplyName(orderDto.getSupplyName());
				periodTermOrderDto.setProductInfoDtoList(productInfoDtoList);
				periodTermOrderDto.setPaymentTerm(orderDto.getPaymentTerm());
				periodTermOrderDto.setPayTypeId(orderDto.getPayTypeId());
				periodTermOrderDto.setBillType(orderDto.getBillType());
				periodTermOrderDto.setLeaveMessage(orderDto.getLeaveMessage());
				periodTermOrderDtoList.add(periodTermOrderDto);
			}
		}
		return periodTermOrderDtoList;
	}

	/**
	 * 移除原始订单数据中的(合法)账期商品，
	 * 目的：为了让生成账期订单的流程不影响正常的下单流程
	 * @param orderDto
	 * @return
     */
	private OrderDto removePeriodTermOrderDto(OrderDto orderDto) {
		if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
			return null;
		}

		OrderDto orderDtoNew = null;

		List<ProductInfoDto> productInfoDtoList = new ArrayList<ProductInfoDto>();
		/* 遍历该供商下的所有商品 */
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(UtilHelper.isEmpty(productInfoDto)) continue;

			/* 校验该商品是否符合账期订单的规则 */
			if (validateRulesOfPeriodTermOrder(orderDto,productInfoDto)) {
				continue;
			}else{
				productInfoDtoList.add(productInfoDto);
			}
		}
		if(!UtilHelper.isEmpty(productInfoDtoList)){
			orderDtoNew = new OrderDto();
			orderDtoNew.setCustId(orderDto.getCustId());
			orderDtoNew.setCustName(orderDto.getCustName());
			orderDtoNew.setSupplyId(orderDto.getSupplyId());
			orderDtoNew.setSupplyName(orderDto.getSupplyName());
			orderDtoNew.setProductInfoDtoList(productInfoDtoList);
			orderDtoNew.setPayTypeId(orderDto.getPayTypeId());
			orderDtoNew.setBillType(orderDto.getBillType());
			orderDtoNew.setLeaveMessage(orderDto.getLeaveMessage());
			orderDtoNew.setPaymentTerm(orderDto.getPaymentTerm());
		}
		return orderDtoNew;
	}

	/**
	 * 校验该商品是否符合账期订单的规则
	 * @param orderDto 组装的订单信息
	 * @param productInfoDto 单个商品信息
     * @return
     */
	private boolean validateRulesOfPeriodTermOrder(OrderDto orderDto,ProductInfoDto productInfoDto) {
		if(UtilHelper.isEmpty(orderDto) ){
			return false;
		}
		if(UtilHelper.isEmpty(orderDto.getPayTypeId()) ){
			return false;
		}
		if(UtilHelper.isEmpty(productInfoDto)){
			return false;
		}
		/* 选择了账期支付方式 */
		if(!SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(orderDto.getPayTypeId())){
			return false;
		}
		/* 资信额度足够 则可以生成账期订单 */
		if(orderDto.getAccountAmount()  == 1  && productInfoDto.getPaymentTerm() > 0){
			return true;
		}
		return false;
	}

	/**
	 *  创建账期订单(一个商品就生成一个订单)
	 * @param periodTermOrderDtoList 筛选后的（选择账期支付方式的)商品信息
	 * @param orderDelivery 订单发货信息
	 * @param userDto			买家用户信息
     * @return
     */
	private List<Order> createPeriodTermOrder(List<OrderDto> periodTermOrderDtoList, OrderDelivery orderDelivery, UserDto userDto) throws Exception {
		if( UtilHelper.isEmpty(periodTermOrderDtoList) || UtilHelper.isEmpty(orderDelivery) || UtilHelper.isEmpty(userDto)){
			return null;
		}
		List<Order> orderNewList =  new ArrayList<Order>();
		OrderDto orderDtoNew = null;
		for (OrderDto orderDto : periodTermOrderDtoList){
			if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
				continue;
			}

			ProductInfoDto productInfoDto = null;
			List<ProductInfoDto> productInfoDtoList = new ArrayList<>();
			for(int i = 0 ;i < orderDto.getProductInfoDtoList().size() ; i++){
				productInfoDto = orderDto.getProductInfoDtoList().get(i);
				if(UtilHelper.isEmpty(productInfoDto )) continue;

				productInfoDtoList.clear();
				productInfoDtoList.add(productInfoDto);

				orderDtoNew = new OrderDto();
				orderDtoNew.setCustId(orderDto.getCustId());
				orderDtoNew.setCustName(orderDto.getCustName());
				orderDtoNew.setSupplyId(orderDto.getSupplyId());
				orderDtoNew.setSupplyName(orderDto.getSupplyName());
				orderDtoNew.setBillType(orderDto.getBillType());
				orderDtoNew.setPayTypeId(orderDto.getPayTypeId());
				orderDtoNew.setLeaveMessage(orderDto.getLeaveMessage());
				/* 设置了账期的商品，插入到订单表的账期字段时，使用商品的账期。否则使用客户的账期 */
				int paymentTerm = !UtilHelper.isEmpty(productInfoDto.getPaymentTerm()) && productInfoDto.getPaymentTerm() > 0
						? productInfoDto.getPaymentTerm()
						: !UtilHelper.isEmpty(orderDto.getPaymentTerm()) && orderDto.getPaymentTerm() > 0 ? orderDto.getPaymentTerm() : 0 ;
				orderDtoNew.setPaymentTerm(paymentTerm);
				orderDtoNew.setProductInfoDtoList(productInfoDtoList);

				/* 创建支付流水号 */
				String payFlowId = RandomUtil.createOrderPayFlowId(systemDateMapper.getSystemDateByformatter("%Y%m%d%H%i%s"),userDto.getCustId());
				payFlowId += "-" + i;

				Order orderNew = createOrderInfo(orderDtoNew,orderDelivery,userDto,payFlowId);
				if(!UtilHelper.isEmpty(orderNew)){
					/* 账期订单生成结算数据 */
					//savePaymentDateSettlement(userDto,orderNew.getOrderId()); 需求变量 在确认收货时生成结算数据
					orderNewList.add(orderNew);
				}
			}
		}
		return orderNewList;
	}



	private OrderDelivery handlerOrderDelivery(UsermanageEnterprise enterprise, OrderCreateDto orderCreateDto) throws Exception {
		if (UtilHelper.isEmpty(orderCreateDto.getCustId()) || UtilHelper.isEmpty(orderCreateDto.getReceiveAddressId())
				|| !(orderCreateDto.getCustId() + "").equals(enterprise.getEnterpriseId()) ) {
			throw  new Exception("非法参数");
		}
		UsermanageReceiverAddress receiverAddress = receiverAddressMapper.getByPK(orderCreateDto.getReceiveAddressId());
		if(UtilHelper.isEmpty(receiverAddress) || UtilHelper.isEmpty(receiverAddress.getEnterpriseId()) || !receiverAddress.getEnterpriseId().equals(enterprise.getEnterpriseId())){
			throw new Exception("非法参数");
		}
		OrderDelivery  orderDelivery = new OrderDelivery();
		orderDelivery.setReceivePerson(receiverAddress.getReceiverName());
		orderDelivery.setReceiveProvince(receiverAddress.getProvinceCode());
		orderDelivery.setReceiveCity(receiverAddress.getCityCode());
		orderDelivery.setReceiveRegion(receiverAddress.getDistrictCode());
		orderDelivery.setReceiveAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
		orderDelivery.setReceiveContactPhone(receiverAddress.getContactPhone());
		orderDelivery.setCreateTime(systemDateMapper.getSystemDate());
		orderDelivery.setCreateUser(orderCreateDto.getUserDto().getUserName());
		return orderDelivery;
	}


	/**
	 * 创建订单相关的所有信息
	 * @param orderDto 订单、商品相关信息
	 * @param orderDelivery 订单发货、配送相关信息
	 * @param userDto		买家用户信息
	 * @param payFlowId	支付流水号
	 * @return
	 */
	private Order createOrderInfo(OrderDto orderDto, OrderDelivery orderDelivery, UserDto userDto, String payFlowId) throws Exception{
		if( UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())
				|| UtilHelper.isEmpty(orderDelivery) || UtilHelper.isEmpty(payFlowId)){
			return null;
		}

		/* TODO  计算订单相关的价格 */
		log.info("创建订单接口 ：计算订单相关的价格,计算前orderDto = " + orderDto);
		orderDto = calculateOrderPrice(orderDto);
		log.info("创建订单接口 ：计算订单相关的价格,计算后orderDto = " + orderDto);

		/*  数据插入订单表、订单详情表 */
		Order order = insertOrderAndOrderDetail(orderDto,userDto);
		log.info("创建订单接口 ：插入订单表、订单详情表 order= " + order);

		/* 订单配送发货信息表 */
		insertOrderDeliver(order, orderDelivery);

		/* 订单跟踪信息表 */
		insertOrderTrace(order);

		/* 插入数据到订单支付表 */
		insertOrderPay(order, userDto, payFlowId);

		/* TODO 删除购物车 */
//		deleteShoppingCart(orderDto);

		/* TODO 短信、邮件等通知买家 */

		/* TODO 自动取消订单相关的定时任务 */

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
	 * 插入订单支付表
	 * @param order 新生成的订单
	 * @param userDto 当前登陆人的信息
	 * @param payFlowId 支付流水号
	 * @throws Exception
     */
	private void insertOrderPay(Order order, UserDto userDto, String payFlowId) throws Exception {
		if(UtilHelper.isEmpty(order)) return;
		if(UtilHelper.isEmpty(userDto)) return;
		if(UtilHelper.isEmpty(payFlowId)) return;
		OrderPay orderPay = new OrderPay();
		orderPay.setOrderId(order.getOrderId());
		orderPay.setFlowId(order.getFlowId());
		orderPay.setPayFlowId(payFlowId);
		orderPay.setPayTypeId(order.getPayTypeId());
		orderPay.setCreateTime(systemDateMapper.getSystemDate());
		orderPay.setPayStatus(OrderPayStatusEnum.UN_PAYED.getPayStatus());
		orderPay.setCreateUser(userDto.getUserName());
		orderPayMapper.save(orderPay);
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
		orderTrace.setCreateUser("");
		orderTraceMapper.save(orderTrace);
	}

	/**
	 * 插入订单收发货表
	 * @param order
	 * @param orderDelivery
     */
	private void insertOrderDeliver(Order order, OrderDelivery orderDelivery) {
		if(UtilHelper.isEmpty(order) || UtilHelper.isEmpty(orderDelivery)){
			return ;
		}
		orderDelivery.setOrderId(order.getOrderId());
		orderDelivery.setFlowId(order.getFlowId());
		log.info("创建订单接口 ：插入订单收发货表 orderDelivery= " + orderDelivery);
		orderDeliveryMapper.save(orderDelivery);
	}

	/**
	 * 数据插入订单表
	 * @param orderDto
	 * @return
	 * @throws Exception
     */
	private Order insertOrderAndOrderDetail(OrderDto orderDto,UserDto userDto)throws Exception {
		if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
			throw new Exception("非法参数");
		}
		Order order = new Order();
		order.setCustId(orderDto.getCustId());
		order.setCustName(orderDto.getCustName());
		order.setSupplyId(orderDto.getSupplyId());
		order.setSupplyName(orderDto.getSupplyName());
		order.setBillType(orderDto.getBillType());
		order.setLeaveMessage(orderDto.getLeaveMessage());
		if(UtilHelper.isEmpty(orderDto.getPayTypeId())){
			throw new Exception("非法支付类型");
		}

		order.setPayTypeId(orderDto.getPayTypeId());
		SystemPayType systemPayType = systemPayTypeService.getByPK(orderDto.getPayTypeId());
		String orderFlowIdPrefix = "";
		/* 线下支付 */
		if(SystemPayTypeEnum.PayOffline.getPayType().equals(  systemPayType.getPayType() )){
			orderFlowIdPrefix = CommonType.ORDER_OFFLINE_PAY_PREFIX;
			order.setPaymentTerm(0);
		/* 在线支付 */
		}else if(SystemPayTypeEnum.PayOnline.getPayType().equals(  systemPayType.getPayType() )){
			orderFlowIdPrefix = CommonType.ORDER_ONLINE_PAY_PREFIX;
			order.setPaymentTerm(0);
		/* 账期支付 */
		}else if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(  systemPayType.getPayType() )){
			orderFlowIdPrefix = CommonType.ORDER_PERIOD_TERM_PAY_PREFIX;
			order.setPaymentTerm(orderDto.getPaymentTerm());
		}

		order.setOrderStatus(SystemOrderStatusEnum.BuyerOrdered.getType());
		order.setCreateTime(systemDateMapper.getSystemDate());
		order.setCreateUser(userDto.getUserName());
		order.setTotalCount( orderDto.getProductInfoDtoList().size());

		/* 订单金额相关 */
		order.setOrderTotal(orderDto.getOrderTotal());//订单总金额
		order.setFreight(orderDto.getFreight());//运费
		order.setPreferentialMoney(orderDto.getPreferentialMoney());//优惠了的金额(如果使用了优惠)
		order.setOrgTotal(orderDto.getOrgTotal());//订单优惠后的金额(如果使用了优惠)
		order.setSettlementMoney(orderDto.getSettlementMoney());//结算金额
		order.setPaymentTermStatus(0);//账期还款状态 0 未还款  1 已还款

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
		log.info("更新数据到订单表：order参数=" + order);
		orderMapper.update(order);


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
			orderDetail.setCreateUser(userDto.getUserName());

			//TODO 商品信息
			orderDetail.setProductPrice(productInfoDto.getProductPrice());
			orderDetail.setProductCount(productInfoDto.getProductCount());
			orderDetail.setProductId(productInfo.getId());
			orderDetail.setProductName(productInfo.getProductName());//商品名称
			orderDetail.setProductCode(productInfo.getProductCode());//商品编码
			orderDetail.setSpecification(productInfo.getSpec());//商品规格
//			orderDetail.setBrandName(productInfo.getBrandId() + "");//todo 品牌名称
			orderDetail.setFormOfDrug(productInfo.getDrugformType());//剂型
			orderDetail.setManufactures(productInfo.getFactoryName());//生产厂家
			orderDetail.setShortName(productInfo.getShortName());//商品通用名
			log.info("更新数据到订单详情表：orderDetail参数=" + orderDetail);
			orderDetailService.save(orderDetail);
		}
		return order;
	}



	/**
	 * 校验要购买的商品(通用方法)
	 * @param currentLoginEnterpriseId 当前登陆人的企业id
	 * @param orderDto 要提交订单的商品数据
     * @return
     */
	public boolean validateProducts(Integer currentLoginEnterpriseId,OrderDto orderDto) throws Exception {
		if(UtilHelper.isEmpty(currentLoginEnterpriseId) || UtilHelper.isEmpty(orderDto)){
			log.info("统一校验订单商品接口-currentLoginCustId：" + currentLoginEnterpriseId +",orderDto=" + orderDto);
			throw new Exception("非法参数");
		}

		//TODO 校验采购商状态、资质
		UsermanageEnterprise buyer = enterpriseMapper.getByEnterpriseId(currentLoginEnterpriseId + "");
		if(UtilHelper.isEmpty(buyer)){
			log.info("统一校验订单商品接口-buyer ：" + buyer);
			throw new Exception("采购商不存在!");
		}
		orderDto.setCustId(Integer.valueOf(buyer.getEnterpriseId()));
		orderDto.setCustName(buyer.getEnterpriseName());

		//TODO 校验要供应商状态、资质
		UsermanageEnterprise seller = enterpriseMapper.getByEnterpriseId(orderDto.getSupplyId() + "");
		if(UtilHelper.isEmpty(seller)){
			log.info("统一校验订单商品接口-seller ：" + seller);
			throw new Exception("供应商不存在!");
		}
		orderDto.setSupplyName(seller.getEnterpriseName());

		//TODO 校验要采购商与供应商是否相同
		if (seller.getEnterpriseId().equals(currentLoginEnterpriseId + "") ){
			log.info("统一校验订单商品接口 ：不能购买自己的商品" );
			throw new Exception("不能购买自己的商品");
		}

		//TODO 校验供应商下的商品信息、价格
		List<ProductInfoDto> productInfoDtoList = orderDto.getProductInfoDtoList();
		if(UtilHelper.isEmpty(productInfoDtoList)){
			log.info("统一校验订单商品接口 ：商品不能为空!" );
			throw new Exception("商品不能为空!");
		}
		ProductInfo productInfo = null;
		for(ProductInfoDto productInfoDto : productInfoDtoList){
			if(UtilHelper.isEmpty(productInfoDto)) continue;
			productInfo =  productInfoMapper.getByPK(productInfoDto.getId());
			if(UtilHelper.isEmpty(productInfo )){
				log.info("统一校验订单商品接口 ：商品(productId=" + productInfoDto.getId() + ")不存在!" );
				throw new Exception("商品不存在!");
			}
			//TODO 商品状态、价格校验
		}

		//TODO 特殊校验(选择账期支付方式的订单，校验买家账期、商品账期)
		if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(orderDto.getPayTypeId())){
			//TODO 校验该供应的账期(是否存在、是否有效)
			//TODO 校验每个商品的账期(是否存在、是否有效)

		}
		log.info("统一校验订单商品接口 ：校验成功" );
		return true;
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
		//订单类型翻译

		if(UtilHelper.isEmpty(order.getCustId())){
			orderDetailsdto.setOrderStatusName(getSellerOrderStatus(orderDetailsdto.getOrderStatus(), orderDetailsdto.getPayType()).getValue());
		}else {
			orderDetailsdto.setOrderStatusName(getBuyerOrderStatus(orderDetailsdto.getOrderStatus(),orderDetailsdto.getPayType()).getValue());
		}
		//计算确认收货金额
		BigDecimal total=new BigDecimal(0);
		BigDecimal productTotal=new BigDecimal(0);
		for (OrderDetail detail:orderDetailsdto.getDetails())
		{
			BigDecimal proudcutCount=new BigDecimal(detail.getProductCount());
			if (!UtilHelper.isEmpty(detail.getRecieveCount())){
				BigDecimal count=new BigDecimal(detail.getRecieveCount());
				total=total.add(detail.getProductPrice().multiply(count));
			}
			productTotal=productTotal.add(detail.getProductPrice().multiply(proudcutCount));
		}

		if(orderDetailsdto.getPayType()==SystemPayTypeEnum.PayOffline.getPayType()){
			OrderSettlement orderSettlement=new OrderSettlement();
			orderSettlement.setOrderId(orderDetailsdto.getOrderId());
			orderSettlement.setBusinessType(1);
			orderSettlement.setPayTypeId(orderDetailsdto.getPayTypeId());
			List<OrderSettlement> settlements= orderSettlementMapper.listByProperty(orderSettlement);
			if(settlements.size()>0){
				orderDetailsdto.setSettlementRemark(settlements.get(0).getRemark());
			}

		}



		orderDetailsdto.setProductTotal(productTotal);
		orderDetailsdto.setReceiveTotal(total);
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
		List<OrderDto> buyerOrderList = orderMapper.listPaginationBuyerOrder(pagination, orderDto);
        pagination.setResultList(buyerOrderList);

		//获取各订单状态下的订单数量
        List<OrderDto> orderDtoList = orderMapper.findOrderStatusCount(orderDto);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        //int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付  0--为选择支付类型
        BuyerOrderStatusEnum buyerorderstatusenum = null;
        if (!UtilHelper.isEmpty(orderDtoList)) {
            for (OrderDto od : orderDtoList) {
				//获取买家视角订单状态
				buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
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

        BigDecimal orderTotalMoney = orderMapper.findBuyerOrderTotal(orderDto);
        int orderCount = 0;
		long time = 0l;
        if(!UtilHelper.isEmpty(buyerOrderList)){
            orderCount = pagination.getTotal();
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
                //if(!UtilHelper.isEmpty(od.getOrderTotal()))
                //    orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
				//在线支付 + 未付款订单
				if(!UtilHelper.isEmpty(od.getNowTime()) && !UtilHelper.isEmpty(od.getCreateTime()) && 1 == od.getPayType() && SystemOrderStatusEnum.BuyerOrdered.getType().equals(od.getOrderStatus())){
					try {
						time = DateUtils.getSeconds(od.getCreateTime(),od.getNowTime());
						if(time > 0){
							//计算当前时间和支付剩余24小时的剩余秒数
							time = CommonType.PAY_TIME*60*60-time;
						}
						if(time < 0)
							time = 0l;
						od.setResidualTime(time);
					} catch (ParseException e) {
						log.debug("date format error"+od.getCreateTime()+" "+od.getNowTime());
						e.printStackTrace();
						throw new RuntimeException("日期转换错误");
					}

				}
				//卖家已发货
				if(!UtilHelper.isEmpty(od.getNowTime()) && !UtilHelper.isEmpty(od.getDeliverTime()) && SystemOrderStatusEnum.SellerDelivered.getType().equals(od.getOrderStatus())){
					try {
						time = DateUtils.getSeconds(od.getDeliverTime(),od.getNowTime());
						if(time > 0){
							//计算自动确认时间与当前时间剩余秒数
							time = CommonType.AUTO_RECEIVE_TIME*60*60*24-time;
						}
						if(time < 0)
							time = 0l;
						od.setReceivedTime(time);
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
        resultMap.put("orderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
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
            } else if(payType == 1 || payType == 3){
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

		if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SellerCanceled.getType())) {//买家已取消+系统自动取消+后台取消+卖家已取消
            return BuyerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerPartReceived.getType()) ) {// 买家全部收货+系统自动确认收货+买家部分收货
            return BuyerOrderStatusEnum.Finished;//已完成
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return BuyerOrderStatusEnum.Finished;//已完成
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
            } else if(payType == 1 || payType == 3) {
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

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SellerCanceled.getType())) {//买家已取消+系统自动取消+后台取消+卖家已取消
            return SellerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerPartReceived.getType())) {// 买家全部收货+系统自动确认收货+买家部分收货
            return SellerOrderStatusEnum.Finished;//已完成
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return SellerOrderStatusEnum.Finished;//已完成
        }
        return null;
    }

	/**
	 * 采购商取消订单
	 * @param userDto
	 * @param orderId
	 */
	public void  updateOrderStatusForBuyer(UserDto userDto,Integer orderId){
		if(UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(orderId)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getByPK(orderId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该买家
		if(userDto.getCustId() == order.getCustId()){
			if(SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){//已下单订单
				order.setOrderStatus(SystemOrderStatusEnum.BuyerCanceled.getType());//标记订单为用户取消状态
				String now = systemDateMapper.getSystemDate();
				order.setUpdateUser(userDto.getUserName());
				order.setUpdateTime(now);
				order.setCancelTime(now);
				order.setCancelResult("买家主动取消");
				int count = orderMapper.update(order);
				if(count == 0){
					log.error("order info :"+order);
					throw new RuntimeException("订单取消失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(order.getOrderId());
				orderTrace.setNodeName("买家取消订单");
				orderTrace.setDealStaff(userDto.getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(userDto.getUserName());
				orderTrace.setOrderStatus(order.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(userDto.getUserName());
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
		List<OrderDto> sellerOrderList = orderMapper.listPaginationSellerOrder(pagination, orderDto);
		pagination.setResultList(sellerOrderList);

		//获取各订单状态下的订单数量
		List<OrderDto> orderDtoList = orderMapper.findSellerOrderStatusCount(orderDto);
		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		//int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付
		SellerOrderStatusEnum sellerOrderStatusEnum ;
		if (!UtilHelper.isEmpty(orderDtoList)) {
			for (OrderDto od : orderDtoList) {
				//卖家视角订单状态
				sellerOrderStatusEnum = getSellerOrderStatus(od.getOrderStatus(),od.getPayType());
				if(sellerOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(sellerOrderStatusEnum.getType())){
						orderStatusCountMap.put(sellerOrderStatusEnum.getType(),orderStatusCountMap.get(sellerOrderStatusEnum.getType())+od.getOrderCount());
					}else{
						orderStatusCountMap.put(sellerOrderStatusEnum.getType(),od.getOrderCount());
					}
				}
			}
		}

		BigDecimal orderTotalMoney = orderMapper.findSellerOrderTotal(orderDto);
		int orderCount = 0;
		if(!UtilHelper.isEmpty(sellerOrderList)){
			orderCount = pagination.getTotal();
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
				//if(!UtilHelper.isEmpty(od.getOrderTotal()))
				//	orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
			}
		}

		log.debug("orderStatusCount====>" + orderStatusCountMap);
		log.debug("orderList====>" + orderDtoList);
		log.debug("buyerOrderList====>" + sellerOrderList);
		log.debug("orderCount====>" + orderCount);
		log.debug("orderTotalMoney====>" + orderTotalMoney);

		resultMap.put("orderStatusCount", orderStatusCountMap);
		resultMap.put("sellerOrderList", pagination);
		resultMap.put("orderCount", orderCount);
		resultMap.put("orderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
		return resultMap;
	}

	/**
	 * 采购商取消订单
	 * @param userDto
	 * @param orderId
	 */
	public void  updateOrderStatusForSeller(UserDto userDto,Integer orderId,String cancelResult){
		if(UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(orderId) || UtilHelper.isEmpty(cancelResult)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getByPK(orderId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该卖家
		if(userDto.getCustId() == order.getSupplyId()){
			if((SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus()) ) || SystemOrderStatusEnum.BuyerAlreadyPaid.getType().equals(order.getOrderStatus())){//已下单订单+买家已付款订单
				order.setOrderStatus(SystemOrderStatusEnum.SellerCanceled.getType());//标记订单为卖家取消状态
				String now = systemDateMapper.getSystemDate();
				order.setCancelResult("卖家取消");
				order.setRemark(cancelResult);
				order.setUpdateUser(userDto.getUserName());
				order.setUpdateTime(now);
				order.setCancelTime(now);
				int count = orderMapper.update(order);
				if(count == 0){
					log.error("order info :"+order);
					throw new RuntimeException("订单取消失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(order.getOrderId());
				orderTrace.setNodeName("卖家取消订单");
				orderTrace.setDealStaff(userDto.getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(userDto.getUserName());
				orderTrace.setOrderStatus(order.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(userDto.getUserName());
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
	 * @param userDto
	 * @param oldShoppingCartListDto
	 */
	public Map<String,Object> checkOrderPage(UserDto userDto, ShoppingCartListDto oldShoppingCartListDto) throws Exception {
		log.info("检查订单页的数据,userDto = " + userDto);
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			return null;
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Integer currentLoginEnterpriseId = userDto.getCustId();

		UsermanageEnterprise enterprise = enterpriseMapper.getByEnterpriseId(currentLoginEnterpriseId + "");
		if(UtilHelper.isEmpty(enterprise)){
			throw new Exception("非法参数");
		}

		/* 获取买家用户的收货地址列表 */
		UsermanageReceiverAddress receiverAddress = new UsermanageReceiverAddress();
		receiverAddress.setEnterpriseId(enterprise.getEnterpriseId());
		List<UsermanageReceiverAddress> receiverAddressList = receiverAddressMapper.listByProperty(receiverAddress);
		resultMap.put("receiveAddressList",receiverAddressList );



		/* 用户从购物车页面选中的商品 */
		List<Integer> shoppingCartIdList = new ArrayList<Integer>();
		if(!UtilHelper.isEmpty(oldShoppingCartListDto) && !UtilHelper.isEmpty(oldShoppingCartListDto.getShoppingCartDtoList())){
			for(ShoppingCartDto shoppingCartDto: oldShoppingCartListDto.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto) || UtilHelper.isEmpty(shoppingCartDto.getShoppingCartId()) || shoppingCartDto.getShoppingCartId()<= 0){
					continue;
				}
				shoppingCartIdList.add(shoppingCartDto.getShoppingCartId());
			}
		}

		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(currentLoginEnterpriseId);
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);
		resultMap.put("allShoppingCart",allShoppingCart);
		if(UtilHelper.isEmpty(allShoppingCart)) return resultMap;

		/*遍历购物车所有供应商信息*/
		BigDecimal productPriceCount = null;
		BigDecimal orderPriceCount = new BigDecimal(0);
		List deleteSupplyList = new ArrayList();
		for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
			if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())) continue;
			productPriceCount = new BigDecimal(0);

			/*遍历购物车所有供应商下的商品信息*/
			List deleteProductList = new ArrayList();
			for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				if(!UtilHelper.isEmpty(shoppingCartIdList) && !shoppingCartIdList.contains(shoppingCartDto.getShoppingCartId())){
					deleteProductList.add(shoppingCartDto);
					continue;
				}
				productPriceCount = productPriceCount.add(shoppingCartDto.getProductPrice().multiply(new BigDecimal(shoppingCartDto.getProductCount())));
			}
			if(!UtilHelper.isEmpty(deleteProductList)){
				shoppingCartListDto.getShoppingCartDtoList().removeAll(deleteProductList);
			}
			if(UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())){
				deleteSupplyList.add(shoppingCartListDto);
				continue;
			}
			shoppingCartListDto.setProductPriceCount(productPriceCount);
			orderPriceCount = orderPriceCount.add(productPriceCount);
		}
		if(!UtilHelper.isEmpty(deleteSupplyList)){
			allShoppingCart.removeAll(deleteSupplyList);
		}

		resultMap.put("allShoppingCart",allShoppingCart);
		resultMap.put("orderPriceCount",orderPriceCount);
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
		List<OrderDto> list = orderMapper.listPaginationExportSellerOrder(pagination, orderDto);
		List<Object[]> dataset = new ArrayList<Object[]>();
		SellerOrderStatusEnum sellerOrderStatusEnum;
		for(OrderDto order:list) {
			String orderStatusName="未知类型";
			if(!UtilHelper.isEmpty(order.getOrderStatus()) && !UtilHelper.isEmpty(order.getPayType())){
				//卖家视角订单状态
				sellerOrderStatusEnum = getSellerOrderStatus(order.getOrderStatus(), order.getPayType());
				if(!UtilHelper.isEmpty(sellerOrderStatusEnum)){
					orderStatusName=sellerOrderStatusEnum.getValue();
				}
			}
			dataset.add(new Object[]{"下单时间styleColor",order.getCreateTime(),"订单号styleColor",order.getFlowId(),"订单状态styleColor",orderStatusName,"发票类型styleColor",BillTypeEnum.getBillTypeName(order.getBillType())});
			dataset.add(new Object[]{"采购商styleColor",order.getCustName(),"收货人styleColor",order.getOrderDelivery().getReceivePerson(),"收货地址styleColor",order.getOrderDelivery().getReceiveAddress(),"联系方式styleColor",order.getOrderDelivery().getReceiveContactPhone()});
			dataset.add(new Object[]{"商品编码styleColor","品名styleColor","规格styleColor","厂商styleColor","单价（元）styleColor","数量styleColor","金额（元）styleColor","促销信息styleColor"});
			Double productTotal=0d;
			for (OrderDetail orderDetail : order.getOrderDetailList()) {
				BigDecimal totalPrice = orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductCount() + ""));
				dataset.add(new Object[]{orderDetail.getProductCode(),orderDetail.getProductName(),orderDetail.getSpecification(),orderDetail.getManufactures(),orderDetail.getProductPrice(),orderDetail.getProductCount(),totalPrice.doubleValue(),""});
				productTotal+=totalPrice.doubleValue();
			}
			dataset.add(new Object[]{"商品金额（元）styleColor", productTotal, "优惠券（元）styleColor", order.getPreferentialMoney(), "订单金额（元）styleColor", order.getOrderTotal(), "", ""});
			dataset.add(new Object[]{"买家留言styleColor", order.getLeaveMessage(), "", "", "", "", "", ""});
			dataset.add(new Object[]{});
		}
		return  ExcelUtil.exportExcel("订单信息", dataset);
	}

	/**
	 * 系统自动取消订单
	 * 1在线支付订单24小时系统自动取消
	 * 2 线下支付7天后未确认收款系统自动取消
	 * @return
	 */
	public void updateCancelOrderForNoPay(){
		orderMapper.cancelOrderForNoPay();
	}

	/**
	 * 系统自动取消订单
	 * 订单7个自然日未发货系统自动取消
	 * @return
	 */
	public void updateCancelOrderForNoDelivery(){
		List<Order> lo=orderMapper.listOrderForNoDelivery();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			//根据订单来源进行在线退款 二期对接

			if(true){//退款成功
				cal.add(od.getOrderId());
			}
		}

		if(UtilHelper.isEmpty(cal)) return;

		//取消订单
		orderMapper. cancelOrderForNoDelivery(cal);
	}

	/**
	 * 系统自动确认收货
	 * 订单发货后7个自然日后系统自动确认收货
	 * @return
	 */
	public void updateDoneOrderForDelivery() throws Exception{
		List<Order> lo=orderMapper.listOrderForDelivery();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			//根据订单来源进行自动分账 二期对接
			if(true){//分账成功
				cal.add(od.getOrderId());
			}
            //如为账期订单则生成结算信息
            orderDeliveryDetailService.saveOrderSettlement(od);
		}
        //退货异常订单生成结算信息
        OrderException orderException=new OrderException();
        orderException.setReturnType("1");
        orderException.setOrderStatus("5");
        List<OrderException> le=orderExceptionMapper.listNodelivery(orderException);
        for(OrderException o:le){
            //异常订单收货
            o.setOrderStatus("7");
            o.setSellerReceiveTime(systemDateMapper.getSystemDate());
            orderExceptionMapper.update(o);
            OrderExceptionService.saveReturnOrderSettlement(o);//生成结算信息
        }
		if(UtilHelper.isEmpty(cal)) return;
		//确认收货
		orderMapper.doneOrderForDelivery(cal);
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
		if(!SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){
			throw new RuntimeException("当前订单状态不能进行收款确认");
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
		orderSettlement.setOrderTime(order.getCreateTime());
		orderSettlement.setSettlementMoney(order.getOrgTotal());
		orderSettlement.setRefunSettlementMoney(orderSettlement.getRefunSettlementMoney());
		orderSettlementMapper.save(orderSettlement);
		//TODO 订单记录表
		insertOrderTrace(order);
		order.setFinalPay(orderSettlement.getSettlementMoney());
		// 2016-8-13修改 收款确认后订单状态为 5-买家已付款
		// order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
		order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
		order.setConfirmSettlement("1");
		order.setSettlementMoney(orderSettlement.getSettlementMoney());
		order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
		order.setPayTime(now);
		order.setSettlementTime(now);
		order.setUpdateUser(order.getCustName());
		order.setUpdateTime(now);
		order.setFinalPay(orderSettlement.getRefunSettlementMoney());
		orderMapper.update(order);
	}

	/**
	 * 提供给武汉使用,在订单状态已完成时
	 * 还款后 可对订单进行已还款状态修改
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderStatus(List<Order> order) {
		// TODO Auto-generated method stub
		boolean re=true;
		try{
		Order on=new Order();
		for(Order o:order){
			Order no=orderMapper.getOrderbyFlowId(o.getFlowId());
			if(no!=null&&o!=null&&(no.getOrderStatus().equals(SystemOrderStatusEnum.BuyerAllReceived.getType())
			 || no.getOrderStatus().equals(SystemOrderStatusEnum.BuyerPartReceived.getType())
			 || no.getOrderStatus().equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType())
			   )){
				// start 修改账单还款 更新结算状态
				on.setOrderId(no.getOrderId());
				on.setPaymentTermStatus(1);
				on.setConfirmSettlement("1");
				orderMapper.update(on);
				// end 修改账单还款 更新结算状态
				// start 修改结算记录信息
				List<OrderSettlement> ld =new ArrayList<OrderSettlement>();
				OrderSettlement orderSettlement=new OrderSettlement();
				orderSettlement.setFlowId(o.getFlowId());
				List<OrderSettlement> ls=orderSettlementMapper.listByProperty(orderSettlement);
				if(ls.size()>0){
					ld.add(ls.get(0));
				}
				OrderException orderException=new OrderException();
				orderException.setFlowId(o.getFlowId());
				List<OrderException> le=orderExceptionMapper.listByProperty(orderException);
				for(OrderException oe:le){
					orderSettlement.setFlowId(oe.getExceptionOrderId());
					List<OrderSettlement> l=orderSettlementMapper.listByProperty(orderSettlement);
					if(l.size()>0){
						ld.add(l.get(0));
					}
				}
                for(OrderSettlement os:ld){
					String now = systemDateMapper.getSystemDate();
					os.setConfirmSettlement("1");
					os.setSettlementTime(now);
					os.setUpdateTime(now);
					orderSettlementMapper.update(orderSettlement);
				}
				// end  修改结算记录信息
			}else{
				re= false;
			}
		 }
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return re;
	}




	/**
	 * 当账期订单时生成结算数据
	 * @param userDto
	 * @param orderId
	 * @throws Exception
	 */
	private void savePaymentDateSettlement(UserDto userDto,Integer orderId){
		Order order = orderMapper.getByPK(orderId);
		if(UtilHelper.isEmpty(order)||UtilHelper.isEmpty(userDto)){
			throw new RuntimeException("未找到订单");
		}
		String now = systemDateMapper.getSystemDate();
		OrderSettlement orderSettlement = new OrderSettlement();
		orderSettlement.setBusinessType(1);
		orderSettlement.setOrderId(orderId);
		orderSettlement.setFlowId(order.getFlowId());
		orderSettlement.setCustId(order.getCustId());
		orderSettlement.setCustName(order.getCustName());
		orderSettlement.setSupplyId(order.getSupplyId());
		orderSettlement.setSupplyName(order.getSupplyName());
		orderSettlement.setConfirmSettlement("0");
		orderSettlement.setPayTypeId(order.getPayTypeId());
		orderSettlement.setSettlementTime(now);
		orderSettlement.setCreateUser(userDto.getCustName());
		orderSettlement.setCreateTime(now);
		orderSettlement.setOrderTime(order.getCreateTime());
		orderSettlement.setSettlementMoney(order.getOrgTotal());
		orderSettlement.setRefunSettlementMoney(order.getOrgTotal());
		orderSettlementMapper.save(orderSettlement);
	}

	//更具订单编号查找订单
	public Order getOrderbyFlowId(String flowId){
		return orderMapper.getOrderbyFlowId(flowId);
	}
}
