package com.yyw.yhyc.job.order.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.job.order.service.OrderIssuedNoRelationshipJobService;
import com.yyw.yhyc.order.service.OrderIssuedExceptionService;
@Service("orderIssuedNoRelationshipJobService")
public class OrderIssuedNoRelationshipJobServiceImpl extends AbstractJob implements OrderIssuedNoRelationshipJobService{

	private static final Logger logger = LoggerFactory.getLogger(OrderIssuedNoRelationshipJobServiceImpl.class);
	@Autowired
	private OrderIssuedExceptionService orderIssuedExceptionService;
	
	@Override
	protected ExecResult doTask(JobExecContext context) {
		logger.info("*****************收集没有对码的订单记录插入到issued下发表任务开始*****************");
		try {
			orderIssuedExceptionService.downNoRelationshipJob();
			
			return new ExecResult(0, "succeed!");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);

            return new ExecResult(4, "failed!");
        }
	}

}
