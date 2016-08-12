/**
 *
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.BuyerOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SellerOrderExceptionStatusEnum;
import com.yyw.yhyc.order.facade.OrderExceptionFacade;
import com.yyw.yhyc.order.service.OrderExceptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.bo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常(包括补货、拒收、退货、换货等订单)订单控制器
 */
@Controller
@RequestMapping(value = "/orderException")
public class OrderExceptionController extends BaseJsonController{
	private static final Logger logger = LoggerFactory.getLogger(OrderExceptionController.class);

	@Reference
	private OrderExceptionFacade orderExceptionFacade;

	@Autowired
	private OrderExceptionService orderExceptionService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderException getByPK(Integer key) throws Exception
	{
		return orderExceptionFacade.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderException> listPgOrderException(@RequestBody RequestModel<OrderException> requestModel) throws Exception
	{
		Pagination<OrderException> pagination = new Pagination<OrderException>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderExceptionFacade.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody OrderException orderException) throws Exception
	{
		orderExceptionFacade.save(orderException);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		orderExceptionFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestBody OrderException orderException) throws Exception
	{
		orderExceptionFacade.update(orderException);
	}


	/**
	 * 退货订单详情
	 * @param userType userType==1 表示以采购商身份查看 ，userType==2 表示以供应商身份查看
	 * @param exceptionOrderId 异常订单编码
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getDetails-{userType}/{exceptionOrderId}", method = RequestMethod.GET)
	public ModelAndView getOrderExceptionDetails(@PathVariable("userType") int userType,@PathVariable("exceptionOrderId")String exceptionOrderId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionOrderId(exceptionOrderId);
		orderExceptionDto.setUserType(userType);
		if (userType == 1) {
			orderExceptionDto.setCustId(user.getCustId());
		} else if(userType == 2) {
			orderExceptionDto.setSupplyId(user.getCustId());
		}
		orderExceptionDto = orderExceptionFacade.getOrderExceptionDetails(orderExceptionDto);
		orderExceptionDto.setUserType(userType);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("orderExceptionDto",orderExceptionDto);
		modelAndView.setViewName("orderException/getOrderExceptionDetails");
		return modelAndView;
	}


	/**
	 * 分页查询记录
	 * @return
	 */
	@RequestMapping(value = {"/sellerRejcetOrderManage"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView buyerRejcetOrderManage()throws Exception{

		ModelAndView model = new ModelAndView();
		model.setViewName("orderException/order_rejection_seller");
		return model;
	}
	/**
	 * 分页查询记录
	 * 1 全部 2 待确认 3退款中 4已完成 5已关闭
	 */
	@RequestMapping(value = {"/sellerRejcetOrderManage/list{type}"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> listPgbuyerRejcetOrderManage(@RequestBody RequestModel<OrderExceptionDto> requestModel,@PathVariable("type") Integer type)throws Exception{

		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		OrderExceptionDto orderExceptionDto = requestModel.getParam()==null?new OrderExceptionDto():requestModel.getParam();
		orderExceptionDto.setType(type);

        UserDto dto = super.getLoginUser();
		if(dto!=null){
			orderExceptionDto.setSupplyId(dto.getCustId());
			orderExceptionDto.setSupplyId(dto.getCustId());
		}
		Map<String,Object> map = new HashMap<String,Object>();
		pagination = orderExceptionService.listPaginationSellerByProperty(pagination,orderExceptionDto);
		map.put("pagination",pagination);
		map.put("orderExceptionDto",orderExceptionDto);
		return map;
	}


	@RequestMapping("/buyerRejectOrderManage")
	public ModelAndView buyerExceptionOrderManage(){
		ModelAndView view = new ModelAndView("orderException/buyer_reject_order_manage");
		return view;
	}

	/**
	 * 采购订单查询
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgBuyerRejectOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgBuyerRejectOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		return orderExceptionService.listPgBuyerRejectOrder(pagination, orderDto);
	}


	/**
	 * 退货订单信息
	 * @param exceptionOrderId 异常订单编码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectOrderDetails/{exceptionOrderId}", method = RequestMethod.GET)
	public ModelAndView getOrderExceptionDetails(@PathVariable("exceptionOrderId")String exceptionOrderId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionOrderId(exceptionOrderId);
		orderExceptionDto.setSupplyId(user.getCustId());
		orderExceptionDto = orderExceptionService.getRejectOrderDetails(orderExceptionDto);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("orderExceptionDto",orderExceptionDto);
		modelAndView.setViewName("orderException/seller_review_regect_order");
		return modelAndView;
	}

	/**
	 * 采购商审核拒收订单
	 * @return
	 */
	@RequestMapping(value = "/sellerReviewRejectOrder", method = RequestMethod.POST)
	@ResponseBody
	public void sellerReviewRejectOrder(@RequestBody OrderException orderException){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.sellerReviewRejectOrder(userDto, orderException);
	}
}


