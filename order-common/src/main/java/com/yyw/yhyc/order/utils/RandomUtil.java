package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.helper.JsonHelper;
import com.yyw.yhyc.order.bo.CommonType;
import com.yyw.yhyc.helper.UtilHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        return CommonType.ORDER_PAY_FLOW_ID_PREFIX+Md5(listStr);
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

    private static String Md5(String plainText) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(8, 24); //md5 16bit
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
