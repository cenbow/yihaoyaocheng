/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:56
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
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.facade.OrderSettlementFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/order/orderSettlement")
public class OrderSettlementController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderSettlementController.class);

	@Reference
	private OrderSettlementFacade orderSettlementFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderSettlement getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderSettlementFacade.getByPK(key);
	}

	/**
	* 分页查询记录
	 * type 1 应收 2 应付
	* @return
	*/
	@RequestMapping(value = {"", "/listPg/t{type}"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderSettlementDto> listPgOrderSettlement(@RequestBody RequestModel<OrderSettlementDto> requestModel, @PathVariable("type") Integer type) throws Exception
	{
		Pagination<OrderSettlementDto> pagination = new Pagination<OrderSettlementDto>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderSettlementDto orderSettlementDto = requestModel.getParam()==null?new OrderSettlementDto():requestModel.getParam();
		orderSettlementDto.setType(type);
		//TODO custId
//		orderSettlementDto.setCustId(256);
		return orderSettlementFacade.listPaginationByProperty(pagination, orderSettlementDto);
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementFacade.save(orderSettlement);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderSettlementFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementFacade.update(orderSettlement);
	}
}
