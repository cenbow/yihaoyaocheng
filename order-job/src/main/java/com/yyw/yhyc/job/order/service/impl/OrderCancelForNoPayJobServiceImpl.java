package com.yyw.yhyc.job.order.service.impl;

import com.yyw.yhyc.job.order.support.AbstractJob;
import com.yyw.yhyc.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/8/3.
 */
//@Service("orderCancelForNoPayJobService")
public class OrderCancelForNoPayJobServiceImpl extends AbstractJob {
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelForNoPayJobServiceImpl.class);

    @Autowired
    private OrderService orderService;

    /**
     * 系统自动取消订单
     * 1在线支付订单24小时系统自动取消
     * 2 线下支付7天后未确认收款系统自动取消
     */
    @Override
    protected void doTask() {
        orderService.updateCancelOrderForNoPay();
    }
}
