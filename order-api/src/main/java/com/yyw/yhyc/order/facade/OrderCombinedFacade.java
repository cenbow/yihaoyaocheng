/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.bo.Pagination;

public interface OrderCombinedFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderCombined getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderCombined> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderCombined> listByProperty(OrderCombined orderCombined)
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
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderCombined orderCombined) throws Exception;

	/**
	 * 保存记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public void save(OrderCombined orderCombined) throws Exception;

	/**
	 * 更新记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int update(OrderCombined orderCombined) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderCombined orderCombined) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderCombined> listPaginationByProperty(Pagination<OrderCombined> pagination, OrderCombined orderCombined) throws Exception;

}
