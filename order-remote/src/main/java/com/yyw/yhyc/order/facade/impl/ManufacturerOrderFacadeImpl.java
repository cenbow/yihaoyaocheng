package com.yyw.yhyc.order.facade.impl;

import com.yyw.yhyc.order.bo.ManufacturerOrder;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.facade.ManufacturerOrderFacade;
import com.yyw.yhyc.order.service.OrderDeliveryService;
import com.yyw.yhyc.order.service.OrderIssuedService;
import com.yyw.yhyc.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2016/9/9.
 */
@Service("manufacturerOrderFacade")
public class ManufacturerOrderFacadeImpl implements ManufacturerOrderFacade {
    private OrderDeliveryService orderDeliveryService;
    private OrderIssuedService orderIssuedService;
    @Autowired
    public void setOrderDeliveryService(OrderDeliveryService orderDeliveryService) {
        this.orderDeliveryService = orderDeliveryService;
    }

    @Autowired
    public void setOrderIssuedService(OrderIssuedService orderIssuedService) {
        this.orderIssuedService = orderIssuedService;
    }

    @Override
    public List<OrderIssued> getManufacturerOrder(Integer supplyId) {
        return orderIssuedService.getManufacturerOrder(supplyId);
    }

    @Override
    public List<ManufacturerOrder> sendOrderDelivery(List<ManufacturerOrder> manufacturerOrderList) {
        return orderDeliveryService.updateOrderDeliver(manufacturerOrderList);
    }
}
