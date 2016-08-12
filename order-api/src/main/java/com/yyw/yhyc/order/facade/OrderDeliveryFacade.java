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
import java.util.Map;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;

import javax.servlet.http.HttpServletRequest;

public interface OrderDeliveryFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDelivery getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> listByProperty(OrderDelivery orderDelivery)
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
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDelivery orderDelivery) throws Exception;

	/**
	 * 保存记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDelivery orderDelivery) throws Exception;

	/**
	 * 更新记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDelivery orderDelivery) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDelivery orderDelivery) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDelivery> listPaginationByProperty(Pagination<OrderDelivery> pagination, OrderDelivery orderDelivery) throws Exception;

	/**
	 * 确认发货
	 * @param orderDeliveryDto
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> sendOrderDelivery(OrderDeliveryDto orderDeliveryDto) throws Exception;

	/**
	 * 更具登录用户获取地址列表
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<UsermanageReceiverAddress> getReceiveAddressList(UserDto user);
}
