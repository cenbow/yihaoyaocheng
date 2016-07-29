package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.order.helper.UtilHelper;

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

}
