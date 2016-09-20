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

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

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
		if(UtilHelper.isEmpty(userDto))
			userDto = new UserDto();userDto.setCustId(6066);
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode ;
		String message = "";
		Object data = null;
		int SUCCESS = 0,EXCEPTION = -3,DATA_EXCEPTION = -1,TOEKN_EXCEPTION= -2;
		try{
			statusCode = SUCCESS;
			message = "成功";
			orderService.updateOrderStatusForBuyer(userDto, orderId);
		}catch (Exception e){
			statusCode = EXCEPTION;
			message = e.getMessage();
		}
		map.put("statusCode",statusCode);
		map.put("message",message);
		map.put("data",data);
		return map;
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
		if(UtilHelper.isEmpty(userDto))
			userDto = new UserDto();userDto.setCustId(6066);
		return convert(orderService.findOrderCancelInfo(orderId, userDto));
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
		if(UtilHelper.isEmpty(userDto))
			userDto = new UserDto();userDto.setCustId(6066);
		return convert(orderService.listBuyerOrderStatusCount(userDto.getCustId()));
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
		if(UtilHelper.isEmpty(userDto))
			userDto = new UserDto();userDto.setCustId(6066);
		return convert(orderService.listBuyerOderForApp(pagination, param.get("orderStatus"),userDto.getCustId()));
	}

	/**
	 * 采购商获取异常订单列表
	 * @return
	 */
	@RequestMapping(value = "/exceptionOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exceptionOrder(@RequestBody RequestModel<Map<String,String>> requestModel) throws Exception
	{
		Pagination<OrderDto> pagination = new Pagination<OrderDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		Map<String,String> param = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto))
			userDto = new UserDto();userDto.setCustId(6066);
		return convert(orderService.listBuyerExceptionOderForApp(pagination, param.get("orderStatus"),userDto.getCustId()));
	}


	Map<String,Object> convert(Object object){
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode ;
		String message = "";
		Object data = null;
		int SUCCESS = 0,EXCEPTION = -3,DATA_EXCEPTION = -1,TOEKN_EXCEPTION= -2;
		try{
			statusCode = SUCCESS;
			message = "成功";
			data = object;
		}catch (Exception e){
			statusCode = EXCEPTION;
			message = e.getMessage();
		}
		map.put("statusCode",statusCode);
		map.put("message",message);
		map.put("data",data);
		return map;
	}

}
