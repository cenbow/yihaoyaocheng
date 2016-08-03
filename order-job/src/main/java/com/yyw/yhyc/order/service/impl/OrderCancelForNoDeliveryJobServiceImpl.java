package com.yyw.yhyc.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.order.facade.OrderFacade;
import com.yyw.yhyc.order.service.OrderCancelForNoDeliveryJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/8/3.
 */
@Service("orderCancelForNoDeliveryJobService")
public class OrderCancelForNoDeliveryJobServiceImpl extends AbstractJob implements OrderCancelForNoDeliveryJobService {
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelForNoDeliveryJobServiceImpl.class);

    @Reference
    private OrderFacade orderFacade;

    /**
     * 系统自动取消订单
     * 1在线支付订单7个自然日未发货系统自动取消
     */
    @Override
    protected ExecResult doTask(JobExecContext jobExecContext) {
        try {
            orderFacade.cancelOrderForNoDelivery();
            return new ExecResult(0, "succeed!");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);

            return new ExecResult(4, "failed!");
        }
    }
}
