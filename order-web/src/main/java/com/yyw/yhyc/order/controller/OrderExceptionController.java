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
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.dto.UserDto;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 异常(包括补货、拒收、退货、换货等订单)订单控制器
 */
@Controller
@RequestMapping(value = "/orderException")
public class OrderExceptionController extends BaseJsonController{
	private static final Logger logger = LoggerFactory.getLogger(OrderExceptionController.class);

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
		return orderExceptionService.getByPK(key);
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

		return orderExceptionService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody OrderException orderException) throws Exception
	{
		orderExceptionService.save(orderException);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		orderExceptionService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestBody OrderException orderException) throws Exception
	{
		orderExceptionService.update(orderException);
	}


	/**
	 * 拒收订单详情
	 * @param userType userType==1 表示以采购商身份查看 ，userType==2 表示以供应商身份查看
	 * @param flowId 原始订单编号
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getDetails-{userType}/{flowId}", method = RequestMethod.GET)
	public ModelAndView getOrderExceptionDetails(@PathVariable("userType") int userType,@PathVariable("flowId")String flowId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setFlowId(flowId);
		orderExceptionDto.setUserType(userType);
		if (userType == 1) {
			orderExceptionDto.setCustId(user.getCustId());
		} else if(userType == 2) {
			orderExceptionDto.setSupplyId(user.getCustId());
		}
		orderExceptionDto = orderExceptionService.getOrderExceptionDetails(orderExceptionDto);
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
	public ModelAndView sllererRejcetOrderManage()throws Exception{

		ModelAndView model = new ModelAndView();
		model.setViewName("orderException/order_rejection_seller");
		return model;
	}
	/**
	 * 分页查询记录
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
		Map<String,Object> map = orderExceptionService.listPaginationSellerByProperty(pagination,orderExceptionDto);

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
	 * @param exceptionId 异常订单编码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getReturnOrderDetails/{exceptionId}", method = RequestMethod.GET)
	public ModelAndView getReturnOrderDetails(@PathVariable("exceptionId")Integer exceptionId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionId(exceptionId);
		orderExceptionDto.setSupplyId(user.getCustId());
		orderExceptionDto = orderExceptionService.getRejectOrderDetails(orderExceptionDto);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("orderExceptionDto",orderExceptionDto);
		modelAndView.setViewName("orderException/seller_review_return_order");
		return modelAndView;
	}

	/**
	 * 拒收订单信息
	 * @param exceptionId 异常订单编码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRejectOrderDetails/{exceptionId}", method = RequestMethod.GET)
	public ModelAndView getOrderExceptionDetails(@PathVariable("exceptionId")Integer exceptionId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionId(exceptionId);
		orderExceptionDto.setSupplyId(user.getCustId());
		orderExceptionDto = orderExceptionService.getRejectOrderDetails(orderExceptionDto);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("orderExceptionDto",orderExceptionDto);
		modelAndView.setViewName("orderException/seller_review_reject_order");
		return modelAndView;
	}

	/**
	 * 供应商审核拒收订单
	 * @return
	 */
	@RequestMapping(value = "/sellerReviewRejectOrder", method = RequestMethod.POST)
	@ResponseBody
	public void sellerReviewRejectOrder(@RequestBody OrderException orderException){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.modifyReviewRejectOrderStatus(userDto, orderException);
	}

	/**
	 * 供应商审核换货订单
	 * @return
	 */
	@RequestMapping(value = "/sellerReviewChangeOrder", method = RequestMethod.POST)
	@ResponseBody
	public void sellerReviewChangeOrder(@RequestBody OrderException orderException){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.updateSellerReviewChangeOrder(userDto, orderException);
	}

	/**
	 * 供应商审核退货订单
	 * @return
	 */
	@RequestMapping(value = "/sellerReviewReturnOrder", method = RequestMethod.POST)
	@ResponseBody
	public void sellerReviewReturnOrder(@RequestBody OrderException orderException){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.modifyReviewReturnOrder(userDto, orderException);
	}

	/**
	 * 买家补货订单管理
	 * @return
     */
	@RequestMapping("/buyerReplenishmentOrderManage")
	public ModelAndView buyerReplenishmentOrderManage(){
		ModelAndView view = new ModelAndView("orderException/buyer_replenishment_order_manage");
		return view;
	}

	/**
	 * 采购订单查询
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgBuyerReplenishmentOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgBuyerReplenishmentOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		return orderExceptionService.listPgBuyerReplenishmentOrder(pagination, orderDto);
	}

	/**
	 * 采购换货订单查询
	 * @return
	 */
	@RequestMapping("/buyerChangeGoodsOrderManage")
	public ModelAndView buyerChangeGoodsOrderManage(){
		ModelAndView view = new ModelAndView("orderException/buyer_change_order_manage");
		return view;
	}

	/**
	 * 采购换货订单查询
	 * @return
	 */
	@RequestMapping(value = {"/listPgBuyerChangeGoodsOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgBuyerChangeGoodsOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		return orderExceptionService.listPgBuyerChangeGoodsOrder(pagination, orderDto);
	}

	/**
	 * 供应商换货订单查询
	 * @return
	 */
	@RequestMapping("/sellerChangeGoodsOrderManage")
	public ModelAndView sellerChangeGoodsOrderManage(){
		ModelAndView view = new ModelAndView("orderException/seller_change_order_manage");
		return view;
	}

	/**
	 * 供应商换货订单查询
	 * @return
	 */
	@RequestMapping(value = {"/listPgSellerChangeGoodsOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgSellerChangeGoodsOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		return orderExceptionService.listPgSellerChangeGoodsOrder(pagination, orderDto);
	}



	/**
	 * 卖家补货订单管理-页面
	 * @return
	 */
	@RequestMapping("/sellerReplenishmentOrderManage")
	public ModelAndView sellerReplenishmentOrderManage(){
		ModelAndView view = new ModelAndView("orderException/seller_replenishment_order_manage");
		return view;
	}

	/**
	 * 卖家补货订单管理-接口
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgSellerReplenishmentOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgSellerReplenishmentOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();

		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto)){
			return null;
		}
		orderDto.setSupplyId(userDto.getCustId());
		return orderExceptionService.listPgSellerReplenishmentOrder(pagination, orderDto);
	}

	/**
	 * 修改订单状态
	 * @param orderStatus
	 * @param id
     * @return
     */
	@RequestMapping(value = {"/updateOrderStatus/{id}/{orderStatus}"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> updateOrderStatus(@PathVariable("orderStatus") String orderStatus, @PathVariable("id") Integer id) throws Exception{
		UserDto u = super.getLoginUser();
		OrderException orderException = new OrderException();
		orderException.setOrderStatus(orderStatus);
		orderException.setExceptionId(id);
		orderException.setCustId(u.getCustId());

		int row = orderExceptionService.updateOrderStatus(orderException);
		Map<String, Object> map = new HashMap<String, Object>();
		if(row >0 )
			map.put("result", "S");
		else
			map.put("result", "F");

		return map;
	}

	/**
	 * 买家退货订单管理-页面
	 * @return
	 */
	@RequestMapping("/buyerRefundOrderManage")
	public ModelAndView buyerRefundOrderManage(){
		ModelAndView view = new ModelAndView("orderException/buyer_refund_order_manage");
		return view;
	}

	/**
	 * 采购商退货订单查询
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgBuyerRefundOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgBuyerRefundOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setCustId(userDto.getCustId());
		return orderExceptionService.listPgBuyerRefundOrder(pagination, orderDto);
	}

	/**
	 * 采购商取消退货订单
	 * @return
	 */
	@RequestMapping(value = "/buyerCancelRefundOrder/{exceptionId}", method = RequestMethod.GET)
	@ResponseBody
	public void buyerCancelRefundOrder(@PathVariable("exceptionId") Integer exceptionId){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.updateRefundOrderStatusForBuyer(userDto, exceptionId);
	}
	/**
	 * 采购订单查询
	 * @return
	 */
	@RequestMapping(value = {"/buyerReReturnOrderDetail/{exceptionId}"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findBuyerReReturnOrderDetail(@PathVariable("exceptionId")Integer exceptionId)throws  Exception{
		ModelAndView model = new ModelAndView();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionId(exceptionId);
//		UserDto user = super.getLoginUser();
//		orderExceptionDto.setCustId(user.getCustId());
		orderExceptionDto = orderExceptionService.getReturnOrderDetails(orderExceptionDto,1);
		model.addObject("orderExceptionDto",orderExceptionDto);
		model.setViewName("orderException/buyer_order_return_detail");
		return model;
	}

	/**
	 * 采购订单查询
	 * @return
	 */
	@RequestMapping(value = {"/sellerReReturnOrderDetail/{exceptionId}"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findSellerReReturnOrderDetail(@PathVariable("exceptionId")Integer exceptionId)throws  Exception{
		ModelAndView model = new ModelAndView();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setExceptionId(exceptionId);
//		UserDto user = super.getLoginUser();
//		orderExceptionDto.setSupplyId(user.getCustId());
		orderExceptionDto = orderExceptionService.getReturnOrderDetails(orderExceptionDto,2);
		model.addObject("orderExceptionDto",orderExceptionDto);
		model.setViewName("orderException/seller_order_return_detail");
		return model;
	}

	/**
	 * 补货订单详情
	 * @param userType userType==1 表示以采购商身份查看 ，userType==2 表示以供应商身份查看
	 * @param flowId 原始订单编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getReplenishmentDetails-{userType}/{flowId}", method = RequestMethod.GET)
	public ModelAndView getReplenishmentDetails(@PathVariable("userType") int userType,@PathVariable("flowId")String flowId) throws Exception {
		UserDto user = super.getLoginUser();
		OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
		orderExceptionDto.setFlowId(flowId);
		orderExceptionDto.setUserType(userType);
		if (userType == 1) {
			orderExceptionDto.setCustId(user.getCustId());
		} else if(userType == 2) {
			orderExceptionDto.setSupplyId(user.getCustId());
		}
		orderExceptionDto = orderExceptionService.getReplenishmentDetails(orderExceptionDto);
		orderExceptionDto.setUserType(userType);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("orderExceptionDto",orderExceptionDto);
		modelAndView.setViewName("orderException/replenishment_order_detail");
		return modelAndView;
	}

	/**
	 * 卖家退货订单管理-页面
	 * @return
	 */
	@RequestMapping("/sellerRefundOrderManage")
	public ModelAndView sellerRefundOrderManage(){
		ModelAndView view = new ModelAndView("orderException/seller_refund_order_manage");
		return view;
	}

	/**
	 * 卖家退货订单查询
	 * @return
	 */
	@RequestMapping(value = {"", "/listPgSellerRefundOrder"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listPgSellerRefundOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel){
		Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderExceptionDto orderDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		orderDto.setSupplyId(userDto.getCustId());
		return orderExceptionService.listPgSellerRefundOrder(pagination, orderDto);
	}
	/**
	 * 确认收货
	 * @return
	 */
	@RequestMapping(value = {"", "/repConfirmReceipt"}, method = RequestMethod.POST)
	@ResponseBody
	public void repConfirmReceipt(String exceptionOrderId){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.repConfirmReceipt(exceptionOrderId, userDto);
	}
	/**
	 * 退货订单确认收货
	 * @return
	 */
	@RequestMapping(value = {"", "/editConfirmReceiptReturn"}, method = RequestMethod.POST)
	@ResponseBody
	public void editConfirmReceiptReturn(String exceptionOrderId){
		UserDto userDto = super.getLoginUser();
		orderExceptionService.editConfirmReceiptReturn(exceptionOrderId, userDto);
	}
}


