/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.framework.core.model.util.StringUtil;
import com.yaoex.usermanage.interfaces.adviser.IAdviserManageDubbo;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.bo.Pagination;import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.annotation.Token;
import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.*;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.*;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;
import com.yyw.yhyc.utils.DateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Reference
	private CreditDubboServiceInterface creditDubboService;

	@Reference
	private IAdviserManageDubbo iAdviserManageDubbo;

	@Autowired
	private SystemPayTypeService systemPayTypeService;

	@Autowired
	private SystemDateService systemDateService;

	@Reference
	private IProductDubboManageService productDubboManageService;

	@Reference
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;

	@Autowired
	private UsermanageEnterpriseService enterpriseService;

	@Reference
	private ProductSearchInterface productSearchInterface;

	@Autowired
	private OrderExportService orderExportService;

	@Reference
	private IPromotionDubboManageService iPromotionDubboManageService;

    /**
     * 通过主键查询实体对象
     * @return
     */
    @RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Order getByPK(@PathVariable("key") Integer key) throws Exception {
        return orderService.getByPK(key);
    }

    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
    @ResponseBody
    public Pagination<Order> listPgOrder(RequestModel<Order> requestModel) throws Exception {
        Pagination<Order> pagination = new Pagination<Order>();

        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());

        return orderService.listPaginationByProperty(pagination, requestModel.getParam());
    }

    /**
     * 新增记录
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody Order order) throws Exception {
		orderService.save(order);
    }

    /**
     * 根据多条主键值删除记录
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception {
		orderService.deleteByPKeys(requestListModel.getList());
    }

    /**
     * 修改记录
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(@RequestBody Order order) throws Exception {
		orderService.update(order);
    }

	/**
	 * 创建订单
	 * @param orderCreateDto
	 * @throws Exception
	 */
	@Token(remove=true)
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createOrder( OrderCreateDto orderCreateDto) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		if(UtilHelper.isEmpty(orderCreateDto))throw new Exception("非法参数");
		orderCreateDto.setUserDto(userDto);
		if(UtilHelper.isEmpty(orderCreateDto.getOrderDtoList()))throw new Exception("非法参数");
		Map<String,Object> map = new HashMap<String, Object>();
		for(OrderDto orderDto : orderCreateDto.getOrderDtoList()){
			/* 校验采购商状态、资质 */
			UsermanageEnterprise buyer = enterpriseService.getByEnterpriseId(userDto.getCustId()+"");
			if(UtilHelper.isEmpty(buyer)){
				logger.info("统一校验订单商品接口-buyer ：" + buyer);
				map.put("result", false);
				map.put("message", "采购商不存在");
				map.put("goToShoppingCart", true);
				return map;
			}
			orderDto.setBuyer(buyer);
			orderDto.setCustId(Integer.valueOf(buyer.getEnterpriseId()));
			orderDto.setCustName(buyer.getEnterpriseName());

			/* 校验要供应商状态、资质 */
			UsermanageEnterprise seller = enterpriseService.getByEnterpriseId(orderDto.getSupplyId() + "");
			if(UtilHelper.isEmpty(seller)){
				logger.info("统一校验订单商品接口-seller ：" + seller);
				map.put("result", false);
				map.put("message", "供应商不存在");
				map.put("goToShoppingCart", true);
				return map;
			}
			orderDto.setSeller(seller);
			orderDto.setSupplyName(seller.getEnterpriseName());

			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
			map = orderService.validateProducts(userDto,orderDto,iCustgroupmanageDubbo,productDubboManageService,
					productSearchInterface,iPromotionDubboManageService);
			boolean result = (boolean) map.get("result");
			if(!result){
				return map;
			}

			/**销售顾问信息**/
			if(StringUtil.isNotEmpty(orderDto.getAdviserName())){
				String [] adviserInfo = orderDto.getAdviserName().split(";");
				orderDto.setAdviserCode(adviserInfo[0]);
				orderDto.setAdviserName(adviserInfo[1]);
				orderDto.setAdviserPhoneNumber(adviserInfo[2]);
				if(adviserInfo.length > 3){
					orderDto.setAdviserRemark(adviserInfo[3]);
				}
			}
		}
		//订单来源 限用pc
		orderCreateDto.setSource(1);
		Map<String,Object> newOrderMap = orderService.createOrder(orderCreateDto);
		List<Order> orderList = (List<Order>) newOrderMap.get("orderNewList");

		String orderIdStr = "";
		if(!UtilHelper.isEmpty(orderList)){
			for(Order order : orderList){
				if(UtilHelper.isEmpty(order)) continue;
				if(UtilHelper.isEmpty(orderIdStr)){
					orderIdStr += order.getOrderId();
				}else{
					orderIdStr += ","+order.getOrderId();
				}
			}
		}
		map = new HashMap<String,Object>();
		map.put("url","/order/createOrderSuccess?orderIds="+orderIdStr);

		/* 生成账期订单后，调用接口更新资信可用额度 */
		List<Order> orderNewList = (List<Order>) newOrderMap.get("orderNewList");
		logger.info("创建订单接口-资信可用额度更新接口,creditDubboService = " + creditDubboService);
		if (!UtilHelper.isEmpty(orderNewList) && !UtilHelper.isEmpty(creditDubboService)) {
			CreditParams creditParams = null;
			for(Order order : orderNewList){
				if(UtilHelper.isEmpty(order)) continue;
				if (!SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(order.getPayTypeId())) {
					continue;
				}
				creditParams = new CreditParams();
				creditParams.setStatus("1");  //创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
				creditParams.setFlowId(order.getFlowId());//订单编号
				creditParams.setOrderTotal(order.getOrderTotal());//订单总金额
				creditParams.setBuyerCode(order.getCustId() + "");
				creditParams.setBuyerName(order.getCustName());
				creditParams.setSellerCode(order.getSupplyId() + "");
				creditParams.setSellerName(order.getSupplyName());
				creditParams.setPaymentDays(order.getPaymentTerm());//账期
				CreditDubboResult creditDubboResult = null;
				try{
					logger.info("创建订单接口-生成账期订单后，调用接口更新资信可用额度,请求参数 creditParams= " + creditParams);
					creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
					logger.info("创建订单接口-生成账期订单后，调用接口更新资信可用额度,响应参数creditDubboResult= " + creditDubboResult);

					/* 账期订单信息发送成功后，更新该订单的支付状态与支付时间 */
					if(!UtilHelper.isEmpty(creditDubboResult) && "1".equals(creditDubboResult.getIsSuccessful())){
						order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
						order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
						order.setPayTime(systemDateService.getSystemDate());
						update(order);
						logger.info("创建订单接口-生成账期订单后，成功更新资信可用额度,更新订单信息，order=" + order);
					}else{
						logger.info("创建订单接口-生成账期订单后，更新资信可用额度失败..message" + (UtilHelper.isEmpty(creditDubboResult) ? "null" : creditDubboResult.getMessage()));
					}
				}catch (Exception e){
					logger.error(e.getMessage());
				}

			}
		}
		return map;
	}

	@RequestMapping(value = "/createOrderSuccess", method = RequestMethod.GET)
	public ModelAndView createOrderSuccess(@RequestParam("orderIds") String orderIds) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto)) {
			throw new Exception("登陆超时");
		}
		ModelAndView model = new ModelAndView();

		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
		OrderDto orderDto = null;
		/* 计算所有订单中，在线支付订单总额 */
		BigDecimal onLinePayOrderPriceCount = new BigDecimal(0);

		logger.info("创建订单成功页面，请求参数 orderIds = " + orderIds);
		if(UtilHelper.isEmpty(orderIds)){
			throw new Exception("非法参数");
		}
		String [] orderIdStr = orderIds.split(",");
		for(String orderId : orderIdStr){
			if(UtilHelper.isEmpty(orderId) || "null".equalsIgnoreCase(orderId)) continue;
			orderDto = new OrderDto();
			Order order = orderService.getByPK(Integer.valueOf(orderId));
			if(UtilHelper.isEmpty(order)) continue;
			orderDto.setOrderId(order.getOrderId());
			orderDto.setCustId(order.getCustId());
			orderDto.setSupplyId(order.getSupplyId());
			orderDto.setFlowId(order.getFlowId());
			orderDto.setSupplyName(order.getSupplyName());
			orderDto.setOrderTotal(order.getOrderTotal());
			orderDto.setFinalPay(order.getFinalPay());
			orderDto.setPayStatus(order.getPayStatus());
			orderDto.setPayTypeId(order.getPayTypeId());

			SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
			if(!UtilHelper.isEmpty(systemPayType)){
				orderDto.setPayTypeName(SystemPayTypeEnum.getPayTypeName(systemPayType.getPayType()));
			}
			orderDtoList.add(orderDto);
			if(OnlinePayTypeEnum.getPayName(order.getPayTypeId())  != null ){
				onLinePayOrderPriceCount = onLinePayOrderPriceCount.add(order.getSettlementMoney());
			}
		}

		model.setViewName("order/createOrderSuccess");
		model.addObject("orderDtoList",orderDtoList);
		model.addObject("onLinePayOrderPriceCount",onLinePayOrderPriceCount);
		model.addObject("userDto",userDto);
		return model;
	}

	/**
	 * 检查订单页
	 * @return
	 * @throws Exception
     */
	@Token(save=true)
	@RequestMapping(value = "/checkOrderPage", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView checkOrderPage(ShoppingCartListDto shoppingCartListDto) throws Exception {
		UserDto userDto = super.getLoginUser();
		ModelAndView model = new ModelAndView();
		model.addObject("userDto",userDto);

		Map<String,Object> dataMap = null;
		if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())){
			model.addObject("dataMap",dataMap);
			model.setViewName("order/checkOrderPage");
			return model;
		}
		List<Integer> shoppingCartIdList = new ArrayList<>();
		for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
			if(UtilHelper.isEmpty(shoppingCartDto) || UtilHelper.isEmpty(shoppingCartDto.getShoppingCartId())){
				continue;
			}
			shoppingCartIdList.add(shoppingCartDto.getShoppingCartId());
		}

		dataMap = orderService.checkOrderPage(userDto,shoppingCartIdList,iAdviserManageDubbo);

		if(!UtilHelper.isEmpty(dataMap) || !UtilHelper.isEmpty(dataMap.get("allShoppingCart"))){
			/* 账期订单拆单逻辑 */
			List<ShoppingCartListDto> allShoppingCart  = (List<ShoppingCartListDto>) dataMap.get("allShoppingCart");
			allShoppingCart = orderService.handleDataForPeriodTermOrder(userDto,allShoppingCart,productDubboManageService,creditDubboService,iPromotionDubboManageService);
			dataMap.put("allShoppingCart",allShoppingCart);
		}

		model.addObject("dataMap",dataMap);
		model.setViewName("order/checkOrderPage");
		return model;
	}

	/**
	 * 订单成功页面-查看收款账号信息页
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkAccountInfo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView checkAccountInfo() throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("order/checkAccountInfo");
		return model;
	}




    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerOrder(@RequestBody RequestModel<OrderDto> requestModel){
        /**
		 * http://localhost:8088/order/listPgBuyerOrder
         * {"pageSize":2,"pageNo":2,"param":{"custId":1,"flowId":"1","payType":1,"supplyName":"上","createBeginTime":"2016-01-02","createEndTime":"2016-8-20","orderStatus":"1"}}
         */
        Pagination<OrderDto> pagination = new Pagination<OrderDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
		OrderDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
        return orderService.listPgBuyerOrder(pagination, orderDto);
    }

	/**
	 * 采购商取消订单
	 * @return
	 */
	@RequestMapping(value = "/buyerCancelOrder/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public void buyerCancelOrder(@PathVariable("orderId") Integer orderId){
		/**
		 *  http://localhost:8088/order/buyerCancelOrder/2
		 */
		UserDto userDto = super.getLoginUser();
		orderService.updateOrderStatusForBuyer(userDto, orderId);
	}

	/**
	 * 销售订单查询
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgSellerOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgSellerOrder(@RequestBody RequestModel<OrderDto> requestModel){
		/**
		 * http://localhost:8088/order/listPgSellerOrder
		 * {"pageSize":2,"pageNo":2,"param":{"supplyId":1,"flowId":"1","payType":1,"custName":"上","createBeginTime":"2016-01-02","createEndTime":"2016-8-20","orderStatus":"1","province":"","city":"","district":""}}
		 */
		Pagination<OrderDto> pagination = new Pagination<OrderDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setSupplyId(userDto.getCustId());
		return orderService.listPgSellerOrder(pagination, orderDto);
	}

	/**
	 * 销售商取消订单
	 * @return
	 */
	@RequestMapping(value = "/sellerCancelOrder", method = RequestMethod.POST)
	@ResponseBody
	public void sellerCancelOrder(@RequestBody Order order){
		UserDto userDto = super.getLoginUser();
		orderService.updateOrderStatusForSeller(userDto, order.getOrderId(), order.getCancelResult());

		try {
			if(UtilHelper.isEmpty(creditDubboService)){
				logger.error("CreditDubboServiceInterface creditDubboService is null");
				throw new RuntimeException("资信接口调用失败！无资信服务！");
			}else{
				Order od =  orderService.getByPK(order.getOrderId());
				SystemPayType systemPayType= systemPayTypeService.getByPK(od.getPayTypeId());
				if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
					CreditParams creditParams = new CreditParams();
					creditParams.setSourceFlowId(od.getFlowId());//订单编码
					creditParams.setBuyerCode(od.getCustId() + "");
					creditParams.setSellerCode(od.getSupplyId() + "");
					creditParams.setBuyerName(od.getCustName());
					creditParams.setSellerName(od.getSupplyName());
					creditParams.setOrderTotal(od.getOrgTotal());//订单原始金额
					creditParams.setFlowId(od.getFlowId());//订单编码
					creditParams.setStatus("5");//创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
					// TODO: 2016/8/24 暂时忽略资信接口调用错误 
					try{
						CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
						if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
							throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
						}
					}catch (Exception e){
						logger.error("invoke creditDubboService.updateCreditRecord(..) error,msg:"+e.getMessage());
						throw new RuntimeException("调用接口调用失败！");
					}
				}
			}
		} catch (Exception e) {
			logger.error("orderService.getByPK error, pk: "+order.getOrderId()+",errorMsg:"+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 导出销售订单信息
	 */
	@RequestMapping(value = {"/exportOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public void exportOrder(String province,String city,String district,String flowId,String custName,Integer payType,String createBeginTime,String createEndTime,String orderStatus){
		// TODO: 2016/8/1 需要从usercontex获取登录用户id
		Pagination<OrderDto> pagination = new Pagination<OrderDto>();
		pagination.setPaginationFlag(true);
		pagination.setPageNo(1);
		pagination.setPageSize(6000);      //默认6000条数据
		OrderDto orderDto=new OrderDto();
		orderDto.setProvince(province);
		orderDto.setCity(city);
		orderDto.setDistrict(district);
		orderDto.setFlowId(flowId);
		orderDto.setCustName(custName);
		if(!UtilHelper.isEmpty(payType)) {
			orderDto.setPayType(payType);
		}
		orderDto.setCreateBeginTime(createBeginTime);
		orderDto.setCreateEndTime(createEndTime);
		orderDto.setOrderStatus(orderStatus);
		UserDto userDto = super.getLoginUser();
		orderDto.setSupplyId(userDto.getCustId());
		byte[] bytes=orderService.exportOrder(pagination, orderDto);
		String  fileName= null;
		try {
			fileName = new String(("订单报表"+new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())+".xls").getBytes("gbk"),"iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		ServletOutputStream stream = null;
		try {
			stream = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream.write(bytes);
			logger.error("导出成功");
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* 收款确认
	 * @return
	 */
	@RequestMapping(value = "/addForConfirmMoney", method = RequestMethod.POST)
	@ResponseBody
	public String addForConfirmMoney(@RequestBody OrderSettlement orderSettlement) throws Exception
	{
		// TODO: 2016/8/1 需要从usercontex获取登录用户id
		UserDto user = super.getLoginUser();
		orderService.addForConfirmMoney(user.getCustId(), orderSettlement);
		return "success";
	}

	/**
	 * 获取确认订单页面
	 * @return
	 */
	@RequestMapping(value = "/getConfirmMoneyView", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getConfirmMoneyView(Order order) throws Exception
	{
		// 登录卖家的id
		UserDto user = super.getLoginUser();
		order.setSupplyId(user.getCustId());
		OrderDetailsDto orderDetailsDto=orderService.getOrderDetails(order);
		//如果订单状态不为买家已下单或者订单支付类型不为现在支付则不能进行确认付款
		if(UtilHelper.isEmpty(orderDetailsDto)
				|| !SystemPayTypeEnum.PayOffline.getPayTypeName().equals(orderDetailsDto.getPayTypeName())
				|| !SystemOrderStatusEnum.BuyerOrdered.getType().equals(orderDetailsDto.getOrderStatus())){
			return null;
		}
		ModelAndView model = new ModelAndView();
		model.addObject("orderDetailsDto",orderDetailsDto);
		model.setViewName("order/confirmMoneyView");
		return model;
	}
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = "/getBuyOrderDetails", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getBuyOrderDetails(Order order) throws Exception
	{
		// 登录买家的id
		UserDto user = super.getLoginUser();
		order.setCustId(user.getCustId());
		OrderDetailsDto orderDetailsDto=orderService.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsDto)){
			return null;
		}
		ModelAndView model = new ModelAndView();
		model.addObject("orderDetailsDto",orderDetailsDto);
		model.setViewName("order/buyer_order_detail");
		return model;
	}

	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = "/getSupplyOrderDetails", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getSupplyOrderDetails(Order order) throws Exception
	{
		// 登录卖家的id
		UserDto user = super.getLoginUser();
		order.setSupplyId(user.getCustId());
		OrderDetailsDto orderDetailsDto=orderService.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsDto)){
			return null;
		}
		ModelAndView model = new ModelAndView();
		model.addObject("orderDetailsDto",orderDetailsDto);
		model.setViewName("order/seller_order_detail");
		return model;
	}

	@RequestMapping("/sellerOrderManage")
	public ModelAndView buyer_order_manage(){
		ModelAndView view = new ModelAndView("order/seller_order_manage");
		return view;
	}

	@RequestMapping("/buyerOrderManage")
	public ModelAndView login(){
		ModelAndView view = new ModelAndView("order/buyer_order_manage");
		return view;
	}

	@RequestMapping(value = "/showPostponeOrder",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> showPostponeOrder(Integer orderId)throws Exception{
		Map<String,Object> map = new HashedMap();
		Order order = orderService.getByPK(orderId);

		Integer day = DateUtils.getDaysBetweenStartAndEnd(systemDateService.getSystemDate(),order.getDeliverTime());
		day += CommonType.AUTO_RECEIVE_TIME; //自动加上七天
		if(order.getDelayTimes()!=null){//如果延期不为空则，加上延期时间
			day += CommonType.POSTPONE_TIME*order.getDelayTimes();
			map.put("delayTimes",order.getDelayTimes());
		}else{
			map.put("delayTimes",0);
		}

		map.put("day",day);
		return map;
	}

	@RequestMapping(value = "/postponeOrder",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> postponeOrder(@RequestBody Order order){
		String code = "1";
		Map<String,Object> map = new HashedMap();
		try {
			orderService.postponeOrder(order.getOrderId(),CommonType.POSTPONE_TIME);
		}catch (Exception e){
			code ="0";
		}
		map.put("code",code);
		return map;
	}

	 /**
     * 采购订单导出
     * @return
     */
    @RequestMapping("/exportSaleOrder")
    public void exportSaleOrder(){
    	String condition = request.getParameter("condition");
    	JSONObject paramJSON = new JSONObject();
		if(StringUtils.isNotBlank(condition)){
			paramJSON = JSON.parseObject(condition).getJSONObject("param");
		}
    	OrderDto orderDto = new OrderDto();
    	orderDto.setCreateBeginTime((String)paramJSON.get("createBeginTime"));
    	orderDto.setCreateEndTime((String)paramJSON.get("createEndTime"));
    	orderDto.setFlowId((String)paramJSON.get("flowId"));
    	orderDto.setSupplyName((String)paramJSON.get("supplyName"));
    	String payType = (String)paramJSON.get("payType");
    	if(StringUtils.isNotBlank(payType)){
    		orderDto.setPayType(Integer.parseInt(payType));
    	}
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());

		String fileName = "订单明细.xls";
		/* 设置字符集为'UTF-8' */
		try {
			response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("GBK"),"iso8859-1" ));

			OutputStream os = response.getOutputStream();
			HSSFWorkbook wb = orderExportService.exportSaleOrder(orderDto);

			wb.write(os);
			os.flush();
			os.close();
		}  catch (Exception e) {
			logger.error("订单导出报错",e);
		}
    }

}
