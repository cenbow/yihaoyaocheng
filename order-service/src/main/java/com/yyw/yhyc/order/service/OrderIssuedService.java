/**
 * Created By: XI
 * Created On: 2016-9-10 10:28:42
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.OrderIssuedLog;
import com.yyw.yhyc.order.mapper.OrderIssuedLogMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedDto;
import com.yyw.yhyc.order.mapper.OrderIssuedExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderIssuedMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;

@Service("orderIssuedService")
public class OrderIssuedService {

	private OrderIssuedMapper	orderIssuedMapper;
	private SystemDateMapper systemDateMapper;
	private OrderIssuedLogMapper orderIssuedLogMapper;
	private Log log = LogFactory.getLog(OrderIssuedService.class);
	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderIssuedMapper(OrderIssuedMapper orderIssuedMapper)
	{
		this.orderIssuedMapper = orderIssuedMapper;
	}
	@Autowired
	private OrderIssuedExceptionMapper orderIssuedExceptionMapper;

	@Autowired
	public void setOrderIssuedLogMapper(OrderIssuedLogMapper orderIssuedLogMapper) {
		this.orderIssuedLogMapper = orderIssuedLogMapper;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssued getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> list() throws Exception
	{
		return orderIssuedMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> listByProperty(OrderIssued orderIssued)
			throws Exception
	{
		return orderIssuedMapper.listByProperty(orderIssued);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssued> listPaginationByProperty(Pagination<OrderIssued> pagination, OrderIssued orderIssued) throws Exception
	{
		List<OrderIssued> list = orderIssuedMapper.listPaginationByProperty(pagination, orderIssued);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderIssuedMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderIssuedMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedMapper.deleteByProperty(orderIssued);
	}

	/**
	 * 保存记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssued orderIssued) throws Exception
	{
		orderIssuedMapper.save(orderIssued);
	}

	/**
	 * 更新记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedMapper.update(orderIssued);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssued orderIssued) throws Exception
	{
		return orderIssuedMapper.findByCount(orderIssued);
	}

	/**
	 * 根据供应商Id查询 该供应商状态为已付款的订单 进行下发
	 * @param supplyId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> editOrderIssuedListBySupplyId(Integer supplyId) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(UtilHelper.isEmpty(supplyId)){
			resultMap.put("code","0");
			resultMap.put("message","供应商为空");
			return resultMap;
		}
		List<OrderIssuedDto> orderIssuedDtoList = orderIssuedMapper.findOrderIssuedListBySupplyId(supplyId);
		if(!UtilHelper.isEmpty(orderIssuedDtoList)){
			String now = systemDateMapper.getSystemDate();
			for(OrderIssuedDto orderIssuedDto :orderIssuedDtoList){
				String flowId = orderIssuedDto.getOrderCode();
				OrderIssued orderIssued = orderIssuedMapper.findByFlowId(flowId);
				if(!UtilHelper.isEmpty(orderIssued)){
					orderIssued.setIssuedCount(orderIssued.getIssuedCount()+1);//设置调用次数，值为原次数+1
					orderIssued.setIssuedStatus("1");//设置下发状态，默认为成功
					orderIssued.setUpdateTime(now);
					orderIssuedMapper.update(orderIssued);
				}else{
					orderIssued = new OrderIssued();
					orderIssued.setFlowId(orderIssuedDto.getOrderCode());//设置订单编号
					orderIssued.setIssuedCount(1);//设置调用次数，初始化为1
					orderIssued.setSupplyId(supplyId);
					orderIssued.setSupplyName(orderIssuedDto.getSupplyName());
					orderIssued.setCreateTime(now);
					orderIssued.setIssuedStatus("1");//设置下发状态，默认为成功
					orderIssued.setIsScan(0);
					try{
						orderIssuedMapper.save(orderIssued);
					}catch(Exception e){
						log.info("*********没有对码的订单又对码了，扫出来插入，对原纪录做更新********");
						orderIssued.setCusRelationship(1);
						orderIssued.setIsScan(0);
						orderIssued.setIssuedCount(1);//设置调用次数，初始化为1
						orderIssued.setIssuedStatus("1");//设置下发状态，默认为成功
						orderIssued.setUpdateTime(now);
						orderIssuedMapper.updateBySelective(orderIssued);
						
						OrderIssuedException orderIssuedException = new OrderIssuedException();
						orderIssuedException.setFlowId(flowId);
						orderIssuedException.setDealStatus(2);
						orderIssuedExceptionMapper.updateBySelective(orderIssuedException );
					}
					
				}
				//下发日志
				OrderIssuedLog orderIssuedLog=new OrderIssuedLog();
				orderIssuedLog.setFlowId(orderIssuedDto.getOrderCode());
				orderIssuedLog.setOperateName("下发");
				orderIssuedLog.setOperator(orderIssuedDto.getSupplyName());
				orderIssuedLog.setOperateTime(now);
				orderIssuedLogMapper.save(orderIssuedLog);
			}
		}
		resultMap.put("code","1");
		resultMap.put("orderIssuedDtoList",orderIssuedDtoList);
		return resultMap;
	}

	/**
	 * 获取下发失败列表集合，更改状态为失败，准备重发
	 * @param flowList
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> updateOrderIssuedStatus(List<String> flowList) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(UtilHelper.isEmpty(flowList)){
			resultMap.put("code","0");
			resultMap.put("message","订单集合为空");
			return resultMap;
		}
		String now = systemDateMapper.getSystemDate();
		for(String flowId:flowList){
			OrderIssued orderIssued = orderIssuedMapper.findByFlowId(flowId);
			orderIssued.setIssuedStatus("0");//设置下发状态，为失败
			orderIssued.setUpdateTime(now);
			orderIssuedMapper.update(orderIssued);

			//下发日志
			OrderIssuedLog orderIssuedLog=new OrderIssuedLog();
			orderIssuedLog.setFlowId(flowId);
			orderIssuedLog.setOperateName("下发失败");
			orderIssuedLog.setOperator(orderIssued.getSupplyName());
			orderIssuedLog.setOperateTime(now);
			orderIssuedLogMapper.save(orderIssuedLog);
		}
		resultMap.put("code","1");
		return resultMap;
	}

	public List<OrderIssued> getManufacturerOrder(Integer supplyId){
		log.info("供应商编码："+supplyId+"订单编码集合：" + orderIssuedMapper.getManufacturerOrder(supplyId));
		return orderIssuedMapper.getManufacturerOrder(supplyId);
	}
	
	/**
	 * 
	 * 查询没有对码的订单记录
	 */
	public List<Map<String,Object>> findOrderIssuedNoRelationshipList(){
		return orderIssuedMapper.findOrderIssuedNoRelationshipList();
	}
	//根据flowId给更新
	public int updateBySelective(OrderIssued orderIssued) {
		return orderIssuedMapper.updateBySelective(orderIssued);
	}
}