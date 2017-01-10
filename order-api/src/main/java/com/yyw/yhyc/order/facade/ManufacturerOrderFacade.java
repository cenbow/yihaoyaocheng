package com.yyw.yhyc.order.facade;

import com.yyw.yhyc.order.bo.ManufacturerOrder;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.dto.OrderIssuedDto;

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
    public List<OrderIssuedDto> getManufacturerOrder(Integer supplyId,String payType);

    /**
     * 发货
     * @param manufacturerOrderList
     * @return
     */
    public List<ManufacturerOrder> sendOrderDelivery(List<ManufacturerOrder> manufacturerOrderList);
    
    /**
     * 发货，包含发货的商品信息
     * @param manufacturerOrderList
     * @return
     */
    public Map<String,Object> sendProductToOrderDelivery(List<ManufacturerOrder> manufacturerOrderList)throws Exception;


}
