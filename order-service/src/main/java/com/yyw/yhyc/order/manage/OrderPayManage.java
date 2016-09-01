package com.yyw.yhyc.order.manage;

import com.sun.xml.internal.bind.v2.TODO;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.mapper.*;
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
    private SystemPayTypeMapper systemPayTypeMapper;
    private AccountPayInfoMapper accountPayInfoMapper;
    private OrderRefundMapper orderRefundMapper;
    private OrderMapper orderMapper;
    private SystemDateMapper systemDateMapper;
    private OrderTraceMapper orderTraceMapper;

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
    public void setOrderRefundMapper(OrderRefundMapper orderRefundMapper) {
        this.orderRefundMapper = orderRefundMapper;
    }

    @Autowired
    public void setOrderPayMapper(OrderPayMapper orderPayMapper) {
        this.orderPayMapper = orderPayMapper;
    }

    @Autowired
    public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
        this.systemPayTypeMapper = systemPayTypeMapper;
    }

    @Autowired
    public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper) {
        this.accountPayInfoMapper = accountPayInfoMapper;
    }

    public Map<String, String> sendPayQuestForOrder(OrderPay orderPay , List<Order> orderList, int actionType) throws Exception {

        //初始化支付相关数据
        Map<String,Object> initmap=this.initPayData(orderPay, orderList);
        //得到定单中支付分账号个数
        Integer merIdNum=(Integer)initmap.get("merIdNum");
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

        //取得当前父订单中是否已经支付
        if(len==cancelNum+doneNum&&merIdNum==len){
            if(orderPay.getPayStatus().equals(OrderPayStatusEnum.PAYED.getPayStatus())){
             /*   //根据当前分账信息判断是否全是由一号药业收款
                boolean isReceive= HttpRequestHandler.isBysystemReceiveMoney(initmap);
                System.out.println("订单是否为代收货款:"+orderParent.getPayFlowId()+"=="+isReceive);
                if(!isReceive){*/
                    Date now=new Date();
                    String date= StringUtil.getRelevantDate(now);
                    String time= StringUtil.getRelevantTime(now);
                    //支付日期
                    String paydate=StringUtil.getRelevantDate(DateUtils.getDateFromString(orderPay.getPayTime()));

                    //赂银联发起分账请求
                    donePay=this.doneOrderToChianPay(orderPay, date, time, paydate,
                            cancelMoney, multiple, MerSplitMsg, fromWhere);
                    log.info(orderPay.getPayFlowId() + "退款订单分账信息= " + RedundMerSplitMsg);
                    System.out.println("分账请求结果= "+donePay.toString());
                    System.out.println("退款订单数量= "+RedundMerSplitMsg);
                    if(donePay.get("respCode").equals("0000")){
                        log.info("分账成功结果"+donePay.toString());
                        //TODO 是否记录分账结果
                        /*orderParent.setRoutingStatus(1);
                        orderParent.setRoutingRemark("分账结果"+donePay.toString());*/
                    }else{
                        log.info("分账失败结果"+donePay.toString());
                        /*orderParent.setRoutingStatus(2);
                        orderParent.setRoutingRemark("分账结果"+donePay.toString());*/
                    }
                    //进行退款
                    if(cancelNum>0&&donePay.get("respCode").equals("0000")){
                        //取消定单
                        cancelPay=this.cancelOrderToChianPay(orderPay, date, time,
                                paydate, cancelMoney, multiple, RedundMerSplitMsg, fromWhere);
                        System.out.println("退款请求结果= "+cancelPay.toString());
                        //当银联进行受理退款时写入退款记录

                        if(cancelPay.get("respCode").equals("1003")
                                ||cancelPay.get("respCode").equals("0000")){
                            log.info("退款成功结果" + donePay.toString());
                            //TODO 是否记录退款结果
                        }else{
                            log.info("退款结果" + cancelPay.toString());
                        }

                        if(cancelPay.get("respCode").equals("1003")
                                ||cancelPay.get("respCode").equals("0000")){

                            //写入退款记录
                            this.createRefundRecord(orderRefundList);
                        }

                    }
            }

        }
        //这里需要拿  donePay cancelPay 这两个银联返回的map进行返回操作结果分析
        return this.excuteResults(actionType, donePay, cancelPay);
    }


    /*
     * 初始化子父定单的状态及分账信息
     */
    private Map<String,Object> initPayData(OrderPay orderPay,List<Order> orderList) throws Exception{
        Map<String,Object> rmap=new HashMap<String,Object>();
        Integer merIdNum=0;
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

            if(OnlinePayTypeEnum.UnionPayB2C.getPayType().intValue()==systemPayType.getPayType().intValue()){
                fromWhere=ChinaPayUtil.B2C;
            }else if(OnlinePayTypeEnum.UnionPayNoCard.getPayType().intValue()==systemPayType.getPayType().intValue()){
                fromWhere=ChinaPayUtil.NOCARD;
            }else{
                fromWhere=ChinaPayUtil.B2C;
            }

            //当已收货
            if(status.equals(SystemOrderStatusEnum.BuyerAllReceived.getType())||status.equals(SystemOrderStatusEnum.BuyerPartReceived.getType())){
                doneNum=doneNum+1;
            }
            AccountPayInfo accountPayInfo=new AccountPayInfo();
            accountPayInfo.setCustId(o.getSupplyId());
            accountPayInfo.setPayTypeId(systemPayType.getPayTypeId());
            accountPayInfo=accountPayInfoMapper.getByCustId(accountPayInfo);

            String MerId = "";
            if(!UtilHelper.isEmpty(orderPay)){
                MerId = accountPayInfo.getReceiveAccountNo();
            }
            if(!UtilHelper.isEmpty(MerId)){
                merIdNum=merIdNum+1;
            }
            //组装分账信息
            if(i==0){
                MerSplitMsg=MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
            }else{
                MerSplitMsg=MerSplitMsg+";"+MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
            }

            //当已取消
            if(status.equals(SystemOrderStatusEnum.BuyerCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.SellerCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType())
                    ||status.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())){
                cancelNum=cancelNum+1;
                OrderRefund orderRefund=orderRefundMapper.getOrderRefundByOrderId(o.getOrderId());
                if(UtilHelper.isEmpty(orderRefund)){
                    BigDecimal payMoney=o.getOrgTotal();
                    cancelMoney=cancelMoney.add(payMoney);
                    orderRefundList.add(o);

                    //组装退款分账信息
                    if(UtilHelper.isEmpty(RedundMerSplitMsg)){
                        RedundMerSplitMsg=MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
                    }else{
                        RedundMerSplitMsg=RedundMerSplitMsg+";"+MerId+"^"+o.getOrgTotal().multiply(multiple).intValue();
                    }
                }
            }

        }


        rmap.put("merIdNum", merIdNum);
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
        splitMap.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "/ConfirmCallBack.action");//不需要转过来
        splitMap.put("MerSplitMsg", MerSplitMsg);//分账信息，需要传输过来
        splitMap.put("fromWhere", fromWhere);

        log.info(orderPay.getPayFlowId() + "分账请求参数1= " + splitMap.toString());
        //支付日期
        Map<String,String> rt=pay.sendPay2ChinaPay(splitMap);
        log.info(orderPay.getPayFlowId() + "分账请求结果1= " + rt.toString());
        if(!rt.get("respCode").equals("0000")){
            paydate=StringUtil.getRelevantDate(DateUtils.getDateFromString(DateUtils.getNextDay(1, orderPay.getPayTime())));
            splitMap.put("OriTranDate", paydate);//原定单交易日期 需要传输
            splitMap.put("MerOrderNo", orderPay.getPayFlowId()+"FZ1");//原定单交易日期 需要传输
            log.info(orderPay.getPayFlowId() + "分账请求参数2= " + splitMap.toString());
            rt = pay.sendPay2ChinaPay(splitMap);
            log.info(orderPay.getPayFlowId()+"分账请求结果2= "+rt.toString());
        }
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
        sendMap.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "/RedundCallBack.action");//要转过来,成功后会返回应答
        sendMap.put("OriOrderNo", orderPay.getPayFlowId());//原定单号 需要传输
        sendMap.put("RefundAmt", new Integer(cancelMoney.multiply(multiple).intValue()).toString());//退款金额 需要传输
        sendMap.put("MerSplitMsg", RedundMerSplitMsg);//分账信息，需要传输过来
        sendMap.put("fromWhere", fromWhere);

        log.info(orderPay.getPayFlowId() + "退款请求参数1= " + sendMap.toString());
        //支付日期
        Map<String,String> rt=pay.cancelOrder(sendMap);
        log.info(orderPay.getPayFlowId() + "退款请求结果1= " + rt.toString());
        if(!rt.get("respCode").equals("1003")
                ||!rt.get("respCode").equals("0000")){
            paydate=StringUtil.getRelevantDate(DateUtils.getDateFromString(DateUtils.getNextDay(1, orderPay.getPayTime())));
            sendMap.put("OriTranDate", paydate);//原定单交易日期 需要传输
            sendMap.put("MerOrderNo", orderPay.getPayFlowId()+"TK1");//原定单交易日期 需要传输
            log.info(orderPay.getPayFlowId()+"退款请求参数2= " + sendMap.toString());
            rt=pay.cancelOrder(sendMap);
            log.info(orderPay.getPayFlowId()+"退款请求结果2= " + rt.toString());
        }
        return rt;
    }

    /*
    * 取消定单时写入退款记录
    */
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
    }


    /*
     * 取消或确认收货时返回的结果
     */
    private Map<String,String> excuteResults(int actionType,Map<String,String> donePay,Map<String,String> cancelPay){
        Map<String,String> rMap=new HashMap<String,String>();
        //这里需要拿  donePay cancelPay 这两个银联返回的map进行返回操作结果分析
        if(actionType==1){
            if(donePay!=null){
                rMap.put("code", donePay.get("respCode"));
                rMap.put("msg", donePay.get("respMsg"));
            }else{
                rMap.put("code", "0000");
                rMap.put("msg", "确认收货成功");
            }

        }
        if(actionType==2){
            if(cancelPay!=null){
                rMap.put("code", cancelPay.get("respCode"));
                rMap.put("msg", cancelPay.get("respMsg"));
            }else{
                rMap.put("code", "0000");
                rMap.put("msg", "取消订单成功");
            }

        }
        return rMap;
    }


    /*// 确认收货
    public void takeConfirmReturn(Map<String, Object> map) throws Exception {
        log.debug("----收到确认收货订单后台通知------" + map.toString());

        String flowPayId = map.get("flowPayId").toString();
        String orderStatus = map.get("orderStatus").toString();
        updateTakeConfirmOrderInfos(flowPayId, orderStatus);
    }

    // 收到订单退款通知
    public void redundCallBack(Map<String, Object> map) throws Exception {
        log.debug("----收到三方支付返回订单退款通知------" + map.toString());

        String flowPayId = map.get("flowPayId").toString();
        String orderStatus = map.get("orderStatus").toString();
        updateRedundOrderInfos(flowPayId, orderStatus, map);
    }*/


    // 支付完成
    public void orderPayReturn(Map<String, Object> map) throws Exception {
        log.debug("----收到三方支付返回的订单后台通知------" + map.toString());

        String flowPayId = map.get("flowPayId").toString();
        String money = map.get("money").toString();
        String MerId = map.get("MerId").toString();
        int fromWhere = OnlinePayTypeEnum.UnionPayB2C.getPayType();
        if (MerId.equals(PayUtil.getValue("MerId"))) {
            fromWhere = OnlinePayTypeEnum.UnionPayB2C.getPayType();
        } else if (MerId.equals(PayUtil.getAppValue("MerId"))) {
            fromWhere = OnlinePayTypeEnum.UnionPayNoCard.getPayType();
        }
       log.info(flowPayId+"支付成功后回调" + StringUtil.paserMaptoStr(map));
       /* boolean isReceive = HttpRequestHandler.isBysystemReceiveMoney(map);*/
        /*log.debug("---当前订单是否为代收货款--"+ flowPayId +"=="+ isReceive);*/
        updateOrderpayInfos(flowPayId, new BigDecimal(money),map.toString());
    }

    // 支付完成更新信息
    public void updateOrderpayInfos(String payFlowId, BigDecimal finalPay,String Payment)
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
                orderPay.setPaymentPlatforReturn(Payment);
                orderPay.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
                orderPayMapper.update(orderPay);

                for (Order order : listOrder) {
                    if (SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())) {
                        // 更新订单信息
                        order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
                        order.setUpdateTime(now);
                        order.setPayTime(now);
                        orderMapper.update(order);
                        /*orderManager.sendSMS(order, null, order.getSupplyId(), MessageTemplate.BUYER_PAY_ORDER_INFO_CODE);*/
                        // 保存订单操作记录

                        //插入日志表
                        OrderTrace orderTrace = new OrderTrace();
                        orderTrace.setOrderId(order.getOrderId());
                        orderTrace.setNodeName("补货卖家已发货");
                        orderTrace.setDealStaff("银联回调");
                        orderTrace.setRecordDate(now);
                        orderTrace.setRecordStaff("银联回调");
                        orderTrace.setOrderStatus(order.getOrderStatus());
                        orderTrace.setCreateTime(now);
                        orderTrace.setCreateUser("银联回调");
                        orderTraceMapper.save(orderTrace);

                        //TODO 从买家支付后开始计算5个自然日内未发货将资金返还买家订单自动取消-与支付接口整合 待接入方法
                    }
                }
               log.info(payFlowId + "-----支付成功后更新信息   update orderInfo end ----");
            }
        }
    }


    // 确认收货更新信息
    private void updateTakeConfirmOrderInfos(String payFlowId, String orderStatus) throws Exception {
        log.info(payFlowId + "----- 分账成功后更新信息  update orderInfo start ----");

        List<Order> listOrder = orderMapper.listOrderByPayFlowId(payFlowId);

        if (UtilHelper.isEmpty(listOrder)||listOrder.size()==0) {
            // 商户数据异常
            log.info("根据订单流水号查询订单不存在");
            throw new Exception("支付信息异常！");
        }
        String now = systemDateMapper.getSystemDate();
        if (orderStatus.equals("0000")) {// 打款成功
            for (Order order : listOrder) {
                if(SystemOrderStatusEnum.BuyerAllReceived.getType().equals(order.getOrderStatus())||SystemOrderStatusEnum.BuyerPartReceived.getType().equals(order.getOrderStatus())) {
                    //生产订单日志
                    createOrderTrace(order, "银联确认收货回调", now, 2, "确认收货打款成功.");
                }
            }
        } else {// 打款异常
            for (Order order : listOrder) {
                if (SystemOrderStatusEnum.BuyerAllReceived.getType().equals(order.getOrderStatus())||SystemOrderStatusEnum.BuyerPartReceived.getType().equals(order.getOrderStatus())) {
                    order.setOrderStatus(SystemOrderStatusEnum.PaidException.getType());
                    orderMapper.update(order);
                    //生产订单日志
                    createOrderTrace(order, "银联确认收货回调", now, 2, "确认收货打款失败.");
                }
            }
        }
        log.info(payFlowId + "----- 分账成功后更新信息  update orderInfo end ----");

    }
/*

    // 退款更新信息
    private void updateRedundOrderInfos(String PayflowId, String orderStatus, Map<String, Object> map)
            throws ServiceException {
        logger.info(PayflowId + "----- 退款成功后更新信息  update orderInfo start ----");

        List<Order> listOrder = orderService.listOrderInfosByPayFlowId(PayflowId);

        if (UtilHelper.isEmpty(listOrder)) {
            // 商户数据异常
            throw new ServiceException(Message3kwResource.ERROR_MESSAGE_3KW_SYSTEM_ERROR, "退款信息异常！");
        }
        for (Order o : listOrder) {
            OrderRefund orderRefund = orderRefundService.getByPK(o.getOrderId());
            if (UtilHelper.isEmpty(orderRefund)) {
                orderRefund.setRemark(map.toString());
                if (orderStatus.equals("0000")) {
                    o.setOrderStatus(CommonType.ORDER_STATUS_BSUCCESS);
                    orderService.updateStatus(o);
                    orderRefund.setRefundStatus(CommonType.ORDER_STATUS_BSUCCESS);
                    orderRefundService.update(orderRefund);
                    o.setOrderStatus(CommonType.ORDER_STATUS_BSUCCESS);
                    orderService.update(o);
                } else {
                    orderRefund.setRefundStatus(CommonType.ORDER_STATUS_PAY_EXCEPTION);
                    orderRefundService.update(orderRefund);
                }

            }
        }

        logger.info(PayflowId + "----- 退款成功后更新信息  update orderInfo end ----");

    }
*/


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
