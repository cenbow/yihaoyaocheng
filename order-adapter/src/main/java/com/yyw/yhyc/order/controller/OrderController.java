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
import com.yyw.yhyc.order.appdto.AddressBean;
import com.yyw.yhyc.order.appdto.BatchBean;
import com.yyw.yhyc.order.appdto.OrdeProductBean;
import com.yyw.yhyc.order.appdto.OrderBean;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;
import com.yyw.yhyc.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDeliveryDetailService orderDeliveryDetailService;

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
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
	@ResponseBody
	public OrderBean getBuyOrderDetails(@PathVariable("orderId") String orderId) throws Exception
	{

		OrderBean orderBean=new OrderBean();

		// 登录买家的id
		UserDto user = super.getLoginUser();
		Order order=new Order();
		order.setCustId(user.getCustId());
		order.setFlowId(orderId);
		OrderDetailsDto orderDetailsDto=orderService.getOrderDetails(order);
		if(UtilHelper.isEmpty(orderDetailsDto)){
			return null;
		}
		//详情对象
		orderBean.setOrderStatus(orderDetailsDto.getOrderStatus());
		orderBean.setOrderId(orderDetailsDto.getFlowId());
		orderBean.setCreateTime(orderDetailsDto.getCreateTime());
		orderBean.setSupplyName(orderDetailsDto.getSupplyName());
		orderBean.setLeaveMsg(orderDetailsDto.getLeaveMessage());
		orderBean.setQq("");
		orderBean.setPayType(orderDetailsDto.getPayType());
		orderBean.setDeliveryMethod(orderDetailsDto.getOrderDelivery().getDeliveryMethod());
		orderBean.setBillType(orderDetailsDto.getBillType());
		orderBean.setOrderTotal(Double.parseDouble(orderDetailsDto.getOrderTotal().toString()));
		orderBean.setFinalPay(Double.parseDouble(orderDetailsDto.getFinalPay().toString()));
		orderBean.setProductNumber(orderDetailsDto.getTotalCount());
		orderBean.setPostponeTime(orderDetailsDto.getDelayTimes());
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
		//商品列表
		List<OrdeProductBean> productList=new ArrayList<OrdeProductBean>();
		for(OrderDetail orderDetail:orderDetailsDto.getDetails()){
			OrdeProductBean ordeProductBean=new OrdeProductBean();
			ordeProductBean.setQuantity(orderDetail.getProductCount());
			ordeProductBean.setProductId(orderDetail.getProductCode());
			ordeProductBean.setProductPicUrl("");
			ordeProductBean.setProductName(orderDetail.getProductName());
			ordeProductBean.setProductPrice(Double.parseDouble(orderDetail.getProductPrice().toString()));
			ordeProductBean.setSpec(orderDetail.getSpecification());
			ordeProductBean.setFactoryName(orderDetail.getManufactures());
			ordeProductBean.setFactoryId(orderDetail.getManufacturesId().toString());
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
		}
		return orderBean;
	}

}
