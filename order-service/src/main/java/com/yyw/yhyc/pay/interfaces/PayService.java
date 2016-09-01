package com.yyw.yhyc.pay.interfaces;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lvhongjie on 2016/8/29.
 */
public interface PayService {

    public static final int ORDER_RECEIVED_ACTION = 1;

    public static final int ORDER_CANCELLED_ACTION = 2;

    /**
     * Action 1 确认收货 2 取消订单
     */
    public Object postToBankForDoneOrder(Map<String,Object> orderInfo,int Action) throws Exception;

    /**
     *  在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @return
     * @throws Exception
     */
    public Map<String,Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType) throws Exception ;


    /**
     * 支付成功回调
     * @param request
     * @return
     */
    public String  paymentCallback(HttpServletRequest request) ;

    /**
     * 分账成功回调
     * @param request
     * @return
     */
    public String spiltPaymentCallback(HttpServletRequest request);

}
