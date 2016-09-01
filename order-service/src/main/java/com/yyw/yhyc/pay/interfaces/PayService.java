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

    /**
     * Action 1 确认收货 2 取消订单
     */
    public Map<String,Object> postToBankForDoneOrder(Map<String,Object> orderInfo,int Action);

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

    /**
     * 发起退款请求
     * @param userDto 用户信息
     * @param orderType 订单类型 1：原始订单 2:拒收订单 3：补货订单
     * @param flowId 订单id
     * @param refundDesc 退款原因
     */
    public void handleRefund(UserDto userDto,int orderType,String flowId,String refundDesc);

}
