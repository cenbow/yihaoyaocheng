/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.List;

import com.yyw.yhyc.order.dto.OrderSettlementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderSettlementFacade;
import com.yyw.yhyc.order.service.OrderSettlementService;

@Service("orderSettlementFacade")
public class OrderSettlementFacadeImpl implements OrderSettlementFacade {

	private OrderSettlementService orderSettlementService;
	
	@Autowired
	public void setOrderSettlementService(OrderSettlementService orderSettlementService)
	{
		this.orderSettlementService = orderSettlementService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderSettlement getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderSettlementService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> list() throws Exception
	{
		return orderSettlementService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> listByProperty(OrderSettlement orderSettlement)
			throws Exception
	{
		return orderSettlementService.listByProperty(orderSettlement);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderSettlementDto> listPaginationByProperty(Pagination<OrderSettlementDto> pagination, OrderSettlementDto orderSettlementDto)
			throws Exception
	{
		return orderSettlementService.listPaginationByProperty(pagination, orderSettlementDto);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderSettlementService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderSettlementService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementService.deleteByProperty(orderSettlement);
	}

	/**
	 * 保存记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public void save(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementService.save(orderSettlement);
	}

	/**
	 * 更新记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int update(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementService.update(orderSettlement);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementService.findByCount(orderSettlement);
	}
}
