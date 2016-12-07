package com.yyw.yhyc.order.facade.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.order.bo.ManufacturerOrder;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.facade.ManufacturerOrderFacade;
import com.yyw.yhyc.order.service.OrderDeliveryService;
import com.yyw.yhyc.order.service.OrderIssuedService;
import com.yyw.yhyc.utils.MyConfigUtil;

/**
 * Created by liqiang on 2016/9/9.
 */
@Service("manufacturerOrderFacade")
public class ManufacturerOrderFacadeImpl implements ManufacturerOrderFacade {
    private OrderDeliveryService orderDeliveryService;
    private OrderIssuedService orderIssuedService;
    @Reference
  	private IPromotionDubboManageService iPromotionDubboManageService;
    @Reference
	private CreditDubboServiceInterface creditDubboService;
    
    @Autowired
    public void setOrderDeliveryService(OrderDeliveryService orderDeliveryService) {
        this.orderDeliveryService = orderDeliveryService;
    }

    @Autowired
    public void setOrderIssuedService(OrderIssuedService orderIssuedService) {
        this.orderIssuedService = orderIssuedService;
    }

    @Override
    public List<OrderIssued> getManufacturerOrder(Integer supplyId,String payType) {
        return orderIssuedService.getManufacturerOrder(supplyId,payType);
    }

    @Override
    public List<ManufacturerOrder> sendOrderDelivery(List<ManufacturerOrder> manufacturerOrderList) {
        return orderDeliveryService.updateOrderDeliver(manufacturerOrderList,MyConfigUtil.FILE_PATH);
    }

	@Override
	public Map<String, Object> sendProductToOrderDelivery(
			List<ManufacturerOrder> manufacturerOrderList) throws Exception {
		return this.orderDeliveryService.updateSendProductToOrderDelivery(manufacturerOrderList, MyConfigUtil.FILE_PATH, iPromotionDubboManageService,creditDubboService);
	}
}
