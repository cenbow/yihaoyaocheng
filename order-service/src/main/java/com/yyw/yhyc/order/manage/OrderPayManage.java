package com.yyw.yhyc.order.manage;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.order.service.OrderSettlementService;
import com.yyw.yhyc.pay.chinapay.httpClient.HttpRequestHandler;
import com.yyw.yhyc.pay.chinapay.pay.ChinaPay;
import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import com.yyw.yhyc.pay.chinapay.utils.StringUtil;
import com.yyw.yhyc.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by jiangshuai on 2016/8/30.
 */
@Service("orderPayMamage")
public class OrderPayManage {

    private static final Logger log = LoggerFactory.getLogger(OrderPayManage.class);

    private OrderPayMapper orderPayMapper;

    private OrderMapper orderMapper;
    private SystemDateMapper systemDateMapper;
    private OrderTraceMapper orderTraceMapper;

    private OrderRefundMapper orderRefundMapper;
    private OrderExceptionMapper orderExceptionMapper;

    @Autowired
    public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
        this.orderExceptionMapper = orderExceptionMapper;
    }

    @Autowired
    private OrderSettlementService orderSettlementService;

    @Autowired
    public void setOrderRefundMapper(OrderRefundMapper orderRefundMapper) {
        this.orderRefundMapper = orderRefundMapper;
    }

    @Autowired
    public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
        this.orderTraceMapper = orderTraceMapper;
    }

    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
        this.orderPayMapper = orderPayMapper;
    }

    // 确认收货
    public void takeConfirmReturn(Map<String, Object> map) throws Exception {
        log.debug("----收到确认收货订单后台通知------" + map.toString());

        String flowPayId = map.get("flowPayId").toString();
        String orderStatus = map.get("orderStatus").toString();
        boolean orderSettlementStatus = false;
        if("0000".equals(orderStatus)){
            orderSettlementStatus = true;
        }
        updateTakeConfirmOrderInfos(flowPayId, orderSettlementStatus);
    }

    // 收到订单退款通知
    public void redundCallBack(Map<String, Object> map) throws Exception {
        log.debug("----收到三方支付返回订单退款通知------" + map);

        String flowPayId = map.get("flowPayId").toString();
        String orderStatus = map.get("orderStatus").toString();

        boolean orderRefundStatus = false;
        if("0000".equals(orderStatus)){
            orderRefundStatus = true;
        }
        updateRedundOrderInfos(flowPayId, orderRefundStatus, map);
    }


    // 支付完成
    public void orderPayReturn(Map<String, Object> map) throws Exception {
        log.info("----收到三方支付返回的订单后台通知------" + map);
        String flowPayId = map.get("flowPayId").toString();
        String money = map.get("money").toString();
        String MerId = map.get("MerId").toString();
       log.info(flowPayId+"支付成功后回调" + StringUtil.paserMaptoStr(map));
        updateOrderpayInfos(flowPayId, new BigDecimal(money), map);
    }


    // 账期支付支付完成
    public Map<String,String> orderPayOfAccountReturn(Map<String, Object> map) throws Exception {
        log.info("----收到三方支付返回的订单后台通知------" + map);
        String flowPayId = map.get("flowPayId").toString();
        String money = map.get("money").toString();
        log.info(flowPayId+"支付成功后回调" + StringUtil.paserMaptoStr(map));
       return updateOfAccountOrderpayInfos(flowPayId, new BigDecimal(money), map.toString());
    }


    // 账期支付更新信息
    public Map<String,String> updateOfAccountOrderpayInfos(String payFlowId, BigDecimal finalPay,String parameter)
            throws Exception {
        log.info(payFlowId + "----- 支付成功后更新信息  update orderInfo start ----");
        Map<String,String> map=new HashMap<String,String>();
        synchronized(payFlowId){
            String now = systemDateMapper.getSystemDate();
            OrderPay orderPay = orderPayMapper.getByPayFlowId(payFlowId);
            StringBuffer flowIds=new StringBuffer();
            if (!UtilHelper.isEmpty(orderPay)&& (orderPay.getPayStatus().equals(OrderPayStatusEnum.UN_PAYED.getPayStatus()))) {//未支付

                List<Order> listOrder = orderMapper.listOrderByPayFlowId(payFlowId);

                if (UtilHelper.isEmpty(listOrder)||listOrder.size()==0) {
                    // 商户数据异常
                    log.info("根据订单流水号查询订单不存在");
                    throw new Exception("支付信息异常！");
                }
                // 更新订单支付信息
                orderPay.setPayMoney(finalPay.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN));
                orderPay.setPayTime(now);
                orderPay.setPaymentPlatforReturn(parameter);
                orderPay.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
                orderPayMapper.update(orderPay);

                for (Order order : listOrder) {
                        flowIds.append(order.getFlowId()+",");
                        // 更新订单信息
                        order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
                        order.setPaymentTermStatus(1);
                        order.setUpdateTime(now);
                        order.setPayTime(now);
                        orderMapper.update(order);
                        // 保存订单操作记录
                        createOrderTrace(order, "账期银联回调", now, 2, "账期买家还款.");
                }
                log.info(payFlowId + "-----支付成功后更新信息   update orderInfo end ----");
                if (flowIds.length()>0)
                    map.put("orderCodes",flowIds.substring(0, flowIds.length() - 1));;
                map.put("isSuccess","1");
                map.put("message","成功");
            }
        }
        return map;
    }



    /**
     * 在线支付成功后，更新支付状态等信息（公用业务逻辑）
     * @param payFlowId  系统内部的支付流水号
     * @param finalPay   用户实际支付的金额
     * @param parameter 支付平台返回的信息
     * @throws Exception
     */
    public void updateOrderpayInfos(String payFlowId, BigDecimal finalPay,Map parameter)
            throws Exception {
        log.info(payFlowId + "----- 支付成功后更新信息  update orderInfo start ----");

        synchronized(payFlowId){

            String now = systemDateMapper.getSystemDate();
            OrderPay orderPay = orderPayMapper.getByPayFlowId(payFlowId);

            if (!UtilHelper.isEmpty(orderPay)&& (orderPay.getPayStatus().equals(OrderPayStatusEnum.UN_PAYED.getPayStatus()))) {//未支付

                List<Order> listOrder = orderMapper.listOrderByPayFlowId(payFlowId);

                if (UtilHelper.isEmpty(listOrder)||listOrder.size()==0) {
                    // 商户数据异常
                    log.info("根据订单流水号查询订单不存在");
                    throw new Exception("支付信息异常！");
                }
                // 更新订单支付信息
                orderPay.setPayMoney(finalPay.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN));
                orderPay.setPayTime(now);

                if(parameter.get("trade_no") != null){
                    orderPay.setPayAccountName("支付宝");
                    orderPay.setPayAccountNo(parameter.get("trade_no").toString());
                }

                orderPay.setPaymentPlatforReturn(parameter.toString());
                orderPay.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
                orderPayMapper.update(orderPay);

                for (Order order : listOrder) {
                    if (SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())) {
                        // 更新订单信息
                        order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
                        order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
                        order.setUpdateTime(now);
                        order.setPayTime(now);
                        orderMapper.update(order);
                        /*orderManager.sendSMS(order, null, order.getSupplyId(), MessageTemplate.BUYER_PAY_ORDER_INFO_CODE);*/
                        // 保存订单操作记录
                        createOrderTrace(order, OnlinePayTypeEnum.getPayName(orderPay.getPayTypeId()), now, 2, "买家已付款.");

                        //TODO 从买家支付后开始计算5个自然日内未发货将资金返还买家订单自动取消-与支付接口整合 待接入方法

                        OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(1,null,null,null,null,order);
                        orderSettlementService.save(orderSettlement);
                    }
                }
               log.info(payFlowId + "-----支付成功后更新信息   update orderInfo end ----");
            }
        }
    }


    /**
     * 确认收货回调(结算)成功后，更新相关表的等信息（公用业务逻辑）
     * @param payFlowId               系统内部的支付流水号
     * @param orderSettlementStatus 订单是否结算
     * @throws Exception
     */
    public void updateTakeConfirmOrderInfos(String payFlowId, boolean orderSettlementStatus) throws Exception {
        log.info(payFlowId + "----- 分账成功后更新信息  update orderInfo start ----");

        List<Order> listOrder = orderMapper.listOrderByPayFlowId(payFlowId);

        if (UtilHelper.isEmpty(listOrder)||listOrder.size()==0) {
            // 商户数据异常
            log.info("根据订单流水号查询订单不存在");
            throw new Exception("支付信息异常！");
        }
        String now = systemDateMapper.getSystemDate();
        // 打款成功
        if (orderSettlementStatus) {
            for (Order order : listOrder) {
               // if(SystemOrderStatusEnum.BuyerAllReceived.getType().equals(order.getOrderStatus())||SystemOrderStatusEnum.BuyerPartReceived.getType().equals(order.getOrderStatus())) {
                    //更新订单支付标记
                    order.setPayFlag(SystemOrderPayFlag.PlayMoneySuccess.getType());
                    orderMapper.update( order);
                    //生产订单日志
                    createOrderTrace(order, "银联确认收货回调", now, 2, "确认收货打款成功.");
                    //更新结算信息为已结算
                    orderSettlementService.updateSettlementByMap(order.getFlowId(),1);
                //}
            }
        } else {// 打款异常
            for (Order order : listOrder) {
                //if (SystemOrderStatusEnum.BuyerAllReceived.getType().equals(order.getOrderStatus())||SystemOrderStatusEnum.BuyerPartReceived.getType().equals(order.getOrderStatus())) {
                //更新订单支付标记
                    order.setPayFlag(SystemOrderPayFlag.PlayMoneyError.getType());
                    orderMapper.update(order);
                    //生产订单日志
                    createOrderTrace(order, "银联确认收货回调", now, 2, "确认收货打款失败.");
               // }
            }
        }
        log.info(payFlowId + "----- 分账成功后更新信息  update orderInfo end ----");

    }


    /**
     * 退款成功后，更新退款表、订单表等信息
     * @param payFlowId          系统内部的支付流水号
     * @param orderRefundStatus 退款状态
     * @param parameter           支付平台返回的信息
     * @throws Exception
     */
    public void updateRedundOrderInfos(String payFlowId, boolean orderRefundStatus, Map parameter)
            throws Exception {
        log.info(payFlowId + "----- 退款成功后更新信息  update orderInfo start ----");

        List<Order> listOrder = orderMapper.listOrderByPayFlowId(payFlowId);

        if (UtilHelper.isEmpty(listOrder)) {
            // 商户数据异常
            log.info("退款信息异常！");
            throw new Exception("退款信息异常！");
        }
        for (Order o : listOrder) {
            OrderRefund orderRefund = orderRefundMapper.getOrderRefundByOrderId(o.getOrderId());
            if (!UtilHelper.isEmpty(orderRefund)) {
                orderRefund.setRemark(parameter.toString());
                if (orderRefundStatus) {
                    orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusOk.getType());
                    orderRefundMapper.update(orderRefund);
                    //更新取消订单退款为已结算
                    orderSettlementService.updateSettlementByMap(o.getFlowId(),4);
                    OrderException orderException=new OrderException();
                    orderException.setFlowId(o.getFlowId());
                    orderException.setReturnType(OrderExceptionTypeEnum.REJECT.getType());
                    List<OrderException> list= orderExceptionMapper.listByProperty(orderException);
                    if(list.size()>0){
                        //更新拒收结算为已结算
                        orderSettlementService.updateSettlementByMap(orderException.getExceptionOrderId(),3);
                    }
                    //更新订单支付标记
                    o.setPayFlag(SystemOrderPayFlag.RefundSuccess.getType());
                    orderMapper.update(o);
                } else {
                    //更新订单支付标记
                    o.setPayFlag(SystemOrderPayFlag.RefundError.getType());
                    orderMapper.update(o);
                    orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusFail.getType());
                    orderRefundMapper.update(orderRefund);
                }

            }
        }
        log.info(payFlowId + "----- 退款成功后更新信息  update orderInfo end ----");
    }


    /**
     * 通过支付宝交易id查询出PayFlowId
     * @param payaccountno
     * @return
     */
    public String getPayFlowIdByPayAccountNo(String payaccountno)
    {
        OrderPay orderPay = orderPayMapper.getPayFlowIdByPayAccountNo(payaccountno);
        return orderPay.getPayFlowId();
    }

    public void createOrderTrace(Object order,String userName,String now,int type,String nodeName){
        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setDealStaff(userName);
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userName);

        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userName);
        if(type==1){
            OrderException orderException=(OrderException)order;
            orderTrace.setOrderId(orderException.getExceptionId());
            orderTrace.setOrderStatus(orderException.getOrderStatus());
            orderTrace.setNodeName(nodeName);
        }else{
            Order newOrder=(Order)order;
            orderTrace.setOrderId(newOrder.getOrderId());
            orderTrace.setOrderStatus(newOrder.getOrderStatus());
            orderTrace.setNodeName(nodeName);
        }
        orderTraceMapper.save(orderTrace);
    }

}
