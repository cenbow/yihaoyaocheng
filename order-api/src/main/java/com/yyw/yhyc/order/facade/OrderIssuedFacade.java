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
package com.yyw.yhyc.order.facade;

import java.util.List;
import java.util.Map;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.bo.OrderIssued;

public interface OrderIssuedFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssued getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssued> listByProperty(OrderIssued orderIssued)
			throws Exception;

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception;

	/**
	 * 根据传入参数删除记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssued orderIssued) throws Exception;

	/**
	 * 保存记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssued orderIssued) throws Exception;

	/**
	 * 更新记录
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssued orderIssued) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderIssued
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssued orderIssued) throws Exception;

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssued> listPaginationByProperty(Pagination<OrderIssued> pagination, OrderIssued orderIssued) throws Exception;

	/**
	 *
	 * @param supplyId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findOrderIssuedListBySupplyId(Integer supplyId , String payType) throws Exception;
	
	
	
	
	/**
	 * 根据订单号集合以及提供商的id和日期查询订单
	 * @param supplyId
	 * @param startDate
	 * @param endDate
	 * @param orderIdList
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> findOrderIssuedListBySupplyAndOrderDate(List<Integer> supplyListIds,String startDate,String endDate,String orderIdList)throws Exception;
	
	
	/**
	 * 获取下发失败列表集合，更改状态为失败，准备重发
	 * @param flowList
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> updateOrderIssuedStatus(List<String> flowList) throws Exception;
}
