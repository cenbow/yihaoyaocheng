package com.yyw.yhyc.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.order.facade.OrderFacade;
import com.yyw.yhyc.order.service.OrderDoneForDeliveryJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/8/3.
 */
@Service("orderDoneForDeliveryJobService")
public class OrderDoneForDeliveryJobServiceImpl extends AbstractJob implements OrderDoneForDeliveryJobService {
    private static final Logger logger = LoggerFactory.getLogger(OrderDoneForDeliveryJobServiceImpl.class);

    @Reference
    private OrderFacade orderFacade;

    /**
     * 系统自动确认收货
     * 订单发货后7个自然日后系统自动确认收货
     */
    @Override
    protected ExecResult doTask(JobExecContext jobExecContext) {
        try {
            orderFacade.doneOrderForDelivery();
            return new ExecResult(0, "succeed!");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);

            return new ExecResult(4, "failed!");
        }
    }
}
