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
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.facade.OrderDeliveryDetailFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/order/orderDeliveryDetail")
public class OrderDeliveryDetailController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryDetailController.class);

	@Reference
	private OrderDeliveryDetailFacade orderDeliveryDetailFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDeliveryDetail getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderDeliveryDetailFacade.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetail(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderDeliveryDetailFacade.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailFacade.save(orderDeliveryDetail);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDeliveryDetailFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailFacade.update(orderDeliveryDetail);
	}

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/confirmReceipt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> confirmReceipt(@RequestBody  RequestListModel<OrderDeliveryDetailDto> requestListModel) throws Exception
	{
		return orderDeliveryDetailFacade.confirmReceipt(requestListModel.getList());
	}
}
