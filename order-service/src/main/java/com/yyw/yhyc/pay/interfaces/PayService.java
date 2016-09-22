package com.yyw.yhyc.pay.interfaces;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lvhongjie on 2016/8/29.
 */
public interface PayService {

    /* 同步响应url */
    public static final String RETURN_RESPONSE_URL = "returnResponseUrl";

    /* 异步通知回调url */
    public static final String ASYNC_CALL_BACK_URL = "asyncCallBackUrl";

    /**
     *  在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @return
     * @throws Exception
     */
    public Map<String,Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType,int type) throws Exception ;


    /**
     * 支付成功回调
     * @param request
     * @return
     */
    public String  paymentCallback(HttpServletRequest request) ;


    /**
     * 账期还款支付成功回调
     * @param request
     * @return
     */
    public String  paymentOfAccountCallback(HttpServletRequest request) ;


    /**
     * 分账成功回调
     * @param request
     * @return
     */
    public String spiltPaymentCallback(HttpServletRequest request);

    /**
     * 退款回调
     * @param request
     * @return
     */
    public String redundCallBack(HttpServletRequest request);


    /**
     * 发起退款请求
     * @param userDto 用户信息
     * @param orderType 订单类型 1：原始订单 2:拒收订单 3：补货订单
     * @param flowId 订单id
     * @param refundDesc 退款原因
     */
    public void handleRefund(UserDto userDto,int orderType,String flowId,String refundDesc);


    /**
     * 确认收货后，向招商银行发送分账请求
     * @param payFlowId  支付流水号
     * @return 发送请求的结果：成功或者失败
     */
    public boolean confirmReceivedOrder(String payFlowId) throws Exception;


    /**
     * 已付款的订单取消后，向招商银行发送撤销请求
     * @param payFlowId 支付流水号
     * @return 发送请求的结果：成功或者失败
     */
    public boolean cancelOrder(String payFlowId) throws Exception;

}
