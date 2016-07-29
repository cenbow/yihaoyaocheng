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
import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.bo.RequestListModel;
import com.yyw.yhyc.order.bo.RequestModel;
import com.yyw.yhyc.order.facade.OrderCombinedFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/order/orderCombined")
public class OrderCombinedController extends BaseJsonController{
	private static final Logger logger = LoggerFactory.getLogger(OrderCombinedController.class);

	@Reference
	private OrderCombinedFacade orderCombinedFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderCombined getByPK(Integer key) throws Exception
	{
		return orderCombinedFacade.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderCombined> listPgOrderCombined(RequestModel<OrderCombined> requestModel) throws Exception
	{
		Pagination<OrderCombined> pagination = new Pagination<OrderCombined>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderCombinedFacade.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderCombined orderCombined) throws Exception
	{
		orderCombinedFacade.save(orderCombined);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderCombinedFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderCombined orderCombined) throws Exception
	{
		orderCombinedFacade.update(orderCombined);
	}
}
