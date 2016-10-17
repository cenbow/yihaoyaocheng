/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.appdto.AddressBean;
import com.yyw.yhyc.order.appdto.BatchBean;
import com.yyw.yhyc.order.appdto.OrderProductBean;
import com.yyw.yhyc.order.appdto.OrderBean;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.dto.*;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderExceptionService;
import com.yyw.yhyc.order.service.OrderDeliveryService;
import com.yyw.yhyc.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;


@Controller
@RequestMapping(value = "/api/order")
public class OrderController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Reference
	private IProductDubboManageService iProductDubboManageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDeliveryDetailService orderDeliveryDetailService;

	@Autowired
	private OrderExceptionService orderExceptionService;

	@Autowired
	private OrderDeliveryService orderDeliveryService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public Order getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<Order> listPgOrder(RequestModel<Order> requestModel) throws Exception
	{
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
	public void add(Order order) throws Exception
	{
		orderService.save(order);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(Order order) throws Exception
	{
		orderService.update(order);
	}

	/**
	 * 采购商取消订单
	 * @return
	 */
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> cancelOrder(String orderId) throws Exception
	{
		UserDto userDto = super.getLoginUser();
		orderService.updateOrderStatusForBuyer(userDto, orderId);
		return ok(null);
	}


	/**
	 * 采购商取消订单信息
	 * @return
	 */
	@RequestMapping(value = "/cancelOrderInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> cancelOrderInfo(String orderId) throws Exception
	{
		UserDto userDto = super.getLoginUser();
		return ok(orderService.findOrderCancelInfo(orderId, userDto));
	}


	/**
	 * 个人中心订单状态统计
	 * @return
	 */
	@RequestMapping(value = "/getUserTipInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> cancelOrderInfo() throws Exception
	{
		UserDto userDto = super.getLoginUser();
		return ok(orderService.listBuyerOrderStatusCount(userDto.getCustId()));
	}

	/**
	 * 采购商获取订单列表
	 * @return
	 */
	@RequestMapping(value = "/listOrder", method = RequestMethod.POST,consumes="application/json")
	@ResponseBody
	public Map<String,Object> listOrder(@RequestBody RequestModel<Map<String,String>> requestModel) throws Exception
	{
		Pagination<OrderDto> pagination = new Pagination<OrderDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		Map<String,String> param = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		return ok(orderService.listBuyerOderForApp(pagination, param.get("orderStatus"), userDto.getCustId(),iProductDubboManageService));
	}

	/**
	 * 采购商获取异常订单列表
	 * @return
	 */
	@RequestMapping(value = "/exceptionOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exceptionOrder(@RequestBody RequestModel<Map<String,String>> requestModel) throws Exception
	{
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		Map<String,String> param = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		return ok(orderExceptionService.listBuyerExceptionOderForApp(pagination, param.get("orderStatus"), userDto.getCustId(),iProductDubboManageService));
	}


	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = {"", "/getOrderDetail"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getBuyOrderDetails(@RequestParam("orderId") String orderId,@RequestParam("orderStatus") String orderStatus) throws Exception
	{
		// 登录买家的id
		UserDto user = super.getLoginUser();
       if(!UtilHelper.isEmpty(orderStatus)){
		   OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		   orderExceptionDto.setFlowId(orderId);
		   orderExceptionDto.setCustId(user.getCustId());
		   return ok(orderExceptionService.getAbnormalOrderDetails(orderExceptionDto,Integer.parseInt(orderStatus),iProductDubboManageService));
	   }else
		   return ok(orderService.getOrderDetailResponseInfo(orderId,user.getCustId(),iProductDubboManageService));
	}

	/**
	 * 订单延迟收货
	 * @return
	 */
	@RequestMapping(value = "/delayDelivery", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> delayDelivery(@RequestParam("orderId") String orderId) throws Exception
	{
		return orderService.updateOrderDelayTimes(orderId);
	}

	/**
	 * 订单物流信息
	 * @return
	 */
	@RequestMapping(value = "/deliveryInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> deliveryInfo(@RequestParam("orderId") String orderId) throws Exception{
		return  orderDeliveryService.getOrderDeliveryByFlowId(orderId);
	}

}
