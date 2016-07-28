package com.yyw.yhyc.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.order.facade.SystemDateFacade;
import com.yyw.yhyc.order.service.TestJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("testJobService")
public class TestJobServiceImpl extends AbstractJob implements TestJobService {

	private static final Logger logger = LoggerFactory.getLogger(TestJobServiceImpl.class);

	@Reference
	private SystemDateFacade systemDateFacade;
	
	@Override
	protected ExecResult doTask(JobExecContext context) {
		try {
			System.out.println("JobServiceAImpl dotask.." );
			logger.info("JobServiceAImpl 任务执行中..");

			logger.info("当前时间是：" + systemDateFacade.getSystemDate());
			Thread.sleep(1000 *3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new ExecResult(4, "failed!");
	}

}
