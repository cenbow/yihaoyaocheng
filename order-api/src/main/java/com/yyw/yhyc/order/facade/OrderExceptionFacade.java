/**
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
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
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.dto.OrderExceptionDto;

public interface OrderExceptionFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderException getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> listByProperty(OrderException orderException)
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
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderException orderException) throws Exception;

	/**
	 * 保存记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderException orderException) throws Exception;

	/**
	 * 更新记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderException orderException) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderException orderException) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException) throws Exception;

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto) throws Exception;

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) throws Exception;

	/**
	 * 后台异常订单列表
	 * @param pagination
	 * @param OrderExceptionDto
	 * @return
	 */
	public Pagination<OrderExceptionDto> listPaginationOrderException(Pagination<OrderExceptionDto> pagination, OrderExceptionDto OrderExceptionDto) throws Exception;

	/**
	 * 后台拒收订单详情
	 *
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getRejectionOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception;

	/**
	 * 后台补货订单详情
	 *
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getReplenishmentOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception;

	/**
	 * 后台退货订单详情
	 *
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getRefundOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception;

	/**
	 * 后台换货订单详情
	 *
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getExchangeOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception;


}
