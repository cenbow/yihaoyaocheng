/**
 *
 * Created By: XI
 * Created On: 2016-7-28 9:55:17
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.facade.OrderDetailFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yyw.yhyc.order.bo.RequestListModel;
import com.yyw.yhyc.order.bo.RequestModel;
import com.yyw.yhyc.order.bo.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "orderDetail")
public class OrderDetailController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

	@Reference
	private OrderDetailFacade orderDetailFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK", method = RequestMethod.GET)
	@ResponseBody
	public OrderDetail getByPK(Integer key) throws Exception
	{
		return orderDetailFacade.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDetail> listPgOrderDetail(RequestModel<OrderDetail> requestModel) throws Exception
	{
		Pagination<OrderDetail> pagination = new Pagination<OrderDetail>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderDetailFacade.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDetail orderDetail) throws Exception
	{
		orderDetailFacade.save(orderDetail);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDetailFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(OrderDetail orderDetail) throws Exception
	{
		orderDetailFacade.update(orderDetail);
	}
}
