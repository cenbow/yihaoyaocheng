package com.yyw.yhyc.order.facade.impl;

import com.yyw.yhyc.order.facade.Order4ManagerFacade;
import com.yyw.yhyc.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/30.
 */
@Service("order4ManagerFacade")
public class Order4ManagerFacadeImpl implements Order4ManagerFacade {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Map<String, Object> listPgOperationsOrder(Map<String, String> data) {
        return orderService.listPgOperationsOrder(data);
    }

    @Override
    public Map<String, Object> listPgProducts(Map<String, String> data) throws Exception {
        return orderService.listPgProducts(data);
    }

    @Override
    public Map<String, Object> getOrderDetails4Manager(String flowId) throws Exception{
        return orderService.getOrderDetails4Manager(flowId);
    }

    @Override
    public void cancelOrder(String userName,String orderId, String cancelResult) throws Exception {
        orderService.updateOrder4Manage(userName,orderId, cancelResult,null);
    }


}
