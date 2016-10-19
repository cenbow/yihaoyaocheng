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
package com.yyw.yhyc.order.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderExceptionDto;

public interface OrderExceptionMapper extends GenericIBatisMapper<OrderException, Integer> {

	public List<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException);

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto);


	/**
	 * 采购商拒收订单查询
	 * @param pagination
	 * @param orderExceptionDto
     * @return
     */
	public List<OrderExceptionDto> listPaginationBuyerRejectOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单总金额
	 * @param orderExceptionDto
	 * @return
     */
	public java.math.BigDecimal findBuyerExceptionOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单状态列表
	 * @param orderExceptionDto
	 * @return
     */
	public List<OrderExceptionDto> findBuyerRejectOrderStatusCount(OrderExceptionDto orderExceptionDto);

	public List<OrderExceptionDto> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto OrderExceptionDto);


	/**
	 * 查询采购商拒收订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findSellerExceptionOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public Integer findSellerRejectOrderStatusCount(OrderExceptionDto orderExceptionDto);

	/**
	 * 采购商换货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPgBuyerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商换货订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findBuyerChangeGoodsExceptionOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商换货订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findBuyerChangeGoodsOrderStatusCount(OrderExceptionDto orderExceptionDto);



	/**
	 * 采购商拒收订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPaginationBuyerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findBuyerReplenishmentOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findBuyerReplenishmentStatusCount(OrderExceptionDto orderExceptionDto);


	/**
	 * 供应商补货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPaginationSellerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询供应商补货订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findSellerReplenishmentOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询供应商补货订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findSellerReplenishmentStatusCount(OrderExceptionDto orderExceptionDto);

	/**
	 * 修改状态
	 * @param orderException
	 * @return
     */
	int updateOrderStatus(OrderException orderException);

	/**
	 * 采购商换货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPgSellerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商换货订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findSellerChangeGoodsExceptionOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商换货订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findSellerChangeGoodsOrderStatusCount(OrderExceptionDto orderExceptionDto);

	/**
	 * 根据异常订单编码查询异常订单
	 * @param exceptionOrderId
	 * @return
	 */
	public OrderException getByExceptionOrderId(String exceptionOrderId);


	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getOrderExceptionDetailsForReview(OrderExceptionDto orderExceptionDto);

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getOrderExceptionDetailsForReturn(OrderExceptionDto orderExceptionDto);


	/**
	 * 采购商退货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPaginationBuyerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商退货订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findBuyerRefundOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商退货订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findBuyerRefundOrderStatusCount(OrderExceptionDto orderExceptionDto);

	/**
	 * 补货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getReplenishmentDetails(OrderExceptionDto orderExceptionDto);


	/**
	 * 供应商退货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPaginationSellerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询供应商退货订单总金额
	 * @param orderExceptionDto
	 * @return
	 */
	public java.math.BigDecimal findSellerRefundOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商退货订单状态列表
	 * @param orderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> findSellerRefundOrderStatusCount(OrderExceptionDto orderExceptionDto);

	/**
	 * 换货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getChangeGoodsOrderDetails(OrderExceptionDto orderExceptionDto);
	/**
	 * 根据订单编码，获取退货和拒收订单金额历史总额
	 * @param flowId
	 * @return
	 */
	public BigDecimal getConfirmHistoryExceptionMoney(String flowId);

	/**
	 * 退货自动确认收货订单查询
	 * @param orderException
	 * @return
	 */
	public List<OrderException> listNodeliveryForReturn(OrderException orderException);

	/**
	 * 补货自动确认收货订单查询
	 * @param orderException
	 * @return
	 */
	public List<OrderException> listNodeliveryForReplenishment(OrderException orderException);


	/**
	 * 换货自动确认收货订单查询
	 * @param orderException
	 * @return
	 */
	public List<OrderException> listNodeliveryForChange(OrderException orderException);


	/**
	 * 获取买家异常订单列表
	 * @param orderExceptionDto
	 * @return
     */
	public List<OrderExceptionDto> listPaginationBuyerExceptionOrder(Pagination<OrderExceptionDto> pagination,OrderExceptionDto orderExceptionDto);

	/**
	 * 获取买家拒收/补货数量
	 * @param custId
	 * @return
	 */
	public int findExceptionCountApp(Integer custId);

	/**
	 * 后台异常订单列表
	 * @param pagination
	 * @param OrderExceptionDto
	 * @return
	 */
	public List<OrderExceptionDto> listPaginationOrderException(Pagination<OrderExceptionDto> pagination, OrderExceptionDto OrderExceptionDto);


}
