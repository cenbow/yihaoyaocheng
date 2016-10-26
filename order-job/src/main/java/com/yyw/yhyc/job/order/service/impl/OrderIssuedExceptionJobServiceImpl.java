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
	private OrderIssuedService orderIssuedService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDeliveryService orderDliveryService;
	@Autowired
	private SystemPayTypeService systemPayTypeService;
	@Autowired
	private OrderIssuedExceptionService orderIssuedExceptionService;
	@Autowired
	private SystemDateMapper systemDateMapper;
	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}
	@Override
	protected ExecResult doTask(JobExecContext jobExecContext) {
		logger.info("*****************收集下发表中失败状态的记录插入到Exception表任务开始*****************");
		 try {
			 
			 OrderIssued orderIssued = new OrderIssued();
			 //orderIssued.setIssuedCount(3);
			 orderIssued.setIssuedStatus("0");
			 List<OrderIssued> listOrderIssued = orderIssuedService.listByProperty(orderIssued);
			 for(OrderIssued one : listOrderIssued){
				 OrderIssuedException orderIssuedException = new OrderIssuedException();
				 Order order = orderService.getOrderbyFlowId(one.getFlowId());
				 OrderDelivery orderDelivery = (OrderDelivery) orderDliveryService.getOrderDeliveryByFlowId(one.getFlowId());
				 PropertyUtils.copyProperties(orderIssuedException, order);
				 PropertyUtils.copyProperties(orderIssuedException, orderDelivery);
				 
				 SystemPayType sysPayType = systemPayTypeService.getByPK(order.getPayTypeId());
				 orderIssuedException.setOrderCreateTime(order.getCreateTime());
				 orderIssuedException.setPayType(sysPayType.getPayType());
				 orderIssuedException.setPayTypeName(sysPayType.getPayTypeName());
				 
				 orderIssuedException.setDealStatus(1);  //待处理
				 if(orderIssued.getCusRelationship() == 1 && orderIssued.getIssuedCount() == 3)
					 orderIssuedException.setExceptionType(3) ;//下发失败
				 else if(orderIssued.getCusRelationship() == 1 && orderIssued.getIssuedCount() != 3)
					 orderIssuedException.setExceptionType(2);//下发返回错误
				 else if(orderIssued.getCusRelationship() == 0)
					 orderIssuedException.setExceptionType(1);//无关联企业用户
				 
				 orderIssuedException.setOperator("system");
				 orderIssuedException.setOperateTime(systemDateMapper.getSystemDate());
				 try{
					 orderIssuedExceptionService.save(orderIssuedException);
				 }catch  (Exception ex){//这种情况很少，所以用这种方式，避免每次去做一次查询
					 logger.error("插入失败，异常表有此flowId", ex);
					 orderIssuedExceptionService.updateBySelective(orderIssuedException);
				 }
				 
			 }
			 return new ExecResult(0, "succeed!");
	        }catch (Exception ex){
	            logger.error(ex.getMessage(), ex);

	            return new ExecResult(4, "failed!");
	        }
	}

}
