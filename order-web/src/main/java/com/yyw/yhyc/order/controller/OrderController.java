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
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.annotation.Token;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.facade.OrderFacade;

import com.yyw.yhyc.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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

    @Reference
    private OrderFacade orderFacade;
	@Autowired
	private OrderService orderService;



    /**
     * 通过主键查询实体对象
     * @return
     */
    @RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Order getByPK(@PathVariable("key") Integer key) throws Exception {
        return orderFacade.getByPK(key);
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

        return orderFacade.listPaginationByProperty(pagination, requestModel.getParam());
    }

    /**
     * 新增记录
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(Order order) throws Exception {
        orderFacade.save(order);
    }

    /**
     * 根据多条主键值删除记录
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(RequestListModel<Integer> requestListModel) throws Exception {
        orderFacade.deleteByPKeys(requestListModel.getList());
    }

    /**
     * 修改记录
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(Order order) throws Exception {
        orderFacade.update(order);
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
			validateResult = orderFacade.validateProducts(currentLoginCustId,orderDto);
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
	public String createOrder( OrderCreateDto orderCreateDto) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		orderCreateDto.setUserDto(userDto);

		List<Order> orderList = orderFacade.createOrder(orderCreateDto);

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
		return "/order/createOrderSuccess?orderIds="+orderIdStr;
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
			Order order = orderFacade.getByPK(Integer.valueOf(orderId));
			orderDto.setOrderId(order.getOrderId());
			orderDto.setFlowId(order.getFlowId());
			orderDto.setSupplyName(order.getSupplyName());
			orderDto.setOrderTotal(order.getOrderTotal());
			orderDto.setFinalPay(order.getFinalPay());
			orderDto.setPayStatus(order.getPayStatus());
			orderDto.setPayTypeId(order.getPayTypeId());
			orderDto.setPayTypeName(SystemPayTypeEnum.getPayTypeName(order.getPayTypeId()));
			orderDtoList.add(orderDto);
			if(SystemPayTypeEnum.PayOnline.getPayType().equals(order.getPayTypeId())){
				onLinePayOrderPriceCount = onLinePayOrderPriceCount.add(order.getFinalPay());
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
		Map<String,Object> dataMap = orderFacade.checkOrderPage(userDto);
		ModelAndView model = new ModelAndView();
		model.addObject("dataMap",dataMap);
		model.addObject("userDto",userDto);
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
		byte[] bytes=orderFacade.exportOrder(pagination, orderDto);
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
		OrderDetailsDto orderDetailsDto=orderFacade.getOrderDetails(order);
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
		OrderDetailsDto orderDetailsDto=orderFacade.getOrderDetails(order);
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
		OrderDetailsDto orderDetailsDto=orderFacade.getOrderDetails(order);
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
