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
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yao.trade.interfaces.credit.model.PeriodDubboResult;
import com.yao.trade.interfaces.credit.model.PeriodParams;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.annotation.Token;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.*;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;

import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
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

	@Reference(timeout = 50000)
	private CreditDubboServiceInterface creditDubboService;


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
	 * 校验要购买的商品(通用方法)
	 * @param orderDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/validateProducts", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> validateProducts(OrderDto orderDto) throws Exception {

		/* 获取当前登陆人的企业用户id */
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		Integer currentLoginCustId = userDto.getCustId();

		Map<String,Object> map = new HashMap<String, Object>();
		boolean validateResult = false;
		try{
			validateResult = orderService.validateProducts(currentLoginCustId,orderDto);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		map.put("result", validateResult);
		return map;
	}

	/**
	 * 创建订单
	 * 请求数据格式：

	 {
	 	 "custId":123,
	 	 "receiveAddressId":2,
		 "orderDtoList": [
				 {
					 "custId": "123",
					 "supplyId": "321",
					 "productInfoDtoList": [
						 {
							 "id": "111",
							 "productCount": "1",
	 						 "productPrice":12.5
						 },
						 {
							 "id": "112",
							 "productCount": "2",
	 						 "productPrice":4.5
						 }
					 ],
					 "billType": "1",
					 "payTypeId": "1",
					 "leaveMessage": "买家留言啦！！"
				 },
				 {
					 "custId": "123",
					 "supplyId": "124",
					 "productInfoDtoList": [
							 {
								 "id": "222",
								 "productCount": "1",
	 							 "productPrice":2.5
							 },
							 {
								 "id": "223",
								 "productCount": "2",
	 							 "productPrice":5
							 }
					 ],
					 "billType": "2",
					 "payTypeId": "2",
					 "leaveMessage": "买家留言咯！！"
				 }
		 ]
	 }

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
		orderCreateDto.setUserDto(userDto);

		List<Order> orderList = orderService.createOrder(orderCreateDto);

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
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("url","/order/createOrderSuccess?orderIds="+orderIdStr);
		return map;
	}

	@RequestMapping(value = "/createOrderSuccess", method = RequestMethod.GET)
	public ModelAndView createOrderSuccess(@RequestParam("orderIds") String orderIds) throws Exception {
		UserDto userDto = super.getLoginUser();
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
			orderDto.setOrderId(order.getOrderId());
			orderDto.setCustId(order.getCustId());
			orderDto.setSupplyId(order.getSupplyId());
			orderDto.setFlowId(order.getFlowId());
			orderDto.setSupplyName(order.getSupplyName());
			orderDto.setOrderTotal(order.getOrderTotal());
			orderDto.setFinalPay(order.getFinalPay());
			orderDto.setPayStatus(order.getPayStatus());
			orderDto.setPayTypeId(order.getPayTypeId());
			orderDto.setPayTypeName(SystemPayTypeEnum.getPayTypeName(order.getPayTypeId()));
			orderDtoList.add(orderDto);
			if(SystemPayTypeEnum.PayOnline.getPayType().equals(order.getPayTypeId())){
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
	@RequestMapping(value = "/checkOrderPage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView checkOrderPage() throws Exception {
		UserDto userDto = super.getLoginUser();
		Map<String,Object> dataMap = orderService.checkOrderPage(userDto);

		if(!UtilHelper.isEmpty(dataMap) || !UtilHelper.isEmpty(dataMap.get("allShoppingCart"))){
			List<ShoppingCartListDto> allShoppingCart  = (List<ShoppingCartListDto>) dataMap.get("allShoppingCart");
			/* 由于账期订单 这种神奇订单的存在，需要拆分出账期订单数据，并展示在页面*/
			List<PeriodParams> periodParamsList = queryAllPeriodList(allShoppingCart);
			allShoppingCart = convertAllShoppingCart(allShoppingCart,periodParamsList);
			dataMap.put("allShoppingCart",allShoppingCart);
		}

		ModelAndView model = new ModelAndView();
		model.addObject("dataMap",dataMap);
		model.addObject("userDto",userDto);
		model.setViewName("order/checkOrderPage");
		return model;
	}

	/**
	 *
	 * @param allShoppingCart
	 * @param periodParamsList
	 * @return
	 */
	private List<ShoppingCartListDto> convertAllShoppingCart(List<ShoppingCartListDto> allShoppingCart,List<PeriodParams> periodParamsList) {
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}
		/* 计算各个供应商下是否有账期商品，如果有，则计算这些账期商品的总价 */
		allShoppingCart = calculatePeriodProductPriceCount(allShoppingCart,periodParamsList);
		logger.info("检查订单页-计算各个供应商下账期商品的总价，计算后的allShoppingCart=" + allShoppingCart);
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}

		List<ShoppingCartListDto> periodTermShoppingCartList = new ArrayList<ShoppingCartListDto>();

		List<ShoppingCartDto> shoppingCartDtoList = null;
		ShoppingCartListDto shoppingCartListDto = null;
		for(ShoppingCartListDto s: allShoppingCart){
			if(UtilHelper.isEmpty(s) || UtilHelper.isEmpty(s.getBuyer())  || UtilHelper.isEmpty(s.getSeller())|| UtilHelper.isEmpty(s.getShoppingCartDtoList()) ){
				continue;
			}
			/* 如果账期商品的总额为0，则不再进行拆单。进行下一个供应商数据的处理 */
			if(UtilHelper.isEmpty(s.getPeriodProductPriceCount()) || s.getPeriodProductPriceCount().compareTo(new BigDecimal(0)) <= 0){
				continue;
			}

			logger.info("检查订单页-查询是否可用资信结算接口，creditDubboService=" + creditDubboService);
			if(UtilHelper.isEmpty(creditDubboService)){
				continue;
			}

			CreditParams creditParams = new CreditParams();
			creditParams.setBuyerCode(s.getBuyer().getEnterpriseId() + "");
			creditParams.setSellerCode(s.getSeller().getEnterpriseId()+ "");
			creditParams.setOrderTotal(s.getPeriodProductPriceCount());
			logger.info("检查订单页-查询是否可用资信结算接口，请求参数creditParams=" + creditParams);
			CreditDubboResult creditDubboResult = creditDubboService.queryCreditAvailability(creditParams);
			logger.info("检查订单页-查询是否可用资信结算接口，响应数据creditDubboResult=" + creditDubboResult);

			if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
				logger.error("检查订单页-查询是否可用资信结算接口，调用失败:" + creditDubboResult.getMessage());
				continue;
			}

			/* 把合法账期商品的拿出来 */
			shoppingCartDtoList = new ArrayList<>();
			for(ShoppingCartDto shoppingCartDto : s.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				if(shoppingCartDto.isPeriodProduct()){
					shoppingCartDtoList.add(shoppingCartDto);
				}
			}

			/* 如果有合法账期商品，组装新的数据  */
			if(!UtilHelper.isEmpty(shoppingCartDtoList)){
				shoppingCartListDto = s;
				shoppingCartListDto.setShoppingCartDtoList(shoppingCartDtoList);
				periodTermShoppingCartList.add(shoppingCartListDto);
			}
		}

		/* 如果有合法账期商品的集合，与原来的购物车数据合并 */
		if(!UtilHelper.isEmpty(periodTermShoppingCartList)){
			allShoppingCart.addAll(periodTermShoppingCartList);
		}
		return allShoppingCart;
	}


	private  List<ShoppingCartListDto>  calculatePeriodProductPriceCount (List<ShoppingCartListDto> allShoppingCart,List<PeriodParams> periodParamsList) {
		if(UtilHelper.isEmpty(allShoppingCart) || UtilHelper.isEmpty(periodParamsList)){
			return allShoppingCart;
		}
		for(ShoppingCartListDto s: allShoppingCart){
			if(UtilHelper.isEmpty(s) || UtilHelper.isEmpty(s.getShoppingCartDtoList())){
				continue;
			}

			/* 当前供应商下含有账期商品的总金额 */
			BigDecimal periodProductPriceCount = new BigDecimal(0);
			for(ShoppingCartDto shoppingCartDto : s.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;
				Map<String,Object>  periodMap = getPeriodByCondition(periodParamsList,shoppingCartDto.getSpuCode(),shoppingCartDto.getCustId(),shoppingCartDto.getSupplyId());
				if(UtilHelper.isEmpty(periodMap)) continue;
//				Integer paymentTermCus = (Integer) periodMap.get("paymentTermCus");
				Integer paymentTermPro = (Integer) periodMap.get("paymentTermPro");
				if(paymentTermPro != null && paymentTermPro > 0){
					shoppingCartDto.setPeriodProduct(true);
					periodProductPriceCount = periodProductPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				}
			}
			s.setPeriodProductPriceCount(periodProductPriceCount);
		}
		return allShoppingCart;
	}

	/**
	 * 组装购物车中的数据，调用查询账期接口
	 * @param allShoppingCart
	 * @return
     */
	private List<PeriodParams> queryAllPeriodList(List<ShoppingCartListDto> allShoppingCart) {
		List<PeriodParams> paramsList  = new ArrayList<>();
		if(UtilHelper.isEmpty(allShoppingCart)){
			return paramsList;
		}

		logger.info("检查订单页-调用武汉的dubbo接口查询商品账期信息:creditDubboService=" + creditDubboService);
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

		logger.info("检查订单页-调用武汉的dubbo接口查询商品账期信息:请求参数paramsList=" + paramsList);
		PeriodDubboResult periodDubboResult = creditDubboService.queryPeriod(paramsList);

		if(UtilHelper.isEmpty(periodDubboResult) || "0".equals(periodDubboResult.getIsSuccessful())
				|| UtilHelper.isEmpty(periodDubboResult.getData())){
			if(!UtilHelper.isEmpty(periodDubboResult)){
				logger.error("检查订单页-调用武汉的dubbo接口查询商品账期信息:" + periodDubboResult.getMessage());
			}
			return  paramsList;
		}
		return  periodDubboResult.getData();
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
		/**
		 *  http://localhost:8088/order/sellerCancelOrder
		 *  {"orderId":1,"cancelResult":"代表月亮取消订单"}
		 */
		UserDto userDto = super.getLoginUser();
		orderService.updateOrderStatusForSeller(userDto, order.getOrderId(), order.getCancelResult());
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


}
