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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.adviser.IAdviserManageDubbo;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.adviser.AdviserModel;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.annotation.Token;
import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.AdviserDto;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.OrderBuyerExportService;
import com.yyw.yhyc.order.service.OrderCreateService;
import com.yyw.yhyc.order.service.OrderExportService;
import com.yyw.yhyc.order.service.OrderFullReductionService;
import com.yyw.yhyc.order.service.OrderLogService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.OrderSubmitService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.order.service.SystemDateService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;
import com.yyw.yhyc.utils.CacheUtil;
import com.yyw.yhyc.utils.DateUtils;

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
	
	@Autowired
	private OrderFullReductionService orderFullReductionService;
	@Autowired
	private OrderCreateService orderCreateService;
	
	@Reference
	private IPromotionDubboManageService promotionDubboManageService;

	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderSubmitService orderSubmitService;
	@Autowired
	private OrderBuyerExportService orderBuyerExportService;
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
		//每隔3秒提交订单，否则提示提交订单频率过高
		String createOrderUserCache = null;
		String createOrderUserCacheKey = "CREATEORDER_"+userDto.getCustId();
		try {
			createOrderUserCache = CacheUtil.getSingleton().get(createOrderUserCacheKey);
		} catch (Exception e1) {
			logger.error("获取缓存失败",e1);
		}
		if(StringUtils.isNotBlank(createOrderUserCache)){
			throw new Exception("提交订单频率过高");
		}else{
			CacheUtil.getSingleton().add(createOrderUserCacheKey,createOrderUserCacheKey,3);
		}
		Map<String,Object> map =this.orderSubmitService.createOrderSubmit(request, orderCreateDto, userDto, iCustgroupmanageDubbo, productDubboManageService, productSearchInterface, iPromotionDubboManageService, creditDubboService);
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
			orderDto.setOrgTotal(order.getOrgTotal());
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
			allShoppingCart = orderService.handleDataForPeriodTermOrder(userDto,allShoppingCart,creditDubboService,productSearchInterface,iCustgroupmanageDubbo);
			dataMap.put("allShoppingCart",allShoppingCart);
		}
		
		/***********以下处理订单的满减促销**************/
		if(!UtilHelper.isEmpty(dataMap) || !UtilHelper.isEmpty(dataMap.get("allShoppingCart"))){
			List<ShoppingCartListDto> allShoppingCart  = (List<ShoppingCartListDto>) dataMap.get("allShoppingCart");
			allShoppingCart=this.orderFullReductionService.processFullReduction(allShoppingCart,promotionDubboManageService);
			Map<String,Object> returnMap=this.orderFullReductionService.processCalculationOrderShareMoney(allShoppingCart);
			allShoppingCart=(List<ShoppingCartListDto>) returnMap.get("allShoppingCart");
			
			BigDecimal allOrderShareMoney=(BigDecimal) returnMap.get("allOrderShareMoney");
			//无优惠的金额
			BigDecimal orderPriceCount=(BigDecimal) dataMap.get("orderPriceCount"); 
			
			orderPriceCount=orderPriceCount.subtract(allOrderShareMoney);
			if(orderPriceCount.compareTo(new BigDecimal(0))==-1){
				dataMap.put("orderPriceCount",0);
			}else{
				dataMap.put("orderPriceCount",orderPriceCount);
			}
		
			dataMap.put("allShoppingCart",allShoppingCart);
			dataMap.put("allOrderShareMoney",allOrderShareMoney);//所有订单的优惠金额
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
        logger.debug("参数requestModel.getParam()=="+requestModel.getParam());
        String jsonStr=JSON.toJSONString(requestModel.getParam());
		OrderDto orderDto =JSON.parseObject(jsonStr, OrderDto.class);
		
	    logger.debug("参数处理结果==orderDto"+orderDto.toString());
	    
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		
		logger.debug("参数userDto=="+userDto.toString());
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
		orderService.updateOrderStatusForBuyer(userDto, orderId,iPromotionDubboManageService);
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
		String jsonStr=JSON.toJSONString(requestModel.getParam());
		OrderDto orderDto =JSON.parseObject(jsonStr, OrderDto.class);
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
		orderService.updateOrderStatusForSeller(userDto, order.getOrderId(), order.getCancelResult(),iPromotionDubboManageService);

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
	 * 销售商下载发货模板
	 * @param flowId
	 */
	@RequestMapping(value = {"/exportBatchTemplate"}, method = RequestMethod.POST)
	@ResponseBody
	public void exportBatchTemplate(String batchTemplateFlowId,String orderType){
		
		
		String fileName = "发货模板.xls";
		/* 设置字符集为'UTF-8' */
		try {
			response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("GBK"),"iso8859-1" ));

			OutputStream os = response.getOutputStream();
			HSSFWorkbook wb =this.orderExportService.exportSaleProduceTemplate(batchTemplateFlowId,orderType);

			wb.write(os);
			os.flush();
			os.close();
		}  catch (Exception e) {
			logger.error("导出模板报错",e);
		}
		
	}

	/**
	 * 导出销售订单信息
	 */
	@RequestMapping(value = {"/exportOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public void exportOrder(String province,String city,String district,String flowId,String custName,Integer payType,String createBeginTime,String createEndTime,String orderStatus,String promotionName,String adviserCode){
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
		orderDto.setAdviserCode(adviserCode);
		orderDto.setPromotionName(promotionName);
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
			//HSSFWorkbook wb = orderExportService.exportSaleOrder(orderDto);
			HSSFWorkbook wb = orderBuyerExportService.exportSaleOrder(orderDto);
			

			wb.write(os);
			os.flush();
			os.close();
		}  catch (Exception e) {
			logger.error("订单导出报错",e);
		}
    }

    @RequestMapping(value = "/queryAdviser",method = RequestMethod.POST)
	@ResponseBody
	public List<AdviserDto>  queryAdviser(HttpServletRequest request) throws Exception{
    	UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		//查询供应商的销售顾问信息
		List<AdviserDto> adviserList = new ArrayList<AdviserDto>();
		List<AdviserModel> list = iAdviserManageDubbo.queryByEnterId(userDto.getCustId());
		for(AdviserModel one : list){
			AdviserDto adviserDto = new AdviserDto();
			adviserDto.setAdviserCode(one.getAdviser_code());
			adviserDto.setAdviserName(one.getAdviser_name());
			adviserDto.setAdviserPhoneNumber(one.getPhone_number());
			adviserDto.setAdviserRemark(one.getRemark());
			adviserList.add(adviserDto);
		}
		return adviserList;
	}
}
