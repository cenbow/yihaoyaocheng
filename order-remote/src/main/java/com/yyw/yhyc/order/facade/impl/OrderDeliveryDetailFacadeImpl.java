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

import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderDeliveryDetailFacade;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;

@Service("orderDeliveryDetailFacade")
public class OrderDeliveryDetailFacadeImpl implements OrderDeliveryDetailFacade {

	private OrderDeliveryDetailService orderDeliveryDetailService;
	
	@Autowired
	public void setOrderDeliveryDetailService(OrderDeliveryDetailService orderDeliveryDetailService)
	{
		this.orderDeliveryDetailService = orderDeliveryDetailService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDeliveryDetail getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryDetailService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> list() throws Exception
	{
		return orderDeliveryDetailService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> listByProperty(OrderDeliveryDetail orderDeliveryDetail)
			throws Exception
	{
		return orderDeliveryDetailService.listByProperty(orderDeliveryDetail);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto)
			throws Exception
	{
		return orderDeliveryDetailService.listPaginationByProperty(pagination, orderDeliveryDetailDto);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryDetailService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDeliveryDetailService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailService.deleteByProperty(orderDeliveryDetail);
	}

	/**
	 * 保存记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailService.save(orderDeliveryDetail);
	}

	/**
	 * 更新记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailService.update(orderDeliveryDetail);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailService.findByCount(orderDeliveryDetail);
	}
}
