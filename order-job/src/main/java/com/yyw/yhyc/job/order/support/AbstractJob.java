package com.yyw.yhyc.job.order.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shiyongxi on 2016/9/9.
 */
public abstract class AbstractJob implements IJobService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJob.class);

    public void execute() {
        try {
            this.doTask();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }

        logger.info("任务执行完毕");
    }

    protected abstract void doTask() throws Exception;

}
