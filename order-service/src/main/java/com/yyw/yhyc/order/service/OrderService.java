/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.enmu.BuyerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SellerOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.helper.UtilHelper;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.dto.OrderDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderMapper;

@Service("orderService")
public class OrderService {

    Log log = LogFactory.getLog(OrderService.class);

    private OrderMapper orderMapper;

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public Order getByPK(java.lang.Integer primaryKey) throws Exception {
        return orderMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     * @throws Exception
     */
    public List<Order> list() throws Exception {
        return orderMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     * @throws Exception
     */
    public List<Order> listByProperty(Order order)
            throws Exception {
        return orderMapper.listByProperty(order);
    }

    /**
     * 根据查询条件查询分页记录
     * @return
     * @throws Exception
     */
    public Pagination<Order> listPaginationByProperty(Pagination<Order> pagination, Order order) throws Exception {
        List<Order> list = orderMapper.listPaginationByProperty(pagination, order);

        pagination.setResultList(list);

        return pagination;
    }

    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public int deleteByPK(java.lang.Integer primaryKey) throws Exception {
        return orderMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception {
        orderMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param order
     * @return
     * @throws Exception
     */
    public int deleteByProperty(Order order) throws Exception {
        return orderMapper.deleteByProperty(order);
    }

    /**
     * 保存记录
     * @param order
     * @return
     * @throws Exception
     */
    public void save(Order order) throws Exception {
        orderMapper.save(order);
    }

    /**
     * 更新记录
     * @param order
     * @return
     * @throws Exception
     */
    public int update(Order order) throws Exception {
        return orderMapper.update(order);
    }

    /**
     * 根据条件查询记录条数
     * @param order
     * @return
     * @throws Exception
     */
    public int findByCount(Order order) throws Exception {
        return orderMapper.findByCount(order);
    }

    /**
     * 创建订单
     * @param orderCreateDto
     * @throws Exception
     */
    public List<OrderDto> createOrder(OrderCreateDto orderCreateDto) throws Exception {

        return null;
    }

    /**
     * 校验要购买的商品(通用方法)
     * @param productInfoDtoList
     * @throws Exception
     */
    public boolean validateProducts(List<ProductInfoDto> productInfoDtoList) {
        //todo
        return false;
    }

    public Map<String, Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto) {
        if(UtilHelper.isEmpty(orderDto))
            throw new RuntimeException("参数错误");
        if(!UtilHelper.isEmpty(orderDto.getCreateEndTime())){
            SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formatter.parse(orderDto.getCreateEndTime());
                GregorianCalendar gc=new GregorianCalendar();
                gc.setTime(date);
                gc.add(5,1);
                orderDto.setCreateEndTime(formatter.format(gc.getTime()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        /*orderDto = new OrderDto();
        orderDto.setCustId(1);
        orderDto.setFlowId("1");
        orderDto.setPayType(1);
        orderDto.setSupplyName("上海");*/
        List<OrderDto> buyerOrderList = orderMapper.listPgBuyerOrder(pagination, orderDto);
        pagination.setResultList(buyerOrderList);

        List<OrderDto> orderDtoList = orderMapper.findOrderStatusCount(orderDto);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        int payType = orderDto.getPayType();//支付方式 1--在线支付  2--账期支付 3--线下支付
        BuyerOrderStatusEnum buyerorderstatusenum;
        if (!UtilHelper.isEmpty(orderDtoList)) {
            for (OrderDto od : orderDtoList) {
                buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),payType);
                if(buyerorderstatusenum != null){
                    if(orderStatusCountMap.containsKey(buyerorderstatusenum.getType())){
                        orderStatusCountMap.put(buyerorderstatusenum.getType(),orderStatusCountMap.get(buyerorderstatusenum.getType())+od.getOrderCount());
                    }else{
                        orderStatusCountMap.put(buyerorderstatusenum.getType(),od.getOrderCount());
                    }
                }
            }
        }

        BigDecimal orderTotalMoney = new BigDecimal(0);
        int orderCount = 0;

        if(!UtilHelper.isEmpty(buyerOrderList)){
            orderCount = buyerOrderList.size();
            for(OrderDto od : buyerOrderList){
                if(!UtilHelper.isEmpty(od.getOrderStatus()) && !UtilHelper.isEmpty(od.getPayType())){
                    buyerorderstatusenum = getBuyerOrderStatus(od.getOrderStatus(),od.getPayType());
                    if(!UtilHelper.isEmpty(buyerorderstatusenum))
                        od.setOrderStatusName(buyerorderstatusenum.getValue());
                    else
                        od.setOrderStatusName("未知类型");
                }
                if(!UtilHelper.isEmpty(od.getOrderTotal()))
                    orderTotalMoney = orderTotalMoney.add(od.getOrderTotal());
            }
        }

        log.debug("orderList====>" + orderDtoList);
        log.debug("buyerOrderList====>" + buyerOrderList);
        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("buyerOrderList", buyerOrderList);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney);
        return resultMap;
    }

    /**
     * 根据订单系统状态和支付类型获取买家视角订单状态
     * @param systemOrderStatus
     * @param payType
     * @return
     */
    BuyerOrderStatusEnum getBuyerOrderStatus(String systemOrderStatus,int payType){
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerOrdered.getType())) {//买家已下单
            if (payType == 2) {
                return BuyerOrderStatusEnum.BackOrder;//待发货
            } else {
                return BuyerOrderStatusEnum.PendingPayment;//待付款
            }
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAlreadyPaid.getType())) {//买家已付款
            return BuyerOrderStatusEnum.BackOrder;//待发货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.SellerDelivered.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerDeferredReceipt.getType())) {//卖家已发货+买家延期收货
            return BuyerOrderStatusEnum.ReceiptOfGoods;//待收货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Rejecting.getType())) {//拒收中
            return BuyerOrderStatusEnum.Rejecting;//拒收中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Replenishing.getType())) {//补货中
            return BuyerOrderStatusEnum.Replenishing;//补货中
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())) {//买家已取消+系统自动取消+后台取消
            return BuyerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType())) {// 买家全部收货+系统自动确认收货
            return BuyerOrderStatusEnum.Finished;//补货中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return BuyerOrderStatusEnum.PaidException;//打款异常
        }
        return null;
    }

    /**
     * 根据订单系统状态和支付类型获取买家视角订单状态
     * @param systemOrderStatus
     * @param payType
     * @return
     */
    SellerOrderStatusEnum getSellerOrderStatus(String systemOrderStatus, int payType){
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerOrdered.getType())) {//买家已下单
            if (payType == 2) {
                return SellerOrderStatusEnum.BackOrder;//待发货
            } else {
                return SellerOrderStatusEnum.PendingPayment;//待付款
            }
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAlreadyPaid.getType())) {//买家已付款
            return SellerOrderStatusEnum.BackOrder;//待发货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.SellerDelivered.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BuyerDeferredReceipt.getType())) {//卖家已发货+买家延期收货
            return SellerOrderStatusEnum.ReceiptOfGoods;//待收货
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Rejecting.getType())) {//拒收中
            return SellerOrderStatusEnum.Rejecting;//拒收中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.Replenishing.getType())) {//补货中
            return SellerOrderStatusEnum.Replenishing;//补货中
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoCanceled.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.BackgroundCancellation.getType())) {//买家已取消+系统自动取消+后台取消
            return SellerOrderStatusEnum.Canceled;//已取消
        }

        if (systemOrderStatus.equals(SystemOrderStatusEnum.BuyerAllReceived.getType()) || systemOrderStatus.equals(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType())) {// 买家全部收货+系统自动确认收货
            return SellerOrderStatusEnum.Finished;//补货中
        }
        if (systemOrderStatus.equals(SystemOrderStatusEnum.PaidException.getType())) {//打款异常
            return SellerOrderStatusEnum.PaidException;//打款异常
        }
        return null;
    }

}