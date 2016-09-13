package com.yyw.yhyc.order.facade;

import com.yyw.yhyc.order.bo.ManufacturerOrder;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderIssued;

import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2016/9/9.
 */
public interface ManufacturerOrderFacade {

    /**
     * 根据供应商编码查询下发成功的订单编码
     * @param supplyId
     * @return
     */
    public List<OrderIssued> getManufacturerOrder(Integer supplyId);

    /**
     * 发货
     * @param manufacturerOrderList
     * @return
     */
    public List<ManufacturerOrder> sendOrderDelivery(List<ManufacturerOrder> manufacturerOrderList);


}
