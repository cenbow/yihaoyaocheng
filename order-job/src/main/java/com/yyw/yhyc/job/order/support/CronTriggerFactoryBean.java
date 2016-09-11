package com.yyw.yhyc.job.order.support;

import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * Created by shiyongxi on 2016/9/9.
 */
public class CronTriggerFactoryBean extends org.springframework.scheduling.quartz.CronTriggerFactoryBean {
    public CronTriggerFactoryBean(Object targetObject, String cronExpression) throws Exception{
       this(targetObject, "execute", cronExpression);
    }

    public CronTriggerFactoryBean(Object targetObject, String targetMethod, String cronExpression) throws Exception{
        super.setCronExpression(cronExpression);
        MethodInvokingJobDetailFactoryBean factory =  new MethodInvokingJobDetailFactoryBean();
        factory.setTargetObject(targetObject);
        factory.setTargetMethod(targetMethod);
        factory.setBeanName(targetObject.getClass().getSimpleName());
        factory.afterPropertiesSet();

        super.setJobDetail(factory.getObject());
    }
}
