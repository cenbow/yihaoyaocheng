package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderPayDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.chinapay.httpClient.HttpRequestHandler;
import com.yyw.yhyc.pay.chinapay.pay.ChinaPay;
import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import com.yyw.yhyc.pay.chinapay.utils.SignUtil;
import com.yyw.yhyc.pay.chinapay.utils.StringUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("chinaPayService")
public class ChinaPayServiceImpl implements PayService {

    private static final Logger log = LoggerFactory.getLogger(ChinaPayServiceImpl.class);

    private OrderPayMapper orderPayMapper;

    private OrderPayManage orderPayManage;
    private SystemDateMapper systemDateMapper;
    private OrderMapper orderMapper;
    private SystemPayTypeMapper systemPayTypeMapper;

    private OrderRefundMapper orderRefundMapper;

    private OrderExceptionMapper orderExceptionMapper;

    private OrderCombinedMapper orderCombinedMapper;

    @Autowired
    public void setOrderPayManage(OrderPayManage orderPayManage) {
        this.orderPayManage = orderPayManage;
    }

    @Autowired
    public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
        this.orderPayMapper = orderPayMapper;
    }

    @Autowired
    public void setOrderRefundMapper(OrderRefundMapper orderRefundMapper) {
        this.orderRefundMapper = orderRefundMapper;
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
    public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
        this.orderExceptionMapper = orderExceptionMapper;
    }

    @Autowired
    public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
        this.systemPayTypeMapper = systemPayTypeMapper;
    }

    @Autowired
    public void setOrderCombinedMapper(OrderCombinedMapper orderCombinedMapper) {
        this.orderCombinedMapper = orderCombinedMapper;
    }

    /**
     * 在发送支付请求之前，组装数据
     * @param orderPay
     * @param systemPayType
     * @param type 1 表示：账期还款  2 表示PC在线支付 3 表示APP在线支付
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> handleDataBeforeSendPayRequest(OrderPay orderPay, SystemPayType systemPayType,int type) throws Exception  {

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
        Map<String,Object> paramMap = new HashMap<>();
        if(type==1){
            paramMap.put(RETURN_RESPONSE_URL,PayUtil.getValue("tradeReturnHost") + "credit/buyerCreditList");
            paramMap.put(ASYNC_CALL_BACK_URL,PayUtil.getValue("payReturnHost") + "orderPay/chinaPayOfAccountCallback");
            return findPayMapOfAccount(orderPay.getPayFlowId(),systemPayType,list,paramMap);
        }else if( 2 == type){
            paramMap.put(RETURN_RESPONSE_URL,PayUtil.getValue("orderReturnHost") + "order/buyerOrderManage");
            paramMap.put(ASYNC_CALL_BACK_URL,PayUtil.getValue("payReturnHost") + "orderPay/chinaPayCallback");
            return findPayMapByPayFlowId(orderPay.getPayFlowId(),systemPayType,list,paramMap);
        }else if( 3 == type){
            paramMap.put(RETURN_RESPONSE_URL,"https://tpay.yaoex.com/orderPay/chinaPayAppSubmitSuccess");
            paramMap.put(ASYNC_CALL_BACK_URL,PayUtil.getValue("payReturnHost") + "orderPay/chinaPayCallback");
            return findPayMapByPayFlowId(orderPay.getPayFlowId(),systemPayType,list,paramMap);
        }else{
            log.error("非法Type参数：type=" + type);
            throw new RuntimeException("非法Type参数");
        }
    }


    /**
     * 组装数据
     * @param payFlowId
     * @param systemPayType
     * @param list
     * @param paramMap  有差异化的参数
     * @return
     * @throws Exception
     */
    private Map<String,Object> findPayMapByPayFlowId(String payFlowId,SystemPayType systemPayType,List<OrderPayDto> list,Map<String,Object> paramMap) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();

        SimpleDateFormat datefomet=new SimpleDateFormat("yyyyMMdd,HHmmss");
        Date date=new Date();
        String fDate=datefomet.format(date);
        String fromWhere="";

        String receiveAccountNo="";
        String receiveAccountName="";

        //查询分账信息；
        StringBuffer MerSplitMsg=new StringBuffer();
        StringBuffer MerSpringCustomer=new StringBuffer("您的货款将在确认收货之后通过银联支付给 ");
        //如果供应商中有一个商户号有问题，就不能进行支付
        boolean isNoHaveMerId=false;

        for(int i=0;i<list.size();i++){
            OrderPayDto orderPayDto=list.get(i);
            receiveAccountNo=orderPayDto.getReceiveAccountNo();
            receiveAccountName=orderPayDto.getReceiveAccountName();
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

        if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere= ChinaPayUtil.B2C;
        }else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.NOCARD;
        }else if(OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.B2B;
        }else if(OnlinePayTypeEnum.UnionPayMobile.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.MOBILE;
        }else{
            fromWhere=ChinaPayUtil.B2C;
        }
        //记录收款帐号
        OrderPay orderPay=orderPayMapper.getByPayFlowId(payFlowId);
        orderPay.setReceiveAccountNo(receiveAccountNo);
        orderPay.setReceiveAccountName(receiveAccountName);
        orderPayMapper.update(orderPay);

        map.put("MerOrderNo", payFlowId);
        map.put("TranDate",fDate.split(",")[0]);
        map.put("TranTime", fDate.split(",")[1]);
        String OrderAmt=String.valueOf(orderPay.getOrderMoney().multiply(new BigDecimal(100)).intValue());
        map.put("OrderAmt", OrderAmt);
        map.put("MerPageUrl", paramMap.get(RETURN_RESPONSE_URL));
        map.put("MerBgUrl", paramMap.get(ASYNC_CALL_BACK_URL));

        String CommodityMsg= HttpRequestHandler.bSubstring(MerSpringCustomer.toString(), 80);
        log.info("CommodityMsg=" + CommodityMsg);
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

    /**
     * 账期还款组装数据
     * @param payFlowId
     * @param systemPayType
     * @param list
     * @param paramMap 有差异化的参数
     * @return
     * @throws Exception
     */
    private Map<String,Object> findPayMapOfAccount(String payFlowId,SystemPayType systemPayType,List<OrderPayDto> list,Map<String,Object> paramMap) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();

        SimpleDateFormat datefomet=new SimpleDateFormat("yyyyMMdd,HHmmss");
        Date date=new Date();
        String fDate=datefomet.format(date);
        String fromWhere="";

        String receiveAccountNo="";
        String receiveAccountName="";

        //查询分账信息；
        StringBuffer MerSplitMsg=new StringBuffer();
        StringBuffer MerSpringCustomer=new StringBuffer("您的货款将在确认收货之后通过银联支付给 ");
        //如果供应商中有一个商户号有问题，就不能进行支付
        boolean isNoHaveMerId=false;

        OrderPayDto orderPayDto=list.get(0);
        receiveAccountNo=orderPayDto.getReceiveAccountNo();
        receiveAccountName=orderPayDto.getReceiveAccountName();
        if(!UtilHelper.isEmpty(orderPayDto)&&!UtilHelper.isEmpty(orderPayDto.getReceiveAccountNo())){
                  MerSpringCustomer.append(orderPayDto.getReceiveAccountName());
                  MerSplitMsg.append(orderPayDto.getReceiveAccountNo()+"^"+orderPayDto.getOrderMoney().multiply(new BigDecimal(100)).intValue());
        }else{
                isNoHaveMerId=true;
        }


        if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere= ChinaPayUtil.B2C;
        }else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.NOCARD;
        }else if(OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.B2B;
        }else if(OnlinePayTypeEnum.UnionPayMobile.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
            fromWhere=ChinaPayUtil.MOBILE;
        }else{
            fromWhere=ChinaPayUtil.B2C;
        }
        //记录收款帐号
        OrderPay orderPay=orderPayMapper.getByPayFlowId(payFlowId);
        orderPay.setReceiveAccountNo(receiveAccountNo);
        orderPay.setReceiveAccountName(receiveAccountName);
        orderPayMapper.update(orderPay);

        map.put("MerOrderNo", payFlowId);
        map.put("TranDate",fDate.split(",")[0]);
        map.put("TranTime", fDate.split(",")[1]);
        String OrderAmt=String.valueOf(orderPay.getOrderMoney().multiply(new BigDecimal(100)).intValue());
        map.put("OrderAmt", OrderAmt);
        map.put("MerPageUrl", paramMap.get(RETURN_RESPONSE_URL));
        map.put("MerBgUrl", paramMap.get(ASYNC_CALL_BACK_URL));

        String CommodityMsg= HttpRequestHandler.bSubstring(MerSpringCustomer.toString(), 80);
        log.info("CommodityMsg=" + CommodityMsg);
        map.put("CommodityMsg", CommodityMsg);

        if(isNoHaveMerId){
            map.put("MerSplitMsg","");
        }else{
            map.put("MerSplitMsg",MerSplitMsg.toString());
        }
        map.put("fromWhere", fromWhere);
        map.put("SplitType","0001");
        map=HttpRequestHandler.getSubmitFormMap(map);
        map=HttpRequestHandler.getSignMap(map);
        log.info("发送银联支付请求之前，组装数据map=" + map);
        return map;
    }

    /**
     * 银联支付回调
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public String paymentCallback(HttpServletRequest request){
        String flag="1";
        try{
            log.info("支付成功后回调开始。。。。。。。。");
            printRequestParam("支付成功后回调",request);
            Map<String,Object> map=new HashMap<String,Object>();
            //解析参数转成map
            map=getParameter(request);
            //if(SignUtil.verify(map)){
            if(true){
                String orderStatus=map.get("OrderStatus").toString();
                if(orderStatus.equals("0000")){
                    map.put("flowPayId",map.get("MerOrderNo"));
                    map.put("money", map.get("OrderAmt"));
                    //回调更新信息
                    orderPayManage.orderPayReturn(map);
                }
            }

        }catch (Exception e){
            flag="0";
            e.printStackTrace();
            log.error("银联支付成功回调");
        }

        return flag;
    }

    // TODO: 2016/9/1 分账成功回调 待江帅编写
    /**
     * 银联分账成功回调
     * @param request
     * @return
     */
    @Override
    public String spiltPaymentCallback(HttpServletRequest request) {
            String flag="1";
            log.info("确认收货后回调开始。。。。。。。。");
            Map<String,Object> map=new HashMap<String,Object>();
        try{
            printRequestParam("确认收货后回调", request);
            //解析参数转成map
            map=getParameter(request);
            /*if(SignUtil.verify(map)){*/
                map.put("flowPayId",map.get("OriOrderNo"));
                map.put("orderStatus", map.get("OrderStatus"));
                orderPayManage.takeConfirmReturn(map);
            /*}*/
        }catch (Exception e){
            flag="0";
            e.printStackTrace();
            log.error("银联支付成功回调异常参数："+map.toString());
        }
        return flag;
    }

    /**
     * 银联退款回调
     * @param request
     * @return
     */
    @Override
    public String redundCallBack(HttpServletRequest request) {
        String flag="1";
        log.info("退款回调开始。。。。。。。。");
        Map<String,Object> map=new HashMap<String,Object>();
        try{
            printRequestParam("退款回调",request);
            map=getParameter(request);
       /*     if(SignUtil.verify(map)){*/
                map.put("flowPayId",map.get("OriOrderNo"));
                map.put("orderStatus", map.get("OrderStatus"));
                orderPayManage.redundCallBack(map);
            /*}*/
        }catch (Exception e){
            flag="0";
            e.printStackTrace();
            log.error("银联支付成功回调异常参数："+map.toString());
        }
        return flag;
    }

    private void printRequestParam(String lonNode,HttpServletRequest request){
        log.info("客户支付后接收银联回调所有参数开始。。。。。。。。");
        Map<String,Object> mapBacnk=request.getParameterMap();
        String re="";
        for(String key:mapBacnk.keySet()){
            try {
                re=re+ URLDecoder.decode(request.getParameter(key), "utf-8");
                log.info(key + "==" + URLDecoder.decode(request.getParameter(key), "utf-8"));
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
        log.info("接收银联回调所有参数结束。。。。。。。。");
    }

    /**
     * 银联分账退款
     * 从江帅那里得知银联的退款策略 ： 必须先全部分账，然后再进行退款
     * @param
     * @return
     */
    public Map<String, String> sendPayQuestForOrder(OrderPay orderPay , List<Order> orderList,BigDecimal orderMoney) throws Exception {

        Map<String,String> rMap=new HashMap<String,String>();
        //初始化支付相关数据
        Map<String,Object> initmap=this.initPayData(orderPay, orderList,orderMoney);
        log.info("银联分账退款组织数据:"+initmap.toString());
        /*//得到定单中支付分账号个数
        Integer merIdNum=(Integer)initmap.get("merIdNum");*/
        //所有子定单中的取消的数量
        Integer cancelNum=(Integer)initmap.get("cancelNum");
        //所有定单中确认收货的数量
        Integer doneNum=(Integer)initmap.get("doneNum");
        //初始化定单中供应商的分账商户所有类型  1 b2c在线支付   2 无卡支付
        String fromWhere=(String)initmap.get("fromWhere");
        //退款记录
        List<Order> orderRefundList=(List<Order>)initmap.get("orderRefundList");
        //定单金额的倍数，银联中的金额以分为单位的整数
        BigDecimal multiple=new BigDecimal("100.00");
        //需要退款的金额
        BigDecimal cancelMoney=(BigDecimal)initmap.get("cancelMoney");
        //分赃信息
        String MerSplitMsg=(String)initmap.get("MerSplitMsg");
        //退款分赃信息
        String RedundMerSplitMsg=(String)initmap.get("RedundMerSplitMsg");


        int len=orderList.size();
        Map<String,String> donePay=null;
        Map<String,String> cancelPay=null;

            if(orderPay.getPayStatus().equals(OrderPayStatusEnum.PAYED.getPayStatus())){

                Date now=new Date();
                String date= StringUtil.getRelevantDate(now);
                String time= StringUtil.getRelevantTime(now);
                //支付日期
                String paydate=StringUtil.getRelevantDate(DateUtils.getDateFromString(orderPay.getPayTime()));

                //赂银联发起分账请求
                donePay=this.doneOrderToChianPay(orderPay, date, time, paydate, cancelMoney, multiple, MerSplitMsg, fromWhere);

                //分账后进行相关操作
                boolean orderSettlementStatus = false;
                if("0000".equals(donePay.get("respCode"))){
                    orderSettlementStatus = true;
                }
                orderPayManage.updateTakeConfirmOrderInfos(orderPay.getPayFlowId(), orderSettlementStatus,orderPay.getPayMoney().subtract(cancelMoney));

                //进行退款
                if(cancelNum>0&&donePay.get("respCode").equals("0000")){
                    //取消定单
                    cancelPay=this.cancelOrderToChianPay(orderPay, date, time,
                            paydate, cancelMoney, multiple, RedundMerSplitMsg, fromWhere);
                    //当银联进行受理退款时写入退款记录

                    if(cancelPay.get("respCode").equals("1003")
                            ||cancelPay.get("respCode").equals("0000")){
                        // //退款成功记录相关信息
                        orderPayManage.updateRedundOrderInfos(orderPay.getPayFlowId(),true,cancelPay,cancelMoney);
                    }else{
                        //退款失败记录相关信息
                        orderPayManage.updateRedundOrderInfos(orderPay.getPayFlowId(),false,cancelPay,cancelMoney);
                    }
                    rMap.put("code", cancelPay.get("respCode"));
                    rMap.put("msg", cancelPay.get("respMsg"));
                }else{
                    rMap.put("code", donePay.get("respCode"));
                    rMap.put("msg", donePay.get("respMsg"));
                }
            }else{
                rMap.put("code", "9998");
                rMap.put("msg", "支付状态错误");
            }
        return rMap;
    }


    /*
     * 初始化子父定单的状态及分账信息
     */
    private Map<String,Object> initPayData(OrderPay orderPay,List<Order> orderList,BigDecimal orderMoney) throws Exception{
        Map<String,Object> rmap=new HashMap<String,Object>();
        Integer cancelNum=0;
        Integer doneNum=0;
        String fromWhere= ChinaPayUtil.B2C;
        List<Order> orderRefundList=new ArrayList<Order>();
        BigDecimal multiple=new BigDecimal("100.00");
        BigDecimal cancelMoney=new BigDecimal("0.00");
        String MerSplitMsg="";
        String RedundMerSplitMsg="";
        SystemPayType systemPayType = systemPayTypeMapper.getByPK(orderPay.getPayTypeId());
        int len=orderList.size();
        for(int i=0;i<len;i++){
            Order o=orderList.get(i);
            String status=o.getOrderStatus();

            if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().intValue()==systemPayType.getPayTypeId().intValue()){
                fromWhere=ChinaPayUtil.B2C;
            }else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().intValue()==systemPayType.getPayTypeId().intValue()){
                fromWhere=ChinaPayUtil.NOCARD;
            }else if(OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
                fromWhere=ChinaPayUtil.B2B;
            }else if(OnlinePayTypeEnum.UnionPayMobile.getPayTypeId().intValue() == systemPayType.getPayTypeId().intValue()){
                fromWhere=ChinaPayUtil.MOBILE;
            }else{
                fromWhere=ChinaPayUtil.B2C;
            }

            //当已收货
            if(status.equals(SystemOrderStatusEnum.BuyerAllReceived.getType())||status.equals(SystemOrderStatusEnum.BuyerPartReceived.getType())){
                doneNum=doneNum+1;
            }

            //确认收货和退款的时候打入付款时商户号
            String MerId = "";
            if(!UtilHelper.isEmpty(orderPay)){
                MerId = orderPay.getReceiveAccountNo();
            }
            //组装分账信息
            if(i==0){
                MerSplitMsg=MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
            }else{
                MerSplitMsg=MerSplitMsg+";"+MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
            }

            boolean isCancel=(status.equals(SystemOrderStatusEnum.BuyerCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.SellerCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())
                    ||status.equals(SystemOrderStatusEnum.BuyerPartReceived.getType()));
            boolean isCancelPreferentialCancelMoney=(!UtilHelper.isEmpty(o.getPreferentialCancelMoney())
                     &&o.getPreferentialCancelMoney().doubleValue()>0);
            //当已取消
            if(isCancel||isCancelPreferentialCancelMoney){//判断当前是否有取消订单或取消发货
                cancelNum=cancelNum+1;
                BigDecimal payMoney=new BigDecimal(0);
                //如果是拒收已同意需要给买家退款
                if(!UtilHelper.isEmpty(orderMoney)){
                    payMoney=orderMoney;
                    cancelMoney=cancelMoney.add(payMoney).add(o.getPreferentialCancelMoney());//把取消的不发货的商品金额也退回去
                }else if(isCancel) {//订单被取消时
                    payMoney=o.getOrgTotal();
                    cancelMoney=cancelMoney.add(payMoney);//当订单状态是取消时直接全部退款
                }else if(isCancelPreferentialCancelMoney){//当订单部分发贫
                    cancelMoney=cancelMoney.add(payMoney).add(o.getPreferentialCancelMoney());//把取消的不发货的商品金额也退回去
                }
                orderRefundList.add(o);
                //组装退款分账信息
                if(UtilHelper.isEmpty(RedundMerSplitMsg)){
                    RedundMerSplitMsg=MerId+"^"+cancelMoney.multiply(multiple).intValue();
                }else{
                    RedundMerSplitMsg=RedundMerSplitMsg+";"+MerId+"^"+cancelMoney.multiply(multiple).intValue();
                }
            }
        }
        rmap.put("cancelNum", cancelNum);
        rmap.put("doneNum", doneNum);
        rmap.put("fromWhere", fromWhere);
        rmap.put("cancelMoney", cancelMoney);
        rmap.put("MerSplitMsg", MerSplitMsg);
        rmap.put("RedundMerSplitMsg", RedundMerSplitMsg);
        rmap.put("orderRefundList", orderRefundList);
        return rmap;
    }


    /*
     * 确认定单时调用 银联进行分账
     */
    private Map<String,String> doneOrderToChianPay(OrderPay orderPay,String date,
                                                   String time,String paydate,BigDecimal cancelMoney,BigDecimal multiple,
                                                   String MerSplitMsg,String fromWhere){

        ChinaPay pay=new ChinaPay();
        //进行分账
        Map<String, Object> splitMap = new HashMap<String, Object>();
        splitMap.put("MerOrderNo", orderPay.getPayFlowId()+"FZ");//确认收货定单号 需要传输，订单号规则     原父定单号+FZ
        splitMap.put("TranDate", date);//交易日期 需要传输
        splitMap.put("TranTime", time);//交易时间 需要传输
        splitMap.put("OriTranDate", paydate);//原定单交易日期 需要传输
        splitMap.put("OrderAmt", new Integer(orderPay.getPayMoney().multiply(multiple).intValue()).toString());//定单金额，需要转过来
        splitMap.put("OriOrderNo", orderPay.getPayFlowId());//原定单号 需要传输
        // 返回参数请参考 (新一代商户接入手册V2.1-) 后续类交易接口 的异步返回报文章
        splitMap.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "orderPay/chinaPaySpiltPaymentCallback");//不需要转过来
        splitMap.put("MerSplitMsg", MerSplitMsg);//分账信息，需要传输过来
        splitMap.put("fromWhere", fromWhere);
        log.info(orderPay.getPayFlowId() + "分账请求参数= " + splitMap.toString());
        //支付日期
        Map<String,String> rt=pay.sendPay2ChinaPay(splitMap);
        log.info(orderPay.getPayFlowId() + "分账请求结果= " + rt.toString());
        return rt;//需要组装定单确认分账的map信息
    }


    /*
     * 赂银联发起退款请求
     */
    private Map<String,String> cancelOrderToChianPay(OrderPay orderPay,String date,
                                                     String time,String paydate,BigDecimal cancelMoney,BigDecimal multiple,
                                                     String RedundMerSplitMsg,String fromWhere){
        ChinaPay pay=new ChinaPay();
        Map<String, Object> sendMap = new HashMap<String, Object>();
        sendMap.put("MerOrderNo", orderPay.getPayFlowId()+"TK");//退款定单号 需要传输，退款寓意号宝规 原子定单号+TK
        sendMap.put("TranDate", date);//当前交易日期
        sendMap.put("TranTime", time);//当前交易时间
        sendMap.put("OriTranDate", paydate);
        sendMap.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "orderPay/chinaPayRedundCallBack");//要转过来,成功后会返回应答
        sendMap.put("OriOrderNo", orderPay.getPayFlowId());//原定单号 需要传输
        sendMap.put("RefundAmt", new Integer(cancelMoney.multiply(multiple).intValue()).toString());//退款金额 需要传输
        sendMap.put("MerSplitMsg", RedundMerSplitMsg);//分账信息，需要传输过来
        sendMap.put("fromWhere", fromWhere);
        log.info(orderPay.getPayFlowId() + "退款请求参数= " + sendMap.toString());
        //支付日期
        Map<String,String> rt=pay.cancelOrder(sendMap);
        log.info(orderPay.getPayFlowId() + "退款请求结果= " + rt.toString());

        return rt;
    }

  /*  *//*
    * 取消定单时写入退款记录
    *//*
    private void createRefundRecord(List<Order> orderRefundList) throws Exception{
        for(Order refund:orderRefundList){
            OrderRefund orderRefund=new OrderRefund();
            orderRefund.setOrderId(refund.getOrderId());
            orderRefund.setRefundSum(refund.getOrgTotal());
            orderRefund.setRefundFreight(new BigDecimal(0));
            orderRefund.setCustId(refund.getCustId());
            orderRefund.setSupplyId(refund.getSupplyId());
            orderRefund.setRefundDesc(refund.getRemark());
            orderRefund.setRefundDate(DateUtils.getNowDate());
            orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusIng.getType());
            orderRefundMapper.save(orderRefund);
        }
    }*/
    /**
     * 发起退款请求
     * @param userDto 用户信息
     * @param orderType 订单类型 1：原始订单 2:拒收订单 3：补货订单
     * @param flowId 订单id
     * @param refundDesc 退款原因
     */
    @Override
    public void handleRefund(UserDto userDto, int orderType, String flowId,String refundDesc){
        OrderRefund orderRefund = new OrderRefund();
        BigDecimal orderMoney = null;
        Order order = null;
        if(orderType == 1){//原始订单
            order = orderMapper.getOrderbyFlowId(flowId);
        }else if(orderType == 2 || orderType == 3 ){//异常订单（20170106  部分发货）
            OrderException orderException = orderExceptionMapper.getByExceptionOrderId(flowId);
            order = orderMapper.getByPK(orderException.getOrderId());
            //拒收订单+买家已确认+已退款
            if(OrderExceptionTypeEnum.REJECT.getType().equals(orderException.getReturnType())
                    && (SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(orderException.getOrderStatus())  ||  SystemOrderExceptionStatusEnum.Refunded.getType().equals(orderException.getOrderStatus()) )){
                orderMoney = orderException.getOrderMoney();
            }

        }else{
            log.error("调用银联退款，orderType类型不正确，orderType="+orderType);
            throw new RuntimeException("orderType类型不正确");
        }
        SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
        log.info("调用银联退款，订单详情:"+order);
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

        OrderCombined orderCombined = orderCombinedMapper.getByPK(order.getOrderCombinedId());

        OrderPay orderPay =  orderPayMapper.getByPayFlowId(orderCombined.getPayFlowId());

        List<Order> orderList = new ArrayList<Order>();
        orderList.add(order);

        //补货订单 + 原始订单全部收货 不需要退款
        if(!(orderType == 3 || SystemOrderStatusEnum.BuyerAllReceived.equals(order.getOrderStatus())))
        {
            //写入退款记录
            String now = systemDateMapper.getSystemDate();
            orderRefund.setCreateUser(userDto.getUserName());
            orderRefund.setCustId(order.getCustId());
            orderRefund.setSupplyId(order.getSupplyId());
            orderRefund.setRefundSum(orderMoney);
            orderRefund.setRefundFreight(new BigDecimal(0));
            orderRefund.setOrderId(order.getOrderId());
            orderRefund.setFlowId(flowId);
            orderRefund.setCreateTime(now);
            orderRefund.setRefundDate(now);
            orderRefund.setRefundStatus(SystemRefundPayStatusEnum.refundStatusIng.getType());//退款中
            orderRefund.setRefundDesc(refundDesc);
            orderRefundMapper.save(orderRefund);
        }

        Map<String, String> resultMap = null;
        try {
            resultMap = this.sendPayQuestForOrder(orderPay,orderList,orderMoney);
        }catch (Exception e){
            e.printStackTrace();
            log.error("调用银联退款，sendPayQuestForOrder方法执行异常，"+e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }

        if(!"0000".equals(resultMap.get("code")) && !"1003".equals(resultMap.get("code"))){
            log.error("调用银联退款，调用银联退款接口失败，"+resultMap.get("msg"));
            //throw new RuntimeException("调用银联退款接口失败，"+resultMap.get("msg"));
        }
    }

    @Override
    public boolean confirmReceivedOrder(String payFlowId) throws Exception {
        return false;
    }

    @Override
    public boolean cancelOrder(String payFlowId) throws Exception {
        return false;
    }



    public Map<String,Object>  getParameter(HttpServletRequest request) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        String[] requests=new String[]{"Version","AccessType","AcqCode","MerId","MerOrderNo","TranDate",
                "TranTime","OriOrderNo","OriTranDate","RefundAmt","OrderAmt","TranType","BusiType","CurryNo",
                "OrderStatus","SplitType","SplitMethod","MerSplitMsg","AcqSeqId","AcqDate","MerResv","TranReserved",
                "CompleteDate","CompleteTime","Signature"};
        for(String str:requests){
            if(!UtilHelper.isEmpty(request.getParameter(str))){
                map.put(str, URLDecoder.decode(request.getParameter(str), "utf-8"));
            }
        }
        if(UtilHelper.isEmpty(map.get("OrderStatus"))&&!UtilHelper.isEmpty(request.getParameter("&OrderStatus"))){
            map.put("OrderStatus",URLDecoder.decode(request.getParameter("&OrderStatus"), "utf-8"));
        }
        return map;
    }

    //账期还款回调
    public Map<String,String>  paymentOfAccountCallback(HttpServletRequest request){
        Map returnMap=new HashMap();
        try{
            log.info("支付成功后回调开始。。。。。。。。");
            printRequestParam("支付成功后回调",request);
            Map<String,Object> map=new HashMap<String,Object>();
            //解析参数转成map
            map=getParameter(request);
            //if(SignUtil.verify(map)){
                String orderStatus=map.get("OrderStatus").toString();
                if(orderStatus.equals("0000")){
                    map.put("flowPayId",map.get("MerOrderNo"));
                    map.put("money", map.get("OrderAmt"));
                    //回调更新信息
                   returnMap=orderPayManage.orderPayOfAccountReturn(map);
                }else{
                    returnMap.put("isSuccess","0");
                    returnMap.put("message",orderStatus);
                }
        }catch (Exception e){
            returnMap.put("isSuccess","0");
            returnMap.put("message",e.getMessage());
            e.printStackTrace();
            log.error("银联支付成功回调");
        }
        return returnMap;
    }

}
