package com.yyw.yhyc.pay.interfaces;

import java.util.Map;

/**
 * Created by lvhongjie on 2016/8/29.
 */
public interface PayService {

    /**
     * Action 1 确认收货 2 取消订单
     */
    public Map<String,Object> postToBankForDoneOrder(Map<String,Object> orderInfo,String Action);

}
