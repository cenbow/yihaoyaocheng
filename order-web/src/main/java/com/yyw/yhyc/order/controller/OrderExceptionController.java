/**
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 */
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.dto.OrderLogDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;
import com.yyw.yhyc.order.enmu.BuyerChangeGoodsOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemRefundOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemReplenishmentOrderStatusEnum;
import com.yyw.yhyc.order.service.OrderExceptionService;
import com.yyw.yhyc.order.service.OrderExportService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.OrderTraceService;
import com.yyw.yhyc.order.service.SystemPayTypeService;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常(包括补货、拒收、退货、换货等订单)订单控制器
 */
@Controller
@RequestMapping(value = "/orderException")
public class OrderExceptionController extends BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(OrderExceptionController.class);

    @Autowired
    private OrderExceptionService orderExceptionService;
    @Reference(timeout = 50000)
    private CreditDubboServiceInterface creditDubboService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SystemPayTypeService systemPayTypeService;

    @Autowired
    private OrderExportService orderExportService;
    @Autowired
    private OrderTraceService orderTraceService;

    /**
     * 通过主键查询实体对象
     * @return
     */
    @RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
    @ResponseBody
    public OrderException getByPK(Integer key) throws Exception {
        return orderExceptionService.getByPK(key);
    }

    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
    @ResponseBody
    public Pagination<OrderException> listPgOrderException(@RequestBody RequestModel<OrderException> requestModel) throws Exception {
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
    public void add(@RequestBody OrderException orderException) throws Exception {
        orderExceptionService.save(orderException);
    }

    /**
     * 根据多条主键值删除记录
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception {
        orderExceptionService.deleteByPKeys(requestListModel.getList());
    }

    /**
     * 修改记录
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(@RequestBody OrderException orderException) throws Exception {
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
    public ModelAndView getOrderExceptionDetails(@PathVariable("userType") int userType, @PathVariable("flowId") String flowId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setFlowId(flowId);
        orderExceptionDto.setUserType(userType);
        if (userType == 1) {
            orderExceptionDto.setCustId(user.getCustId());
        } else if (userType == 2) {
            orderExceptionDto.setSupplyId(user.getCustId());
        }
        orderExceptionDto = orderExceptionService.getOrderExceptionDetails(orderExceptionDto);
        orderExceptionDto.setUserType(userType);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/getOrderExceptionDetails");
        return modelAndView;
    }


    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = {"/sellerRejcetOrderManage"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView sllererRejcetOrderManage() throws Exception {

        ModelAndView model = new ModelAndView();
        model.setViewName("orderException/order_rejection_seller");
        return model;
    }

    /**
     * 分页查询记录
     */
    @RequestMapping(value = {"/sellerRejcetOrderManage/list{type}"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgbuyerRejcetOrderManage(@RequestBody RequestModel<OrderExceptionDto> requestModel, @PathVariable("type") Integer type) throws Exception {

        Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();

        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());

        OrderExceptionDto orderExceptionDto = requestModel.getParam() == null ? new OrderExceptionDto() : requestModel.getParam();
        orderExceptionDto.setType(type);

        UserDto dto = super.getLoginUser();
        if (dto != null) {
            orderExceptionDto.setSupplyId(dto.getCustId());
            orderExceptionDto.setSupplyId(dto.getCustId());
        }
        Map<String, Object> map = orderExceptionService.listPaginationSellerByProperty(pagination, orderExceptionDto);

        return map;
    }


    @RequestMapping("/buyerRejectOrderManage")
    public ModelAndView buyerExceptionOrderManage() {
        ModelAndView view = new ModelAndView("orderException/buyer_reject_order_manage");
        return view;
    }

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerRejectOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerRejectOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
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
    public ModelAndView getReturnOrderDetails(@PathVariable("exceptionId") Integer exceptionId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionId(exceptionId);
        orderExceptionDto.setSupplyId(user.getCustId());
        orderExceptionDto = orderExceptionService.getRejectOrderDetails(orderExceptionDto, user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/seller_review_return_order");
        return modelAndView;
    }

    /**
     * 拒收订单信息
     * @param exceptionId 异常订单编码sellerReviewReturnOrder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getRejectOrderDetails/{exceptionId}", method = RequestMethod.GET)
    public ModelAndView getOrderExceptionDetails(@PathVariable("exceptionId") Integer exceptionId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionId(exceptionId);
        orderExceptionDto.setSupplyId(user.getCustId());
        orderExceptionDto = orderExceptionService.getRejectOrderDetails(orderExceptionDto, user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/seller_review_reject_order");
        return modelAndView;
    }

    /**
     * 换货订单信息 审核
     * @param exceptionId 异常订单编码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getChangeOrderExceptionDetails/{exceptionId}", method = RequestMethod.GET)
    public ModelAndView getChangeOrderExceptionDetails(@PathVariable("exceptionId") Integer exceptionId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionId(exceptionId);
        orderExceptionDto.setSupplyId(user.getCustId());
        orderExceptionDto = orderExceptionService.getChangeOrderDetails(orderExceptionDto, user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/seller_review_change_order");
        return modelAndView;
    }

    /**
     * 供应商审核拒收订单
     * @return
     */
    @RequestMapping(value = "/sellerReviewRejectOrder", method = RequestMethod.POST)
    @ResponseBody
    public void sellerReviewRejectOrder(@RequestBody OrderException orderException) throws Exception {
        UserDto userDto = super.getLoginUser();
        //20170106部分发货  订单完成时 才调用资信
        Map<String ,Object> resultMap= orderExceptionService.modifyReviewRejectOrderStatus(userDto, orderException);
        if ("1".equals(resultMap.get("code").toString())) {    //订单完成
            OrderException oe;
            Order order;
            SystemPayType systemPayType;
            try {
                oe = (OrderException)resultMap.get("oe");
                order = (Order)resultMap.get("order");
                systemPayType = (SystemPayType)resultMap.get("systemPayType");
            } catch (Exception e) {
                throw new RuntimeException("未找到拒收订单");
            }
            if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType()) && !UtilHelper.isEmpty(creditDubboService)) {
                CreditParams creditParams = new CreditParams();
                //creditParams.setSourceFlowId(oe.getFlowId());//拒收时，拒收单对应的源订单单号
                creditParams.setBuyerCode(oe.getCustId() + "");
                creditParams.setSellerCode(oe.getSupplyId() + "");
                creditParams.setBuyerName(oe.getCustName());
                creditParams.setSellerName(oe.getSupplyName());
                creditParams.setOrderTotal(order.getOrgTotal());//订单金额
                creditParams.setFlowId(oe.getFlowId());//订单编码
                creditParams.setStatus("2");
                creditParams.setReceiveTime(DateHelper.parseTime(order.getReceiveTime()));
                CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
                if (UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())) {
                    logger.error("creditDubboResult error:" + (creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！"));
                    throw new RuntimeException(creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！");
                }
            }
        }
    }

    /**
     * 供应商审核换货订单
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/sellerReviewChangeOrder", method = RequestMethod.POST)
    @ResponseBody
    public void sellerReviewChangeOrder(@RequestBody OrderException orderException) throws Exception {
        UserDto userDto = super.getLoginUser();
        orderExceptionService.updateSellerReviewChangeOrder(userDto, orderException);
    }

    /**
     * 供应商审核退货订单
     * @return
     */
    @RequestMapping(value = "/sellerReviewReturnOrder", method = RequestMethod.POST)
    @ResponseBody
    public void sellerReviewReturnOrder(@RequestBody OrderException orderException) {
        UserDto userDto = super.getLoginUser();
        orderExceptionService.modifyReviewReturnOrder(userDto, orderException);
//		OrderException oe;
//		Order order;
//		SystemPayType systemPayType;
//
//		try{
//			oe = orderExceptionService.getByPK(orderException.getExceptionId());
//			order = orderService.getByPK(oe.getOrderId());
//			systemPayType= systemPayTypeService.getByPK(order.getPayTypeId());
//		}catch (Exception e){
//			throw new RuntimeException("未找到退货订单");
//		}
//		if(UtilHelper.isEmpty(order)||UtilHelper.isEmpty(systemPayType)){
//			throw new RuntimeException("未找到订单");
//		}
//		if(UtilHelper.isEmpty(oe)){
//			throw new RuntimeException("未找到退货订单");
//		}
//		if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType()) && !UtilHelper.isEmpty(creditDubboService)
//				&& SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(orderException.getOrderStatus())){
//			CreditParams creditParams = new CreditParams();
//			creditParams.setSourceFlowId(oe.getFlowId());//退货时，退货单对应的源订单单号
//			creditParams.setBuyerCode(oe.getCustId() + "");
//			creditParams.setSellerCode(oe.getSupplyId() + "");
//			creditParams.setBuyerName(oe.getCustName());
//			creditParams.setSellerName(oe.getSupplyName());
////			creditParams.setOrderTotal(order.getOrderTotal().subtract(orderExceptionService.getConfirmHistoryExceptionMoney(oe.getFlowId())));//订单金额
//			creditParams.setOrderTotal(oe.getOrderMoney());//订单金额
//			creditParams.setFlowId(oe.getExceptionOrderId());//订单编码
//			creditParams.setStatus("6");
//			CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
//			if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
//				// TODO: 2016/8/25 暂时注释 不抛出异常
//				logger.error("creditDubboResult error:"+(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！"));
//				//throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
//			}
//		}
    }

    /**
     * 买家补货订单管理
     * @return
     */
    @RequestMapping("/buyerReplenishmentOrderManage")
    public ModelAndView buyerReplenishmentOrderManage() {
        ModelAndView view = new ModelAndView("orderException/buyer_replenishment_order_manage");
        return view;
    }

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerReplenishmentOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerReplenishmentOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
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
    public ModelAndView buyerChangeGoodsOrderManage() {
        ModelAndView view = new ModelAndView("orderException/buyer_change_order_manage");
        return view;
    }


    /**
     * 采购换货订单查询
     * @return
     */
    @RequestMapping(value = {"/listPgBuyerChangeGoodsOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerChangeGoodsOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
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
    public ModelAndView sellerChangeGoodsOrderManage() {
        ModelAndView view = new ModelAndView("orderException/seller_change_order_manage");
        return view;
    }

    /**
     * 供应商换货订单查询
     * @return
     */
    @RequestMapping(value = {"/listPgSellerChangeGoodsOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgSellerChangeGoodsOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
        Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        OrderExceptionDto orderDto = requestModel.getParam();
        UserDto userDto = super.getLoginUser();
        orderDto.setSupplyId(userDto.getCustId());
        return orderExceptionService.listPgSellerChangeGoodsOrder(pagination, orderDto);
    }


    /**
     * 卖家补货订单管理-页面
     * @return
     */
    @RequestMapping("/sellerReplenishmentOrderManage")
    public ModelAndView sellerReplenishmentOrderManage() {
        ModelAndView view = new ModelAndView("orderException/seller_replenishment_order_manage");
        return view;
    }

    /**
     * 卖家补货订单管理-接口
     * @return
     */
    @RequestMapping(value = {"", "/listPgSellerReplenishmentOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgSellerReplenishmentOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
        Pagination<OrderExceptionDto> pagination = new Pagination<OrderExceptionDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        OrderExceptionDto orderDto = requestModel.getParam();

        UserDto userDto = super.getLoginUser();
        if (UtilHelper.isEmpty(userDto)) {
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
    @RequestMapping(value = {"/cancleOrder/{id}/{orderStatus}"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> cancleOrder(@PathVariable("orderStatus") String orderStatus, @PathVariable("id") Integer id) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", "F");

        //确定该信息是否可取消
        OrderException orderException = orderExceptionService.getByPK(id);
        if (UtilHelper.isEmpty(orderException) || !BuyerChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(orderException.getOrderStatus())) {
            map.put("message", "当前状态下不可取消，请刷新页面后重试。");
            return map;
        }

        UserDto u = super.getLoginUser();
        orderException = new OrderException();
        orderException.setExceptionId(id);
        orderException.setCustId(u.getCustId());
        orderException.setOrderStatus(orderStatus);
        int row = orderExceptionService.updateOrderStatus(orderException);
        if (row > 0)
            map.put("result", "S");
        else
            map.put("message", "你没有操作该记录的权限，请刷新页面后重试。");


        //插入日志
        OrderLogDto logDto = new OrderLogDto();
        logDto.setOrderId(orderException.getOrderId());
        logDto.setNodeName("取消订单 flowId==" + orderException.getExceptionOrderId());
        logDto.setOrderStatus(orderException.getOrderStatus());
        logDto.setRemark("请求参数id==" + id + " orderStatus==" + orderStatus);
        this.orderTraceService.saveOrderLog(logDto);
        return map;
    }

    /**
     * 买家退货订单管理-页面
     * @return
     */
    @RequestMapping("/buyerRefundOrderManage")
    public ModelAndView buyerRefundOrderManage() {
        ModelAndView view = new ModelAndView("orderException/buyer_refund_order_manage");
        return view;
    }

    /**
     * 采购商退货订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerRefundOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerRefundOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
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
    public void buyerCancelRefundOrder(@PathVariable("exceptionId") Integer exceptionId) {
        UserDto userDto = super.getLoginUser();
        orderExceptionService.updateRefundOrderStatusForBuyer(userDto, exceptionId);
    }

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"/buyerReReturnOrderDetail/{exceptionId}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findBuyerReReturnOrderDetail(@PathVariable("exceptionId") Integer exceptionId) throws Exception {
        ModelAndView model = new ModelAndView();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionId(exceptionId);
        UserDto user = super.getLoginUser();
        orderExceptionDto.setCustId(user.getCustId());
        orderExceptionDto = orderExceptionService.getReturnOrderDetails(orderExceptionDto, 1);
        model.addObject("orderExceptionDto", orderExceptionDto);
        model.setViewName("orderException/buyer_order_return_detail");
        return model;
    }

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"/sellerReReturnOrderDetail/{exceptionId}"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findSellerReReturnOrderDetail(@PathVariable("exceptionId") Integer exceptionId) throws Exception {
        ModelAndView model = new ModelAndView();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionId(exceptionId);
        UserDto user = super.getLoginUser();
        orderExceptionDto.setSupplyId(user.getCustId());
        orderExceptionDto = orderExceptionService.getReturnOrderDetails(orderExceptionDto, 2);
        model.addObject("orderExceptionDto", orderExceptionDto);
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
    public ModelAndView getReplenishmentDetails(@PathVariable("userType") int userType, @PathVariable("flowId") String flowId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionOrderId(flowId);
        orderExceptionDto.setUserType(userType);
        if (userType == 1) {
            orderExceptionDto.setCustId(user.getCustId());
        } else if (userType == 2) {
            orderExceptionDto.setSupplyId(user.getCustId());
        }
        orderExceptionDto = orderExceptionService.getReplenishmentDetails(orderExceptionDto);
        orderExceptionDto.setUserType(userType);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/replenishment_order_detail");
        return modelAndView;
    }

    /**
     * 卖家退货订单管理-页面
     * @return
     */
    @RequestMapping("/sellerRefundOrderManage")
    public ModelAndView sellerRefundOrderManage() {
        ModelAndView view = new ModelAndView("orderException/seller_refund_order_manage");
        return view;
    }

    /**
     * 卖家退货订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgSellerRefundOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgSellerRefundOrder(@RequestBody RequestModel<OrderExceptionDto> requestModel) {
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
     * 买家补货确认收货
     * @return
     */
    @RequestMapping(value = {"/repConfirmReceipt/{exceptionOrderId}"}, method = RequestMethod.GET)
    @ResponseBody
    public void repConfirmReceipt(@PathVariable("exceptionOrderId") String exceptionOrderId) throws Exception {
        //部分发货  20170106
        UserDto userDto = super.getLoginUser();
        Map<String, Object> resultMap = orderExceptionService.updateRepConfirmReceipt(exceptionOrderId, userDto);
        if("1".equals(resultMap.get("code").toString())){
            //补货确认收货调用账期接口
            if (UtilHelper.isEmpty(creditDubboService))
                logger.error("CreditDubboServiceInterface creditDubboService is null");
            else {
                OrderException oe = (OrderException) resultMap.get("orderException");
                Order order = (Order) resultMap.get("order");
                SystemPayType systemPayType =  (SystemPayType) resultMap.get("systemPayType");
                if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
                    logger.info("补货确认收货调用资讯接口");
                    CreditParams creditParams = new CreditParams();
                    //creditParams.setSourceFlowId(oe.getFlowId());源订单单号
                    creditParams.setBuyerCode(oe.getCustId() + "");
                    creditParams.setSellerCode(oe.getSupplyId() + "");
                    creditParams.setBuyerName(oe.getCustName());
                    creditParams.setSellerName(oe.getSupplyName());
                    creditParams.setOrderTotal(order.getOrgTotal());//订单金额
                    creditParams.setFlowId(order.getFlowId());//订单编码
                    creditParams.setStatus("2");
                    creditParams.setReceiveTime(DateHelper.parseTime(oe.getReceiveTime()));
                    CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
                    if (UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())) {
                        throw new RuntimeException(creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！");
                    }
                }
            }
        }
    }

    /**
     * 供应商审核补货订单页
     * @param flowId 原始订单编号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getReviewReplenishmentDetails/{flowId}", method = RequestMethod.GET)
    public ModelAndView getReviewReplenishmentDetails(@PathVariable("flowId") String flowId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setUserType(3);
        orderExceptionDto.setSupplyId(user.getCustId());
        //orderExceptionDto.setFlowId(flowId);
        orderExceptionDto.setExceptionOrderId(flowId);
        orderExceptionDto = orderExceptionService.getReplenishmentDetails(orderExceptionDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/seller_review_replenishment_order");
        return modelAndView;
    }

    /**
     * 供应商审核补货订单
     * @return
     */
    @RequestMapping(value = "/sellerReviewReplenishmentOrder", method = RequestMethod.POST)
    @ResponseBody
    public void sellerReviewReplenishmentOrder(@RequestBody OrderException orderException) throws Exception {
        UserDto userDto = super.getLoginUser();
        //20170106  部分发货
        Map<String, Object> resultMap = orderExceptionService.updateReviewReplenishmentOrderStatusForSeller(userDto, orderException);
        if ("1".equals(resultMap.get("code").toString())) {
            try {
                if (UtilHelper.isEmpty(creditDubboService)) {
                    logger.error("CreditDubboServiceInterface creditDubboService is null");
                    throw new RuntimeException("CreditDubboServiceInterface creditDubboService is null");
                } else {
                    //	OrderException oe = orderExceptionService.getByPK(orderException.getExceptionId());
                    Order order = (Order) resultMap.get("order");
                    SystemPayType systemPayType =(SystemPayType)resultMap.get("systemPayType");
                    if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
                        CreditParams creditParams = new CreditParams();
                        //creditParams.setSourceFlowId(oe.getFlowId());//源订单单号
                        creditParams.setBuyerCode(order.getCustId() + "");
                        creditParams.setSellerCode(order.getSupplyId() + "");
                        creditParams.setBuyerName(order.getCustName());
                        creditParams.setSellerName(order.getSupplyName());
                        creditParams.setOrderTotal(order.getOrgTotal());//订单金额
                        creditParams.setFlowId(order.getFlowId());//订单编码
                        creditParams.setStatus("2");
                        creditParams.setReceiveTime(DateHelper.parseTime(order.getReceiveTime()));
                        CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
                        if (UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())) {
                            logger.error("接口调用失败！");
                            throw new RuntimeException(creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("接口调用失败！");
                throw new RuntimeException("接口调用失败！" + orderException.toString());
            }
        }
    }

    /**
     * 退货订单确认收货
     * @return
     */
    @RequestMapping(value = {"/editConfirmReceiptReturn"}, method = RequestMethod.POST)
    @ResponseBody
    public String editConfirmReceiptReturn(@RequestBody OrderException orderException) throws Exception {
        UserDto userDto = super.getLoginUser();

        String msg = orderExceptionService.editConfirmReceiptReturn(orderException.getExceptionOrderId(), userDto, creditDubboService);
        return "{\"msg\":" + msg + "}";
    }

    /**
     * 换货订单确认收货-卖家
     * @return
     */
    @RequestMapping(value = {"/editConfirmReceiptChange"}, method = RequestMethod.POST)
    @ResponseBody
    public String editConfirmReceiptChange(@RequestBody OrderException orderException) {
        UserDto userDto = super.getLoginUser();
        String msg = orderExceptionService.editConfirmReceiptChange(orderException.getExceptionOrderId(), userDto);
        return "{\"msg\":" + msg + "}";
    }

    /**
     * 供应商换货订单详情
     * @param exceptionOrderId 原始订单编号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sellerChangeGoodsOrderDetails/{exceptionOrderId}", method = RequestMethod.GET)
    public ModelAndView sellerChangeGoodsOrderDetails(@PathVariable("exceptionOrderId") String exceptionOrderId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionOrderId(exceptionOrderId);
        orderExceptionDto.setSupplyId(user.getCustId());
        orderExceptionDto = orderExceptionService.getSellerChangeGoodsOrderDetails(orderExceptionDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/seller_change_order_detail");
        return modelAndView;
    }

    /**
     * 采购商换货订单详情
     * @param exceptionId 异常订单编号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/buyerChangeGoodsOrderDetails/{exceptionId}", method = RequestMethod.GET)
    public ModelAndView buyerChangeGoodsOrderDetails(@PathVariable("exceptionId") String exceptionId) throws Exception {
        UserDto user = super.getLoginUser();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setExceptionOrderId(exceptionId);
        orderExceptionDto.setCustId(user.getCustId());
        orderExceptionDto = orderExceptionService.getBuyerChangeGoodsOrderDetails(orderExceptionDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderExceptionDto", orderExceptionDto);
        modelAndView.setViewName("orderException/buyer_change_order_detail");
        return modelAndView;
    }


    /**
     * 换货买家确认收货
     * @return
     */
    @RequestMapping(value = {"/changeGoodsBuyerConfirmReceipt/{exceptionOrderId}"}, method = RequestMethod.GET)
    @ResponseBody
    public void updateChangeGoodsBuyerConfirmReceipt(@PathVariable("exceptionOrderId") String exceptionOrderId) throws Exception {
        UserDto userDto = super.getLoginUser();
        orderExceptionService.updateChangeGoodsBuyerConfirmReceipt(exceptionOrderId, userDto);
    }

    /**
     * 异常订单导出
     * @return
     */
    @RequestMapping("/exportExceptionOrder")
    public void exportExceptionOrder() {
        String condition = request.getParameter("condition");
        JSONObject paramJSON = new JSONObject();
        if (StringUtils.isNotBlank(condition)) {
            paramJSON = JSON.parseObject(condition).getJSONObject("param");
        }
        OrderExceptionDto orderDto = new OrderExceptionDto();
        orderDto.setStartTime((String) paramJSON.get("startTime"));
        orderDto.setEndTime((String) paramJSON.get("endTime"));
        orderDto.setExceptionOrderId((String) paramJSON.get("exceptionOrderId"));
        orderDto.setFlowId((String) paramJSON.get("flowId"));
        orderDto.setSupplyName((String) paramJSON.get("supplyName"));
        UserDto userDto = super.getLoginUser();
        orderDto.setCustId(userDto.getCustId());
        String returnType = request.getParameter("returnType");
        orderDto.setReturnType(returnType);

        String fileName = "订单明细.xls";
        /* 设置字符集为'UTF-8' */
        try {
            response.setCharacterEncoding("UTF-8");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));

            OutputStream os = response.getOutputStream();
            HSSFWorkbook wb = null;
            if ("3".equals(returnType) || "4".equals(returnType)) {
                wb = orderExportService.exportRelenishOrRejectOrder(orderDto);
            } else if ("1".equals(returnType)) {
                wb = orderExportService.exportReturnOrder(orderDto);
            } else if ("2".equals(returnType)) {
                wb = orderExportService.exportChangeOrder(orderDto);
            }

            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error("订单导出报错", e);
        }
    }


}


