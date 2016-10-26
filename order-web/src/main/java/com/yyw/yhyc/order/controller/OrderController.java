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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
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
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.OrderExportService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.order.service.SystemDateService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;
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
			map = orderService.validateProducts(userDto,orderDto,iCustgroupmanageDubbo,productDubboManageService,productSearchInterface);
			boolean result = (boolean) map.get("result");
			if(!result){
				return map;
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

		dataMap = orderService.checkOrderPage(userDto,shoppingCartIdList);

		if(!UtilHelper.isEmpty(dataMap) || !UtilHelper.isEmpty(dataMap.get("allShoppingCart"))){
			/* 账期订单拆单逻辑 */
			List<ShoppingCartListDto> allShoppingCart  = (List<ShoppingCartListDto>) dataMap.get("allShoppingCart");
			allShoppingCart = orderService.handleDataForPeriodTermOrder(userDto,allShoppingCart,productDubboManageService,creditDubboService);
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
			List<Map<String,Object>> list =  orderService.getOrderDetailForExport(orderDto);
			
			HSSFWorkbook  wb = new HSSFWorkbook(); 
			HSSFSheet sheet = wb.createSheet("明细");
			
			if(list != null && list.size()>0){
				int rownum=0;
				int listSize = list.size();
				Map<String,Object> tempMap = null;
				//设置列宽
				sheet.setDefaultColumnWidth(30);  
				sheet.setDefaultRowHeightInPoints(20);  
				HSSFCellStyle cellStyle = orderExportService.createCellStyle(wb);
				
				HSSFCellStyle contentStyle = orderExportService.createContentCellStyle(wb);
				for(int i=0;i<listSize;i++){
					tempMap = list.get(i);
					String flow_id = (String)tempMap.get("flow_id");
					
					if(i>0){
						rownum +=3;
					}
					rownum = this.createInitRow(sheet, cellStyle, rownum);
					//订单的第一行
					HSSFRow row1 = sheet.getRow(rownum-3);  
					HSSFCell cell1 = row1.createCell(1);
					cell1.setCellValue((String)tempMap.get("create_time"));
					cell1.setCellStyle(contentStyle);
					
					HSSFCell cell2 = row1.createCell(3);
					cell2.setCellValue(flow_id);
					cell2.setCellStyle(contentStyle);
					
					
					HSSFCell cell3 = row1.createCell(5);
					cell3.setCellValue(orderService.getBuyerOrderStatus((String)tempMap.get("order_status"),
							(Integer)tempMap.get("pay_type")).getValue());
					cell3.setCellStyle(contentStyle);
					
					HSSFCell cell4 = row1.createCell(7);
					cell4.setCellValue(BillTypeEnum.getBillTypeName((Integer)tempMap.get("bill_type")));
					cell4.setCellStyle(contentStyle);
					
					//订单的第二行
					HSSFRow row2 = sheet.getRow(rownum-2);  
					HSSFCell cell5 = row2.createCell(1);
					cell5.setCellValue((String)tempMap.get("supply_name"));
					cell5.setCellStyle(contentStyle);
					
					HSSFCell cell6 = row2.createCell(3);
					cell6.setCellValue((String)tempMap.get("delivery_address"));
					cell6.setCellStyle(contentStyle);
					
					HSSFCell cell7 = row2.createCell(5);
					cell7.setCellValue((String)tempMap.get("delivery_person"));
					cell7.setCellStyle(contentStyle);
					
					HSSFCell cell8 = row2.createCell(7);
					cell8.setCellValue((String)tempMap.get("delivery_contact_phone"));
					cell8.setCellStyle(contentStyle);
					
					//订单的第三行
					HSSFRow row3 = sheet.getRow(rownum-1);  
					HSSFCell cell9 = row3.createCell(3);
					cell9.setCellValue((String)tempMap.get("receive_address"));
					cell9.setCellStyle(contentStyle);
					
					HSSFCell cell10 = row3.createCell(5);
					cell10.setCellValue((String)tempMap.get("receive_person"));
					cell10.setCellStyle(contentStyle);
					
					HSSFCell cell11 = row3.createCell(7);
					cell11.setCellValue((String)tempMap.get("receive_contact_phone"));
					cell11.setCellStyle(contentStyle);
					
					//明细
					String orderDetail = (String)tempMap.get("detail");
					//商品金额
					double productAmount = 0.00;
					if(StringUtils.isNotBlank(orderDetail)){
						String[] orderDetailArray = orderDetail.split(";");
						for(int j=0;j<orderDetailArray.length;j++){
							rownum++;
							String[] detail = orderDetailArray[j].split(",");
						 
							HSSFRow detailRow = sheet.createRow(rownum);  
							HSSFCell newcell1 = detailRow.createCell(0);
							newcell1.setCellValue(detail[0]);
							newcell1.setCellStyle(contentStyle);
							
							HSSFCell newcell2 = detailRow.createCell(1);
							newcell2.setCellValue(detail[1]);
							newcell2.setCellStyle(contentStyle);
							
							HSSFCell newcell3 = detailRow.createCell(2);
							newcell3.setCellValue(detail[2]);
							newcell3.setCellStyle(contentStyle);
							
							HSSFCell newcell4 = detailRow.createCell(3);
							newcell4.setCellValue(detail[3]);
							newcell4.setCellStyle(contentStyle);
							
							HSSFCell newcell5 = detailRow.createCell(4);
							newcell5.setCellValue(detail[4]);
							newcell5.setCellStyle(contentStyle);
							
							HSSFCell newcell6 = detailRow.createCell(5);
							newcell6.setCellValue(detail[5]);
							newcell6.setCellStyle(contentStyle);
							
							HSSFCell newcell7 = detailRow.createCell(6);
							newcell7.setCellValue(detail[6]);
							newcell7.setCellStyle(contentStyle);
							productAmount += Double.parseDouble(detail[6]);
							
							HSSFCell newcell8 = detailRow.createCell(7);
							newcell8.setCellValue("");
							newcell8.setCellStyle(contentStyle);
						}
					}
					rownum = this.createExtraRow(sheet, cellStyle,contentStyle, rownum);
					HSSFRow detailRow = sheet.getRow(rownum-1);  
					HSSFCell newcell1 = detailRow.createCell(1);
					newcell1.setCellValue(productAmount);
					newcell1.setCellStyle(contentStyle);
					
					HSSFCell newcell2 = detailRow.createCell(3);
					newcell2.setCellValue(String.valueOf(tempMap.get("preferential_money")));
					newcell2.setCellStyle(contentStyle);
					
					HSSFCell newcell3 = detailRow.createCell(5);
					newcell3.setCellValue(String.valueOf(tempMap.get("order_total")));
					newcell3.setCellStyle(contentStyle);
					
					HSSFRow lastRow = sheet.getRow(rownum);  
					HSSFCell lastcell1 = lastRow.createCell(1);
					lastcell1.setCellValue(String.valueOf(tempMap.get("leave_message")));
					lastcell1.setCellStyle(contentStyle);
					
				}
				 
			}
			
			wb.write(os);
			os.flush();
			os.close();
		}  catch (Exception e) {
			logger.error("订单导出报错",e);
		}
    }
    
    private int createInitRow(HSSFSheet sheet,HSSFCellStyle cellStyle,int rownum){
    	HSSFRow row1 = sheet.createRow(rownum);  
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("下单时间");
		cell11.setCellStyle(cellStyle);
		
		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("订单号");
		cell12.setCellStyle(cellStyle);
		
		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("订单状态");
		cell13.setCellStyle(cellStyle);
		
		HSSFCell cell14 = row1.createCell(6);
		cell14.setCellValue("发票类型");
		cell14.setCellStyle(cellStyle);
		
		rownum++;
		
		HSSFRow row2 = sheet.createRow(rownum);  
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("供应商");
		cell21.setCellStyle(cellStyle);
		
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("发货地址");
		cell22.setCellStyle(cellStyle);
		
		HSSFCell cell23 = row2.createCell(4);
		cell23.setCellValue("发货联系人");
		cell23.setCellStyle(cellStyle);
		
		HSSFCell cell24 = row2.createCell(6);
		cell24.setCellValue("发货人联系方式");
		cell24.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row3 = sheet.createRow(rownum);  
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellValue("");
		cell31.setCellStyle(cellStyle);
		
		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellValue("收货地址");
		cell32.setCellStyle(cellStyle);
		
		HSSFCell cell33 = row3.createCell(4);
		cell33.setCellValue("收货联系人");
		cell33.setCellStyle(cellStyle);
		
		HSSFCell cell34 = row3.createCell(6);
		cell34.setCellValue("收货人联系方式");
		cell34.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row4 = sheet.createRow(rownum);  
		
		HSSFCell cell41 = row4.createCell(0);
		cell41.setCellValue("商品编码");
		cell41.setCellStyle(cellStyle);
		 
		HSSFCell cell42 = row4.createCell(1);
		cell42.setCellValue("通用名");
		cell42.setCellStyle(cellStyle);
		
		HSSFCell cell43 = row4.createCell(2);
		cell43.setCellValue("规格");
		cell43.setCellStyle(cellStyle);
		
		HSSFCell cell44 = row4.createCell(3);
		cell44.setCellValue("厂商");
		cell44.setCellStyle(cellStyle);
		
		HSSFCell cell54 = row4.createCell(4);
		cell54.setCellValue("单价（元）");
		cell54.setCellStyle(cellStyle);
		
		HSSFCell cell64 = row4.createCell(5);
		cell64.setCellValue("数量");
		cell64.setCellStyle(cellStyle);
		
		HSSFCell cell74 = row4.createCell(6);
		cell74.setCellValue("金额（元）");
		cell74.setCellStyle(cellStyle);
		
		HSSFCell cell84 = row4.createCell(7);
		cell84.setCellValue("促销信息");
		cell84.setCellStyle(cellStyle);
		
		return rownum;
    }
    
    private int createExtraRow(HSSFSheet sheet,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle,int rownum){
    	rownum++;
    	HSSFRow row1 = sheet.createRow(rownum);  
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("商品金额（元）");
		cell11.setCellStyle(cellStyle);
		
		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("优惠券（元）");
		cell12.setCellStyle(cellStyle);
		
		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("订单金额（元）");
		cell13.setCellStyle(cellStyle);
		
		orderExportService.fillEmptyCell(row1,contentStyle,4,7);
		
		rownum++;
		HSSFRow row2 = sheet.createRow(rownum);  
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("买家留言");
		cell21.setCellStyle(cellStyle);
		orderExportService.fillEmptyCell(row2,contentStyle,0,7);
		return rownum;
    }
    
}
