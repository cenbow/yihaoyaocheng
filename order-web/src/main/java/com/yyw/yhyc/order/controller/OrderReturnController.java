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

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderReturnDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/order/orderReturn")
public class OrderReturnController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderReturnController.class);

	@Autowired
	private OrderReturnService orderReturnService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderReturn getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderReturnService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderReturn> listPgOrderReturn(RequestModel<OrderReturn> requestModel) throws Exception
	{
		Pagination<OrderReturn> pagination = new Pagination<OrderReturn>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderReturnService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderReturn orderReturn) throws Exception
	{
		orderReturnService.save(orderReturn);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderReturnService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderReturn orderReturn) throws Exception
	{
		orderReturnService.update(orderReturn);
	}

    /**
     * 退货
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmSaleReturn", method = RequestMethod.POST)
    @ResponseBody
    public String confirmSaleReturn2(@RequestBody List<OrderReturn> orderReturnList) throws Exception
    {
        UserDto userDto = super.getLoginUser();

        return orderReturnService.saveProductReturn(orderReturnList,userDto);
    }

	/**
	 * 退货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listOrderReturn/{orderExceptionId}", method = RequestMethod.POST)
	@ResponseBody
	public List<OrderReturnDto> listOrderReturn(String orderExceptionId) throws Exception
	{
		UserDto userDto = super.getLoginUser();

		return orderReturnService.listOrderReturn(orderExceptionId);
	}
}
