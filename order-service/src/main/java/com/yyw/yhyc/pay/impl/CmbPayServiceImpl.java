package com.yyw.yhyc.pay.impl;


import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.utils.XmlUtils;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.cmbPay.CmbPayUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.utils.HttpClientUtil;
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
 * Created by lizhou on 2016/8/30
 *  招商银行支付相关
 */
@Service("cmbPayService")
public class CmbPayServiceImpl implements PayService{

    private static final Logger log = LoggerFactory.getLogger(CmbPayServiceImpl.class);

    public static final int ORDER_RECEIVED_ACTION = 1;

    public static final int ORDER_CANCELLED_ACTION = 2;

    private OrderCombinedMapper orderCombinedMapper;
    @Autowired
    public void setOrderCombinedMapper(OrderCombinedMapper orderCombinedMapper) {
        this.orderCombinedMapper = orderCombinedMapper;
    }

    private OrderMapper orderMapper;
    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    private AccountPayInfoMapper accountPayInfoMapper;
    @Autowired
    public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper) {
        this.accountPayInfoMapper = accountPayInfoMapper;
    }

    private OrderPayMapper orderPayMapper;
    @Autowired
    public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
        this.orderPayMapper = orderPayMapper;
    }

    private SystemDateMapper systemDateMapper;
    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    private OrderExceptionMapper orderExceptionMapper;
    @Autowired
    public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
        this.orderExceptionMapper = orderExceptionMapper;
    }

    private OrderRefundMapper orderRefundMapper;
    @Autowired
    public void setOrderRefundMapper(OrderRefundMapper orderRefundMapper) {
        this.orderRefundMapper = orderRefundMapper;
    }

    private SystemPayTypeMapper systemPayTypeMapper;
    @Autowired
    public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
        this.systemPayTypeMapper = systemPayTypeMapper;
    }

    private boolean sendPostToCmbBank(OrderPay orderPay, int action) throws Exception {
        if (UtilHelper.isEmpty(orderPay)) throw  new Exception("非法参数");
        String operationAction = "";
        if (ORDER_RECEIVED_ACTION == action) {
            operationAction = "A";
        } else if (ORDER_CANCELLED_ACTION == action) {
            operationAction = "C";
        } else{
            throw  new Exception("非法参数");
        }

        String requestXML =
                "<?xml version=\"1.0\" encoding = \"GBK\"?>" +
                    "<CMBSDKPGK>" +
                        "<INFO>" +
                            "<FUNNAM>%s</FUNNAM>" +
                            "<DATTYP>%s</DATTYP>" +
                            "<LGNNAM>%s</LGNNAM>" +
                        "</INFO>" +
                        "<NTORDCFMX>" +
                            "<MCHNBR>%s</MCHNBR>" +
                            "<SEQNBR>%s</SEQNBR>" +
                            "<SUBSEQ>%s</SUBSEQ>" +
                            "<STSCOD>%s</STSCOD>" +
                        "</NTORDCFMX>" +
                    "</CMBSDKPGK>";
        //TODO 请填写登录名
        requestXML = String.format(requestXML,"NTORDCFM" , 2 , "---请填写登录名---",
                CmbPayUtil.getValue(CmbPayUtil.MCHNBR) , orderPay.getPayFlowId() , orderPay.getOrderPayId() , operationAction);
        return HttpClientUtil.sendPost(CmbPayUtil.CMB_PAY_URL_WITHOUT_SIGNED,requestXML,HttpClientUtil.CONTENT_TYPE_XML);
    }

    /**
     * 确认收货后，向招商银行发送分账请求
     * @param payFlowId  支付流水号
     * @return
     * @throws Exception
     */
    @Override
    public boolean confirmReceivedOrder(String payFlowId) throws Exception {
        OrderPay orderPay = validateConfirmReceivedOrder(payFlowId);
        if(UtilHelper.isEmpty(orderPay)){
            log.error("支付流水号校验失败!");
            return false;
        }
        return sendPostToCmbBank(orderPay, ORDER_RECEIVED_ACTION);
    }

    /**
     * 已付款的订单取消后，向招商银行发送撤销请求
     * @param payFlowId 支付流水号
     * @return
     * @throws Exception
     */
    @Override
    public boolean cancelOrder(String payFlowId) throws Exception {
        OrderPay orderPay = validateCancelOrder(payFlowId);
        if(UtilHelper.isEmpty(orderPay)){
            log.error("支付流水号校验失败!");
            return false;
        }
        return sendPostToCmbBank(orderPay,ORDER_CANCELLED_ACTION);
    }

    private OrderPay validateConfirmReceivedOrder(String payFlowId){
        OrderPay orderPay = validateOrderPay(payFlowId);
        if(UtilHelper.isEmpty(orderPay)) return null;
        OrderCombined orderCombined = orderCombinedMapper.findByPayFlowId(orderPay.getPayFlowId());
        Order order = new Order();
        order.setOrderCombinedId(orderCombined.getOrderCombinedId());
        List<Order> orderList = orderMapper.listByProperty(order);
        if(UtilHelper.isEmpty(orderList)) return null;
        for(Order orderTemp : orderList){
            if(UtilHelper.isEmpty(orderTemp)){
                return null;
            }
            if( !SystemOrderStatusEnum.BuyerAllReceived.getValue().equals(orderTemp.getOrderStatus())){
                log.error("订单状态异常!");
                return null;
            }
        }
        return orderPay;
    }

    private OrderPay validateCancelOrder(String payFlowId){
        OrderPay orderPay = validateOrderPay(payFlowId);
        if(UtilHelper.isEmpty(orderPay)) return null;
        OrderCombined orderCombined = orderCombinedMapper.findByPayFlowId(orderPay.getPayFlowId());
        Order order = new Order();
        order.setOrderCombinedId(orderCombined.getOrderCombinedId());
        List<Order> orderList = orderMapper.listByProperty(order);
        if(UtilHelper.isEmpty(orderList)) return null;
        for(Order orderTemp : orderList){
            if(UtilHelper.isEmpty(orderTemp)){
                return null;
            }
            if( !SystemOrderStatusEnum.BuyerAlreadyPaid.getValue().equals(orderTemp.getOrderStatus())){
                log.error("订单状态异常!");
                return null;
            }
        }
        return orderPay;
    }
    private OrderPay validateOrderPay(String payFlowId){
        if (UtilHelper.isEmpty(payFlowId)) return null;
        OrderPay orderPay = orderPayMapper.getByPayFlowId(payFlowId);
        if (UtilHelper.isEmpty(orderPay)) return null;
        if ( !OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(orderPay.getOrderPayId())) {
            log.error("该orderPay的支付方式不是招商银行");
            return null;
        }
        if ( !OrderPayStatusEnum.PAYED.getPayStatus().equals(orderPay.getPayStatus())) {
            log.error("该orderPay的支付状态为待支付");
            return null;
        }
        return orderPay;
    }

    /**
     * 在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @return
     */
    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType,int type) throws Exception {
        if(UtilHelper.isEmpty(orderPay) || UtilHelper.isEmpty(orderPay.getPayFlowId())){
            return null;
        }
        OrderCombined orderCombined = orderCombinedMapper.findByPayFlowId(orderPay.getPayFlowId());
        if(UtilHelper.isEmpty(orderCombined)) return null;

        Order orderQuery = new Order();
        orderQuery.setOrderCombinedId(orderCombined.getOrderCombinedId());
        List<Order> orderList = orderMapper.listByProperty(orderQuery);
        if(UtilHelper.isEmpty(orderList)) return null;

        StringBuffer orderDataInfo = new StringBuffer();
        AccountPayInfo accountPayInfo = null;
        for(Order order : orderList){
            accountPayInfo = new AccountPayInfo();
            accountPayInfo.setCustId(order.getSupplyId());
            accountPayInfo.setPayTypeId(order.getPayTypeId());
            List<AccountPayInfo> accountPayInfoList = accountPayInfoMapper.listByProperty(accountPayInfo);
            if(UtilHelper.isEmpty(accountPayInfoList) || accountPayInfoList.size() != 1){
                throw  new Exception("供应商收款账户异常");
            }
            accountPayInfo = accountPayInfoList.get(0);
            if(UtilHelper.isEmpty(accountPayInfo) || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountNo())
                    || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName())){
                throw  new Exception("供应商收款账户异常");
            }
            orderDataInfo.append( CmbPayUtil.MCHNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.MCHNBR) + " ;");//商户编码
            orderDataInfo.append( CmbPayUtil.REFORD + "=" + orderPay.getPayFlowId() + " ;");//订单号
            orderDataInfo.append( CmbPayUtil.SUBORD + "=" + order.getOrderId() + " ;");//订单支付号
            orderDataInfo.append( CmbPayUtil.CCYNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.CCYNBR) + " ;");//订单币种
            orderDataInfo.append( CmbPayUtil.TRSAMT + "=" + order.getOrgTotal() + " ;");//订单金额
            orderDataInfo.append( CmbPayUtil.CRTACC + "=" + accountPayInfo.getReceiveAccountNo()  + " ;");//收方账号
            orderDataInfo.append( CmbPayUtil.CRTNAM + "=" + accountPayInfo.getReceiveAccountName() + " ;");//收方账户名
            orderDataInfo.append( CmbPayUtil.CRTBNK + "=" + accountPayInfo.getSubbankName()  + " ;");//收方开户行
            orderDataInfo.append( CmbPayUtil.CRTPVC + "=" +  accountPayInfo.getProvinceName() + " ;");//收方省份
            orderDataInfo.append( CmbPayUtil.CRTCTY + "=" +  accountPayInfo.getCityName() + " ;");//收方城市
            orderDataInfo.append( CmbPayUtil.RETURL + "=" + CmbPayUtil.getValue("payHost") + CmbPayUtil.getValue("payReturnUrl") + " ;");//同步响应URL
            orderDataInfo.append("\0");
        }
        log.info("招商银行支付请求之前，组装数据:\n orderDataInfo=" + orderDataInfo);

        String sigdat = "";
        //TODO 调用生成签名接口
        log.info("招商银行支付请求之前，生成签名:sigdat=" + sigdat);

        String CMB_PAY_URL = UtilHelper.isEmpty(sigdat) ? CmbPayUtil.getValue(CmbPayUtil.CMB_PAY_URL_WITHOUT_SIGNED) : CmbPayUtil.getValue(CmbPayUtil.CMB_PAY_URL_WITH_SIGNED);

        Map<String, Object> payRequestParamMap = new HashMap<>();
        payRequestParamMap.put("order",orderDataInfo);//订单信息
        payRequestParamMap.put("sigdat", sigdat);//签名
        payRequestParamMap.put("CMB_PAY_URL",CMB_PAY_URL);
        return payRequestParamMap;
    }

    @Override
    public String paymentCallback(HttpServletRequest request) {
        // TODO: 2016/9/1 需验证签名

        log.info("招行支付回调请求信息，request:"+request);
        String code = CmbPayUtil.CALLBACK_FAILURE_CODE;
        String msg = "未知异常";
        try{
            String xml = "<?xml version=\"1.0\" encoding=\"ISO8859-1\"?>" +
                    "             <DATA>" +
                    "             <REQUEST>" +
                    "             <NCB2BFIN>" +
                    "             <REQNBR>流程实例号</REQNBR>" +
                    "             <MCHNBR>商户编号</MCHNBR>" +
                    "             <REFORD>订单号</REFORD>" +
                    "             <SUBORD>订单支付号</SUBORD>" +
                    "             <CCYNBR>订单币种</CCYNBR>" +
                    "             <TRSAMT>订单金额</TRSAMT>" +
                    "             <ENDAMT>结账金额</ENDAMT>" +
                    "             <BBKNBR>付方分行号</BBKNBR>" +
                    "             <PAYACC>付方账号</PAYACC>" +
                    "             <ACCNAM>付方户名</ACCNAM>" +
                    "             <YURREF>业务参考号</YURREF>" +
                    "             <ENDDAT>付款日期</ENDDAT>" +
                    "             <RTNFLG>业务请求结果</RTNFLG>" +
                    "             <RTNDSP>结果描述</RTNDSP>" +
                    "             </NCB2BFIN>" +
                    "             </REQUEST>" +
                    "             </DATA>";
            log.info("paymentCallback xml:"+xml);
            Map<String,String> requestMap = getCallBackRequestData(xml);
            log.info("招行支付回调,请求参数requestMap:"+requestMap);
            String REQNBR = requestMap.get("REQNBR");
            String MCHNBR = requestMap.get("MCHNBR");
            String REFORD = requestMap.get("REFORD");//支付流水号
            String SUBORD = requestMap.get("SUBORD");
            String CCYNBR = requestMap.get("CCYNBR");
            String TRSAMT = requestMap.get("TRSAMT");
            String ENDAMT = requestMap.get("ENDAMT");
            String BBKNBR = requestMap.get("BBKNBR");
            String PAYACC = requestMap.get("PAYACC");
            String ACCNAM = requestMap.get("ACCNAM");
            String YURREF = requestMap.get("YURREF");
            String ENDDAT = requestMap.get("ENDDAT");
            String RTNFLG = requestMap.get("RTNFLG");
            String RTNDSP = requestMap.get("RTNDSP");

            // TODO: 2016/9/2  需验证哪些字段必填

            //更新支付状态为已支付
            if(!UtilHelper.isEmpty(REFORD)){
                OrderPay orderPay =  orderPayMapper.getByPayFlowId(REFORD);
                if(!UtilHelper.isEmpty(orderPay)){
                    if(!OrderPayStatusEnum.PAYED.getPayStatus().equals(orderPay.getPayStatus())){
                        List<Order> orderList = orderMapper.listOrderByPayFlowId(REFORD);
                        if(!UtilHelper.isEmpty(orderList)){
                            /*更新订单表支付状态*/
                            List<Integer> primaryKeys = new ArrayList<Integer>();
                            for(Order o : orderList)
                                primaryKeys.add(o.getOrderId());
                            Map<String,Object> updateMap = new HashMap<String,Object>();
                            updateMap.put("payStatus",OrderPayStatusEnum.PAYED.getPayStatus());
                            updateMap.put("primaryKeys",primaryKeys);
                            orderMapper.updateOrderPayStatusByPk(updateMap);

                            /*更新支付表支付状态*/
                            String now = systemDateMapper.getSystemDate();
                            orderPay.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());//已支付
                            orderPay.setUpdateUser("招商银行");
                            orderPay.setPayAccountName(ACCNAM);
                            orderPay.setPayAccountNo(PAYACC);
                            orderPay.setPayMoney(new BigDecimal(ENDAMT));
                            orderPay.setUpdateTime(now);
                            orderPay.setPaymentPlatforReturn(xml);
                            int count = orderPayMapper.update(orderPay);
                            if(count != 0){
                                code = CmbPayUtil.CALLBACK_SUCCESS_CODE;
                                msg = "响应成功";
                            }else{
                                msg = "更新t_order_pay失败";
                                log.error("招行支付回调,更新t_order_pay失败");
                            }
                        }else{
                            msg = "未找到订单记录,REFORD:"+REFORD;
                            log.error("招行支付回调,未找到订单记录");
                        }
                    }else{
                        msg = "订单已支付";
                        log.error("招行支付回调,订单已支付");
                    }
                }else{
                    msg = "未找到订单支付记录,REFORD:"+REFORD;
                    log.error("招行支付回调,未找到订单支付记录");
                }
            }else{
                msg = "请求参数【REFORD】为空";
                log.error("招行支付回调,招行请求参数【REFORD】为空");
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("招行支付回调,招行支付回调处理失败，e:"+e.getMessage());
        }

        return String.format(CmbPayUtil.CALLBACK_RESPONSE_TEMPATE,code,msg);
    }


    /**
     * 解析招行支付成功回调接口请求数据
     * @param xml
     * @return
     */
    private Map<String,String> getCallBackRequestData(String xml){
        Map<String,String> map = new HashMap<String,String>();
        List<Map<String,String>> list = XmlUtils.readXmlAsList(xml);
        for(Map<String,String> item: list){
            for(Map.Entry<String,String> entry : item.entrySet()){
                map.put(entry.getKey(),entry.getValue());
            }
        }
        return map;
    }

    /**
     * 招行分账成功回调
     * @param request
     * @return
     */
    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
        return "";
    }

    /**
     * 发起退款请求
     * @param userDto 用户信息
     * @param orderType 订单类型 1：原始订单 2:拒收订单 3：补货订单
     * @param flowId 订单id
     * @param refundDesc 退款原因
     */
    @Override
    public void handleRefund(UserDto userDto, int orderType, String flowId,String refundDesc) {
        OrderRefund orderRefund = new OrderRefund();
        Order order = null;
        if(orderType == 1){
            order = orderMapper.getOrderbyFlowId(flowId);
            orderRefund.setCustId(order.getCustId());
            orderRefund.setSupplyId(order.getSupplyId());
        }else if(orderType == 2 || orderType == 3 ){
            OrderException orderException = orderExceptionMapper.getByExceptionOrderId(flowId);
            order = orderMapper.getByPK(orderException.getOrderId());
        }else{
            log.error("调用招商银行退款，orderType类型不正确，orderType="+orderType);
            throw new RuntimeException("orderType类型不正确");
        }
        SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
        log.info("调用银联招商银行退款，订单详情:"+order);
        //在线支付订单
        if(!SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType()))
            return;
        //买家已付款
        if(!OrderPayStatusEnum.PAYED.getPayStatus().equals(order.getPayStatus()))
            return;

        OrderRefund er=orderRefundMapper.getOrderRefundByOrderId(order.getOrderId());
        //订单是否已退款
        if(!UtilHelper.isEmpty(er)){
            throw new RuntimeException("订单已申请退款");
        }

        OrderPay orderPay =  orderPayMapper.getByPayFlowId(order.getFlowId());
        //调用招行退款
        try{
            boolean result = this.cancelOrder(orderPay.getPayFlowId());
            if(!result){
                log.error("调用招商银行退款，发送请求失败!");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("调用招商银行退款，调用退款接口失败，e:"+e.getMessage());
        }

        String now = systemDateMapper.getSystemDate();
        orderRefund.setCreateUser(userDto.getUserName());
        orderRefund.setCustId(order.getCustId());
        orderRefund.setSupplyId(order.getSupplyId());
        orderRefund.setRefundSum(order.getOrgTotal());
        orderRefund.setRefundFreight(new BigDecimal(0));
        orderRefund.setOrderId(order.getOrderId());
        orderRefund.setFlowId(flowId);
        orderRefund.setCreateTime(now);
        orderRefund.setRefundDate(now);
        orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusIng.getType());//退款中
        orderRefund.setRefundDesc(refundDesc);
        orderRefundMapper.save(orderRefund);
    }

    @Override
    public String redundCallBack(HttpServletRequest request)
    {
     return "";
    }


    //账期还款回调
    public String  paymentOfAccountCallback(HttpServletRequest request){

        return "";
    }
}
