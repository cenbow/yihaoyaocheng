package com.yyw.yhyc.job.order.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.job.order.service.OrderIssuedNoRelationshipJobService;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.mapper.OrderIssuedMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.service.OrderIssuedService;
@Service("orderIssuedNoRelationshipJobService")
public class OrderIssuedNoRelationshipJobServiceImpl extends AbstractJob implements OrderIssuedNoRelationshipJobService{

	private static final Logger logger = LoggerFactory.getLogger(OrderIssuedNoRelationshipJobServiceImpl.class);
	@Autowired
	private OrderIssuedService orderIssuedService;
	
	private OrderIssuedMapper	orderIssuedMapper;
	private SystemDateMapper systemDateMapper;
	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}
	@Autowired
	public void setOrderIssuedMapper(OrderIssuedMapper orderIssuedMapper)
	{
		this.orderIssuedMapper = orderIssuedMapper;
	}
	@Override
	protected ExecResult doTask(JobExecContext context) {
		logger.info("*****************收集没有对码的订单记录插入到issued下发表任务开始*****************");
		try {
			List<Map<String,String>>  list = orderIssuedService.findOrderIssuedNoRelationshipList();
			
			for(Map<String,String> param : list){
				OrderIssued orderIssued = new OrderIssued();
				orderIssued.setFlowId(param.get("FLOW_ID"));//设置订单编号
				orderIssued.setSupplyId(Integer.valueOf(param.get("SUPPLY_ID")));
				orderIssued.setCreateTime(systemDateMapper.getSystemDate());
				//这样订单下发时不会扫描出来这些没对码的数据
				orderIssued.setIssuedStatus("0");//设置下发状态，默认为失败
				orderIssued.setIssuedCount(3);//设置调用次数，初始化为3
				orderIssued.setCusRelationship(0);//无客户关联关系
				orderIssuedMapper.save(orderIssued);
			}
			return new ExecResult(0, "succeed!");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);

            return new ExecResult(4, "failed!");
        }
	}

}
