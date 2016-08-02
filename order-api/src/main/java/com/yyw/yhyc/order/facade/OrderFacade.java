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

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.dto.OrderDto;

public interface OrderFacade {

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public Order getByPK(java.lang.Integer primaryKey) throws Exception;

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> list() throws Exception;

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> listByProperty(Order order)
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
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(Order order) throws Exception;

	/**
	 * 保存记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void save(Order order) throws Exception;

	/**
	 * 更新记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int update(Order order) throws Exception;

	/**
	 * 根据条件查询记录条数
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int findByCount(Order order) throws Exception;
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<Order> listPaginationByProperty(Pagination<Order> pagination, Order order) throws Exception;

	/**
	 * 创建订单
	 * @param orderCreateDto
	 * @throws Exception
	 */
	public List<Order> createOrder(OrderCreateDto orderCreateDto)throws Exception;

	/**
	 * 校验要购买的商品(通用方法)
	 * @param orderDto
	 * @throws Exception
	 */
	public boolean validateProducts(OrderDto orderDto)throws Exception;


	/**
	 * 采购商订单查询
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public Map<String,Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto);


	/**
	 * 根据订单号查询订单详情
	 *
	 * @param order
	 * @throws Exception
	 */
	public OrderDetailsDto getOrderDetails(Order order) throws Exception;

	/**
	 * 采购商取消订单
	 * @param custId
	 * @param orderId
     */
	public void  buyerCancelOrder(Integer custId,Integer orderId);


	/**
	 * 销售订单查询
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public Map<String,Object> listPgSellerOrder(Pagination<OrderDto> pagination, OrderDto orderDto);

	/**
	 * 卖家取消订单
	 * @param custId
	 * @param orderId
	 */
	public void  sellerCancelOrder(Integer custId,Integer orderId,String buyerCancelOrder);

	public Map<String,Object> checkOrderPage() throws Exception;

	/**
	 * 导出销售订单
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public byte[] exportOrder(Pagination<OrderDto> pagination, OrderDto orderDto);
	public OrderCreateDto checkOrderPage();

	/**
	 * 系统自动取消订单
	 * 1在线支付订单24小时系统自动取消
	 * 2 线下支付7天后未确认收款系统自动取消
	 * @return
	 */
	public void cancelOrderForNoPay();

	/**
	 * 系统自动取消订单
	 * 1在线支付订单7个自然日未发货系统自动取消
	 * @return
	 */
	public void cancelOrderFor7DayNoDelivery();

	/**
	 * 系统自动确认收货
	 * 订单发货后7个自然日后系统自动确认收货
	 * @return
	 */
	public void doneOrderForDeliveryAfter7Day();
}
