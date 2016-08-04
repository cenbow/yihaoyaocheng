package com.yyw.yhyc.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.order.facade.OrderFacade;
import com.yyw.yhyc.order.service.OrderCancelForNoPayJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/8/3.
 */
@Service("orderCancelForNoPayJobService")
public class OrderCancelForNoPayJobServiceImpl extends AbstractJob implements OrderCancelForNoPayJobService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelForNoPayJobServiceImpl.class);

    @Reference
    private OrderFacade orderFacade;

    /**
     * 系统自动取消订单
     * 1在线支付订单24小时系统自动取消
     * 2 线下支付7天后未确认收款系统自动取消
     */
    @Override
    protected ExecResult doTask(JobExecContext jobExecContext) {
        try {
            orderFacade.cancelOrderForNoPay();
            return new ExecResult(0, "succeed!");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);

            return new ExecResult(4, "failed!");
        }
    }
}
