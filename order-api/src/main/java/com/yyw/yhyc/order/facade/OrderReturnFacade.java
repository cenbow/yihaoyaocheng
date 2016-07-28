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
package com.yyw.yhyc.order.facade;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.order.bo.Pagination;

public interface OrderReturnFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderReturn getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> listByProperty(OrderReturn orderReturn)
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
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderReturn orderReturn) throws Exception;

	/**
	 * 保存记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public void save(OrderReturn orderReturn) throws Exception;

	/**
	 * 更新记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int update(OrderReturn orderReturn) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderReturn orderReturn) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderReturn> listPaginationByProperty(Pagination<OrderReturn> pagination, OrderReturn orderReturn) throws Exception;

}
