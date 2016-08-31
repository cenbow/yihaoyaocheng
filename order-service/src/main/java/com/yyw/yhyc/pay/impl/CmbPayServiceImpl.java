package com.yyw.yhyc.pay.impl;


import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.service.AccountPayInfoService;
import com.yyw.yhyc.order.service.OrderCombinedService;
import com.yyw.yhyc.order.service.OrderPayService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.pay.cmbPay.CmbPayUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

    private OrderCombinedService orderCombinedService;
    @Autowired
    public void setOrderCombinedService(OrderCombinedService orderCombinedService) {
        this.orderCombinedService = orderCombinedService;
    }

    private OrderService orderService;
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private AccountPayInfoService accountPayInfoService;
    @Autowired
    public void setAccountPayInfoService(AccountPayInfoService accountPayInfoService) {
        this.accountPayInfoService = accountPayInfoService;
    }

    private OrderPayService orderPayService;
    @Autowired
    public void setOrderPayService(OrderPayService orderPayService) {
        this.orderPayService = orderPayService;
    }

    private SystemDateMapper systemDateMapper;
    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    @Override
    public Map<String, Object> postToBankForDoneOrder(Map<String, Object> orderInfo, int Action) {
        return null;
    }

    /**
     * 在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @return
     */
    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType) throws Exception {
        if(UtilHelper.isEmpty(orderPay) || UtilHelper.isEmpty(orderPay.getPayFlowId())){
            return null;
        }
        OrderCombined orderCombined = orderCombinedService.findByPayFlowId(orderPay.getPayFlowId());
        if(UtilHelper.isEmpty(orderCombined)) return null;

        Order orderQuery = new Order();
        orderQuery.setOrderCombinedId(orderCombined.getOrderCombinedId());
        List<Order> orderList = orderService.listByProperty(orderQuery);
        if(UtilHelper.isEmpty(orderList)) return null;

        StringBuffer orderDataInfo = new StringBuffer();
        Integer orderDataInfoCount = 0;
        AccountPayInfo accountPayInfo = null;
        for(Order order : orderList){
            accountPayInfo = new AccountPayInfo();
            accountPayInfo.setCustId(order.getSupplyId());
            accountPayInfo.setPayTypeId(order.getPayTypeId());
            List<AccountPayInfo> accountPayInfoList = accountPayInfoService.listByProperty(accountPayInfo);
            if(UtilHelper.isEmpty(accountPayInfoList) || accountPayInfoList.size() != 1){
                throw  new Exception("供应商收款账户异常");
            }
            accountPayInfo = accountPayInfoList.get(0);
            if(UtilHelper.isEmpty(accountPayInfo) || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountNo())
                    || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName())){
                throw  new Exception("供应商收款账户异常");
            }

            if(orderDataInfoCount != 0){
                orderDataInfo.append("\r\n");
            }

            orderDataInfo.append( CmbPayUtil.MCHNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.MCHNBR) + " ;");//商户编码
            orderDataInfo.append( CmbPayUtil.REFORD + "=" + orderPay.getPayFlowId() + " ;");//订单号
            orderDataInfo.append( CmbPayUtil.CCYNBR + "=" + CmbPayUtil.getValue(CmbPayUtil.CCYNBR) + " ;");//订单币种
            orderDataInfo.append( CmbPayUtil.TRSAMT + "=" + orderPay.getOrderMoney() + " ;");//订单金额
            orderDataInfo.append( CmbPayUtil.CRTACC + "=" + accountPayInfo.getReceiveAccountNo()  + " ;");//收方账号
            orderDataInfo.append( CmbPayUtil.CRTNAM + "=" + accountPayInfo.getReceiveAccountName() + " ;");//收方账户名
            orderDataInfo.append( CmbPayUtil.CRTBNK + "=" + accountPayInfo.getSubbankName()  + " ;");//收方开户行
//            TODO 省市区的字段如何处理？
//            orderDataInfo.append( CmbPayUtil.CRTPVC + "=" +   + " ;");//收方省份
//            orderDataInfo.append( CmbPayUtil.CRTCTY + "=" +   + " ;");//收方城市
            orderDataInfo.append( CmbPayUtil.RETURL + "=" + CmbPayUtil.getValue("payHost") + " /" + CmbPayUtil.getValue("payReturnUrl") + " ;");//同步响应URL
            orderDataInfo.append("\0");
            orderDataInfoCount++;
        }
        log.info("招商银行支付请求之前，组装数据:orderDataInfo=" + orderDataInfo);

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
        log.info("招行支付回调请求信息，request:"+request);

        String code = CmbPayUtil.CALLBACK_FAILURE_CODE;
        String msg = "未知异常";
        try{
            // TODO: 2016/8/31 解析request数据
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
            log.info("招行支付回调请求参数，requestMap:"+requestMap);
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

            if(!UtilHelper.isEmpty(REFORD)){
                OrderPay orderPay =  orderPayService.getByPayFlowId(REFORD);
                if(!UtilHelper.isEmpty(orderPay)){
                    String now = systemDateMapper.getSystemDate();
                    orderPay.setPayStatus("1");//已支付
                    orderPay.setUpdateUser("招商银行");
                    orderPay.setPayAccountName(ACCNAM);
                    orderPay.setPayAccountNo(PAYACC);
                    orderPay.setPayMoney(new BigDecimal(ENDAMT));
                    orderPay.setUpdateTime(now);
                    orderPay.setPaymentPlatforReturn(xml);
                    int count = orderPayService.update(orderPay);
                    if(count != 0){
                        code = CmbPayUtil.CALLBACK_SUCCESS_CODE;
                        msg = "响应成功";
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("招行支付回调处理失败，e:"+e.getMessage());
        }


        return String.format(CmbPayUtil.CALLBACK_RESPONSE_TEMPATE,code,msg);
    }


    /**
     * 解析招行支付成功回调接口请求数据
     * @param xml
     * @return
     */
    private Map<String,String> getCallBackRequestData(String xml){
        return null;
    }

    /**
     * 招行分账成功回调
     * @param request
     * @return
     */
    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
        return null;
    }


}
