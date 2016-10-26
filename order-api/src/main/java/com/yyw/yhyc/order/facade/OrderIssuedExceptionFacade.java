/**
 * Created By: XI
 * Created On: 2016-10-24 15:52:00
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
import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;

public interface OrderIssuedExceptionFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderIssuedException getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderIssuedException> listByProperty(OrderIssuedException orderIssuedException)
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
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderIssuedException orderIssuedException) throws Exception;

	/**
	 * 保存记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderIssuedException orderIssuedException) throws Exception;

	/**
	 * 更新记录
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderIssuedException orderIssuedException) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderIssuedException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderIssuedException orderIssuedException) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedException> listPaginationByProperty(Pagination<OrderIssuedException> pagination, OrderIssuedException orderIssuedException) throws Exception;
 
	/**
	 * 订单下发接口，调用此接口更改下发表中issued_count为0
	 * @param flowId
	 * @return
	 */
	public Map<String,String> orderIssued(String flowId,String operator);
	/**
	 * 订单标记接口，会出现实际下发成功，超时返回或相关问题，或顾客通过其他方式将订单录入到其ERP系统。此处增加一个手工标记成功的机制，标记后则状态变为已完成。仅针对非关联问题的异常订单。
	 * @param flowId
	 * @return
	 */
	public Map<String,String> orderMark(String flowId,String operator);

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderIssuedExceptionDto> listPaginationByPropertyEx(Pagination<OrderIssuedExceptionDto> pagination, OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception;

}
