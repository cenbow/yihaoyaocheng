package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.helper.JsonHelper;
import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.helper.UtilHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhou on 2016/7/29
 * 生成随机数、随机编号等字符串
 */
public class RandomUtil {

    /**
     * 自动生成订单编号
     * @param nowTime 数据库当前时间(字符串)
     * @param orderId 订单编号
     * @param orderPrefix 订单编号前缀
     * @return
     */
    public static String createOrderFlowId(String nowTime ,String orderId,String orderPrefix){
        if(UtilHelper.isEmpty(nowTime)){
            return null;
        }
        if(UtilHelper.isEmpty(orderId)){
            return null;
        }
        if(UtilHelper.isEmpty(orderPrefix)){
            return null;
        }
        StringBuilder flowId = new StringBuilder( orderPrefix );
        flowId.append(nowTime + orderId);
        return flowId.toString();
    }

    /**
     * 创建订单支付流水编号
     * @param nowTime 当前时间 ： 20160801104321
     * @param custId  买家企业id
     * @return
     */
    public static String createOrderPayFlowId(String nowTime ,Integer custId){
        if(UtilHelper.isEmpty(nowTime)){
            return null;
        }
        if(UtilHelper.isEmpty(custId)){
            return null;
        }
        StringBuilder flowId = new StringBuilder(CommonType.ORDER_PAY_FLOW_ID_PREFIX);
        flowId.append(nowTime + custId);
        return flowId.toString();
    }

    /**
     * 创建订单支付流水编号
     * @param listStr
     * @return
     */
    public static String createOrderPayFlowId(String listStr){;
        List<Map> list = JsonHelper.fromList(listStr, Map.class);
        for(Map<String,String> orderMap : list){
           return  "PF"+orderMap.get("flowId").trim();
        }
        return null;
    }

    public static String createRoundNum(Integer roundNum,Integer length){
        return createRoundNum(roundNum.toString(),length);
    }
    public static String createRoundNum(String roundNum,Integer length){
        if(roundNum==null||length==null){
            return  null;
        }

        Integer strLength = roundNum.length();
        StringBuffer sb = new StringBuffer();
        for(int i =0;i<(length-strLength);i++){
            sb.append("0");
        }
        roundNum = (Integer.parseInt(roundNum)+1)+"";
        roundNum= sb.toString()+roundNum;
        return roundNum;
    }

    public static void main(String[] args) {
        System.out.println(createRoundNum(26,2));
        System.out.println(createRoundNum(27,2));
    }
}
