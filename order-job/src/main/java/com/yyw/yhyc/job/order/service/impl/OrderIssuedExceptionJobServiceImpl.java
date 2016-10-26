package com.yyw.yhyc.job.order.service.impl;


import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.job.order.service.OrderIssuedExceptionJobService;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.mapper.OrderIssuedMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.service.OrderDeliveryService;
import com.yyw.yhyc.order.service.OrderIssuedExceptionService;
import com.yyw.yhyc.order.service.OrderIssuedService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.SystemPayTypeService;

@Service("orderIssuedExceptionJobService")
public class OrderIssuedExceptionJobServiceImpl extends AbstractJob implements OrderIssuedExceptionJobService{

	private static final Logger logger = LoggerFactory.getLogger(OrderIssuedExceptionJobServiceImpl.class);
	@Autowired
	private OrderIssuedExceptionService orderIssuedExceptionService;
	
	@Override
	protected ExecResult doTask(JobExecContext jobExecContext) {
		logger.info("*****************收集下发表中失败状态的记录插入到Exception表任务开始*****************");
		 try {
			 
			 orderIssuedExceptionService.exceptionJob();
				 
			 return new ExecResult(0, "succeed!");
	        }catch (Exception ex){
	            logger.error(ex.getMessage(), ex);

	            return new ExecResult(4, "failed!");
	        }
	}

}
