package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.alipay.config.AlipayConfig;
import com.yyw.yhyc.pay.alipay.util.AlipayCore;
import com.yyw.yhyc.pay.alipay.util.AlipaySubmit;
import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhou on 2016/10/29.
 */
@Service("aliPayService")
public class AlipayServiceImpl  implements PayService {

    private static final Logger log = LoggerFactory.getLogger(AlipayServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderPayMapper orderPayMapper;

    @Autowired
    private SystemDateMapper systemDateMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private OrderSettlementMapper orderSettlementMapper;

    @Autowired
    private  OrderExceptionMapper orderExceptionMapper;

    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType, int type) throws Exception {
        Map<String,Object>  payRequestParamMap = new HashMap<>();
        if(UtilHelper.isEmpty(orderPay) || UtilHelper.isEmpty(orderPay.getPayTypeId())){
            return payRequestParamMap;
        }
        List<Order> orderList = orderMapper.listOrderByPayFlowId(orderPay.getPayFlowId());
        if(UtilHelper.isEmpty(orderList)){
            return payRequestParamMap;
        }
        String flowIds = "";
        for(Order order: orderList){
            if(UtilHelper.isEmpty(order)) continue;
            if(UtilHelper.isEmpty(flowIds)){
                flowIds +=  order.getFlowId();
            }else{
                flowIds += "," + order.getFlowId();
            }
        }

        //处理请求支付的参数，返回请求支付的url/已经签名过的数据，方便后续直接发起请求
        String payRequestUrl= this.alipayCommit(orderPay.getPayFlowId(), flowIds , orderPay.getOrderMoney()+"","");

        payRequestParamMap.put("payRequestUrl",payRequestUrl);
        return payRequestParamMap;
    }

    @Override
    public String paymentCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public Map<String, String> paymentOfAccountCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
        return null;
    }

    @Override
    public String redundCallBack(HttpServletRequest request) {
        return null;
    }

    @Override
    public void handleRefund(UserDto userDto, int orderType, String flowId, String refundDesc) {

    }

    @Override
    public boolean confirmReceivedOrder(String payFlowId) throws Exception {
        return false;
    }

    @Override
    public boolean cancelOrder(String payFlowId) throws Exception {
        return false;
    }

    /**
     * 封装请求支付的url、参数、数据
     * @param out_trade_no
     * @param subject
     * @param total_fee
     * @param body
     * @return
     */
    public String alipayCommit(String out_trade_no, String subject, String total_fee, String body) {

        if(StringUtils.isEmpty(out_trade_no)){
            log.info("支付订单号不能为空");
            throw new RuntimeException("支付订单号不能为空");
        }
        if(StringUtils.isEmpty(subject)){
            log.info("订单名称不能为空");
            throw new RuntimeException("订单名称不能为空");
        }
        if(StringUtils.isEmpty(total_fee)){
            log.info("付款金额不能为空");
            throw new RuntimeException("付款金额不能为空");
        }

        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);

        String sHtmlText = AlipaySubmit.buildRequestUrl(sParaTemp);

        log.info(sHtmlText);
        return sHtmlText;

    }



    /**
     * 封装退款的url、参数、数据
     * @param batch_num
     * @param refundMap
     * @return
     */
    public String alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap,String batchNo,String refundFlowId,String businessType) throws Exception{
        log.debug("支付宝退款接口-------------start");
        if(refundMap.size()<0){
            log.debug("退款参数不能为空");
            throw new RuntimeException("退款参数不能为空");
        }
        if(refundMap.size()-batch_num !=0){
            log.debug("退款总笔数跟参数集数量不一致");
            throw new RuntimeException("退款总笔数跟参数集数量不一致");
        }

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.servicerefund);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", AlipayConfig.notify_url_refund);
        sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
        sParaTemp.put("refund_date", AlipayConfig.refund_date);
        sParaTemp.put("batch_no", UtilDate.getOrderNum());
        sParaTemp.put("batch_num", String.valueOf(batch_num));
        sParaTemp.put("detail_data", AlipayCore.createLinkStringByrefund(refundMap));
        log.debug("封装退款请求数据----" + AlipayCore.createLinkStringByrefund(refundMap));
        String sHtmlText = AlipaySubmit.buildRequestUrl(sParaTemp);
        log.debug("封装退款请求URL----" + sHtmlText);
        log.debug("支付宝退款接口-------------end");


        //计入支付宝退款记录
        OrderPay orderPay = orderPayMapper.getPayFlowIdByPayAccountNo(batchNo);
        List<Order> listOrder = orderMapper.listOrderByPayFlowId(orderPay.getPayFlowId());
        Order order=null;
        if(listOrder.size()>0){
            OrderRefund  orderRefund = new OrderRefund();
            order=listOrder.get(0);
            orderRefund.setOrderId(order.getOrderId());
            orderRefund.setFlowId(refundFlowId);
            orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusIng.getType());
            List<OrderRefund> orderRefundList = orderRefundMapper.listByProperty(orderRefund);
            if(orderRefundList.size()>0){
                orderRefund = orderRefundList.get(0);
                log.debug("二次退款记录："+orderRefund.toString());
            }
            if(refundFlowId.trim().equals(order.getFlowId())){
                orderRefund.setRefundSum(orderPay.getPayMoney());
            }else{
                OrderException exception = orderExceptionMapper.getByExceptionOrderId(refundFlowId);
                if(!UtilHelper.isEmpty(exception)){
                    orderRefund.setRefundSum(exception.getOrderMoney());
                }
            }
            //写入退款记录
            String now = systemDateMapper.getSystemDate();
            orderRefund.setCreateUser("运营后台");
            orderRefund.setCustId(order.getCustId());
            orderRefund.setSupplyId(order.getSupplyId());
            orderRefund.setRefundType(businessType);
            orderRefund.setRefundFreight(new BigDecimal(0));
            orderRefund.setCreateTime(now);
            orderRefund.setRefundDate(now);
            orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusIng.getType());//退款中
            orderRefund.setRefundDesc("后台协议退款");

            if(orderRefundList.size()>0){
                orderRefundMapper.update(orderRefund);
            }else{
                orderRefundMapper.save(orderRefund);
            }


        }else {
            log.error("根据支付宝支付账号无法找到对应订单"+batchNo);
            //订单是否已退款
            throw new RuntimeException("根据支付宝支付账号无法找到对应订单"+batchNo);
        }

        return sHtmlText;
    }


}
