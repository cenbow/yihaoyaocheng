package com.yyw.yhyc.order.facade;

import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/30.
 */
public interface Order4ManagerFacade {
    /**
     * 运营人员查询订单
     * @param data
     * @return
     */
    public Map<String,Object> listPgOperationsOrder(Map<String,String>  data);

    public Map<String,Object> getOrderDetails4Manager(String flowId) throws Exception;
}
