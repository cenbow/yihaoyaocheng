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
import java.util.Map;

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.UserDto;

public interface OrderDeliveryDetailFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDeliveryDetail getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> listByProperty(OrderDeliveryDetail orderDeliveryDetail)
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
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDeliveryDetail orderDeliveryDetail) throws Exception;

	/**
	 * 保存记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDeliveryDetail orderDeliveryDetail) throws Exception;

	/**
	 * 更新记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDeliveryDetail orderDeliveryDetail) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDeliveryDetail orderDeliveryDetail) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception;

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> confirmReceipt(List<OrderDeliveryDetailDto> list,UserDto user) throws Exception;

	/**
	 * 补货、换货订单发货、収货商品列表
	 * @param pagination
	 * @param orderDeliveryDetailDto
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationOrderDeliveryDetail(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception;

}
