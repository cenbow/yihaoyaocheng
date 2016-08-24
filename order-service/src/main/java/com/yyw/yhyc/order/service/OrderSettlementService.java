/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.service;

import java.util.List;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.dto.OrderSettlementDto;

import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;

@Service("orderSettlementService")
public class OrderSettlementService {

    private OrderSettlementMapper orderSettlementMapper;

    private SystemDateMapper systemDateMapper;

    @Autowired
    private OrderExceptionMapper orderExceptionMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper) {
        this.orderSettlementMapper = orderSettlementMapper;
    }

    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public OrderSettlement getByPK(java.lang.Integer primaryKey) throws Exception {
        return orderSettlementMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     * @throws Exception
     */
    public List<OrderSettlement> list() throws Exception {
        return orderSettlementMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     * @throws Exception
     */
    public List<OrderSettlement> listByProperty(OrderSettlement orderSettlement)
            throws Exception {
        return orderSettlementMapper.listByProperty(orderSettlement);
    }

    /**
     * 根据查询条件查询分页记录
     * @return
     * @throws Exception
     */
    public Pagination<OrderSettlementDto> listPaginationByProperty(Pagination<OrderSettlementDto> pagination, OrderSettlementDto orderSettlementDto) throws Exception {
        List<OrderSettlementDto> list = orderSettlementMapper.listPaginationDtoByProperty(pagination, orderSettlementDto);
        if (!UtilHelper.isEmpty(list)) {
            for (OrderSettlementDto osd : list) {
                if (orderSettlementDto.getType() == 1) {//type = 1 卖家
                    if (osd.getBusinessType() == 1) {
                        osd.setBusinessTypeName("销售货款");
                    } else if (osd.getBusinessType() == 2) {
                        osd.setBusinessTypeName("退款货款");
                    } else{
                        osd.setBusinessTypeName("拒收");
                    }
                } else {// type =2 买家
                    if (osd.getBusinessType() == 1) {
                        osd.setBusinessTypeName("采购货款");
                    } else if (osd.getBusinessType() == 2) {
                        osd.setBusinessTypeName("退款货款");
                    } else{
                        osd.setBusinessTypeName("拒收");
                    }
                }

                if(osd.getConfirmSettlement()!=null&&osd.getConfirmSettlement().equals("1")){
                    osd.setConfirmSettlementName("已结算");
                }else{
                    osd.setConfirmSettlementName("未结算");
                }

            }
        }
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
        return orderSettlementMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception {
        orderSettlementMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param orderSettlement
     * @return
     * @throws Exception
     */
    public int deleteByProperty(OrderSettlement orderSettlement) throws Exception {
        return orderSettlementMapper.deleteByProperty(orderSettlement);
    }

    /**
     * 保存记录
     * @param orderSettlement
     * @return
     * @throws Exception
     */
    public void save(OrderSettlement orderSettlement) throws Exception {
        orderSettlementMapper.save(orderSettlement);
    }

    /**
     * 更新记录
     * @param orderSettlement
     * @return
     * @throws Exception
     */
    public int update(OrderSettlement orderSettlement) throws Exception {
        return orderSettlementMapper.update(orderSettlement);
    }

    /**
     * 根据条件查询记录条数
     * @param orderSettlement
     * @return
     * @throws Exception
     */
    public int findByCount(OrderSettlement orderSettlement) throws Exception {
        return orderSettlementMapper.findByCount(orderSettlement);
    }

    /**
     * 退款结算
     * @param orderSettlement
     */
    public void updateRefundSettlement(OrderSettlement orderSettlement) {
        if (UtilHelper.isEmpty(orderSettlement)
                || UtilHelper.isEmpty(orderSettlement.getRefunSettlementMoney())
                || UtilHelper.isEmpty(orderSettlement.getOrderSettlementId())
                ) {
            throw new RuntimeException("参数错误");
        }

        OrderSettlement os = orderSettlementMapper.getByPK(orderSettlement.getOrderSettlementId());

        if (UtilHelper.isEmpty(os))
            throw new RuntimeException("未找到结算订单");

        if (os.getSupplyId().intValue() != orderSettlement.getSupplyId().intValue())
            throw new RuntimeException("未找到结算订单");

        if(os.getBusinessType() == 1)
            throw new RuntimeException("结算订单类型不正确");

        if(!"0".equals(os.getConfirmSettlement()))
            throw new RuntimeException("订单已结算");


        String nowTime = systemDateMapper.getSystemDate();
        os.setRefunSettlementMoney(orderSettlement.getRefunSettlementMoney());
        os.setUpdateTime(nowTime);
        os.setUpdateUser(orderSettlement.getUpdateUser());
        os.setSettlementTime(nowTime);
        os.setRemark(orderSettlement.getRemark());
        os.setConfirmSettlement("1");

        //修改异常订单里的结算状态
        OrderException orderException=new OrderException();
        orderException.setExceptionOrderId(orderSettlement.getFlowId());
        List<OrderException> lo=orderExceptionMapper.listByProperty(orderException);
        if(!UtilHelper.isEmpty(lo)){
            orderException= lo.get(0);
            if("1".equals(orderException.getReturnType())){
                orderException.setOrderStatus("8");
            }else if("4".equals(orderException.getReturnType())){
                orderException.setOrderStatus("4");
            }
            orderExceptionMapper.update(orderException);
            //拒收 异常订单结算 原订单状态变成部分收货
            if("4".equals(orderException.getReturnType())){
                Order order=orderMapper.getOrderbyFlowId(orderException.getFlowId());
                order.setOrderStatus(SystemOrderStatusEnum.BuyerPartReceived.getType());
                order.setUpdateTime(nowTime);
                orderMapper.update(order);
            }
        }
        int result = orderSettlementMapper.update(os);
        if (result == 0)
            throw new RuntimeException("结算失败");

    }
}