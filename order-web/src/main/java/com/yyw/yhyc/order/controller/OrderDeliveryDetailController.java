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
import com.yyw.yhyc.helper.JsonHelper;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order/orderDeliveryDetail")
public class OrderDeliveryDetailController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryDetailController.class);

	@Autowired
	private OrderDeliveryDetailService orderDeliveryDetailService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDeliveryDetail getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderDeliveryDetailService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetail(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		//用户登录信息
		UserDto userDto=super.getLoginUser();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
        if("1".equals(orderDeliveryDetailDto.getUserType())){
			orderDeliveryDetailDto.setCustId(userDto.getCustId());
		}else {
			orderDeliveryDetailDto.setSupplyId(userDto.getCustId());
		}
		return orderDeliveryDetailService.listPaginationByProperty(pagination, orderDeliveryDetailDto);
	}


	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailService.save(orderDeliveryDetail);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDeliveryDetailService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailService.update(orderDeliveryDetail);
	}

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/confirmReceipt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> confirmReceipt(@RequestBody  String listStr) throws Exception
	{
		List<OrderDeliveryDetailDto> list = new JsonHelper<OrderDeliveryDetailDto>().fromList(listStr, OrderDeliveryDetailDto.class);
		UserDto user = super.getLoginUser();
		return orderDeliveryDetailService.confirmReceipt(list, user);
	}

    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = { "/listPgReturn"}, method = RequestMethod.POST)
    @ResponseBody
    public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetailReturn(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
    {
        //获取用户信息
        UserDto userDto=super.getLoginUser();
        Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
        if(userDto==null){
            userDto = new UserDto();
            userDto.setCustId(123);
        }
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
        if("1".equals(orderDeliveryDetailDto.getUserType())){
            orderDeliveryDetailDto.setCustId(userDto.getCustId());
        }else {
            orderDeliveryDetailDto.setSupplyId(userDto.getCustId());
        }
        return orderDeliveryDetailService.listPaginationReturnByProperty(pagination, orderDeliveryDetailDto);
    }

}
