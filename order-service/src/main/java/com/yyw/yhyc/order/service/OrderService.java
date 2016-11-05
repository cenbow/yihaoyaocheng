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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.remote.yhyc.ProductSearchInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yao.trade.interfaces.credit.model.PeriodDubboResult;
import com.yao.trade.interfaces.credit.model.PeriodParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.adviser.IAdviserManageDubbo;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.adviser.AdviserModel;
import com.yaoex.usermanage.model.custgroup.CustGroupDubboRet;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.appdto.AddressBean;
import com.yyw.yhyc.order.appdto.BatchBean;
import com.yyw.yhyc.order.appdto.OrderBean;
import com.yyw.yhyc.order.appdto.OrderProductBean;
import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.AdviserDto;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;
import com.yyw.yhyc.order.enmu.BuyerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderExceptionTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SellerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.enmu.SystemChangeGoodsOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderPayFlag;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemRefundOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemReplenishmentOrderStatusEnum;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.*;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.manage.OrderManage;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.OrderCombinedMapper;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.OrderPayMapper;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;
import com.yyw.yhyc.order.mapper.OrderTraceMapper;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.mapper.SystemPayTypeMapper;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.product.bo.ProductInfo;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.product.mapper.ProductInfoMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.DateUtils;
import com.yyw.yhyc.utils.ExcelUtil;
import com.yyw.yhyc.utils.MyConfigUtil;

@Service("orderService")
public class OrderService {

	private Log log = LogFactory.getLog(OrderService.class);

	private OrderMapper	orderMapper;
	private SystemPayTypeMapper systemPayTypeMapper;
	private SystemDateMapper systemDateMapper;
	private OrderDetailMapper orderDetailMapper;
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
	private OrderPayManage orderPayManage;

	private ProductInventoryManage productInventoryManage;
	@Autowired
	public void set(ProductInventoryManage productInventoryManage) {
		this.productInventoryManage = productInventoryManage;
	}

	@Autowired
    private OrderDeliveryDetailService orderDeliveryDetailService;

    @Autowired
    private OrderExceptionService orderExceptionService;

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
	public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
		this.systemPayTypeMapper = systemPayTypeMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper) {
		this.orderDetailMapper = orderDetailMapper;
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

	@Autowired
	private  OrderSettlementService orderSettlementService;

	@Autowired
	private OrderManage orderManage;


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
	public Map<String,Object> createOrder(OrderCreateDto orderCreateDto,IPromotionDubboManageService iPromotionDubboManageService) throws Exception{

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

		List<Order> orderNewList =  new ArrayList<Order>();
		/* 遍历订单数据中的各个供应商,即按供应商为单位拆单(一期需求) */
		for (OrderDto orderDto : orderCreateDto.getOrderDtoList()){
			if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
				continue;
			}

            /* 发票类型 */
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
            // pc 订单来源设置
			if(orderDto.getSource()==0){
				orderDto.setSource(orderCreateDto.getSource());
			};
			/* 创建订单相关的所有信息 */
			Order orderNew = createOrderInfo(orderDto,orderDelivery,orderCreateDto.getUserDto(),iPromotionDubboManageService);

			if(null == orderNew) continue;
			orderNewList.add(orderNew);
		}
		log.info("创建订单接口-生成的订单数据(以供应商为单位拆单),orderNewList = " + orderNewList);
		Map<String,Object> resultMap = new HashMap<>();



		/* 从原始订单信息中筛选出合法的账期商品信息 */
		List<OrderDto> periodTermOrderDtoList = getPeriodTermOrderDtoList(orderCreateDto.getOrderDtoList());
		/* 创建账期订单 */
		List<Order> periodTermOrderList =  createPeriodTermOrder(periodTermOrderDtoList,orderDelivery,orderCreateDto.getUserDto(),iPromotionDubboManageService);
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
			orderDtoNew.setAdviserCode(orderDto.getAdviserCode());
			orderDtoNew.setAdviserName(orderDto.getAdviserName());
			orderDtoNew.setAdviserPhoneNumber(orderDto.getAdviserPhoneNumber());
			orderDtoNew.setAdviserRemark(orderDto.getAdviserRemark());
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
	private List<Order> createPeriodTermOrder(List<OrderDto> periodTermOrderDtoList, OrderDelivery orderDelivery, UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService) throws Exception {
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
//				String payFlowId = RandomUtil.createOrderPayFlowId(systemDateMapper.getSystemDateByformatter("%Y%m%d%H%i%s"),userDto.getCustId());
//				payFlowId += "-" + (i+1);

				Order orderNew = createOrderInfo(orderDtoNew,orderDelivery,userDto,iPromotionDubboManageService);
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
		if ( UtilHelper.isEmpty(orderCreateDto.getReceiveAddressId())) {
			throw  new Exception("非法参数");
		}
		UsermanageReceiverAddress receiverAddress = receiverAddressMapper.getByPK(orderCreateDto.getReceiveAddressId());
		if(UtilHelper.isEmpty(receiverAddress) || UtilHelper.isEmpty(receiverAddress.getEnterpriseId()) || !receiverAddress.getEnterpriseId().equals(enterprise.getEnterpriseId())){
			throw new Exception("非法收货地址");
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
	 * @return
	 */
	private Order createOrderInfo(OrderDto orderDto, OrderDelivery orderDelivery, UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService) throws Exception{
		if( UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList())
				|| UtilHelper.isEmpty(orderDelivery) ){
			return null;
		}

		/* TODO  计算订单相关的价格 */
		log.info("创建订单接口 ：计算订单相关的价格,计算前orderDto = " + orderDto);
		orderDto = calculateOrderPrice(orderDto);
		log.info("创建订单接口 ：计算订单相关的价格,计算后orderDto = " + orderDto);

		/*  数据插入订单表、订单详情表 */
		Order order = insertOrderAndOrderDetail(orderDto,userDto);
		log.info("创建订单接口 ：插入订单表、订单详情表 order= " + order);

		/* 冻结库存 */
		productInventoryManage.frozenInventory(orderDto);

		/* 若是活动商品，则减掉活动库存 */
		orderManage.reducePromotionInventory(orderDto,iPromotionDubboManageService);

		/* 订单配送发货信息表 */
		insertOrderDeliver(order, orderDelivery);

		/* 订单跟踪信息表 */
		insertOrderTrace(order);

		/* 删除购物车中相关的商品 */
		deleteShoppingCart(orderDto);

		return order;
	}



	private OrderDto calculateOrderPrice(OrderDto orderDto) {
		if(null == orderDto || null == orderDto.getProductInfoDtoList()) return null;

		BigDecimal orderTotal = new BigDecimal(0);
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(null == productInfoDto) continue;
			orderTotal = orderTotal.add(productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())));
		}
		if(!UtilHelper.isEmpty(orderTotal)){
			orderTotal = orderTotal.setScale(2,BigDecimal.ROUND_HALF_UP);
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
			shoppingCart.setFromWhere(productInfoDto.getFromWhere() == null ? ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere() : productInfoDto.getFromWhere());
			if(!UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId() > 0){
				shoppingCart.setPromotionId(productInfoDto.getPromotionId());
			}
			shoppingCartMapper.deleteByProperty(shoppingCart);
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
		SystemPayType systemPayType = systemPayTypeMapper.getByPK(orderDto.getPayTypeId());
		String orderFlowIdPrefix = "";
		/* 线下支付 */
		if(SystemPayTypeEnum.PayOffline.getPayType().equals(  systemPayType.getPayType() )){
			orderFlowIdPrefix = CommonType.ORDER_OFFLINE_PAY_PREFIX;
			order.setPaymentTerm(0);
		/* 在线支付 */
		}else if(SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType())) {
			orderFlowIdPrefix = CommonType.ORDER_ONLINE_PAY_PREFIX;
			order.setPaymentTerm(0);
		/* 账期支付 */
		}else if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(  systemPayType.getPayType() )){
			orderFlowIdPrefix = CommonType.ORDER_PERIOD_TERM_PAY_PREFIX;
			order.setPaymentTerm(orderDto.getPaymentTerm());
		}

		order.setPayStatus(OrderPayStatusEnum.UN_PAYED.getPayStatus());
		order.setOrderStatus(SystemOrderStatusEnum.BuyerOrdered.getType());
		order.setCreateTime(systemDateMapper.getSystemDate());
		order.setCreateUser(userDto.getUserName());

		int totalCount = 0;
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()) {
			if (UtilHelper.isEmpty(productInfoDto)) {
				continue;
			}
			if ( UtilHelper.isEmpty(productInfoDto.getProductCount()) || productInfoDto.getProductCount() <= 0 ) {
				continue;
			}
			totalCount += productInfoDto.getProductCount();
		}
		order.setTotalCount(totalCount);//订单商品总数量
		order.setProductSortCount(orderDto.getProductInfoDtoList().size());//订单商品种类数量

		/* 订单金额相关 */
		order.setOrderTotal(orderDto.getOrderTotal());//订单总金额
		order.setFreight(orderDto.getFreight());//运费
		order.setPreferentialMoney(orderDto.getPreferentialMoney());//优惠了的金额(如果使用了优惠)
		order.setOrgTotal(orderDto.getOrgTotal());//订单优惠后的金额(如果使用了优惠)
		order.setSettlementMoney(orderDto.getSettlementMoney());//结算金额
		order.setPaymentTermStatus(0);//账期还款状态 0 未还款  1 已还款
		order.setSource(orderDto.getSource());//订单来源
		/**
		 * 销售顾问信息
		 */
		order.setAdviserCode(orderDto.getAdviserCode());
		order.setAdviserName(orderDto.getAdviserName());
		order.setAdviserPhoneNumber(orderDto.getAdviserPhoneNumber());
		order.setAdviserRemark(orderDto.getAdviserRemark());
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
		List<OrderDetail> orderDetailList = new ArrayList<>();
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

			//商品信息
			orderDetail.setProductPrice(productInfoDto.getProductPrice());
			orderDetail.setProductCount(productInfoDto.getProductCount());
			orderDetail.setProductSettlementPrice(productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())));
			orderDetail.setProductId(productInfo.getId());
			orderDetail.setProductName(productInfo.getProductName());//商品名称
			orderDetail.setProductCode(productInfoDto.getProductCodeCompany());//存的是本公司商品编码
			orderDetail.setSpecification(productInfo.getSpec());//商品规格
//			orderDetail.setBrandName(productInfo.getBrandId() + "");//todo 品牌名称
			orderDetail.setFormOfDrug(productInfo.getDrugformType());//剂型
			if (!UtilHelper.isEmpty(productInfoDto.getPromotionId())){
				orderDetail.setPromotionId(productInfoDto.getPromotionId());
			}
			if(!UtilHelper.isEmpty(productInfoDto.getPromotionName())){
				orderDetail.setPromotionName(productInfoDto.getPromotionName());
			}

			//生产厂家
			int manufacturesId = 0;
			try{
				manufacturesId = Integer.valueOf(productInfo.getFactoryName());
			}catch (Exception e){
				log.error("生产厂家id(" + productInfo.getFactoryName() + ")获取失败：" + e.getMessage(),e);
			}
			orderDetail.setManufacturesId(manufacturesId);//厂家id
			if(UtilHelper.isEmpty(productInfoDto.getManufactures())){
				ProductInfoDto p = productInfoMapper.getFactory(manufacturesId);
				if( !UtilHelper.isEmpty(p) ){
					orderDetail.setManufactures(p.getManufactures());//厂家名称
				}
			}else{
				orderDetail.setManufactures(productInfoDto.getManufactures());//厂家名称
			}


			orderDetail.setShortName(productInfo.getShortName());//商品通用名
			orderDetail.setSpuCode(productInfo.getSpuCode());
			log.info("更新数据到订单详情表：orderDetail参数=" + orderDetail);
			orderDetailMapper.save(orderDetail);
			orderDetailList.add(orderDetail);
		}
		orderDto.setOrderDetailList(orderDetailList);
		return order;
	}



	/**
	 * 校验要购买的商品(通用方法)
	 * @param userDto 当前登陆人
	 * @param orderDto 要提交订单的商品数据
	 * @param iCustgroupmanageDubbo
	 * @param productDubboManageService
	 * @param productSearchInterface
	 * @return
     */
	public Map<String,Object> validateProducts(UserDto userDto, OrderDto orderDto,
											   ICustgroupmanageDubbo iCustgroupmanageDubbo, IProductDubboManageService productDubboManageService,
											   ProductSearchInterface productSearchInterface,IPromotionDubboManageService iPromotionDubboManageService){

		/* 区分是来自进货单的商品还是极速下单的商品 */
		int productFromFastOrderCount = 0;
		if( ! UtilHelper.isEmpty(orderDto) &&  !UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
			for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()) {
				if (UtilHelper.isEmpty(productInfoDto)) continue;
				if (productInfoDto.getFromWhere() != null && ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == productInfoDto.getFromWhere()) {
					productFromFastOrderCount ++;
				}
			}
		}
		log.info("统一校验订单商品接口：区分是来自进货单的商品还是极速下单的商品，productFromFastOrderCount=" + productFromFastOrderCount );

		Map<String,Object> map = new HashMap<String, Object>();

		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList()) ){
			log.info("统一校验订单商品接口-currentLoginCustId：" + userDto +",orderDto=" + orderDto);
			return returnFalse("非法参数",productFromFastOrderCount);
		}

		UsermanageEnterprise buyer = enterpriseMapper.getByEnterpriseId(orderDto.getCustId() + "");
		UsermanageEnterprise seller = enterpriseMapper.getByEnterpriseId(orderDto.getSupplyId() + "");
		if(UtilHelper.isEmpty(buyer) || UtilHelper.isEmpty(seller)){
			return returnFalse("非法参数",productFromFastOrderCount);
		}

		/* 校验要采购商与供应商是否相同 */
		if (orderDto.getSupplyId().equals(userDto.getCustId()) ){
			log.info("统一校验订单商品接口 ：不能购买自己的商品" );
			return returnFalse("不能购买自己的商品",productFromFastOrderCount);
		}


		if(UtilHelper.isEmpty(productDubboManageService)){
			log.error("统一校验订单商品接口,查询商品价格，productDubboManageService = " + productDubboManageService);
			return returnFalse("请稍后再试",productFromFastOrderCount);
		}



		/* 该供应商下所有商品的总金额（用于判断是否符合供应商的订单起售金额） */
		BigDecimal productPriceCount = new BigDecimal(0);

		ProductInfo productInfo = null;
		//校验库存数量，是否可以购买
		ProductInventory productInventory = new ProductInventory();
		productInventory.setSupplyId(orderDto.getSupplyId());
		String productFromWhere = "进货单";
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(UtilHelper.isEmpty(productInfoDto)) continue;

			if(productInfoDto.getFromWhere() != null && ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == productInfoDto.getFromWhere()){
				productFromWhere = "极速下单页";
			}

			/* 检查库存 */
			productInfo =  productInfoMapper.getByPK(productInfoDto.getId());
			if(UtilHelper.isEmpty(productInfo )){
				log.info("统一校验订单商品接口 ：商品(productId=" + productInfoDto.getId() + ")不存在!" );
				return returnFalse("商品不存在",productFromFastOrderCount);
			}
			productInventory.setSpuCode(productInfo.getSpuCode());
			productInventory.setFrontInventory(productInfoDto.getProductCount());
			Map<String, Object> m = productInventoryManage.findInventoryNumber(productInventory);
			String code = m.get("code").toString();
			if("0".equals(code) || "1".equals(code)){
				log.info("统一校验订单商品接口 ：商品(spuCode=" + productInfo.getSpuCode() + ")库存校验失败!resultMap=" + m );
				return returnFalse("您的进货单中，有部分商品缺货或下架了，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}

			/* 查询商品上下架状态 */
			Map mapQuery = new HashMap();
			mapQuery.put("spu_code", productInfoDto.getSpuCode());
			mapQuery.put("seller_code", orderDto.getSupplyId());
			List productList = null;
			Integer putawayStatus = 0;// 0未上架  1上架  2本次下架  3非本次下架
			Integer isChannel = 0;// 是否渠道商品(0否，1是),
			try{
				log.info("统一校验订单商品接口-查询商品上下架状态,请求参数:" + mapQuery);
				productList = productDubboManageService.selectProductBySPUCodeAndSellerCode(mapQuery);
				log.info("统一校验订单商品接口-查询商品上下架状态,响应参数:" + JSONArray.fromObject(productList));
				JSONObject productJson = JSONObject.fromObject(productList.get(0));

				//（客户组）商品上下架状态：t_product_putaway表中的state字段 （上下架状态 0未上架  1上架  2本次下架  3非本次下架 ）
				putawayStatus = UtilHelper.isEmpty(productJson.get("putaway_status")+"") ? 0 : Integer.valueOf(productJson.get("putaway_status")+"");
				isChannel = UtilHelper.isEmpty(productJson.get("is_channel")+"") ? 0 : Integer.valueOf(productJson.get("is_channel")+"");
			}catch (Exception e){
				log.error("统一校验订单商品接口-查询商品上下架状态信息失败:" + e.getMessage(),e);
				return returnFalse("查询商品状态失败",productFromFastOrderCount);
			}

			if(UtilHelper.isEmpty(putawayStatus) || putawayStatus != 1){
				log.info("统一校验订单商品接口-查询商品上下架状态,putawayStatus:" + putawayStatus + ",// 0未上架  1上架  2本次下架  3非本次下架");
				return returnFalse("您的进货单中，有部分商品缺货或下架了，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}
			productInfoDto.setIsChannel(isChannel);

			/* 查询价格 */
			BigDecimal productPrice = null;
			long startTime = System.currentTimeMillis();
			try{
				productPrice = orderManage.getProductPrice(productInfoDto.getSpuCode(),orderDto.getCustId(),orderDto.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface) ;
			}catch (Exception e){
				log.error("统一校验订单商品接口,查询商品价格，发生异常," + e.getMessage(),e);
				return returnFalse("查询商品价格失败",productFromFastOrderCount);
			}
			long endTime = System.currentTimeMillis();

			log.info("统一校验订单商品接口,查询完成，耗时："+ (endTime - startTime) +"毫秒，商品价格:productPrice=" + productPrice);
			if(UtilHelper.isEmpty(productPrice)){
				return returnFalse("查询商品价格失败",productFromFastOrderCount);
			}

			/* 若商品的最新价格 小于等于0，则提示该商品无法购买 */
			if(productPrice.compareTo(new BigDecimal(0)) <= 0){
				updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice);
				return returnFalse("部分商品您无法购买，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}
			/* 若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格 */
			if(UtilHelper.isEmpty(productInfoDto.getPromotionId())){
				if( productPrice.compareTo(productInfoDto.getProductPrice()) != 0){
					updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice);
					return returnFalse("存在价格变化的商品，请返回" + productFromWhere + "重新结算",productFromFastOrderCount);
				}
			}

			/* 如果该商品没有缺货、没有下架、价格合法，则统计该供应商下的已买商品总额 */
			if( "2".equals(code) && 1 == putawayStatus && productPrice.compareTo(new BigDecimal(0)) > 0){
				productPriceCount = productPriceCount.add( productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())) );
			}


			/* 校验活动商品相关的限购逻辑 */
			/* 1、 非空校验*/
			if(UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() <= 0){
				continue;
			}
			ProductPromotionDto productPromotionDto = orderManage.queryProductWithPromotion(iPromotionDubboManageService,productInfoDto.getSpuCode(),
					seller.getEnterpriseId(),productInfoDto.getPromotionId(),buyer.getEnterpriseId());
			if(UtilHelper.isEmpty(productPromotionDto)){
				continue;
			}

			/* 2、 校验 购买活动商品的数量 是否合法 */
			if(productInfoDto.getProductCount() < productPromotionDto.getMinimumPacking()){
				return returnFalse("购买活动商品的数量低于最小起批量",productFromFastOrderCount);
			}

			/* 3、查询该商品在该活动中的历史购买量*/
			int buyedInHistory = 0;
			if(!UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId() > 0){
				ShoppingCart shoppingCart  = new ShoppingCart();
				shoppingCart.setCustId(Integer.parseInt(buyer.getEnterpriseId()));
				shoppingCart.setSupplyId(Integer.parseInt(seller.getEnterpriseId()));
				shoppingCart.setSpuCode(productInfoDto.getSpuCode());
				shoppingCart.setProductId(productInfoDto.getId());
				shoppingCart.setPromotionId(productInfoDto.getPromotionId());
				buyedInHistory  = shoppingCartMapper.countBuyedNumInHistory(shoppingCart);
			}

			/* 4、判断理论上可以以特价购买的数量 */
			int canBuyByPromotionPrice = productPromotionDto.getLimitNum() - buyedInHistory;
			if (canBuyByPromotionPrice <= 0){
				return  returnFalse("该活动商品每人限购"+productPromotionDto.getLimitNum() +"件,您已购买了" + buyedInHistory +
						"件,还能购买" + canBuyByPromotionPrice +"件。",productFromFastOrderCount);
			}

			/* 5、若还能以特价购买，则根据活动实时库存判断能买多少 */
			if(productPromotionDto.getCurrentInventory() - canBuyByPromotionPrice <= 0 ){
				if(productInfoDto.getProductCount() > productPromotionDto.getCurrentInventory()  ){
					return returnFalse("本次购买的活动商品数量已超过活动实时库存",productFromFastOrderCount);
				}
			}else{
				if(productInfoDto.getProductCount() > canBuyByPromotionPrice ){
					return returnFalse("本次购买的活动商品数量已超过个人限购数量",productFromFastOrderCount);
				}
			}
		}
		log.info("统一校验订单商品接口:供应商[" + seller.getEnterpriseName() + "]("+seller.getEnterpriseId()+")的订单起售金额=" + seller.getOrderSamount() + ",在该供应商下购买的商品总额=" + productPriceCount);

		if(!UtilHelper.isEmpty(seller.getOrderSamount()) && productPriceCount.compareTo(seller.getOrderSamount()) < 0 ){
			return returnFalse("你有部分商品金额低于供货商的发货标准，此商品无法结算",productFromFastOrderCount);
		}

		log.info("统一校验订单商品接口 ：校验成功" );
		map.put("result", true);
		return map;
	}



	private Map<String,Object> returnFalse(String message,Integer productFromFastOrderCount){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", false);
		map.put("message", message);
		if(UtilHelper.isEmpty(productFromFastOrderCount) || productFromFastOrderCount <= 0){
			/* 跳转到进货单 */
			map.put("goToShoppingCart", true);
		}else{
			/* 跳转到极速下单 */
			map.put("goToFastOrder", true);
		}
		return map;
	}


	/**
	 * 提交订单时，若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格
	 * @param userDto
	 * @param supplyId
	 * @param spuCode
	 * @param newProductPrice
	 */
	private void updateProductPrice(UserDto userDto, Integer supplyId, String spuCode, BigDecimal newProductPrice){
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		shoppingCart.setSupplyId(supplyId);
		shoppingCart.setSpuCode(spuCode);

		List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByProperty(shoppingCart);
		if(!UtilHelper.isEmpty(shoppingCartList) && shoppingCartList.size() == 1){
			shoppingCart = shoppingCartList.get(0);
			shoppingCart.setProductPrice(newProductPrice);
			shoppingCart.setProductSettlementPrice(newProductPrice.multiply(new BigDecimal(shoppingCart.getProductCount())));
			shoppingCart.setUpdateUser(userDto.getUserName());
			shoppingCart.setUpdateTime(systemDateMapper.getSystemDate());
			log.info("统一校验订单商品接口,查询商品价格后价格发生变化，更新数据：" + shoppingCart);
			shoppingCartMapper.update(shoppingCart);
		}
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
						if(od.getDelayTimes()!=null){//延期次数*每次延期的天数
							time -= CommonType.POSTPONE_TIME*od.getDelayTimes()*60*60*24;
						}
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
    public BuyerOrderStatusEnum getBuyerOrderStatus(String systemOrderStatus,int payType){
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerOrdered.getType())) {//买家已下单
			return BuyerOrderStatusEnum.PendingPayment;//待付款
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
		/*打款异常单独一个字段保存
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return BuyerOrderStatusEnum.Finished;//已完成
        }
        */
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
			return SellerOrderStatusEnum.PendingPayment;//待付款
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
		/* 打款异常单独一个字段保存
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return SellerOrderStatusEnum.Finished;//已完成
        }
		*/
        return null;
    }

	/**
	 * 采购商取消订单
	 * @param userDto
	 * @param orderId
	 * @param iPromotionDubboManageService 
	 */
	public void  updateOrderStatusForBuyer(UserDto userDto,Integer orderId, IPromotionDubboManageService iPromotionDubboManageService){
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

				//释放冻结库存
				productInventoryManage.releaseInventory(order.getOrderId(),order.getSupplyName(),userDto.getUserName(),iPromotionDubboManageService);

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
	 * 供应商取消订单
	 * @param userDto
	 * @param orderId
	 * @param iPromotionDubboManageService 
	 */
	public void  updateOrderStatusForSeller(UserDto userDto,Integer orderId,String cancelResult, IPromotionDubboManageService iPromotionDubboManageService){
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

				SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
				if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())) {
					PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
					payService.handleRefund(userDto, 1, order.getFlowId(), "卖家主动取消订单");

				}
				//如果是银联在线支付，生成结算信息，类型为订单取消退款
				if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())){
					OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(5,null,null,userDto.getUserName(),null,order);
					orderSettlementMapper.save(orderSettlement);
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

				//释放冻结库存
				productInventoryManage.releaseInventory(order.getOrderId(),order.getSupplyName(),userDto.getUserName(),iPromotionDubboManageService);
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
	 * @param oldShoppingCartIdList
	 * @param iAdviserManageDubbo
	 */
	public Map<String,Object> checkOrderPage(UserDto userDto, List<Integer> oldShoppingCartIdList, IAdviserManageDubbo iAdviserManageDubbo) throws Exception {
		log.info("检查订单页的数据,userDto = " + userDto);
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(oldShoppingCartIdList)){
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


		/* 查找出当前买家的进货单里面，有哪些供应商 */
		ShoppingCart shoppingCartQuery = new ShoppingCart();
		shoppingCartQuery.setCustId(userDto.getCustId());
		List<ShoppingCart> custIdAndSupplyIdList = shoppingCartMapper.listDistinctCustIdAndSupplyId(shoppingCartQuery);
		if(UtilHelper.isEmpty(custIdAndSupplyIdList)){
			resultMap.put("result",false);
			resultMap.put("message","您的进货单中没有商品");
			return resultMap;
		}

		List<ShoppingCartListDto> allShoppingCart = new ArrayList<>();
		ShoppingCartListDto shoppingCartListDto = null;
		List<ShoppingCartDto> shoppingCartDtoList = null;
		ShoppingCartDto shoppingCartDto = null;
		BigDecimal productPriceCount = null;
		BigDecimal orderPriceCount = new BigDecimal(0);

		for(ShoppingCart custIdAndSupplyId : custIdAndSupplyIdList) {
			if (UtilHelper.isEmpty(custIdAndSupplyId)) continue;
			shoppingCartListDto = new ShoppingCartListDto();
			shoppingCartListDto.setBuyer(enterpriseMapper.getByEnterpriseId(custIdAndSupplyId.getCustId()+""));
			shoppingCartListDto.setSeller(enterpriseMapper.getByEnterpriseId(custIdAndSupplyId.getSupplyId()+""));

			productPriceCount = new BigDecimal(0);
			shoppingCartDtoList = new ArrayList<>();
			/* 遍历进货单中 选中的商品 */
			for( Integer shoppingCartId : oldShoppingCartIdList){
				if(UtilHelper.isEmpty(shoppingCartId) || shoppingCartId <= 0){
					continue;
				}
				ShoppingCart temp = shoppingCartMapper.getByPK(shoppingCartId);
				if(UtilHelper.isEmpty(temp)) continue;
				if(custIdAndSupplyId.getSupplyId().equals(temp.getSupplyId())){
					shoppingCartDto = new ShoppingCartDto();
					shoppingCartDto.setCustId(temp.getCustId());
					shoppingCartDto.setSupplyId(temp.getSupplyId());
					shoppingCartDto.setSpuCode(temp.getSpuCode());
					shoppingCartDto.setProductId(temp.getProductId());
					shoppingCartDto.setProductName(temp.getProductName());
					shoppingCartDto.setManufactures(temp.getManufactures());
					shoppingCartDto.setSpecification(temp.getSpecification());
					shoppingCartDto.setProductCount(temp.getProductCount());
					shoppingCartDto.setProductPrice(temp.getProductPrice());
					shoppingCartDto.setProductSettlementPrice(temp.getProductSettlementPrice());
					shoppingCartDto.setProductCodeCompany(temp.getProductCodeCompany());
					shoppingCartDto.setFromWhere(temp.getFromWhere());
					shoppingCartDto.setPromotionId(temp.getPromotionId());
					shoppingCartDto.setPromotionName(temp.getPromotionName());
					shoppingCartDtoList.add(shoppingCartDto);

					productPriceCount = productPriceCount.add(temp.getProductPrice().multiply(new BigDecimal(temp.getProductCount())));
				}
			}
			shoppingCartListDto.setProductPriceCount(productPriceCount);
			orderPriceCount = orderPriceCount.add(productPriceCount);
			shoppingCartListDto.setShoppingCartDtoList(shoppingCartDtoList);

			//查询供应商的销售顾问信息
			//custIdAndSupplyId.getSupplyId();
			List<AdviserDto> adviserList = new ArrayList<AdviserDto>();
			List<AdviserModel> list = iAdviserManageDubbo.queryByEnterId(custIdAndSupplyId.getSupplyId());
			for(AdviserModel one : list){
				AdviserDto adviserDto = new AdviserDto();
				adviserDto.setAdviserCode(one.getAdviser_code());
				adviserDto.setAdviserName(one.getAdviser_name());
				adviserDto.setAdviserPhoneNumber(one.getPhone_number());
				adviserDto.setAdviserRemark(one.getRemark());
				adviserList.add(adviserDto);
			}
			shoppingCartListDto.setAdviserList(adviserList);

			allShoppingCart.add(shoppingCartListDto);
		}

		resultMap.put("allShoppingCart",allShoppingCart);
		if(!UtilHelper.isEmpty(orderPriceCount)){
			orderPriceCount = orderPriceCount.setScale(2,BigDecimal.ROUND_HALF_UP);
		}
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
				dataset.add(new Object[]{orderDetail.getSpuCode(),orderDetail.getShortName(),orderDetail.getSpecification(),orderDetail.getManufactures(),orderDetail.getProductPrice(),orderDetail.getProductCount(),totalPrice.doubleValue(),""});
				productTotal+=totalPrice.doubleValue();
			}
			dataset.add(new Object[]{"商品金额（元）styleColor", productTotal, "优惠券（元）styleColor", order.getPreferentialMoney(), "订单金额（元）styleColor", order.getOrderTotal(), "", ""});
			dataset.add(new Object[]{"买家留言styleColor", order.getLeaveMessage(), "", "", "", "", "", ""});
			dataset.add(new Object[]{});
		}
		return  ExcelUtil.exportExcel("订单信息", dataset);
	}

	private void sendCredit(Order od,CreditDubboServiceInterface creditDubboService,String status){

		if(UtilHelper.isEmpty(creditDubboService)){
			throw new RuntimeException("接口调用失败,creditDubboService 服务为空"+od.toString());
		}
		SystemPayType systemPayType= systemPayTypeMapper.getByPK(od.getPayTypeId());
		if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
			CreditParams creditParams = new CreditParams();
			creditParams.setSourceFlowId(od.getFlowId());//订单编码
			creditParams.setBuyerCode(od.getCustId() + "");
			creditParams.setSellerCode(od.getSupplyId() + "");
			creditParams.setBuyerName(od.getCustName());
			creditParams.setSellerName(od.getSupplyName());
			creditParams.setOrderTotal(od.getOrgTotal());//订单金额  扣减后的
			creditParams.setFlowId(od.getFlowId());//订单编码
			creditParams.setStatus(status);//创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
			if("2".equals(status)){
				creditParams.setReceiveTime(DateHelper.parseTime(od.getReceiveTime()));
			}

			CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
			if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
				throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
			}
		}
	}

	/**
	 * 系统自动取消订单
	 * 1在线支付订单24小时系统自动取消
	 * 2 线下支付7天后未确认收款系统自动取消
	 * @return
	 */
	public void updateCancelOrderForNoPay(IPromotionDubboManageService iPromotionDubboManageService){
		log.debug("开始准备系统自动取消未支付订单");
		List<Order> lo=orderMapper.listCancelOrderForNoPay();
		log.debug("要自动取消订单结果集是【" + Arrays.toString(lo.toArray()) + "】。");

		for(Order od:lo){
			productInventoryManage.releaseInventory(od.getOrderId(),od.getSupplyName(),"admin",iPromotionDubboManageService);
		}
		int count = orderMapper.cancelOrderForNoPay();
		log.debug("成功取消未支付订单【" + count + "】条。");
	}

	/**
	 * 系统自动取消订单
	 * 订单7个自然日未发货系统自动取消
	 * @return
	 */
	public void updateCancelOrderForNoDelivery(CreditDubboServiceInterface creditDubboService,IPromotionDubboManageService iPromotionDubboManageService){
		List<Order> lo=orderMapper.listOrderForNoDelivery();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			String now = systemDateMapper.getSystemDate();
			log.debug("订单7个自然日未发货系统自动取消=" + od.toString());
			od.setCancelTime(now);
			od.setCancelResult("系统自动取消");
			od.setOrderStatus(SystemOrderStatusEnum.SystemAutoCanceled.getType());
			orderMapper.update(od);
			Integer payTypeId=od.getPayTypeId();
			SystemPayType systemPayType = systemPayTypeMapper.getByPK(payTypeId);
			//如果是银联在线支付，生成结算信息，类型为订单取消退款
			if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(payTypeId)
					||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(payTypeId)
					||OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(payTypeId)
					||OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(payTypeId)
					||OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId())
					||OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())){
				OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(5,null,null,"systemAuto",null,od);
				orderSettlement.setConfirmSettlement("1");
				orderSettlementMapper.save(orderSettlement);
				if(!UtilHelper.isEmpty(payTypeId)){
					PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
					UserDto userDto=new UserDto();
					userDto.setCustName("admin");
					payService.handleRefund(userDto,1,od.getFlowId(),"系统自动取消");
					productInventoryManage.releaseInventory(od.getOrderId(),od.getSupplyName(),"admin",iPromotionDubboManageService);
				}
			}else{
				if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayPeriodTerm.getPayType())){
					sendCredit(od,creditDubboService,"5");
				}
			}
			//库存
			productInventoryManage.releaseInventory(od.getOrderId(),od.getSupplyName(),"admin",iPromotionDubboManageService);
		}
		//if(UtilHelper.isEmpty(cal)) return;
		//取消订单
		//orderMapper. cancelOrderForNoDelivery(cal);
	}

	/**
	 * 系统自动确认收货
	 * 订单发货后7个自然日后系统自动确认收货
	 * @return
	 */
	public void updateDoneOrderForDelivery(CreditDubboServiceInterface creditDubboService) throws Exception{
		List<Order> lo=orderMapper.listOrderForDelivery();
		List<Integer> cal=new ArrayList<Integer>();
		for(Order od:lo){
			String now = systemDateMapper.getSystemDate();
			od.setOrderStatus(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType());
			od.setReceiveTime(now);
			od.setReceiveType(2);
			orderMapper.update(od);
			//账期结算信息
			orderDeliveryDetailService.saveOrderSettlement(od,null);
			//根据订单来源进行自动分账 三期 对接
			log.debug("订单发货后7个自然日后系统自动确认收货=" + od.toString());
			SystemPayType systemPayType = systemPayTypeMapper.getByPK(od.getPayTypeId());
			if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(od.getPayTypeId())
					||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(od.getPayTypeId())
					||OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(od.getPayTypeId())){

				OrderCombined orderCombined=orderCombinedMapper.getByPK(od.getOrderCombinedId());
				if(!UtilHelper.isEmpty(orderCombined.getPayFlowId())){
					PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
					UserDto userDto=new UserDto();
					userDto.setCustName("admin");
					payService.handleRefund(userDto,1,od.getFlowId(),"自动确认收货");
					cal.add(od.getOrderId());
				}
			}else{
				if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayPeriodTerm.getPayType())){
					sendCredit(od,creditDubboService,"2");
				}

			}
		}
        //退货异常订单自动确认
		autoConfirmRefundOrder(creditDubboService);
		//补货异常订单自动确认
		cal=autoConfirmReplenishmentOrder(cal,creditDubboService);
		//换货异常订单自动确认
		autoConfirmChangeOrder();

		//if(UtilHelper.isEmpty(cal)) return;
		//确认收货
		//orderMapper.doneOrderForDelivery(cal);
	}

	/*
	 *退货异常订单自动确认
	 */
	private void autoConfirmRefundOrder(CreditDubboServiceInterface creditDubboService)throws Exception{
		OrderException orderException=new OrderException();
		orderException.setReturnType(OrderExceptionTypeEnum.RETURN.getType());
		orderException.setOrderStatus(SystemRefundOrderStatusEnum.BuyerDelivered.getType());
		List<OrderException> le=orderExceptionMapper.listNodeliveryForReturn(orderException);
		for(OrderException o:le){
			//异常订单收货
			log.debug("退货异常订单自动确认=" + o.toString());
			Order order = orderMapper.getByPK(o.getOrderId());
			SystemPayType systemPayType= systemPayTypeMapper.getByPK(order.getPayTypeId());
			o.setOrderStatus(SystemRefundOrderStatusEnum.SystemAutoConfirmReceipt.getType());
			o.setSellerReceiveTime(systemDateMapper.getSystemDate());
			orderExceptionMapper.update(o);
			orderExceptionService.saveReturnOrderSettlement(o);//生成结算信息
			//调用资信接口
			sendReundCredit(creditDubboService,systemPayType,o);
		}
	}

	/*
	 *补货异常订单自动确认
	 */
	private List<Integer> autoConfirmReplenishmentOrder(List<Integer> cal,CreditDubboServiceInterface creditDubboService)throws Exception{

		OrderException orderException1=new OrderException();
		orderException1.setReturnType(OrderExceptionTypeEnum.REPLENISHMENT.getType());
		orderException1.setOrderStatus(SystemReplenishmentOrderStatusEnum.SellerDelivered.getType());
		List<OrderException> le1=orderExceptionMapper.listNodeliveryForReplenishment(orderException1);
		for(OrderException o:le1){
			Order od= orderMapper.getOrderbyFlowId(o.getFlowId());
			String now = systemDateMapper.getSystemDate();
			od.setOrderStatus(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType());
			od.setReceiveTime(now);
			od.setReceiveType(2);
			orderMapper.update(od);
			//生成结算信息
			orderDeliveryDetailService.saveOrderSettlement(od,null);
			log.debug("补货异常订单自动确认=" + o.toString() + ";" + od.toString());
			Integer payTypeId=od.getPayTypeId();
			SystemPayType systemPayType = systemPayTypeMapper.getByPK(payTypeId);
			if(!UtilHelper.isEmpty(od)){
				if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(payTypeId)
						||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(payTypeId)
						||OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(payTypeId)){
					OrderCombined orderCombined=orderCombinedMapper.getByPK(od.getOrderCombinedId());
					PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
					UserDto userDto=new UserDto();
					userDto.setCustName("admin");
					payService.handleRefund(userDto,3,o.getFlowId(),"补货自动确认收货");
					cal.add(od.getOrderId());
				}else{
					if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayPeriodTerm.getPayType())){
						sendCredit(od,creditDubboService,"2");
					}

				}

			}
			//异常订单收货
			o.setOrderStatus(SystemReplenishmentOrderStatusEnum.SystemAutoConfirmReceipt.getType());
			o.setSellerReceiveTime(systemDateMapper.getSystemDate());
			orderExceptionMapper.update(o);
		}
		return cal;
	}

	/*
	* 换货异常订单自动确认
	 */
	private void autoConfirmChangeOrder(){
		OrderException orderException2=new OrderException();
		orderException2.setReturnType(OrderExceptionTypeEnum.CHANGE.getType());
		orderException2.setOrderStatus(SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType());
		List<OrderException> le2=orderExceptionMapper.listNodeliveryForChange(orderException2);
		for(OrderException o:le2){
			//异常订单收货
			log.debug("换货异常订单自动确认=" + o.toString());
			o.setOrderStatus(SystemChangeGoodsOrderStatusEnum.AutoFinished.getType());
			o.setSellerReceiveTime(systemDateMapper.getSystemDate());
			orderExceptionMapper.update(o);
		}
	}

	/*
	* 退货通知资信接口
	 */
	public void sendReundCredit(CreditDubboServiceInterface creditDubboService,
								 SystemPayType systemPayType,OrderException orderException) {
		log.debug("******************：调用资信接口,sendReundCredit开始");
		if(UtilHelper.isEmpty(creditDubboService)){
			throw new RuntimeException("接口调用失败,creditDubboService 服务为空"+orderException.toString());
		}
		if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
			CreditParams creditParams = new CreditParams();
			creditParams.setSourceFlowId(orderException.getFlowId());//退货时，退货单对应的源订单单号
			creditParams.setBuyerCode(orderException.getCustId() + "");
			creditParams.setSellerCode(orderException.getSupplyId() + "");
			creditParams.setBuyerName(orderException.getCustName());
			creditParams.setSellerName(orderException.getSupplyName());
            creditParams.setOrderTotal(orderException.getOrderMoney());//订单金额
			creditParams.setFlowId(orderException.getExceptionOrderId());//订单编码
			creditParams.setStatus("6");
			log.debug("******************：调用资信接口开始");
			CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
			log.debug("******************：调用资信接口结束,返回结果为：" + creditDubboResult);
			if (UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())) {
				log.debug("******************：调用资信接口结束,返回结果为：" + creditDubboResult.getIsSuccessful());
				log.error("creditDubboResult error:" + (creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！"));
				throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
			}
		}else{
			log.error("creditDubboService  error:" +  "接口调用失败，请求参数"+orderException.toString());
		}
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
		log.debug("updateOrderStatus start 还款接口调用="+order.size());
		boolean re=true;
		try{
		Order on=new Order();
		for(Order o:order){
			log.debug("还款接口调用="+o.toString());
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
				log.debug("updateOrderStatus ld.size 还款接口调用="+ld.size());
                for(OrderSettlement os:ld){
					String now = systemDateMapper.getSystemDate();
					os.setConfirmSettlement("1");
					os.setSettlementTime(now);
					os.setUpdateTime(now);
					log.debug("updateOrderStatus doing 还款接口调用="+os.toString());
					orderSettlementMapper.update(os);
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


	/**
	 * 运营人员查询订单
	 * @param data
     * @return
     */
	public Map<String,Object> listPgOperationsOrder(Map<String,String> data){
		if(UtilHelper.isEmpty(data)) throw  new RuntimeException("参数异常");
		Map<String,Object> resutlMap = new HashMap<String,Object>();

		Pagination<OrderDto> pagination = new Pagination<OrderDto>();
		pagination.setPageNo(Integer.valueOf(data.get("pageNo")));
		pagination.setPageSize(Integer.valueOf(data.get("pageSize")));
		pagination.setPaginationFlag(Boolean.valueOf(data.get("paginationFlag")));
		OrderDto orderDto = new OrderDto();
		orderDto.setSupplyName(data.get("supplyName"));
		orderDto.setCustName(data.get("custName"));
		if(StringUtils.isNotBlank(data.get("payType"))){
			orderDto.setPayType(Integer.valueOf(data.get("payType")));
		}
		orderDto.setCreateBeginTime(data.get("createBeginTime"));
		orderDto.setCreateEndTime(data.get("createEndTime"));
		orderDto.setOrderStatus(data.get("orderStatus"));
		orderDto.setFlowId(data.get("flowId"));
		
		if(StringUtils.isNotBlank(data.get("payFlag"))){
			orderDto.setPayFlag(Integer.valueOf(data.get("payFlag")));
		}
		
		if(StringUtils.isNotBlank(data.get("source"))){
			orderDto.setSource(Integer.valueOf((data.get("source"))));
		}
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

		log.info("listPgOperationsOrder pagination:"+pagination);
		List<OrderDto> orderDtoList = orderMapper.listPaginationOperationsOrder(pagination,orderDto);
		if(!UtilHelper.isEmpty(orderDtoList)){
			for (OrderDto od : orderDtoList){
				od.setOrderStatusName(SystemOrderStatusEnum.getName(od.getOrderStatus()));
				od.setPayFlagName(SystemOrderPayFlag.getName(od.getPayFlag()));
			}
		}
		log.info("listPgOperationsOrder orderDtoList:"+orderDtoList);
		pagination.setResultList(orderDtoList);

		resutlMap.put("orderDtoList",pagination);
		return resutlMap;
	}

	//延期收货
	public void postponeOrder(Integer orderId,Integer day){
		Order order = orderMapper.getByPK(orderId);
		if(order==null){
			throw  new RuntimeException("未找到订单");
		}
        // 延期收货订单逻辑
		Integer delayTimes = order.getDelayTimes()==null?0:order.getDelayTimes();
        delayTimes++ ;
        String nowTimeStr = systemDateMapper.getSystemDate();
        order.setDelayTimes(delayTimes);
        order.setUpdateTime(nowTimeStr);
        order.setDelayLog((order.getDelayLog()==null?"":order.getDelayLog())+nowTimeStr+",当前第"+delayTimes+"次延期收货;");
        orderMapper.update(order);

	}

	public Map<String, Object> getOrderDetails4Manager(String flowId) throws Exception{
		Order order = new Order();
		order.setFlowId(flowId);
		OrderDetailsDto orderDetailsdto=orderMapper.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsdto)){
			return null;
		}
		//订单类型翻译
		orderDetailsdto.setOrderStatusName(SystemOrderStatusEnum.getName(orderDetailsdto.getOrderStatus()));
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
		OrderCombined orderPay = orderCombinedMapper.getByPK(orderDetailsdto.getOrderCombinedId());
		if(!UtilHelper.isEmpty(orderPay))
			orderDetailsdto.setPayFlowId(orderPay.getPayFlowId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderDetailsDto",orderDetailsdto);
		return map;
	}

	public Map<String, Object> listPgProducts(Map<String, String> data) throws Exception {
		if(UtilHelper.isEmpty(data)){
			throw new RuntimeException("参数异常");
		}
		Map<String,Object> resutlMap = new HashMap<String,Object>();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPageNo(Integer.valueOf(data.get("pageNo")));
		pagination.setPageSize(Integer.valueOf(data.get("pageSize")));
		pagination.setPaginationFlag(Boolean.valueOf(data.get("paginationFlag")));
		OrderDeliveryDetailDto orderDeliveryDetailDto = new OrderDeliveryDetailDto();
		orderDeliveryDetailDto.setFlowId(data.get("flowId"));
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationByProperty(pagination, orderDeliveryDetailDto);

		pagination.setResultList(list);

		resutlMap.put("productsList", pagination);
		return resutlMap;
	}


	/**
	 * 后台管理取消订单
	 * @param userName
	 * @param orderId
	 * @param cancelResult
     */
	public void  updateOrder4Manage(String userName,String orderId,String cancelResult,IPromotionDubboManageService iPromotionDubboManageService){
		if( UtilHelper.isEmpty(orderId) || UtilHelper.isEmpty(cancelResult)){
			throw new RuntimeException("参数错误:订单编号为空");
		}
		if(  UtilHelper.isEmpty(cancelResult)){
			throw new RuntimeException("参数错误:取消原因为空");
		}
		Order order =  orderMapper.getByPK(Integer.parseInt(orderId));
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		if((SystemOrderStatusEnum.SellerDelivered.getType().equals(order.getOrderStatus()) )){//卖家已发货
			order.setOrderStatus(SystemOrderStatusEnum.BackgroundCancellation.getType());//标记订单为卖家取消状态
			String now = systemDateMapper.getSystemDate();
			order.setCancelResult("后台取消");
			order.setRemark(cancelResult);
			order.setUpdateUser(userName);
			order.setUpdateTime(now);
			order.setCancelTime(now);
			int count = orderMapper.update(order);
			if(count == 0){
				log.error("order info :"+order);
				throw new RuntimeException("订单取消失败");
			}

			UserDto userDto = new UserDto();
			userDto.setUserName(userName);

			//插入日志表
			OrderTrace orderTrace = new OrderTrace();
			orderTrace.setOrderId(order.getOrderId());
			orderTrace.setNodeName("后台取消订单");
			orderTrace.setDealStaff(userName);
			orderTrace.setRecordDate(now);
			orderTrace.setRecordStaff(userName);
			orderTrace.setOrderStatus(order.getOrderStatus());
			orderTrace.setCreateTime(now);
			orderTrace.setCreateUser(userName);
			orderTraceMapper.save(orderTrace);

			SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
			if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())) {
				PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
				payService.handleRefund(userDto, 1, order.getFlowId(), "运营后台取消订单");
			}

			//如果是银联在线支付，生成结算信息，类型为订单取消退款
			if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId())
					||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())
					||OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(systemPayType.getPayTypeId())
					||OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId())
					||OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())){
				OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(5,null,null,"systemManage",null,order);
				orderSettlementMapper.save(orderSettlement);
			}else if(SystemPayTypeEnum.PayOffline.getPayType().equals(systemPayType.getPayType())){
				OrderSettlement orderSettlement = new OrderSettlement();
				orderSettlement.setOrderId(order.getOrderId());
				orderSettlement.setFlowId(order.getFlowId());
				orderSettlement.setCustName(order.getCustName());
				orderSettlement.setSupplyName(order.getSupplyName());
				orderSettlement.setCreateTime(now);
				orderSettlement.setOrderTime(order.getCreateTime());
				orderSettlement.setSettlementTime(now);
				orderSettlement.setPayTypeId(order.getPayTypeId());
				orderSettlement.setCreateUser("systemManage");
				orderSettlement.setBusinessType(4);
				orderSettlement.setCustId(order.getCustId());
				orderSettlement.setSupplyId(order.getSupplyId());
				orderSettlement.setRemark(cancelResult);
				orderSettlement.setConfirmSettlement("0");//生成结算信息时都未结算
				orderSettlement.setSettlementMoney(order.getOrgTotal());
				orderSettlementMapper.save(orderSettlement);
			}

			//释放冻结库存
			productInventoryManage.releaseInventory(order.getOrderId(),order.getSupplyName(),userName,iPromotionDubboManageService);


		}else{
			log.error("order status error ,orderStatus:"+order.getOrderStatus());
			throw new RuntimeException("当前订单状态下不能进行取消订单！");
		}
	}

	/**
	 * 账期订单拆单逻辑
	 * @param userDto
	 * @param allShoppingCart
	 * @param productDubboManageService
	 * @param creditDubboService
     * @param iPromotionDubboManageService
	 * @return
     */
	public List<ShoppingCartListDto> handleDataForPeriodTermOrder(UserDto userDto, List<ShoppingCartListDto> allShoppingCart,
																  IProductDubboManageService productDubboManageService, CreditDubboServiceInterface creditDubboService,
																  IPromotionDubboManageService iPromotionDubboManageService){

		/* 组装购物车中的数据，查询客户账期、商品账期 */
		List<PeriodParams> periodParamsList = queryAllPeriodList(allShoppingCart,creditDubboService);

		/* 具体的拆单逻辑 */
		allShoppingCart = convertAllShoppingCart(allShoppingCart,periodParamsList,creditDubboService);

		/* 查询商品的信息 */
		allShoppingCart = handlerProductInfo(allShoppingCart,productDubboManageService,iPromotionDubboManageService);
		return allShoppingCart;
	}

	/**
	 * 组装购物车中的数据，调用查询账期接口
	 * @param allShoppingCart
	 * @return
	 */
	private List<PeriodParams> queryAllPeriodList(List<ShoppingCartListDto> allShoppingCart, CreditDubboServiceInterface creditDubboService) {
		List<PeriodParams> paramsList  = new ArrayList<>();
		if(UtilHelper.isEmpty(allShoppingCart)){
			return paramsList;
		}

		log.info("检查订单页-调用武汉的dubbo接口查询商品账期信息:creditDubboService=" + creditDubboService);
		if(UtilHelper.isEmpty(creditDubboService)){
			return paramsList;
		}

		for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
			if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())){
				continue;
			}
			PeriodParams periodParams = null;
			for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList() ){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				if(UtilHelper.isEmpty(shoppingCartDto.getCustId()) || UtilHelper.isEmpty(shoppingCartDto.getSupplyId())
						|| UtilHelper.isEmpty(shoppingCartDto.getSpuCode())){
					continue;
				}
				periodParams = new PeriodParams();
				periodParams.setBuyerCode(shoppingCartDto.getCustId().toString());
				periodParams.setSellerCode(shoppingCartDto.getSupplyId().toString());
				periodParams.setProductCode(shoppingCartDto.getSpuCode());
				paramsList.add(periodParams);
			}
		}

		log.info("检查订单页-调用武汉的dubbo接口查询商品账期信息:请求参数paramsList=" + JSONArray.fromObject(paramsList));
		PeriodDubboResult periodDubboResult=null;
		try{
			periodDubboResult = creditDubboService.queryPeriod(paramsList);
		}catch(Exception e){
			log.error("检查订单页-调用武汉的dubbo接口查询商品账期信息异常:"+e.getMessage(),e);
			return  paramsList;
		}

		if(UtilHelper.isEmpty(periodDubboResult) || "0".equals(periodDubboResult.getIsSuccessful())
				|| UtilHelper.isEmpty(periodDubboResult.getData())){
			if(!UtilHelper.isEmpty(periodDubboResult)){
				log.error("检查订单页-调用武汉的dubbo接口查询商品账期信息:" + periodDubboResult.getMessage());
			}
			return  paramsList;
		}
		log.info("检查订单页-调用武汉的dubbo接口查询商品账期信息:响应参数periodDubboResult=" + JSONObject.fromObject(periodDubboResult) );
		return  periodDubboResult.getData();
	}

	/**
	 * 处理商品信息
	 * @param allShoppingCart
	 * @return
	 */
	private List<ShoppingCartListDto> handlerProductInfo(List<ShoppingCartListDto> allShoppingCart, IProductDubboManageService productDubboManageService,
														 IPromotionDubboManageService iPromotionDubboManageService ) {
		if(UtilHelper.isEmpty(allShoppingCart)) return allShoppingCart;

		Map mapQuery = new HashMap();
		List productList = null;
		Integer isChannel = 0;// 是否渠道商品(0否，1是),

		for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
			if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())) continue;
			for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				mapQuery.put("spu_code", shoppingCartDto.getSpuCode());
				mapQuery.put("seller_code", shoppingCartDto.getSupplyId());

				try{
					log.info("检查订单页-查询商品信息,请求参数:" + mapQuery);
					productList = productDubboManageService.selectProductBySPUCodeAndSellerCode(mapQuery);
					log.info("检查订单页-查询商品信息,响应参数:" + JSONArray.fromObject(productList));
					JSONObject productJson = JSONObject.fromObject(productList.get(0));
					isChannel = UtilHelper.isEmpty(productJson.get("is_channel")+"") ? 0 : Integer.valueOf(productJson.get("is_channel")+"");
					shoppingCartDto.setIsChannel(isChannel);
				}catch (Exception e){
					log.error("检查订单页-查询商品信息失败，message:"+e.getMessage(),e);
				}

				/* 检查活动商品信息 */
				ProductPromotionDto productPromotionDto = orderManage.queryProductWithPromotion(iPromotionDubboManageService,shoppingCartDto.getSpuCode(),
						shoppingCartDto.getSupplyId()+"",shoppingCartDto.getPromotionId(),shoppingCartDto.getCustId()+"");


			}
		}
		return allShoppingCart;
	}


	/**
	 * 可以用账期支付的(商品)拆单逻辑
	 * @param allShoppingCart
	 * @param periodParamsList
	 * @return
	 */
	private List<ShoppingCartListDto> convertAllShoppingCart(List<ShoppingCartListDto> allShoppingCart,List<PeriodParams> periodParamsList,CreditDubboServiceInterface creditDubboService) {
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}
		/* 计算各个供应商下是否有账期商品，如果有，则计算这些账期商品的总价 */
		allShoppingCart = calculatePeriodProductPriceCount(allShoppingCart,periodParamsList);
		log.info("检查订单页-计算各个供应商下账期商品的总价，计算后的allShoppingCart=" + allShoppingCart);
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}

		List<ShoppingCartListDto> resultShoppingCartList = new ArrayList<ShoppingCartListDto>();

		/* 账期商品-组装信息 */
		List<ShoppingCartDto> shoppingCartDtoListPeriodTerm = null;
		ShoppingCartListDto shoppingCartListDtoPeriodTerm = null;

		/* 非账期商品-组装信息 */
		List<ShoppingCartDto> shoppingCartDtoList = null;
		ShoppingCartListDto shoppingCartListDto = null;

		for(ShoppingCartListDto s: allShoppingCart){
			if(UtilHelper.isEmpty(s) || UtilHelper.isEmpty(s.getBuyer())  || UtilHelper.isEmpty(s.getSeller())|| UtilHelper.isEmpty(s.getShoppingCartDtoList()) ){
				continue;
			}

			log.info("检查订单页-查询是否可用资信结算接口，creditDubboService=" + creditDubboService);
			if(UtilHelper.isEmpty(creditDubboService)){
				shoppingCartListDto = new ShoppingCartListDto();
				shoppingCartListDto.setBuyer(s.getBuyer());
				shoppingCartListDto.setSeller(s.getSeller());
				shoppingCartListDto.setPaymentTermCus(s.getPaymentTermCus());
				shoppingCartListDto.setAccountAmount(0);
				shoppingCartListDto.setProductPriceCount(s.getProductPriceCount());
				shoppingCartListDto.setShoppingCartDtoList(s.getShoppingCartDtoList());
				shoppingCartListDto.setAdviserList(s.getAdviserList());
				resultShoppingCartList.add(shoppingCartListDto);
				continue;
			}

			CreditParams creditParams = new CreditParams();
			creditParams.setBuyerCode(s.getBuyer().getEnterpriseId() + "");
			creditParams.setSellerCode(s.getSeller().getEnterpriseId()+ "");
			creditParams.setOrderTotal(s.getProductPriceCount());
			log.info("检查订单页-查询是否可用资信结算接口，请求参数creditParams=" + JSONObject.fromObject(creditParams));
			CreditDubboResult creditDubboResult = null;
			try{
				creditDubboResult = creditDubboService.queryCreditAvailability(creditParams);
			}catch (Exception e){
				log.error(e.getMessage());
			}
			log.info("检查订单页-查询是否可用资信结算接口，响应数据creditDubboResult=" + JSONObject.fromObject(creditDubboResult));

			if(UtilHelper.isEmpty(creditDubboResult) || !"1".equals(creditDubboResult.getIsSuccessful())){
				/* 供应商对采供商设置的账期额度，1 表示账期额度可以用。  0 表示账期额度已用完 或 没有设置账期额度 */
				log.error("检查订单页-查询是否可用资信结算接口:资信为空或查询资信失败");

				shoppingCartListDto = new ShoppingCartListDto();
				shoppingCartListDto.setBuyer(s.getBuyer());
				shoppingCartListDto.setSeller(s.getSeller());
				shoppingCartListDto.setPaymentTermCus(s.getPaymentTermCus());
				shoppingCartListDto.setAccountAmount(0);
				shoppingCartListDto.setProductPriceCount(s.getProductPriceCount());
				shoppingCartListDto.setShoppingCartDtoList(s.getShoppingCartDtoList());
				shoppingCartListDto.setAdviserList(s.getAdviserList());
				resultShoppingCartList.add(shoppingCartListDto);

				continue;
			}

			/* 把合法账期商品的拿出来 */
			shoppingCartDtoListPeriodTerm = new ArrayList<>();
			BigDecimal productPriceCountPeriodTerm = new BigDecimal(0);
			BigDecimal productPriceCount = new BigDecimal(0);
			shoppingCartDtoList = new ArrayList<>();

			for(ShoppingCartDto shoppingCartDto : s.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				if(shoppingCartDto.isPeriodProduct()){
					shoppingCartDtoListPeriodTerm.add(shoppingCartDto);
					productPriceCountPeriodTerm = productPriceCountPeriodTerm.add(shoppingCartDto.getProductSettlementPrice());
				}else{
					shoppingCartDtoList.add(shoppingCartDto);
					productPriceCount = productPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				}
			}

			/* 如果有合法账期商品，组装新的数据  */
			if(!UtilHelper.isEmpty(shoppingCartDtoListPeriodTerm)){
				shoppingCartListDtoPeriodTerm = new ShoppingCartListDto();
				shoppingCartListDtoPeriodTerm.setBuyer(s.getBuyer());
				shoppingCartListDtoPeriodTerm.setSeller(s.getSeller());
				shoppingCartListDtoPeriodTerm.setPaymentTermCus(s.getPaymentTermCus());
				shoppingCartListDtoPeriodTerm.setAccountAmount(1);
				shoppingCartListDtoPeriodTerm.setShoppingCartDtoList(shoppingCartDtoListPeriodTerm);
				shoppingCartListDtoPeriodTerm.setProductPriceCount(productPriceCountPeriodTerm);
				shoppingCartListDtoPeriodTerm.setAdviserList(s.getAdviserList());
				resultShoppingCartList.add(shoppingCartListDtoPeriodTerm);
			}

			/* 如果不是账期商品，组装新的数据  */
			if(!UtilHelper.isEmpty(shoppingCartDtoList)){
				shoppingCartListDto = new ShoppingCartListDto();
				shoppingCartListDto.setBuyer(s.getBuyer());
				shoppingCartListDto.setSeller(s.getSeller());
				shoppingCartListDto.setPaymentTermCus(s.getPaymentTermCus());
				shoppingCartListDto.setAccountAmount(1);
				shoppingCartListDto.setProductPriceCount(productPriceCount);
				shoppingCartListDto.setShoppingCartDtoList(shoppingCartDtoList);
				shoppingCartListDto.setAdviserList(s.getAdviserList());
				resultShoppingCartList.add(shoppingCartListDto);
			}
		}
		return resultShoppingCartList;
	}


	private  List<ShoppingCartListDto>  calculatePeriodProductPriceCount (List<ShoppingCartListDto> allShoppingCart,List<PeriodParams> periodParamsList) {
		if(UtilHelper.isEmpty(allShoppingCart) || UtilHelper.isEmpty(periodParamsList)){
			return allShoppingCart;
		}
		for(ShoppingCartListDto s: allShoppingCart){
			if(UtilHelper.isEmpty(s) || UtilHelper.isEmpty(s.getShoppingCartDtoList())){
				continue;
			}

			/* 当前供应商下(设置了账期的)商品的总金额 */
			BigDecimal periodProductPriceCount = new BigDecimal(0);

			/* 当前供应商下(没有设置账期的)商品的总金额 */
			BigDecimal nonPeriodProductPriceCount = new BigDecimal(0);

			int paymentTermCus = 0;
			for(ShoppingCartDto shoppingCartDto : s.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				Map<String,Object>  periodMap = getPeriodByCondition(periodParamsList,shoppingCartDto.getSpuCode(),shoppingCartDto.getCustId(),shoppingCartDto.getSupplyId());
				if(UtilHelper.isEmpty(periodMap)) continue;
				paymentTermCus = periodMap.get("paymentTermCus") == null ? 0 :(Integer) periodMap.get("paymentTermCus");//客户账期  TODO 查询客户账期的 武汉那边应该另提供一个接口
				int paymentTermPro = periodMap.get("paymentTermPro") == null ? 0 : (Integer) periodMap.get("paymentTermPro");//商品账期
				if ( paymentTermPro > 0) {
					shoppingCartDto.setPeriodProduct(true);
					shoppingCartDto.setPaymentTerm(paymentTermPro);
					periodProductPriceCount = periodProductPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				} else {
					shoppingCartDto.setPeriodProduct(false);
					shoppingCartDto.setPaymentTerm(0);
					nonPeriodProductPriceCount = nonPeriodProductPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				}
			}
			s.setPeriodProductPriceCount(periodProductPriceCount);
			s.setNonPeriodProductPriceCount(nonPeriodProductPriceCount);
			s.setPaymentTermCus(paymentTermCus);
		}
		return allShoppingCart;
	}

	/**
	 * 根据购物车中商品的信息查询账期信息
	 * @param periodParamsList
	 * @param spuCode
	 * @param buyerEnterpriseId
	 * @param sellerEnterpriseId
	 * @return
	 */
	private Map<String,Object> getPeriodByCondition(List<PeriodParams> periodParamsList ,String spuCode,Integer buyerEnterpriseId,Integer sellerEnterpriseId){
		if(UtilHelper.isEmpty(periodParamsList) || UtilHelper.isEmpty(spuCode) || UtilHelper.isEmpty(buyerEnterpriseId)
				|| UtilHelper.isEmpty(sellerEnterpriseId)) {
			return null;
		}
		Map<String,Object> map = null;
		for(PeriodParams periodParams : periodParamsList){
			if(UtilHelper.isEmpty(periodParams)) continue;
			if(spuCode.equals(periodParams.getProductCode()) && buyerEnterpriseId.toString().equals(periodParams.getBuyerCode())
					&& sellerEnterpriseId.toString().equals(periodParams.getSellerCode())){
				map = new HashMap<>();
				map.put("paymentTermCus",periodParams.getPaymentTermCus());
				map.put("paymentTermPro",periodParams.getPaymentTermPro());
				break;
			}
		}
		return map;
	}




	/**
	 * 	APP个人中心 查看待付款、待发货、待收货、拒收/补货 订单数量
	 * @return
     */
	public Map<String,Integer> listBuyerOrderStatusCount(Integer custId){
		if(UtilHelper.isEmpty(custId))
			throw  new RuntimeException("用户id不能为空");
		Map<String,Integer> statusMap = new HashMap<String,Integer>();
		int unPayNumber = 0;   //待付款
		int deliverNumber = 0; //待发货
		int reciveNumber = 0;  //待收货
		int unRejRep = 0;      //拒收/补货
		OrderDto orderDto = new OrderDto();
		orderDto.setCustId(custId);
		List<OrderDto> orderCountList = orderMapper.findOrderStatusCount(orderDto);
		if(!UtilHelper.isEmpty(orderCountList)){
			BuyerOrderStatusEnum buyerorderstatusenum;
			//统计订单状态
			for(OrderDto od : orderCountList){
				//获取买家视角订单状态
				buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
				if(UtilHelper.isEmpty(buyerorderstatusenum))
					continue;
				//待付款
				if(BuyerOrderStatusEnum.PendingPayment.equals(buyerorderstatusenum))
					unPayNumber=unPayNumber + od.getOrderCount();
				//待发货
				if(BuyerOrderStatusEnum.BackOrder.equals(buyerorderstatusenum))
					deliverNumber = deliverNumber + od.getOrderCount();
				//待收货
				if(BuyerOrderStatusEnum.ReceiptOfGoods.equals(buyerorderstatusenum))
					reciveNumber = reciveNumber + od.getOrderCount();
				//拒收+补货
				//这里的注释掉了，补货中和拒收中状态的订单不能表示所有补货/拒收订单 LiuY 2016-10-12
//				if(BuyerOrderStatusEnum.Rejecting.equals(buyerorderstatusenum) || BuyerOrderStatusEnum.Replenishing.equals(buyerorderstatusenum))
//					unRejRep  = unRejRep + od.getOrderCount();
			}
		}
		unRejRep = orderExceptionMapper.findExceptionCountApp(custId);
		statusMap.put("unPayNumber",unPayNumber);
		statusMap.put("deliverNumber",deliverNumber);
		statusMap.put("reciveNumber",reciveNumber);
		statusMap.put("unRejRep",unRejRep);
		return statusMap;
	}

	/**
	 *  APP获取取消订单原因
	 * @return
     */
	public Map<String,String> findOrderCancelInfo(String flowId,UserDto userDto){
		Map<String,String> resultMap = new HashMap<String,String>();
		if(UtilHelper.isEmpty(flowId))
			throw  new RuntimeException("订单编号不能为空");
		Order order = orderMapper.getOrderbyFlowId(flowId);
		if(UtilHelper.isEmpty(order))
			throw  new RuntimeException("未找到订单信息");
		resultMap.put("cancelTime", order.getCancelTime());
		resultMap.put("cancelResult",order.getCancelResult());
		return resultMap;
	}

	/**
	 * APP 采购商取消订单
	 * @param userDto
	 * @param flowId
	 */
	public void  updateOrderStatusForBuyer(UserDto userDto,String flowId,IPromotionDubboManageService iPromotionDubboManageService){
		if(UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(flowId)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getOrderbyFlowId(flowId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+flowId);
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

				//释放冻结库存
				productInventoryManage.releaseInventory(order.getOrderId(),order.getSupplyName(),userDto.getUserName(),iPromotionDubboManageService);

			}else{
				log.error("order status error ,orderStatus:"+order.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}
		}else{
			log.error("db orderId not equals to request orderId ,orderId:"+flowId+",db orderId:"+order.getOrderId());
			throw new RuntimeException("未找到订单");
		}
	}

	/**
	 * APP获取订单列表
	 * @param pagination
	 * @param orderStatus
     * @return
     */
	public Map<String,Object> listBuyerOderForApp(Pagination<OrderDto> pagination,String orderStatus,int custId,IProductDubboManageService iProductDubboManageService){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		OrderDto orderDto = new OrderDto();
		orderDto.setCustId(custId);
		orderStatus = convertAppOrderStatus(orderStatus,1);
		if(UtilHelper.isEmpty(orderStatus))
			throw new RuntimeException("订单状态不正确");
		orderDto.setOrderStatus(orderStatus);
		//获取订单列表
		List<OrderDto> buyerOrderList = orderMapper.listPaginationBuyerOrderForApp(pagination, orderDto);
		pagination.setResultList(buyerOrderList);
		List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
		Map<String,Object> temp = null;
		if(!UtilHelper.isEmpty(buyerOrderList)){
			BuyerOrderStatusEnum buyerorderstatusenum;
			long time = 0l;
			for(OrderDto od : buyerOrderList){
				if(!UtilHelper.isEmpty(od.getOrderStatus()) && !UtilHelper.isEmpty(od.getPayType())){
					//获取买家视角订单状态
					buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
					if(!UtilHelper.isEmpty(buyerorderstatusenum)){
						od.setOrderStatus(buyerorderstatusenum.getType());
						od.setOrderStatusName(buyerorderstatusenum.getValue());
					}
					else
						od.setOrderStatusName("未知类型");
				}
				//获取支付剩余时间
				if(!UtilHelper.isEmpty(od.getNowTime()) && !UtilHelper.isEmpty(od.getCreateTime()) && SystemPayTypeEnum.PayOnline.getPayType() == od.getPayType() && SystemOrderStatusEnum.BuyerOrdered.getType().equals(od.getOrderStatus())){
					try {
						time = DateUtils.getSeconds(od.getCreateTime(),od.getNowTime());
						//计算当前时间和支付剩余24小时的剩余秒数
						time = time > 0 ? CommonType.PAY_TIME*60*60-time : time;
						time = time < 0 ? 0l : time;
						od.setResidualTime(time);
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期转换错误");
					}
				}
				temp = new HashMap<String,Object>();
				temp.put("orderId",od.getFlowId());
				String hideOrderStatus = convertAppOrderStatus(od.getOrderStatus(), 2);
				if(BuyerOrderStatusEnum.Finished.getType().equals(hideOrderStatus)){
					OrderExceptionDto oed = new OrderExceptionDto();
					oed.setFlowId(od.getFlowId());
					oed.setCustId(custId);
					List<OrderExceptionDto> oedRejectList =orderExceptionMapper.findBuyerRejectOrderStatusCount(oed);
					if(!UtilHelper.isEmpty(oedRejectList)&& oedRejectList.size()>0){
						hideOrderStatus ="804";
					}
					List<OrderExceptionDto> oedReplenisList = orderExceptionMapper.findBuyerReplenishmentStatusCount(oed);
					if(!UtilHelper.isEmpty(oedReplenisList)&& oedReplenisList.size()>0){
						hideOrderStatus ="905";
					}
				}
				temp.put("orderStatus",hideOrderStatus);
				temp.put("orderStatusName",od.getOrderStatusName());
				temp.put("createTime",od.getCreateTime());
				temp.put("supplyName",od.getSupplyName());
				temp.put("orderTotal",od.getOrgTotal());
				temp.put("finalPay",od.getOrgTotal());
				temp.put("varietyNumber",UtilHelper.isEmpty(od.getOrderDetailList()) ? 0 : od.getOrderDetailList().size());//品种
				temp.put("productNumber",od.getTotalCount());//商品数量
				temp.put("residualTime",time);//⽀付剩余时间 秒
				temp.put("delayTimes", UtilHelper.isEmpty(od.getDelayTimes()) ? 0 : od.getDelayTimes());
				temp.put("postponeTime",CommonType.CAN_DELAY_TIME);//能延期次数
				temp.put("qq","");
				temp.put("productList",getProductList(od.getOrderDetailList(),iProductDubboManageService));
				orderList.add(temp);
			}
		}
		resultMap.put("totalCount",pagination.getTotal());
		resultMap.put("pageCount", pagination.getTotalPage());
		resultMap.put("orderList",orderList);
		return resultMap;
	}

	/**
	 * 转换商品列表详情
	 * @param orderDetailList
	 * @return
     */
	List<Map<String,Object>> getProductList(List<OrderDetail> orderDetailList,IProductDubboManageService iProductDubboManageService){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		if(!UtilHelper.isEmpty(orderDetailList)){
			for(OrderDetail od : orderDetailList){
				map = new HashMap<String,Object>();
				map.put("productId",od.getProductId());
				map.put("productPicUrl",getProductImg(od.getSpuCode(),iProductDubboManageService));
				map.put("productName",od.getProductName());
				map.put("spec",od.getSpecification());
				map.put("unit","");
				map.put("productPrice","0");
				map.put("factoryName","");
				map.put("quantity",od.getProductCount());
				resultList.add(map);
			}
		}

		return resultList;
	}

	/**
	 * 获取商品图片链接
	 * @param spuCode
	 * @param iProductDubboManageService
     * @return
     */
	private String getProductImg(String spuCode,IProductDubboManageService iProductDubboManageService){
		String filePath = "http://oms.yaoex.com/static/images/product_default_img.jpg";
		String file_path ="";
		Map map = new HashMap();
		map.put("spu_code", spuCode);
		map.put("type_id", "1");
		try{
			List picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
			if(UtilHelper.isEmpty(picUrlList))
				return filePath;
			JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
			file_path = (String)productJson.get("file_path");
		}catch (Exception e){
			log.error("查询图片接口:调用异常," + e.getMessage(),e);
		}

		if (UtilHelper.isEmpty(file_path)){
			return filePath;
		}else{

			/* 图片中文处理，只针对特定部位URL编码 */
			String head =  file_path.substring(0,file_path.lastIndexOf("/")+1);
			String body =  file_path.substring(file_path.lastIndexOf("/")+1,file_path.lastIndexOf("."));
			String foot =  file_path.substring(file_path.lastIndexOf("."),file_path.length());
			try {
				file_path =  head + URLEncoder.encode(body,"UTF-8") + foot;
			} catch (UnsupportedEncodingException e) {
				log.error("查询图片接口:URLEncoder编码(UTF-8)异常:"+e.getMessage(),e);
				return filePath;
			}
			return  MyConfigUtil.IMG_DOMAIN + file_path;
		}
	}

	/**z`
	 * APP订单状态和系统订单状态互换
	 * @param orderStatus
	 * @param type
     * @return
     */
	public String  convertAppOrderStatus(String orderStatus,int type){
		String status = null;
		if(type == 1){//APP => this
			//全部订单 0 待付款 1 待发货2 待收货3 已完成7
			//全部
			if("0".equals(orderStatus))
				return "0";
			//待付款
			if("1".equals(orderStatus))
				return "1";
			//待发货
			if("2".equals(orderStatus))
				return "2";
			//待收货
			if("3".equals(orderStatus))
				return "3";
			//已完成
			if("7".equals(orderStatus))
				return "7";
			//拒收中
			if("800".equals(orderStatus))
				return "4";
			//补货中
			if("900".equals(orderStatus))
				return "5";
			//已取消
			if("10".equals(orderStatus))
				return "6";
		}else if(type == 2) {//this => APP
			//待付款
			if(BuyerOrderStatusEnum.PendingPayment.getType().equals(orderStatus))
				return "1";
			//待发货
			if(BuyerOrderStatusEnum.BackOrder.getType().equals(orderStatus))
				return "2";
			//待收货
			if(BuyerOrderStatusEnum.ReceiptOfGoods.getType().equals(orderStatus))
				return "3";
			//已完成
			if(BuyerOrderStatusEnum.Finished.getType().equals(orderStatus))
				return "7";
			//拒收中
			if(BuyerOrderStatusEnum.Rejecting.getType().equals(orderStatus))
				return "800";
			//补货中
			if(BuyerOrderStatusEnum.Replenishing.getType().equals(orderStatus))
				return "900";
			//已取消
			if(BuyerOrderStatusEnum.Canceled.getType().equals(orderStatus))
				return "10";
		}
		return status;
	}


	/**
	 * 延期收货
	 * @param flowId
	 */
	public Map<String,Object> updateOrderDelayTimes(String flowId){
		Map<String,Object> resutlMap = new HashMap<String,Object>();
		Order order = orderMapper.getOrderbyFlowId(flowId);
		if(UtilHelper.isEmpty(order)){
			resutlMap.put("statusCode",-3);
			resutlMap.put("message","未找到订单");
			return  resutlMap;
		}
		Integer day =0;
		if(!UtilHelper.isEmpty(order.getDelayTimes())){
			if(order.getDelayTimes()>=2){
				resutlMap.put("statusCode",-3);
				resutlMap.put("message","当前订单已延期两次，不可延期");
				return  resutlMap;
			}
			day += CommonType.POSTPONE_TIME*order.getDelayTimes();
		}
		day += CommonType.AUTO_RECEIVE_TIME; //自动加上七天
		day +=DateUtils.getDaysBetweenStartAndEnd(systemDateMapper.getSystemDate(),order.getDeliverTime());
		if(day<=3){
			// 延期收货订单逻辑
			Integer delayTimes = order.getDelayTimes()==null?0:order.getDelayTimes();
			delayTimes++ ;
			String nowTimeStr = systemDateMapper.getSystemDate();
			order.setDelayTimes(delayTimes);
			order.setUpdateTime(nowTimeStr);
			order.setDelayLog((order.getDelayLog() == null ? "" : order.getDelayLog()) + nowTimeStr + ",当前第" + delayTimes + "次延期收货;");
			try {
				orderMapper.update(order);
				resutlMap.put("statusCode", 0);
				resutlMap.put("message","成功");
			}catch (Exception e){
				resutlMap.put("statusCode",-3);
				resutlMap.put("message","延迟收货异常,请您稍后再试");
			}
		}else{
			resutlMap.put("statusCode",-3);
			resutlMap.put("message","您好！距离确认收货截止日期前3天内才可以延期!");
		}
		return  resutlMap;
	}

	public OrderBean getOrderDetailResponseInfo(String orderId,Integer custId,IProductDubboManageService iProductDubboManageService) throws Exception{
		OrderBean orderBean=new OrderBean();
		Order order=new Order();
		order.setCustId(custId);
		order.setFlowId(orderId);
		OrderDetailsDto orderDetailsDto=this.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsDto)){
			return null;
		}
		//详情对象
		String hideOrderStatus = this.convertAppOrderStatus(this.getBuyerOrderStatus(orderDetailsDto.getOrderStatus(), orderDetailsDto.getPayType()).getType(), 2);
		if(BuyerOrderStatusEnum.Finished.getType().equals(hideOrderStatus)){
			OrderExceptionDto oed = new OrderExceptionDto();
			oed.setFlowId(orderDetailsDto.getFlowId());
			oed.setCustId(custId);
			List<OrderExceptionDto> oedRejectList =orderExceptionMapper.findBuyerRejectOrderStatusCount(oed);
			if(!UtilHelper.isEmpty(oedRejectList)&& oedRejectList.size()>0){
				hideOrderStatus ="804";
			}
			List<OrderExceptionDto> oedReplenisList = orderExceptionMapper.findBuyerReplenishmentStatusCount(oed);
			if(!UtilHelper.isEmpty(oedReplenisList)&& oedReplenisList.size()>0){
				hideOrderStatus ="905";
			}
		}
		orderBean.setOrderStatus(hideOrderStatus);
		orderBean.setOrderId(orderDetailsDto.getFlowId());
		orderBean.setCreateTime(orderDetailsDto.getCreateTime());
		orderBean.setSupplyName(orderDetailsDto.getSupplyName());
		orderBean.setLeaveMsg(orderDetailsDto.getLeaveMessage());
		orderBean.setQq("");
		orderBean.setPayType(orderDetailsDto.getPayType());
		orderBean.setDeliveryMethod(orderDetailsDto.getOrderDelivery().getDeliveryMethod());
		orderBean.setBillType(orderDetailsDto.getBillType());
		orderBean.setOrderTotal(Double.parseDouble(orderDetailsDto.getOrderTotal().toString()));
		orderBean.setFinalPay(Double.parseDouble(UtilHelper.isEmpty(orderDetailsDto.getFinalPay()) ? "0" : orderDetailsDto.getFinalPay().toString()));
		orderBean.setProductNumber(orderDetailsDto.getTotalCount());
		orderBean.setSupplyId(orderDetailsDto.getSupplyId());
		orderBean.setPostponeTime(orderDetailsDto.getDelayTimes());
		orderBean.setVarietyNumber(orderDetailsDto.getDetails().size());
		//地址对象
		AddressBean address=new AddressBean();
		address.setDeliveryPhone(orderDetailsDto.getOrderDelivery().getReceiveContactPhone());
		address.setDeliveryName(orderDetailsDto.getOrderDelivery().getReceivePerson());
		address.setAddressType(0);
		address.setAddressProvince(orderDetailsDto.getOrderDelivery().getReceiveProvince());
		address.setAddressCity(orderDetailsDto.getOrderDelivery().getReceiveCity());
		address.setAddressCounty(orderDetailsDto.getOrderDelivery().getReceiveRegion());
		address.setAddressDetail(orderDetailsDto.getOrderDelivery().getReceiveAddress());
		address.setAddressId(orderDetailsDto.getOrderDelivery().getDeliveryId());
		orderBean.setAddress(address);
		orderBean.setOrderStatusName(this.getBuyerOrderStatus(orderDetailsDto.getOrderStatus(), orderDetailsDto.getPayType()).getValue());
		//商品列表
		List<OrderProductBean> productList=new ArrayList<OrderProductBean>();
		for(OrderDetail orderDetail:orderDetailsDto.getDetails()){
			OrderProductBean ordeProductBean=new OrderProductBean();
			ordeProductBean.setQuantity(orderDetail.getProductCount());
			ordeProductBean.setProductId(orderDetail.getSpuCode());
			ordeProductBean.setProductPicUrl(getProductImg(orderDetail.getSpuCode(),iProductDubboManageService));
			ordeProductBean.setProductName(orderDetail.getShortName());
			ordeProductBean.setProductPrice(Double.parseDouble(orderDetail.getProductPrice().toString()));
			ordeProductBean.setSpec(orderDetail.getSpecification());
			ordeProductBean.setFactoryName(orderDetail.getManufactures());
			//批次信息
			OrderDeliveryDetail orderDeliveryDetail=new OrderDeliveryDetail();
			orderDeliveryDetail.setFlowId(orderId);
			orderDeliveryDetail.setOrderDetailId(orderDetail.getOrderDetailId());
			orderDeliveryDetail.setDeliveryStatus(1);
			List<OrderDeliveryDetail> deliveryList = orderDeliveryDetailService.listByProperty(orderDeliveryDetail);
			List<BatchBean> batchList=new ArrayList<BatchBean>();
			for (OrderDeliveryDetail detail : deliveryList){
				BatchBean batchBean=new BatchBean();
				batchBean.setBatchId(detail.getBatchNumber());
				batchBean.setBuyNumber(detail.getDeliveryProductCount());
				batchBean.setProductId(orderDetail.getProductCode());
				batchList.add(batchBean);
			}
			ordeProductBean.setBatchList(batchList);
			productList.add(ordeProductBean);
		}
		orderBean.setProductList(productList);
		return orderBean;
	}

	public List<Map<String,Object>> getOrderDetailForExport(OrderDto orderDto){
		return orderMapper.getOrderDetailForExport(orderDto);
	}
}
