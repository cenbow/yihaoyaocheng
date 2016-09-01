package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderPayDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.OrderPayMapper;
import com.yyw.yhyc.pay.chinapay.httpClient.HttpRequestHandler;
import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import com.yyw.yhyc.pay.chinapay.utils.SignUtil;
import com.yyw.yhyc.pay.chinapay.utils.StringUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("chinaPayService")
public class ChinaPayServiceImpl implements PayService {

    private static final Logger log = LoggerFactory.getLogger(ChinaPayServiceImpl.class);

    private OrderPayMapper orderPayMapper;
    private OrderPayManage orderPayManage;

    @Autowired
    public void setOrderPayManage(OrderPayManage orderPayManage) {
        this.orderPayManage = orderPayManage;
    }

    @Autowired
    public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
        this.orderPayMapper = orderPayMapper;
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
     * @throws Exception
     */
    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType) throws Exception  {

        if(UtilHelper.isEmpty(orderPay)){
            log.info("支付参数不能为空");
            throw new RuntimeException("参数不能为空");
        }

        if(UtilHelper.isEmpty(orderPay.getPayFlowId())){
            log.info("支付流水不能为空");
            throw new RuntimeException("支付流水不能为空");
        }

        if(UtilHelper.isEmpty(orderPay.getPayTypeId())){
            log.info("支付类型不能为空");
            throw new RuntimeException("参数不能为空");
        }

        List<OrderPayDto> list = orderPayMapper.listOrderPayDtoByProperty(orderPay);

        if(UtilHelper.isEmpty(list)||list.size()==0||UtilHelper.isEmpty(systemPayType)){
            log.info("订单支付信息不存在");
            throw new RuntimeException("订单支付信息不存在");
        }
        return findPayMapByPayFlowId(orderPay.getPayFlowId(),systemPayType,list);
    }



    private Map<String,Object> findPayMapByPayFlowId(String payFlowId,SystemPayType systemPayType,List<OrderPayDto> list) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();

        SimpleDateFormat datefomet=new SimpleDateFormat("yyyyMMdd,HHmmss");
        Date date=new Date();
        String fDate=datefomet.format(date);
        String fromWhere="";

        //查询分账信息；
        StringBuffer MerSplitMsg=new StringBuffer();
        StringBuffer MerSpringCustomer=new StringBuffer("您的货款将在确认收货之后通过银联支付给 ");
        //如果供应商中有一个商户号有问题，就不能进行支付
        boolean isNoHaveMerId=false;

        for(int i=0;i<list.size();i++){
            OrderPayDto orderPayDto=list.get(i);
            if(!UtilHelper.isEmpty(orderPayDto)&&!UtilHelper.isEmpty(orderPayDto.getReceiveAccountNo())){
                if(i==0){
                    MerSpringCustomer.append(orderPayDto.getReceiveAccountName());
                    MerSplitMsg.append(orderPayDto.getReceiveAccountNo()+"^"+orderPayDto.getOrgTotal().multiply(new BigDecimal(100)).intValue());
                }else{
                    MerSpringCustomer.append("、"+orderPayDto.getReceiveAccountName());
                    MerSplitMsg.append(";"+orderPayDto.getReceiveAccountNo()+"^"+orderPayDto.getOrgTotal().multiply(new BigDecimal(100)).intValue());
                }
            }else{
                isNoHaveMerId=true;
            }
        }

        if(OnlinePayTypeEnum.UnionPayB2C.getPayType().intValue()==systemPayType.getPayType().intValue()){
            fromWhere= ChinaPayUtil.B2C;
        }else if(OnlinePayTypeEnum.UnionPayNoCard.getPayType().intValue()==systemPayType.getPayType().intValue()){
            fromWhere=ChinaPayUtil.NOCARD;
        }else{
            fromWhere=ChinaPayUtil.B2C;
        }

        OrderPay orderPay=orderPayMapper.getByPayFlowId(payFlowId);

        map.put("MerOrderNo", payFlowId);
        map.put("TranDate",fDate.split(",")[0]);
        map.put("TranTime", fDate.split(",")[1]);
        String OrderAmt=String.valueOf(orderPay.getOrderMoney().multiply(new BigDecimal(100)).intValue());
        map.put("OrderAmt", OrderAmt);
        map.put("MerPageUrl", PayUtil.getValue("payReturnHost") + "/buyerOrderManage");
        map.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "/OrderCallBackPay");
        log.info(PayUtil.getValue("payReturnHost") + "/OrderCallBackPay");
        String CommodityMsg= HttpRequestHandler.bSubstring(MerSpringCustomer.toString(), 80);
        log.info("CommodityMsg="+CommodityMsg);
        map.put("CommodityMsg", CommodityMsg);

        if(isNoHaveMerId){
            map.put("MerSplitMsg","");
        }else{
            map.put("MerSplitMsg",MerSplitMsg.toString());
        }
        map.put("fromWhere", fromWhere);
        map=HttpRequestHandler.getSubmitFormMap(map);
        map=HttpRequestHandler.getSignMap(map);
        log.info("发送银联支付请求之前，组装数据map=" + map);
        return map;
    }

    // TODO: 2016/8/31 银联支付回调  待江帅编写 
    @Override
    public String paymentCallback(HttpServletRequest request){
        String orderStatus="";
        try{
            System.out.println("支付成功后回调开始。。。。。。。。");
            printRequestParam("支付成功后回调",request);
            Map<String,Object> map=new HashMap<String,Object>();
            String[] requests=new String[]{"Version", "AccessType" , "AcqCode" , "MerId" , "MerOrderNo" , "TranDate" , "TranTime" , "OrderAmt" , "TranType" , "BusiType" , "CurryNo" , "OrderStatus" , "SplitType" , "SplitMethod" ,
                    "MerSplitMsg" , "AcqSeqId" , "AcqDate" , "ChannelSeqId" , "ChannelDate" , "ChannelTime" , "PayBillNo" , "BankInstNo" , "CommodityMsg" ,
                    "MerResv" , "TranReserved" , "CardTranData" , "PayTimeOut" , "TimeStamp" , "RemoteAddr" , "CompleteDate" , "CompleteTime" , "Signature"};
            for(String str:requests){
                if(!UtilHelper.isEmpty(request.getParameter(str))){
                    map.put(str,URLDecoder.decode(request.getParameter(str), "utf-8"));
                }
            }
            if(UtilHelper.isEmpty(map.get("OrderStatus"))&&!UtilHelper.isEmpty(request.getParameter("&OrderStatus"))){
                map.put("OrderStatus",URLDecoder.decode(request.getParameter("&OrderStatus"), "utf-8"));
            }
            if(SignUtil.verify(map)){
                orderStatus=map.get("OrderStatus").toString();
                if(orderStatus.equals("0000")){
                    map.put("flowPayId",map.get("MerOrderNo"));
                    map.put("money",map.get("OrderAmt"));
                }
            }
            //回调更新信息
            orderPayManage.orderPayReturn(map);
        }catch (Exception e){
            e.printStackTrace();
            log.error("银联支付成功回调");
        }

        return orderStatus;
    }

    // TODO: 2016/9/1 分账成功回调 待江帅编写 
    /**
     * 银联分账成功回调
     * @param request
     * @return
     */
    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
        return null;
    }


    private void printRequestParam(String lonNode,HttpServletRequest request){

        System.out.println("客户支付后接收银联回调所有参数开始。。。。。。。。");
        Map<String,Object> mapBacnk=request.getParameterMap();
        String re="";
        for(String key:mapBacnk.keySet()){
            try {
                re=re+ URLDecoder.decode(request.getParameter(key), "utf-8");
                System.out.println(key+"=="+URLDecoder.decode(request.getParameter(key), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            if(!UtilHelper.isEmpty(request.getParameter("MerOrderNo"))){
                log.info(URLDecoder.decode(request.getParameter("MerOrderNo"), "utf-8")+lonNode+"="+ StringUtil.paserMaptoStr(mapBacnk) );
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("接收银联回调所有参数结束。。。。。。。。");
    }

}
