/**
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.order.appdto.OrderBean;
import com.yyw.yhyc.order.appdto.OrderProductBean;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.OrderExceptionDto;

import com.yyw.yhyc.order.dto.OrderReturnDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.utils.DateUtils;
import com.yyw.yhyc.utils.MyConfigUtil;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hpsf.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.helper.UtilHelper;

@Service("orderExceptionService")
public class OrderExceptionService {

    private Log log = LogFactory.getLog(OrderExceptionService.class);

    private OrderExceptionMapper orderExceptionMapper;
    private OrderSettlementMapper orderSettlementMapper;
    private SystemDateMapper systemDateMapper;
    private OrderMapper orderMapper;
    private OrderTraceMapper orderTraceMapper;
    private OrderDeliveryDetailMapper orderDeliveryDetailMapper;
    private SystemPayTypeMapper systemPayTypeMapper;
    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;
    @Autowired
    private SystemPayTypeService systemPayTypeService;

    @Autowired
    public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
        this.orderExceptionMapper = orderExceptionMapper;
    }

    @Autowired
    public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper) {
        this.orderSettlementMapper = orderSettlementMapper;
    }

    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
        this.orderTraceMapper = orderTraceMapper;
    }

    @Autowired
    public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper) {
        this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
    }

    @Autowired
    public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
        this.systemPayTypeMapper = systemPayTypeMapper;
    }

    @Autowired
    private OrderSettlementService orderSettlementService;


    /**
     * 通过主键查询实体对象
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public OrderException getByPK(Integer primaryKey) throws Exception {
        return orderExceptionMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<OrderException> list() throws Exception {
        return orderExceptionMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<OrderException> listByProperty(OrderException orderException)
            throws Exception {
        return orderExceptionMapper.listByProperty(orderException);
    }

    /**
     * 根据查询条件查询分页记录
     *
     * @return
     * @throws Exception
     */
    public Pagination<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException) throws Exception {
        List<OrderException> list = orderExceptionMapper.listPaginationByProperty(pagination, orderException);

        pagination.setResultList(list);

        return pagination;
    }

    /**
     * 根据主键删除记录
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public int deleteByPK(Integer primaryKey) throws Exception {
        return orderExceptionMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     *
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<Integer> primaryKeys) throws Exception {
        orderExceptionMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     *
     * @param orderException
     * @return
     * @throws Exception
     */
    public int deleteByProperty(OrderException orderException) throws Exception {
        return orderExceptionMapper.deleteByProperty(orderException);
    }

    /**
     * 保存记录
     *
     * @param orderException
     * @return
     * @throws Exception
     */
    public void save(OrderException orderException) throws Exception {
        orderExceptionMapper.save(orderException);
    }

    /**
     * 更新记录
     *
     * @param orderException
     * @return
     * @throws Exception
     */
    public int update(OrderException orderException) throws Exception {
        return orderExceptionMapper.update(orderException);
    }

    /**
     * 根据条件查询记录条数
     *
     * @param orderException
     * @return
     * @throws Exception
     */
    public int findByCount(OrderException orderException) throws Exception {
        return orderExceptionMapper.findByCount(orderException);
    }

    /**
     * 退货订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        Integer userType = orderExceptionDto.getUserType();
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
        /* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay())) continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }

		/* 获得拒收订单的状态名 */
        if (userType == 1) {
            BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType());
            orderExceptionDto.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
        } else if (userType == 2) {
            SellerOrderExceptionStatusEnum sellerOrderExceptionStatus = getSellerOrderExceptionStatus(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType());
            orderExceptionDto.setOrderStatusName(sellerOrderExceptionStatus.getValue());
        }
        return orderExceptionDto;
    }

    /**
     * 拒收订单卖家审核通过生成结算记录
     *
     * @param custId
     * @param orderException
     * @throws Exception
     */
    private void saveRefuseOrderSettlement(Integer custId, OrderException orderException) throws Exception {
        Order order = orderMapper.getOrderbyFlowId(orderException.getFlowId());
        if (UtilHelper.isEmpty(order) || !custId.equals(order.getSupplyId())) {
            throw new RuntimeException("未找到订单");
        }
        String now = systemDateMapper.getSystemDate();
        SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
        //当为账期支付时
        /*if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
			//结算订单金额=原始订单金额-拒收订单金额
			OrderSettlement byFlowid=new OrderSettlement();
			byFlowid.setFlowId(order.getFlowId());
			List<OrderSettlement> ls=orderSettlementMapper.listByProperty(byFlowid);
			if(ls.size()>0){
				OrderSettlement settlement=ls.get(0);
				settlement.setSettlementMoney(settlement.getSettlementMoney().subtract(orderException.getOrderMoney()));
				settlement.setUpdateTime(now);
				orderSettlementMapper.update(settlement);
			}
			return;
		}*/

        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setBusinessType(3);
        orderSettlement.setOrderId(orderException.getExceptionId());
        orderSettlement.setFlowId(orderException.getExceptionOrderId());
        orderSettlement.setCustId(orderException.getCustId());
        orderSettlement.setCustName(orderException.getCustName());
        orderSettlement.setSupplyName(orderException.getSupplyName());
        orderSettlement.setConfirmSettlement("0");//生成结算信息时都是未结算
        orderSettlement.setPayTypeId(order.getPayTypeId());
        orderSettlement.setSettlementTime(now);
        orderSettlement.setCreateUser(orderException.getCustName());
        orderSettlement.setCreateTime(now);
        orderSettlement.setOrderTime(order.getCreateTime());
        orderSettlement.setSettlementMoney(orderException.getOrderMoney());
        orderSettlement.setRefunSettlementMoney(orderException.getOrderMoney());
        log.info("account-yes:systemPayType.getPayType():" + systemPayType.getPayType());
        if (OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId()) || OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())) {
            //如银联支付 只有买家看到
            orderSettlement.setCustId(orderException.getCustId());
            orderSettlement.setConfirmSettlement("1");//生成结算信息 已结算
        } else if (OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId()) || OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())) {
            //支付宝 只有买家看到
            orderSettlement.setCustId(orderException.getCustId());
            orderSettlement.setConfirmSettlement("1");//生成结算信息 已结算
        }
        else if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
            //账期支付
            log.info("account-yescreate:systemPayType.getPayType():" + systemPayType.getPayType());
            orderSettlement.setBusinessType(1);
            orderSettlement.setCustId(orderException.getCustId());
            orderSettlement.setSupplyId(orderException.getSupplyId());
            orderSettlement.setSettlementMoney(order.getOrderTotal().subtract(orderException.getOrderMoney()));
        } else if (SystemPayTypeEnum.PayOffline.getPayType().equals(systemPayType.getPayType())) {
            //线下支付
            orderSettlement.setCustId(orderException.getCustId());
            orderSettlement.setSupplyId(orderException.getSupplyId());
        } else {
            //招行支付 等 其他支付
            log.info("account-yescreate-other:systemPayType.getPayType():" + systemPayType.getPayType());
            orderSettlement.setBusinessType(1);
            orderSettlement.setCustId(orderException.getCustId());
            orderSettlement.setSupplyId(orderException.getSupplyId());
            orderSettlement.setSettlementMoney(order.getOrderTotal().subtract(orderException.getOrderMoney()));
        }
        //加上省市区
        log.info("account-yes-create-done:systemPayType.getPayType():" + systemPayType.getPayType());
        orderSettlementService.parseSettlementProvince(orderSettlement, orderException.getCustId() + "");
        orderSettlementMapper.save(orderSettlement);
        //银联支付生成一条订单金额为原订单金额-拒收退款金额的结算信息
        if ((OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId()) ||
                OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())||
                OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId()) ||
                OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId()))
                && orderException.getOrderMoney() != null) {
            orderSettlement.setOrderSettlementId(null);
            orderSettlement.setBusinessType(1);
            orderSettlement.setFlowId(order.getFlowId());//支付宝方式加
            orderSettlement.setCustId(null);
            orderSettlement.setSupplyId(order.getSupplyId());
            orderSettlement.setSettlementMoney(order.getOrderTotal().subtract(orderException.getOrderMoney()));
            orderSettlementMapper.save(orderSettlement);
        }
    }

    /**
     * 采购商拒收订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgBuyerRejectOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }

        }

        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerExceptionOrderTotal(orderExceptionDto);

        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerRejectOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);


        BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (!UtilHelper.isEmpty(buyerOrderExceptionStatusEnum))
                    oed.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }

        pagination.setResultList(orderExceptionDtoList);

        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerRejectOrderStatusCount(orderExceptionDto);
        ;

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //卖家视角订单状态
                buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (buyerOrderExceptionStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(buyerOrderExceptionStatusEnum.getType())) {
                        orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), orderStatusCountMap.get(buyerOrderExceptionStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);

        resultMap.put("rejectOrderStatusCount", orderStatusCountMap);
        resultMap.put("rejectOrderList", pagination);
        resultMap.put("rejectOrderCount", orderCount);
        resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    /**
     * 获取采购商视角异常订单状态
     *
     * @param systemExceptionOrderStatus
     * @param payType
     * @return
     */
    BuyerOrderExceptionStatusEnum getBuyerOrderExceptionStatus(String systemExceptionOrderStatus, int payType) {
        BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum = null;
        if (SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(systemExceptionOrderStatus)) {//拒收申请中
            buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.WaitingConfirmation;//待确认
        }
        if (SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(systemExceptionOrderStatus)) {//卖家已确认
            if (payType == 1 || payType == 2) {//在线支付+账期支付
                buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunded;//已完成
            }
            if (payType == 3) {//线下支付
                buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunding;//退款中
            }
        }
        if (SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(systemExceptionOrderStatus)) {//卖家已关闭
            buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Closed;//已关闭
        }
        if (SystemOrderExceptionStatusEnum.Refunded.getType().equals(systemExceptionOrderStatus) && payType == 3) {//已退款+线下支付
            buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunded;//已关闭
        }
        return buyerOrderExceptionStatusEnum;
    }

    /**
     * 获取卖家视角异常订单状态
     *
     * @param systemExceptionOrderStatus
     * @param payType
     * @return
     */
    SellerOrderExceptionStatusEnum getSellerOrderExceptionStatus(String systemExceptionOrderStatus, int payType) {
        SellerOrderExceptionStatusEnum sellerOrderExceptionStatusEnum = null;
        if (SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(systemExceptionOrderStatus)) {//拒收申请中
            sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.WaitingConfirmation;//待确认
        }
        if (SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(systemExceptionOrderStatus)) {//卖家已确认
            if (payType == 1 || payType == 2) {//在线支付+账期支付
                sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunded;//已完成
            }
            if (payType == 3) {//线下支付
                sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunding;//退款中
            }
        }
        if (SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(systemExceptionOrderStatus)) {//卖家已关闭
            sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Closed;//已关闭
        }
        if (SystemOrderExceptionStatusEnum.Refunded.getType().equals(systemExceptionOrderStatus) && payType == 3) {//已退款+线下支付
            sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunded;//已关闭
        }
        return sellerOrderExceptionStatusEnum;
    }


    public Map<String, Object> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) throws Exception {

        List<OrderExceptionDto> list = orderExceptionMapper.listPaginationSellerByProperty(pagination, orderExceptionDto);
        Integer sourceType = orderExceptionDto.getType();
        //查询总价
        BigDecimal totalMoney = orderExceptionMapper.findSellerExceptionOrderTotal(orderExceptionDto);
        //查询
        Integer waitCount = pagination.getTotal();
        Integer refundCount = pagination.getTotal();
        orderExceptionDto.setType(2);
        if (sourceType != 2) {
            waitCount = orderExceptionMapper.findSellerRejectOrderStatusCount(orderExceptionDto);
        }
        orderExceptionDto.setType(3);
        if (sourceType != 3) {
            refundCount = orderExceptionMapper.findSellerRejectOrderStatusCount(orderExceptionDto);
        }
        orderExceptionDto.setWaitingConfirmCount(waitCount);
        orderExceptionDto.setRefundingCount(refundCount);
        orderExceptionDto.setOrderMoneyTotal(totalMoney);
        orderExceptionDto.setType(sourceType);
        if (!UtilHelper.isEmpty(list)) { //查全部的时候转换
            for (OrderExceptionDto oed : list) {
                switch (sourceType) {
                    case 1:
                        SellerOrderExceptionStatusEnum statusEnum = getSellerOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                        oed.setOrderStatusName(statusEnum.getValue());
                        break;
                    case 2:
                        oed.setOrderStatusName(SellerOrderExceptionStatusEnum.WaitingConfirmation.getValue());
                        break;
                    case 3:
                        oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Refunding.getValue());
                        break;
                    case 4:
                        oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Refunded.getValue());
                        break;
                    case 5:
                        oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Closed.getValue());
                        break;
                    default:
                        oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Closed.getValue());
                        break;
                }
            }
        }
        pagination.setResultList(list);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagination", pagination);
        map.put("orderExceptionDto", orderExceptionDto);
        return map;
    }


    /**
     * 审核拒收、退货订单详情（异常订单详情）
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getRejectOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReview(orderExceptionDto);
        if (!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto)) continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
            if (!"1".equals(orderExceptionDto.getReturnType()))
                orderExceptionDto.setOrderStatusName(getSellerOrderExceptionStatus(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType()).getValue());
            else
                orderExceptionDto.setOrderStatusName(getSellerRefundOrderStatusEnum(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType()).getValue());
        }
        return orderExceptionDto;
    }

    /**
     * 退货订单详情（异常订单详情）
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getReturnOrderDetails(OrderExceptionDto orderExceptionDto, Integer type) throws Exception {
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReturn(orderExceptionDto);
        if (!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay())) continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
            if (type == 1) { //买家视角
                orderExceptionDto.setOrderStatusName(getBuyerRefundOrderStatusEnum(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType()).getValue());
            } else if (type == 2) {//卖家视角
                orderExceptionDto.setOrderStatusName(getSellerRefundOrderStatusEnum(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType()).getValue());
            }
        }
        return orderExceptionDto;
    }

    /**
     * 审核换货订单详情（异常订单详情）
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getChangeOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReview(orderExceptionDto);
        if (!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto)) continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
            orderExceptionDto.setOrderStatusName(getSellerChangeGoodsOrderExceptionStatus(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType()).getValue());
        }
        return orderExceptionDto;
    }

    /**
     * 卖家审核拒收订单
     *
     * @param userDto
     * @param orderException
     */
    public void modifyReviewRejectOrderStatus(UserDto userDto, OrderException orderException) throws Exception {
        if (UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
            throw new RuntimeException("参数异常");

        // 验证审核状态
        if (!(SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(orderException.getOrderStatus()) || SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())))
            throw new RuntimeException("参数异常");

        OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
        if (UtilHelper.isEmpty(oe))
            throw new RuntimeException("未找到拒收订单");
        if (userDto.getCustId() != oe.getSupplyId()) {
            log.info("拒收订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("未找到拒收订单");
        }
        //判断是否是拒收订单
        if (!"4".equals(oe.getReturnType())) {
            log.info("拒收订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("该订单不是拒收订单");
        }
        if (!SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(oe.getOrderStatus())) {
            log.info("拒收订单状态不正确,OrderException:" + oe);
            throw new RuntimeException("拒收订单状态不正确");
        }
        String now = systemDateMapper.getSystemDate();
        oe.setRemark(orderException.getRemark());
        oe.setOrderStatus(orderException.getOrderStatus());
        oe.setUpdateUser(userDto.getUserName());
        oe.setUpdateTime(now);
        oe.setReviewTime(now);
        int count = orderExceptionMapper.update(oe);
        if (count == 0) {
            log.error("拒收订单审核失败,OrderException info :" + oe);
            throw new RuntimeException("拒收订单审核失败");
        }

        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setOrderId(oe.getExceptionId());
        orderTrace.setNodeName(SystemOrderExceptionStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
        orderTrace.setDealStaff(userDto.getUserName());
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userDto.getUserName());
        orderTrace.setOrderStatus(oe.getOrderStatus());
        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userDto.getUserName());
        orderTraceMapper.save(orderTrace);

        //卖家审核通过 则原订单部分确认收货 不能过则全部确认收货
        Order order;
        if (SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(oe.getOrderStatus())) {
            order = orderMapper.getOrderbyFlowId(oe.getFlowId());
            order.setOrderStatus(SystemOrderStatusEnum.BuyerPartReceived.getType());
            order.setUpdateTime(now);
            order.setReceiveTime(now);
            order.setUpdateUser(userDto.getUserName());
            count = orderMapper.update(order);
        } else {
            order = orderMapper.getOrderbyFlowId(oe.getFlowId());
            order.setOrderStatus(SystemOrderStatusEnum.BuyerAllReceived.getType());
            order.setUpdateTime(now);
            order.setReceiveTime(now);
            order.setUpdateUser(userDto.getUserName());
            count = orderMapper.update(order);
        }

        SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
        if (systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())) {
            PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
            payService.handleRefund(userDto, 2, oe.getExceptionOrderId(), "卖家审核通过拒收订单");
        }

        if (count == 0) {
            log.error("原始订单更新失败,order info :" + order);
            throw new RuntimeException("原始订单更新失败");
        }
        //拒收订单卖家审核通过生成结算记录
        log.info("account:systemPayType.getPayType():" + systemPayType.getPayType());
        log.info("account:SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType()):" + SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType()));
        if (SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(orderException.getOrderStatus())) {
            this.saveRefuseOrderSettlement(userDto.getCustId(), oe);
        } else if (OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId())
                || OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())
                || OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId())
                || OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())) {
            //银联支付 拒收审核未通过，生成卖家结算信息，金额为全部订单金额
            OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(2, null, null, userDto.getUserName(), null, order);
            //默认 为已结算
            orderSettlement.setConfirmSettlement("1");
            orderSettlementMapper.save(orderSettlement);
        } else if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
            //账期支付
            OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(6, null, null, userDto.getUserName(), null, order);
            orderSettlementMapper.save(orderSettlement);
            log.info("account:create settlement账期审核不通过该生成结算");
        }

    }

    /**
     * 卖家审核换货订单
     *
     * @param userDto
     * @param orderException
     */
    public void updateSellerReviewChangeOrder(UserDto userDto, OrderException orderException) {
        if (UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
            throw new RuntimeException("订单是参数异常");

        // 验证审核状态
        if (!(SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(orderException.getOrderStatus())) &&
                !(SystemChangeGoodsOrderStatusEnum.Closed.getType().equals(orderException.getOrderStatus()))) {
            log.info("状态异常参数:" + orderException.getOrderStatus());
            throw new RuntimeException("参数异常");
        }


        OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
        if (UtilHelper.isEmpty(oe))
            throw new RuntimeException("未找到换货订单");
        if (userDto.getCustId() != oe.getSupplyId()) {
            log.info("换货订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("未找到换货订单");
        }
        //判断是否是拒收订单
        if (!"2".equals(oe.getReturnType())) {
            log.info("换货订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("该订单不是换货订单");
        }
        if (!SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(oe.getOrderStatus())) {
            log.info("换货订单状态不正确,OrderException:" + oe);
            throw new RuntimeException("换货订单状态不正确");
        }
        String now = systemDateMapper.getSystemDate();
        oe.setRemark(orderException.getRemark());
        oe.setOrderStatus(orderException.getOrderStatus());
        oe.setUpdateUser(userDto.getUserName());
        oe.setUpdateTime(now);
        oe.setReviewTime(now);
        int count = orderExceptionMapper.update(oe);
        if (count == 0) {
            log.error("OrderException info :" + oe);
            throw new RuntimeException("换货订单审核失败");
        }
        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setOrderId(oe.getExceptionId());
        orderTrace.setNodeName(SystemChangeGoodsOrderStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
        orderTrace.setDealStaff(userDto.getUserName());
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userDto.getUserName());
        orderTrace.setOrderStatus(oe.getOrderStatus());
        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userDto.getUserName());
        orderTraceMapper.save(orderTrace);

    }


    /**
     * 采购商换货订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgBuyerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }
        }
        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerChangeGoodsExceptionOrderTotal(orderExceptionDto);
        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPgBuyerChangeGoodsOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);

        BuyerChangeGoodsOrderStatusEnum buyerOrderExceptionStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                buyerOrderExceptionStatusEnum = getBuyerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (!UtilHelper.isEmpty(buyerOrderExceptionStatusEnum))
                    oed.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }
        pagination.setResultList(orderExceptionDtoList);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerChangeGoodsOrderStatusCount(orderExceptionDto);
        ;

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //买家视角订单状态数量
                buyerOrderExceptionStatusEnum = getBuyerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (buyerOrderExceptionStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(buyerOrderExceptionStatusEnum.getType())) {
                        orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), orderStatusCountMap.get(buyerOrderExceptionStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);
        resultMap.put("rejectOrderStatusCount", orderStatusCountMap);
        resultMap.put("rejectOrderList", pagination);
        resultMap.put("rejectOrderCount", orderCount);
        resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    /**
     * 获取采购商视角换货异常订单状态
     *
     * @param systemExceptionOrderStatus
     * @param payType
     * @return
     */
    BuyerChangeGoodsOrderStatusEnum getBuyerChangeGoodsOrderExceptionStatus(String systemExceptionOrderStatus, int payType) {
        BuyerChangeGoodsOrderStatusEnum buyerOrderExceptionStatusEnum = null;
        if (SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(systemExceptionOrderStatus)) {//申请中
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingConfirmation;//待确认
        }
        if (SystemChangeGoodsOrderStatusEnum.Canceled.getType().equals(systemExceptionOrderStatus)) {//已取消
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Canceled;
        }
        if (SystemChangeGoodsOrderStatusEnum.Closed.getType().equals(systemExceptionOrderStatus)) {//卖家已关闭
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Closed;//已关闭
        }
        if (SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(systemExceptionOrderStatus)) {//待买家发货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingBuyerDelivered;//待买家发货
        }
        if (SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType().equals(systemExceptionOrderStatus)) {//买家已发货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingSellerReceived;//
        }

        if (SystemChangeGoodsOrderStatusEnum.WaitingSellerDelivered.getType().equals(systemExceptionOrderStatus)) {//卖家已收货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingSellerDelivered;//
        }

        if (SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType().equals(systemExceptionOrderStatus)) {//卖家已发货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingBuyerReceived;//
        }

        if (SystemChangeGoodsOrderStatusEnum.Finished.getType().equals(systemExceptionOrderStatus)) {//买家已收货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Finished;//
        }

        if (SystemChangeGoodsOrderStatusEnum.AutoFinished.getType().equals(systemExceptionOrderStatus)) {//买家自动收货
            buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Finished;//
        }

        return buyerOrderExceptionStatusEnum;
    }


    /**
     * 采购商补货订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgBuyerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }

        }

        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerReplenishmentOrderTotal(orderExceptionDto);

        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerReplenishmentOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);


        BuyerReplenishmentOrderStatusEnum buyerReplenishmentOrderStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                buyerReplenishmentOrderStatusEnum = getBuyerReplenishmentOrderStatus(oed.getOrderStatus());
                if (!UtilHelper.isEmpty(buyerReplenishmentOrderStatusEnum))
                    oed.setOrderStatusName(buyerReplenishmentOrderStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }

        pagination.setResultList(orderExceptionDtoList);

        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerReplenishmentStatusCount(orderExceptionDto);
        ;

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //卖家视角订单状态
                buyerReplenishmentOrderStatusEnum = getBuyerReplenishmentOrderStatus(oed.getOrderStatus());
                if (buyerReplenishmentOrderStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(buyerReplenishmentOrderStatusEnum.getType())) {
                        orderStatusCountMap.put(buyerReplenishmentOrderStatusEnum.getType(), orderStatusCountMap.get(buyerReplenishmentOrderStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(buyerReplenishmentOrderStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);

        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("orderList", pagination);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    /**
     * 获取买家视角补货订单状态
     *
     * @param systemReplementOrderStatus
     * @return
     */
    BuyerReplenishmentOrderStatusEnum getBuyerReplenishmentOrderStatus(String systemReplementOrderStatus) {
        if (SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(systemReplementOrderStatus))//买家已申请
            return BuyerReplenishmentOrderStatusEnum.WaitingConfirmation;
        if (SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType().equals(systemReplementOrderStatus))//卖家已确认
            return BuyerReplenishmentOrderStatusEnum.WaitingDelivered;
        if (SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(systemReplementOrderStatus))//卖家已关闭
            return BuyerReplenishmentOrderStatusEnum.Closed;
        if (SystemReplenishmentOrderStatusEnum.SellerDelivered.getType().equals(systemReplementOrderStatus))//卖家已发货
            return BuyerReplenishmentOrderStatusEnum.WaitingReceived;
        if (SystemReplenishmentOrderStatusEnum.BuyerReceived.getType().equals(systemReplementOrderStatus) || SystemReplenishmentOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemReplementOrderStatus))//买家已收货+系统自动确认收货
            return BuyerReplenishmentOrderStatusEnum.Finished;
        return null;
    }

    /**
     * 获取卖家视角补货订单状态
     *
     * @param systemReplementOrderStatus
     * @return
     */
    SellerReplenishmentOrderStatusEnum getSellerReplenishmentOrderStatus(String systemReplementOrderStatus) {
        if (SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(systemReplementOrderStatus))//买家已申请
            return SellerReplenishmentOrderStatusEnum.WaitingConfirmation;
        if (SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType().equals(systemReplementOrderStatus))//卖家已确认
            return SellerReplenishmentOrderStatusEnum.WaitingDelivered;
        if (SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(systemReplementOrderStatus))//卖家已关闭
            return SellerReplenishmentOrderStatusEnum.Closed;
        if (SystemReplenishmentOrderStatusEnum.SellerDelivered.getType().equals(systemReplementOrderStatus))//卖家已发货
            return SellerReplenishmentOrderStatusEnum.WaitingReceived;
        if (SystemReplenishmentOrderStatusEnum.BuyerReceived.getType().equals(systemReplementOrderStatus) || SystemReplenishmentOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemReplementOrderStatus))//买家已收货+系统自动确认收货
            return SellerReplenishmentOrderStatusEnum.Finished;
        return null;
    }

    /**
     * 卖家审核退货订单
     *
     * @param userDto
     * @param orderException
     */
    public void modifyReviewReturnOrder(UserDto userDto, OrderException orderException) {
        if (UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
            throw new RuntimeException("参数异常");

        // 验证审核状态
        if (!(SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(orderException.getOrderStatus()) || SystemRefundOrderStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())))
            throw new RuntimeException("参数异常");

        OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
        if (UtilHelper.isEmpty(oe))
            throw new RuntimeException("未找到退货订单");
        if (userDto.getCustId() != oe.getSupplyId()) {
            log.info("退货订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("未找到退货订单");
        }
        //判断是否是退货订单
        if (!"1".equals(oe.getReturnType())) {
            log.info("退货订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("该订单不是退货订单");
        }
        if (!SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(oe.getOrderStatus())) {
            log.info("退货订单状态不正确,OrderException:" + oe);
            throw new RuntimeException("退货订单状态不正确");
        }
        String now = systemDateMapper.getSystemDate();
        oe.setRemark(orderException.getRemark());
        oe.setOrderStatus(orderException.getOrderStatus());
        oe.setUpdateUser(userDto.getUserName());
        oe.setUpdateTime(now);
        oe.setReviewTime(now);
        int count = orderExceptionMapper.update(oe);
        if (count == 0) {
            log.error("OrderException info :" + oe);
            throw new RuntimeException("退货订单审核失败");
        }

        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setOrderId(oe.getExceptionId());
        orderTrace.setNodeName(SystemRefundOrderStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
        orderTrace.setDealStaff(userDto.getUserName());
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userDto.getUserName());
        orderTrace.setOrderStatus(oe.getOrderStatus());
        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userDto.getUserName());
        orderTraceMapper.save(orderTrace);
    }

    /**
     * 修改状态
     *
     * @param orderException
     * @return
     */
    public int updateOrderStatus(OrderException orderException) {
        return orderExceptionMapper.updateOrderStatus(orderException);
    }

    /**
     * 供应商补货订单管理-分页查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgSellerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

		/* 非法参数过滤 */
        if (UtilHelper.isEmpty(pagination) || UtilHelper.isEmpty(orderExceptionDto)) throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());

		/* 转换日期查询条件 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }
        }

		/* 查询供应商补货订单总金额 */
        BigDecimal orderTotalMoney = orderExceptionMapper.findSellerReplenishmentOrderTotal(orderExceptionDto);
        log.info("orderTotalMoney:" + orderTotalMoney);

		/* 供应商补货订单查询 */
        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationSellerReplenishmentOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);

		/* 根据供应商的视角 获取补货订单状态名称 */
        int orderCount = 0;
        SellerReplenishmentOrderStatusEnum sellerReplenishmentOrderStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                sellerReplenishmentOrderStatusEnum = getSellerReplenishmentOrderStatus(oed.getOrderStatus());
                if (!UtilHelper.isEmpty(sellerReplenishmentOrderStatusEnum)) {
                    oed.setOrderStatusName(sellerReplenishmentOrderStatusEnum.getValue());
                } else {
                    oed.setOrderStatusName("未知状态");
                }
            }
        }
        pagination.setResultList(orderExceptionDtoList);

		/* 补货订单状态统计 */
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerReplenishmentStatusCount(orderExceptionDto);
        ;

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                sellerReplenishmentOrderStatusEnum = getSellerReplenishmentOrderStatus(oed.getOrderStatus());
                if (sellerReplenishmentOrderStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(sellerReplenishmentOrderStatusEnum.getType())) {
                        orderStatusCountMap.put(sellerReplenishmentOrderStatusEnum.getType(), orderStatusCountMap.get(sellerReplenishmentOrderStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(sellerReplenishmentOrderStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);

		/* 把需要响应到页面的数据塞入 map 中 */
        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("orderList", pagination);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    /**
     * 供应商换货订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgSellerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }
        }
        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findSellerChangeGoodsExceptionOrderTotal(orderExceptionDto);
        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPgSellerChangeGoodsOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);

        SellerChangeGoodsOrderStatusEnum sellerOrderExceptionStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                sellerOrderExceptionStatusEnum = getSellerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (!UtilHelper.isEmpty(sellerOrderExceptionStatusEnum))
                    oed.setOrderStatusName(sellerOrderExceptionStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }
        pagination.setResultList(orderExceptionDtoList);
        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerChangeGoodsOrderStatusCount(orderExceptionDto);
        ;

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //卖家视角订单状态数量
                sellerOrderExceptionStatusEnum = getSellerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(), oed.getPayType());
                if (sellerOrderExceptionStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(sellerOrderExceptionStatusEnum.getType())) {
                        orderStatusCountMap.put(sellerOrderExceptionStatusEnum.getType(), orderStatusCountMap.get(sellerOrderExceptionStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(sellerOrderExceptionStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);
        resultMap.put("rejectOrderStatusCount", orderStatusCountMap);
        resultMap.put("rejectOrderList", pagination);
        resultMap.put("rejectOrderCount", orderCount);
        resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    /**
     * 获取供应商视角换货异常订单状态
     *
     * @param systemExceptionOrderStatus
     * @param payType
     * @return
     */
    SellerChangeGoodsOrderStatusEnum getSellerChangeGoodsOrderExceptionStatus(String systemExceptionOrderStatus, int payType) {
        SellerChangeGoodsOrderStatusEnum sellerOrderExceptionStatusEnum = null;
        if (SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(systemExceptionOrderStatus)) {//申请中
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingConfirmation;//待确认
        }
        if (SystemChangeGoodsOrderStatusEnum.Canceled.getType().equals(systemExceptionOrderStatus)) {//已取消
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Canceled;
        }
        if (SystemChangeGoodsOrderStatusEnum.Closed.getType().equals(systemExceptionOrderStatus)) {//卖家已关闭
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Closed;//已关闭
        }
        if (SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(systemExceptionOrderStatus)) {//待买家发货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingBuyerDelivered;//待买家发货
        }
        if (SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType().equals(systemExceptionOrderStatus)) {//买家已发货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingSellerReceived;//
        }

        if (SystemChangeGoodsOrderStatusEnum.WaitingSellerDelivered.getType().equals(systemExceptionOrderStatus)) {//卖家已收货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingSellerDelivered;//
        }

        if (SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType().equals(systemExceptionOrderStatus)) {//卖家已发货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingBuyerReceived;//
        }

        if (SystemChangeGoodsOrderStatusEnum.Finished.getType().equals(systemExceptionOrderStatus)) {//买家已收货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Finished;//
        }

        if (SystemChangeGoodsOrderStatusEnum.AutoFinished.getType().equals(systemExceptionOrderStatus)) {//买家自动收货
            sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Finished;//
        }

        return sellerOrderExceptionStatusEnum;
    }

    /**
     * 买家视角退货订单状态
     *
     * @param systemStatus
     * @param payType
     * @return
     */
    BuyerRefundOrderStatusEnum getBuyerRefundOrderStatusEnum(String systemStatus, int payType) {
        if (SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.BuyerApplying;
        if (SystemRefundOrderStatusEnum.BuyerCanceled.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.Canceled;
        if (SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.WaitingBuyerDelivered;
        if (SystemRefundOrderStatusEnum.SellerClosed.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.Closed;
        if (SystemRefundOrderStatusEnum.BuyerDelivered.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.WaitingSellerReceived;
        if (SystemRefundOrderStatusEnum.SellerReceived.getType().equals(systemStatus) || SystemRefundOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemStatus)) {
            if (payType == SystemPayTypeEnum.PayOffline.getPayType() || payType == SystemPayTypeEnum.PayOnline.getPayType())
                return BuyerRefundOrderStatusEnum.refunding;
            if (payType == SystemPayTypeEnum.PayPeriodTerm.getPayType())
                return BuyerRefundOrderStatusEnum.Finished;
        }
        if (SystemRefundOrderStatusEnum.Refunded.getType().equals(systemStatus))
            return BuyerRefundOrderStatusEnum.Finished;
        return null;
    }

    /**
     * 卖家视角退货订单状态
     *
     * @param systemStatus
     * @param payType
     * @return
     */
    SellerRefundOrderStatusEnum getSellerRefundOrderStatusEnum(String systemStatus, int payType) {
        if (SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.BuyerApplying;
        if (SystemRefundOrderStatusEnum.BuyerCanceled.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.Canceled;
        if (SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.WaitingBuyerDelivered;
        if (SystemRefundOrderStatusEnum.SellerClosed.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.Closed;
        if (SystemRefundOrderStatusEnum.BuyerDelivered.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.WaitingSellerReceived;
        if (SystemRefundOrderStatusEnum.SellerReceived.getType().equals(systemStatus) || SystemRefundOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemStatus)) {
            if (payType == SystemPayTypeEnum.PayOffline.getPayType() || payType == SystemPayTypeEnum.PayOnline.getPayType())
                return SellerRefundOrderStatusEnum.refunding;
            if (payType == SystemPayTypeEnum.PayPeriodTerm.getPayType())
                return SellerRefundOrderStatusEnum.Finished;
        }
        if (SystemRefundOrderStatusEnum.Refunded.getType().equals(systemStatus))
            return SellerRefundOrderStatusEnum.Finished;
        return null;
    }

    /**
     * 采购商退货订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgBuyerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }

        }

        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerRefundOrderTotal(orderExceptionDto);

        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerRefundOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);


        BuyerRefundOrderStatusEnum buyerRefundOrderStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(), oed.getPayType());
                if (!UtilHelper.isEmpty(buyerRefundOrderStatusEnum))
                    oed.setOrderStatusName(buyerRefundOrderStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }

        pagination.setResultList(orderExceptionDtoList);

        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerRefundOrderStatusCount(orderExceptionDto);

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //卖家视角订单状态
                buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(), oed.getPayType());
                if (buyerRefundOrderStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(buyerRefundOrderStatusEnum.getType())) {
                        orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(), orderStatusCountMap.get(buyerRefundOrderStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);

        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("orderList", pagination);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }


    /**
     * 买家取消退货订单
     *
     * @param userDto
     * @param exceptionId
     */
    public void updateRefundOrderStatusForBuyer(UserDto userDto, Integer exceptionId) {
        if (UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(exceptionId)) {
            throw new RuntimeException("参数错误");
        }
        OrderException orderException = orderExceptionMapper.getByPK(exceptionId);
        log.info(orderException);
        if (UtilHelper.isEmpty(orderException)) {
            log.error("can not find order ,exceptionId:" + exceptionId);
            throw new RuntimeException("未找到订单");
        }
        //判断订单是否属于该买家
        if (userDto.getCustId() == orderException.getCustId()) {
            if (SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(orderException.getOrderStatus())) {//买家已申请
                orderException.setOrderStatus(SystemRefundOrderStatusEnum.BuyerCanceled.getType());//标记订单为用户取消状态
                String now = systemDateMapper.getSystemDate();
                orderException.setUpdateUser(userDto.getUserName());
                orderException.setUpdateTime(now);
                int count = orderExceptionMapper.update(orderException);
                if (count == 0) {
                    log.error("orderException info :" + orderException);
                    throw new RuntimeException("订单取消失败");
                }
                //插入日志表
                OrderTrace orderTrace = new OrderTrace();
                orderTrace.setOrderId(orderException.getExceptionId());
                orderTrace.setNodeName("买家取消退货订单");
                orderTrace.setDealStaff(userDto.getUserName());
                orderTrace.setRecordDate(now);
                orderTrace.setRecordStaff(userDto.getUserName());
                orderTrace.setOrderStatus(orderException.getOrderStatus());
                orderTrace.setCreateTime(now);
                orderTrace.setCreateUser(userDto.getUserName());
                orderTraceMapper.save(orderTrace);

            } else {
                log.error("orderException status error ,orderStatus:" + orderException.getOrderStatus());
                throw new RuntimeException("订单状态不正确");
            }
        } else {
            log.error("db orderException not equals to request exceptionId ,exceptionId:" + exceptionId + ",db exceptionId:" + orderException.getExceptionId());
            throw new RuntimeException("未找到订单");
        }
    }

    /**
     * 补货订单订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getReplenishmentDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        Integer userType = orderExceptionDto.getUserType();
        orderExceptionDto = orderExceptionMapper.getReplenishmentDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        if (userType == 1) { //买家视角订单状态
            BuyerReplenishmentOrderStatusEnum buyerReplenishmentOrderStatusEnum;
            buyerReplenishmentOrderStatusEnum = getBuyerReplenishmentOrderStatus(orderExceptionDto.getOrderStatus());
            if (!UtilHelper.isEmpty(buyerReplenishmentOrderStatusEnum))
                orderExceptionDto.setOrderStatusName(buyerReplenishmentOrderStatusEnum.getValue());
            else
                orderExceptionDto.setOrderStatusName("未知状态");
        }
        if (userType == 2 || userType == 3) { //卖家视角订单状态 2、卖家订单详情 3、卖家审核订单详情页
            SellerReplenishmentOrderStatusEnum sellerReplenishmentOrderStatusEnum;
            sellerReplenishmentOrderStatusEnum = getSellerReplenishmentOrderStatus(orderExceptionDto.getOrderStatus());
            if (!UtilHelper.isEmpty(sellerReplenishmentOrderStatusEnum))
                orderExceptionDto.setOrderStatusName(sellerReplenishmentOrderStatusEnum.getValue());
            else
                orderExceptionDto.setOrderStatusName("未知状态");
        }
        if (userType == 2) {  //供应商的时候取发货信息
            OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
            orderDeliveryDetail.setFlowId(orderExceptionDto.getExceptionOrderId());
            List<OrderDeliveryDetail> list = orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
            if (list.size() > 0) {
                orderDeliveryDetail = (OrderDeliveryDetail) list.get(0);
                if (orderDeliveryDetail.getDeliveryStatus() == 0) {
                    orderExceptionDto.setImportStatusName("导入失败");
                    orderExceptionDto.setFileName("下载失败原因");
                } else {
                    orderExceptionDto.setImportStatusName("导入成功");
                    orderExceptionDto.setFileName("下载批号列表");
                }
                orderExceptionDto.setImportFileUrl(orderDeliveryDetail.getImportFileUrl());
            }
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }

        return orderExceptionDto;
    }


    /**
     * 卖家退货订单查询
     *
     * @param pagination
     * @param orderExceptionDto
     * @return
     */
    public Map<String, Object> listPgSellerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (UtilHelper.isEmpty(orderExceptionDto))
            throw new RuntimeException("参数错误");
        log.info("request orderExceptionDto :" + orderExceptionDto.toString());
        if (!UtilHelper.isEmpty(orderExceptionDto.getEndTime())) {
            try {
                Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(), "yyyy-MM-dd");
                Date endTimeAddOne = DateUtils.addDays(endTime, 1);
                orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
            } catch (ParseException e) {
                log.error("datefromat error,date: " + orderExceptionDto.getEndTime());
                e.printStackTrace();
                throw new RuntimeException("日期错误");
            }

        }

        int orderCount = 0;
        BigDecimal orderTotalMoney = orderExceptionMapper.findSellerRefundOrderTotal(orderExceptionDto);

        log.info("orderTotalMoney:" + orderTotalMoney);

        List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationSellerRefundOrder(pagination, orderExceptionDto);
        log.info("orderExceptionDtoList:" + orderExceptionDtoList);


        BuyerRefundOrderStatusEnum buyerRefundOrderStatusEnum;
        if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
            orderCount = pagination.getTotal();
            for (OrderExceptionDto oed : orderExceptionDtoList) {
                buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(), oed.getPayType());
                if (!UtilHelper.isEmpty(buyerRefundOrderStatusEnum))
                    oed.setOrderStatusName(buyerRefundOrderStatusEnum.getValue());
                else
                    oed.setOrderStatusName("未知状态");
            }
        }

        pagination.setResultList(orderExceptionDtoList);

        Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
        List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerRefundOrderStatusCount(orderExceptionDto);

        if (!UtilHelper.isEmpty(orderExceptionDtos)) {
            for (OrderExceptionDto oed : orderExceptionDtos) {
                //卖家视角订单状态
                buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(), oed.getPayType());
                if (buyerRefundOrderStatusEnum != null) {
                    if (orderStatusCountMap.containsKey(buyerRefundOrderStatusEnum.getType())) {
                        orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(), orderStatusCountMap.get(buyerRefundOrderStatusEnum.getType()) + oed.getOrderCount());
                    } else {
                        orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(), oed.getOrderCount());
                    }
                }
            }
        }
        log.info("orderStatusCountMap:" + orderStatusCountMap);

        resultMap.put("orderStatusCount", orderStatusCountMap);
        resultMap.put("orderList", pagination);
        resultMap.put("orderCount", orderCount);
        resultMap.put("orderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
        return resultMap;
    }

    //补货确认收货
    public void updateRepConfirmReceipt(String exceptionOrderId, UserDto userDto) throws Exception {

        OrderException orderException = orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
        if (UtilHelper.isEmpty(orderException)) {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("未找到订单");
        }
        if (userDto.getCustId() == orderException.getCustId()) {
            if (SystemReplenishmentOrderStatusEnum.SellerDelivered.getType().equals(orderException.getOrderStatus())) {//买家已申请
                orderException.setOrderStatus(SystemReplenishmentOrderStatusEnum.BuyerReceived.getType());//买家已收货
                String now = systemDateMapper.getSystemDate();
                orderException.setReceiveTime(now);
                orderException.setUpdateUser(userDto.getUserName());
                orderException.setUpdateTime(now);
                int count = orderExceptionMapper.update(orderException);
                if (count == 0) {
                    log.info("orderException info :" + orderException);
                    throw new RuntimeException("订单收货失败");
                }
                //生成日志
                createOrderTrace(orderException, userDto, now, 1, "买家已收货");
                //更新原订单状态
                Order order = orderMapper.getOrderbyFlowId(orderException.getFlowId());
                order.setOrderStatus(SystemOrderStatusEnum.BuyerPartReceived.getType());
                order.setUpdateTime(now);
                order.setUpdateUser(userDto.getUserName());
                orderMapper.update(order);
                createOrderTrace(order, userDto, now, 2, "买家部分收货");
                SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
                if (systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())) {
                    PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
                    payService.handleRefund(userDto, 3, orderException.getExceptionOrderId(), "买家补货确认收货");
                }
                //补货买家确认收货时候，产生结算信息
                this.saveOrderSettlement(order);
            } else {
                log.info("订单状态不正确:" + orderException.getOrderStatus());
                throw new RuntimeException("订单状态不正确");
            }

        } else {
            log.info("订单不存在");
            throw new RuntimeException("未找到订单");
        }
    }

    public void createOrderTrace(Object order, UserDto userDto, String now, int type, String nodeName) {
        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setDealStaff(userDto.getUserName());
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userDto.getUserName());

        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userDto.getUserName());
        if (type == 1) {
            OrderException orderException = (OrderException) order;
            orderTrace.setOrderId(orderException.getExceptionId());
            orderTrace.setOrderStatus(orderException.getOrderStatus());
            orderTrace.setNodeName(nodeName);
        } else {
            Order newOrder = (Order) order;
            orderTrace.setOrderId(newOrder.getOrderId());
            orderTrace.setOrderStatus(newOrder.getOrderStatus());
            orderTrace.setNodeName(nodeName);
        }
        orderTraceMapper.save(orderTrace);
    }

    /**
     * 卖家审核补货订单
     *
     * @param userDto
     * @param orderException
     */
    public void updateReviewReplenishmentOrderStatusForSeller(UserDto userDto, OrderException orderException) {
        if (UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
            throw new RuntimeException("参数异常");

        // 验证审核状态
        if (!(SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType().equals(orderException.getOrderStatus()) || SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())))
            throw new RuntimeException("参数异常");

        OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
        if (UtilHelper.isEmpty(oe))
            throw new RuntimeException("未找到补货订单");
        if (userDto.getCustId() != oe.getSupplyId()) {
            log.info("补货订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("未找到补货订单");
        }
        //判断是否是拒收订单
        if (!"3".equals(oe.getReturnType())) {
            log.info("该订单不是拒收订单");
            throw new RuntimeException("该订单不是拒收订单");
        }
        if (!SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(oe.getOrderStatus())) {
            log.info("补货订单状态不正确,OrderException:" + oe);
            throw new RuntimeException("补货订单状态不正确");
        }
        Order order = orderMapper.getOrderbyFlowId(oe.getFlowId());
        if (UtilHelper.isEmpty(order))
            throw new RuntimeException("未找到原订单");
        if (userDto.getCustId() != order.getSupplyId()) {
            log.info("原订单不属于该卖家,OrderException:" + oe + ",UserDto:" + userDto);
            throw new RuntimeException("未找到原订单");
        }
        if (!SystemOrderStatusEnum.Replenishing.getType().equals(order.getOrderStatus())) {
            log.info("原订单不是补货中的订单");
            throw new RuntimeException("原订单不是补货中的订单");
        }
        String now = systemDateMapper.getSystemDate();
        oe.setRemark(orderException.getRemark());
        oe.setOrderStatus(orderException.getOrderStatus());
        oe.setUpdateUser(userDto.getUserName());
        oe.setUpdateTime(now);
        oe.setReviewTime(now);
        int count = orderExceptionMapper.update(oe);
        if (count == 0) {
            log.error("OrderException info :" + oe);
            throw new RuntimeException("补货订单审核失败");
        }

        //插入日志表
        OrderTrace orderTrace = new OrderTrace();
        orderTrace.setOrderId(oe.getExceptionId());
        orderTrace.setNodeName(SystemReplenishmentOrderStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
        orderTrace.setDealStaff(userDto.getUserName());
        orderTrace.setRecordDate(now);
        orderTrace.setRecordStaff(userDto.getUserName());
        orderTrace.setOrderStatus(oe.getOrderStatus());
        orderTrace.setCreateTime(now);
        orderTrace.setCreateUser(userDto.getUserName());
        orderTraceMapper.save(orderTrace);
        //补货订单卖家审核不通过时、原订单状态改为买家全部收货
        if (SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())) {
            order.setOrderStatus(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType());
            order.setReceiveTime(systemDateMapper.getSystemDate());
            order.setReceiveType(2);//系统自动确认收货
            order.setUpdateTime(systemDateMapper.getSystemDate());
            order.setUpdateUser(userDto.getUserName());
            orderMapper.update(order);

            //插入日志表
            OrderTrace orderTrace1 = new OrderTrace();
            orderTrace1.setOrderId(order.getOrderId());
            orderTrace1.setNodeName("系统自动确认收货");
            orderTrace1.setDealStaff(userDto.getUserName());
            orderTrace1.setRecordDate(now);
            orderTrace1.setRecordStaff(userDto.getUserName());
            orderTrace1.setOrderStatus(SystemOrderStatusEnum.SystemAutoConfirmReceipt.getType());
            orderTrace1.setCreateTime(now);
            orderTrace1.setCreateUser(userDto.getUserName());
            orderTraceMapper.save(orderTrace);

            try {//审核不通过直接生成结算信息，通过的结算信息在买家确认收货时产生
                saveOrderSettlement(order);
            } catch (Exception e) {
                throw new RuntimeException("补货订单审核结算失败");
            }
        }
    }

    /**
     * 卖家收货（账期支付，银联支付）,自动收货，手动收货，生成结算记录
     *
     * @param order
     * @throws Exception
     */
    public void saveOrderSettlement(Order order) throws Exception {
        if (UtilHelper.isEmpty(order)) {
            throw new RuntimeException("未找到订单");
        }
        //当为账期支付时
        String now = systemDateMapper.getSystemDate();
        SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
        OrderSettlement orderSettlement = new OrderSettlement();
        if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
            //非拒收账期产生结算，拒收账期在审核通过产生
            orderSettlement.setBusinessType(1);
            orderSettlement.setOrderId(order.getOrderId());
            orderSettlement.setFlowId(order.getFlowId());
            orderSettlement.setCustId(order.getCustId());
            orderSettlement.setCustName(order.getCustName());
            orderSettlement.setSupplyId(order.getSupplyId());
            orderSettlement.setSupplyName(order.getSupplyName());
            orderSettlement.setConfirmSettlement("0");//生成结算信息时都是未结算
            orderSettlement.setPayTypeId(order.getPayTypeId());
            orderSettlement.setSettlementTime(now);
            orderSettlement.setCreateUser(order.getCustName());
            orderSettlement.setCreateTime(now);
            orderSettlement.setOrderTime(order.getCreateTime());
            orderSettlement.setSettlementMoney(order.getOrgTotal());
        } else if (SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType())) {//在线支付
            if (OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(systemPayType.getPayTypeId())) {
                //如果是招商支付则加上 买家id 银联支付不加
                orderSettlement.setCustId(order.getCustId());
            }
            orderSettlement.setBusinessType(1);
            orderSettlement.setOrderId(order.getOrderId());
            orderSettlement.setFlowId(order.getFlowId());
            orderSettlement.setCustName(order.getCustName());
            orderSettlement.setSupplyId(order.getSupplyId());
            orderSettlement.setSupplyName(order.getSupplyName());
            orderSettlement.setConfirmSettlement("0");//
            orderSettlement.setPayTypeId(order.getPayTypeId());
            orderSettlement.setSettlementTime(now);
            orderSettlement.setCreateUser(order.getCustName());
            orderSettlement.setCreateTime(now);
            orderSettlement.setOrderTime(order.getCreateTime());

            //补货的 结算金额全额

            orderSettlement.setSettlementMoney(order.getOrgTotal());
        }
        log.info("BBHH补货:orderId:--------------------------->");
        log.info("BBHH补货:orderId:" + order.getOrderId());
        log.info("BBHH补货支付类型systemPayType.getPayTypeId:" + systemPayType.getPayTypeId());
        log.info("BBHH补货结算moneyTotal:" + order.getOrderTotal());
        log.info("BBHH补货:order.getOrderStatus:" + order.getOrderStatus());
        orderSettlementService.parseSettlementProvince(orderSettlement, order.getCustId() + "");
        orderSettlementMapper.save(orderSettlement);
    }

    /**
     * 退货订单确认收货
     *
     * @param exceptionOrderId
     * @param userDto
     */
    public String editConfirmReceiptReturn(String exceptionOrderId, UserDto userDto, CreditDubboServiceInterface creditDubboService) throws Exception {
        String msg = "false";
        log.info("******************：进入退货订单确认收货，ID"+exceptionOrderId);
        OrderException orderException = orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
        if (UtilHelper.isEmpty(orderException) || userDto.getCustId() != orderException.getSupplyId()) {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("未找到订单");
        }
        if (SystemRefundOrderStatusEnum.BuyerDelivered.getType().equals(orderException.getOrderStatus())) {//买家已发货
            orderException.setOrderStatus(SystemRefundOrderStatusEnum.SellerReceived.getType());
            String now = systemDateMapper.getSystemDate();
            orderException.setUpdateUser(userDto.getUserName());
            orderException.setUpdateTime(now);
            orderException.setReceiveTime(now);
            int count = orderExceptionMapper.update(orderException);
            if (count == 0) {
                log.info("orderException info :" + orderException);
                throw new RuntimeException("订单收货失败");
            }
            //插入日志表
            OrderTrace orderTrace = new OrderTrace();
            orderTrace.setOrderId(orderException.getExceptionId());
            orderTrace.setNodeName("退货订单收货");
            orderTrace.setDealStaff(userDto.getUserName());
            orderTrace.setRecordDate(now);
            orderTrace.setRecordStaff(userDto.getUserName());
            orderTrace.setOrderStatus(orderException.getOrderStatus());
            orderTrace.setCreateTime(now);
            orderTrace.setCreateUser(userDto.getUserName());
            orderTraceMapper.save(orderTrace);


            //调用资信接口
            SystemPayType systemPayType = null;
            Order order = orderMapper.getByPK(orderException.getOrderId());
            try {
                systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
            } catch (Exception e) {
                throw new RuntimeException("未找到订单");
            }
            OrderService orderService = (OrderService) SpringBeanHelper.getBean("orderService");
            log.info("******************：调用资信接口，参数：{creditDubboService:"+creditDubboService+"**systemPayType:"+systemPayType+",orderException:"+orderException);
            orderService.sendReundCredit(creditDubboService, systemPayType, orderException);
            log.info("******************：调用资信接口,在OrderExceptionService中结束");
            msg = "true";
            saveReturnOrderSettlement(orderException);
        } else {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("当前订单状态不能进行收货!");
        }
        return msg;
    }

    /**
     * 退货订单卖家收货生成结算记录
     *
     * @param orderException
     * @throws Exception
     */
    public void saveReturnOrderSettlement(OrderException orderException) throws Exception {
        Order order = orderMapper.getByPK(orderException.getOrderId());
        if (UtilHelper.isEmpty(order)) {
            throw new RuntimeException("未找到订单");
        }
        SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
        String now = systemDateMapper.getSystemDate();
        OrderSettlement orderSettlement = new OrderSettlement();
        orderSettlement.setBusinessType(2);
        orderSettlement.setOrderId(orderException.getExceptionId());
        orderSettlement.setFlowId(orderException.getExceptionOrderId());
        orderSettlement.setCustId(orderException.getCustId());
        orderSettlement.setCustName(orderException.getCustName());
        orderSettlement.setSupplyId(orderException.getSupplyId());
        orderSettlement.setSupplyName(orderException.getSupplyName());
        orderSettlement.setConfirmSettlement("0");//生成结算信息时都是未结算
        orderSettlement.setPayTypeId(order.getPayTypeId());
        orderSettlement.setSettlementTime(now);
        orderSettlement.setCreateUser(orderException.getCustName());
        orderSettlement.setCreateTime(now);
        orderSettlement.setOrderTime(order.getCreateTime());
        orderSettlement.setSettlementMoney(orderException.getOrderMoney());

        orderSettlementService.parseSettlementProvince(orderSettlement, orderException.getCustId() + "");
        orderSettlementMapper.save(orderSettlement);
    }


    /**
     * 换货订单确认收货-卖家确认收货
     *
     * @param exceptionOrderId
     * @param userDto
     */
    public String editConfirmReceiptChange(String exceptionOrderId, UserDto userDto) {
        String msg = "false";
        OrderException orderException = orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
        if (UtilHelper.isEmpty(orderException) || userDto.getCustId() != orderException.getSupplyId()) {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("未找到订单");
        }
        if (SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType().equals(orderException.getOrderStatus())) {//买家已发货
            orderException.setOrderStatus(SystemChangeGoodsOrderStatusEnum.WaitingSellerDelivered.getType());
            String now = systemDateMapper.getSystemDate();
            orderException.setUpdateUser(userDto.getUserName());
            orderException.setUpdateTime(now);
            orderException.setSellerReceiveTime(now);
            int count = orderExceptionMapper.update(orderException);
            if (count == 0) {
                log.info("orderException info :" + orderException);
                throw new RuntimeException("订单收货失败");
            }
            //插入日志表
            OrderTrace orderTrace = new OrderTrace();
            orderTrace.setOrderId(orderException.getExceptionId());
            orderTrace.setNodeName("换货订单收货");
            orderTrace.setDealStaff(userDto.getUserName());
            orderTrace.setRecordDate(now);
            orderTrace.setRecordStaff(userDto.getUserName());
            orderTrace.setOrderStatus(orderException.getOrderStatus());
            orderTrace.setCreateTime(now);
            orderTrace.setCreateUser(userDto.getUserName());
            orderTraceMapper.save(orderTrace);
            msg = "true";
        } else {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("当前订单状态不能进行收货!");
        }
        return msg;
    }


    /**
     * 采购商换货订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getBuyerChangeGoodsOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getChangeGoodsOrderDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
        BuyerChangeGoodsOrderStatusEnum buyerChangeGoodsOrderStatusEnum;
        buyerChangeGoodsOrderStatusEnum = getBuyerChangeGoodsOrderExceptionStatus(orderExceptionDto.getOrderStatus(), orderExceptionDto.getPayType());
        if (!UtilHelper.isEmpty(buyerChangeGoodsOrderStatusEnum))
            orderExceptionDto.setOrderStatusName(buyerChangeGoodsOrderStatusEnum.getValue());
        else
            orderExceptionDto.setOrderStatusName("未知状态");

		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }

        return orderExceptionDto;
    }

    /**
     * 补货订单订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getSellerChangeGoodsOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto.setReturnType("2");
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setOrderStatusName(SellerChangeGoodsOrderStatusEnum.getName(orderExceptionDto.getOrderStatus()));
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }

        if (!UtilHelper.isEmpty(orderExceptionDto.getExceptionOrderId())) {
            OrderDelivery orderDelivery = new OrderDelivery();
            orderDelivery.setFlowId(orderExceptionDto.getExceptionOrderId());
            List<OrderDelivery> orderDeliveries = orderDeliveryMapper.listByProperty(orderDelivery);
            orderExceptionDto.setOrderDeliverys(orderDeliveries);
        }

        return orderExceptionDto;
    }


    //换货确认收货
    public void updateChangeGoodsBuyerConfirmReceipt(String exceptionOrderId, UserDto userDto) {
        OrderException orderException = orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
        if (UtilHelper.isEmpty(orderException)) {
            log.info("订单不存在，编号为：" + exceptionOrderId);
            throw new RuntimeException("未找到订单");
        }
        if (userDto.getCustId() == orderException.getCustId()) {
            if (SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType().equals(orderException.getOrderStatus())) {//卖家已发货
                orderException.setOrderStatus(SystemChangeGoodsOrderStatusEnum.Finished.getType());//买家已收货
                String now = systemDateMapper.getSystemDate();
                orderException.setReceiveTime(now);
                orderException.setUpdateUser(userDto.getUserName());
                orderException.setUpdateTime(now);
                int count = orderExceptionMapper.update(orderException);
                if (count == 0) {
                    log.info("orderException info :" + orderException);
                    throw new RuntimeException("订单收货失败");
                }
                //生成日志
                createOrderTrace(orderException, userDto, now, 1, "买家已收货");
            } else {
                log.info("订单状态不正确:" + orderException.getOrderStatus());
                throw new RuntimeException("订单状态不正确");
            }

        } else {
            log.info("订单不存在");
            throw new RuntimeException("未找到订单");
        }
    }

    /**
     * 根据订单编码，获取退货和拒收订单金额历史总额
     *
     * @param flowId
     * @return
     */
    public BigDecimal getConfirmHistoryExceptionMoney(String flowId) {
        BigDecimal historyMoneys = orderExceptionMapper.getConfirmHistoryExceptionMoney(flowId);
        return historyMoneys;
    }

    /**
     * 根据异常订单编码查询异常订单
     *
     * @param exceptionOrderId
     * @return
     */
    public OrderException getByExceptionOrderId(String exceptionOrderId) {
        return orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
    }

    ;


    /**
     * APP异常订单状态和系统订单状态互换
     *
     * @param orderStatus
     * @param type        1 补货 ，2 拒收
     * @return
     */
    private String convertAppExceptionOrderStatus(String orderStatus, String type) {
        String status = null;
        if (OrderExceptionTypeEnum.REPLENISHMENT.getType().equals(type)) {//补货
            //待确认
            if (BuyerReplenishmentOrderStatusEnum.WaitingConfirmation.getType().equals(orderStatus))
                return "901";
            //待发货
            if (BuyerReplenishmentOrderStatusEnum.WaitingDelivered.getType().equals(orderStatus))
                return "902";
            //待收货
            if (BuyerReplenishmentOrderStatusEnum.WaitingReceived.getType().equals(orderStatus))
                return "903";
            //已关闭
            if (BuyerReplenishmentOrderStatusEnum.Closed.getType().equals(orderStatus))
                return "904";
            //已完成
            if (BuyerReplenishmentOrderStatusEnum.Finished.getType().equals(orderStatus))
                return "905";
        } else if (OrderExceptionTypeEnum.REJECT.getType().equals(type)) {//拒收
            //待确认
            if (BuyerOrderExceptionStatusEnum.WaitingConfirmation.getType().equals(orderStatus))
                return "801";
            //退款中
            if (BuyerOrderExceptionStatusEnum.Refunding.getType().equals(orderStatus))
                return "802";
            //已关闭
            if (BuyerOrderExceptionStatusEnum.Closed.getType().equals(orderStatus))
                return "803";
            //已完成
            if (BuyerOrderExceptionStatusEnum.Refunded.getType().equals(orderStatus))
                return "804";
        }
        return status;
    }

    /**
     * APP获取订单列表
     *
     * @param pagination
     * @param orderStatus
     * @return
     */
    public Map<String, Object> listBuyerExceptionOderForApp(Pagination<OrderExceptionDto> pagination, String orderStatus, int custId,IProductDubboManageService iProductDubboManageService) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        OrderExceptionDto orderExceptionDto = new OrderExceptionDto();
        orderExceptionDto.setCustId(custId);
        if ("800".equals(orderStatus))//拒收
            orderExceptionDto.setReturnType(OrderExceptionTypeEnum.REJECT.getType());
        else if ("900".equals(orderStatus))//补货
            orderExceptionDto.setReturnType(OrderExceptionTypeEnum.REPLENISHMENT.getType());
        else
            throw new RuntimeException("订单状态不正确");
        //获取订单列表
        List<OrderExceptionDto> buyerExceptionOrderList = orderExceptionMapper.listPaginationBuyerExceptionOrder(pagination, orderExceptionDto);
        pagination.setResultList(buyerExceptionOrderList);
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        Map<String, Object> temp = null;
        if (!UtilHelper.isEmpty(buyerExceptionOrderList)) {
            for (OrderExceptionDto od : buyerExceptionOrderList) {
                if (!UtilHelper.isEmpty(od.getOrderStatus()) && !UtilHelper.isEmpty(od.getPayType())) {
                    //获取买家视角订单状态
                    if (OrderExceptionTypeEnum.REPLENISHMENT.getType().equals(od.getReturnType())) {//补货
                        BuyerReplenishmentOrderStatusEnum buyerorderstatusenum = getBuyerReplenishmentOrderStatus(od.getOrderStatus());
                        if (!UtilHelper.isEmpty(buyerorderstatusenum)){
                            od.setOrderStatusName(buyerorderstatusenum.getValue());
                            od.setOrderStatus(buyerorderstatusenum.getType());
                        }
                        else
                            od.setOrderStatusName("未知类型");
                    }
                    if (OrderExceptionTypeEnum.REJECT.getType().equals(od.getReturnType())) {//拒收
                        BuyerOrderExceptionStatusEnum buyerorderstatusenum = getBuyerOrderExceptionStatus(od.getOrderStatus(),od.getPayType());
                        if (!UtilHelper.isEmpty(buyerorderstatusenum)){
                            od.setOrderStatusName(buyerorderstatusenum.getValue());
                            od.setOrderStatus(buyerorderstatusenum.getType());
                        }
                        else
                            od.setOrderStatusName("未知类型");

                    }
                }

                temp = new HashMap<String, Object>();
                temp.put("orderId", od.getFlowId());
                temp.put("exceptionOrderId", od.getExceptionOrderId());
                temp.put("orderStatus", convertAppExceptionOrderStatus(od.getOrderStatus(), od.getReturnType()));
                temp.put("orderStatusName", od.getOrderStatusName());
                temp.put("createTime", od.getCreateTime());
                temp.put("supplyName", od.getSupplyName());
                temp.put("orderTotal", od.getOrderMoney());
                temp.put("finalPay", od.getOrderMoney());
                temp.put("varietyNumber", UtilHelper.isEmpty(od.getOrderReturnList()) ? 0 : od.getOrderReturnList().size());//品种
                temp.put("productNumber", sumProductNumber(od.getOrderReturnList()));//商品数量
                temp.put("qq", "");
                temp.put("productList", getProductList(od.getOrderReturnList(),iProductDubboManageService));
                orderList.add(temp);
            }
        }
        resultMap.put("totalCount", pagination.getTotal());
        resultMap.put("pageCount", pagination.getTotalPage());
        resultMap.put("orderList", orderList);
        return resultMap;
    }

    /**
     * 计算商品总数量
     * @param orderReturnDtoList
     * @return
     */
    int sumProductNumber(List<OrderReturnDto> orderReturnDtoList){
        int sum = 0;
        if(!UtilHelper.isEmpty(orderReturnDtoList)){
            for(OrderReturnDto oed :orderReturnDtoList)
                sum +=  oed.getReturnCount();
        }
        return sum;
    }

    /**
     * 转换商品列表详情
     * @param orderReturnDtoList
     * @return
     */
    List<Map<String,Object>> getProductList(List<OrderReturnDto> orderReturnDtoList,IProductDubboManageService iProductDubboManageService){
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = null;
        if(!UtilHelper.isEmpty(orderReturnDtoList)){
            for(OrderReturnDto od : orderReturnDtoList){
                map = new HashMap<String,Object>();
                map.put("productId",od.getProductId());
                map.put("productPicUrl",getProductImg(od.getSpuCode(),iProductDubboManageService));
                map.put("productName",od.getProductName());
                map.put("spec",od.getSpecification());
                map.put("unit","");
                map.put("productPrice",od.getProductPrice());
                map.put("factoryName","");
                map.put("quantity",od.getReturnCount());
                resultList.add(map);
            }
        }

        return resultList;
    }

    /**
     * 获取商品图片链接
     * @param spuCode
     * @param iProductDubboManageService
     * @return
     */
    private String getProductImg(String spuCode,IProductDubboManageService iProductDubboManageService){
        String filePath = "http://oms.yaoex.com/static/images/product_default_img.jpg";
        String file_path ="";
        Map map = new HashMap();
        map.put("spu_code", spuCode);
        map.put("type_id", "1");
        try{
            List picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
            if(UtilHelper.isEmpty(picUrlList))
                return filePath;
            JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
            file_path = (String)productJson.get("file_path");
        }catch (Exception e){
            log.error("查询图片接口:调用异常," + e.getMessage(),e);
        }

        if (UtilHelper.isEmpty(file_path)){
            return filePath;
        }else{
            /* 图片中文处理，只针对特定部位URL编码 */
            String head =  file_path.substring(0,file_path.lastIndexOf("/")+1);
            String body =  file_path.substring(file_path.lastIndexOf("/")+1,file_path.lastIndexOf("."));
            String foot =  file_path.substring(file_path.lastIndexOf("."),file_path.length());
            try {
                file_path =  head + URLEncoder.encode(body,"UTF-8") + foot;
            } catch (UnsupportedEncodingException e) {
                log.error("查询图片接口:URLEncoder编码(UTF-8)异常:"+e.getMessage(),e);
                return filePath;
            }
            return  MyConfigUtil.IMG_DOMAIN + file_path;
        }
    }


    /**
     * app异常订单详情
     *
     * @param orderExceptionDto
     * @param orderStatus
     * @return
     * @throws Exception
     */
    public OrderBean getAbnormalOrderDetails(OrderExceptionDto orderExceptionDto, Integer orderStatus,IProductDubboManageService iProductDubboManageService) throws Exception {
        if (orderStatus == 800) {  //拒收订单详情
            orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
            if (UtilHelper.isEmpty(orderExceptionDto)) {
                return null;
            }
        } else if (orderStatus == 900) {  //补货订单详情
            orderExceptionDto = orderExceptionMapper.getReplenishmentDetails(orderExceptionDto);
            if (UtilHelper.isEmpty(orderExceptionDto)) {
                return null;
            }
        } else {
            return null;
        }
        OrderBean orderBean = new OrderBean();
        if (orderStatus == 800) {
            BuyerOrderExceptionStatusEnum buyerorderstatusenum = getBuyerOrderExceptionStatus(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType());
            if (!UtilHelper.isEmpty(buyerorderstatusenum)){
                orderBean.setOrderStatusName(buyerorderstatusenum.getValue());
                orderBean.setOrderStatus(buyerorderstatusenum.getType());
            }
            else
                orderBean.setOrderStatusName("未知类型");
        }
        if (orderStatus == 900) {
            BuyerReplenishmentOrderStatusEnum buyerorderstatusenum = getBuyerReplenishmentOrderStatus(orderExceptionDto.getOrderStatus());
            if (!UtilHelper.isEmpty(buyerorderstatusenum)){
                orderBean.setOrderStatusName(buyerorderstatusenum.getValue());
                orderBean.setOrderStatus(buyerorderstatusenum.getType());
            }
            else
                orderBean.setOrderStatusName("未知类型");
        }
        orderBean.setOrderStatus(convertAppExceptionOrderStatus(orderBean.getOrderStatus(), orderExceptionDto.getReturnType()));
        orderBean.setOrderId(orderExceptionDto.getFlowId());
        orderBean.setCreateTime(orderExceptionDto.getCreateTime());
        orderBean.setApplyTime(orderExceptionDto.getCreateTime());
        orderBean.setSupplyId(orderExceptionDto.getSupplyId());
        orderBean.setSupplyName(orderExceptionDto.getSupplyName());
        orderBean.setLeaveMsg("");
        orderBean.setQq("");
        orderBean.setReturnDesc(orderExceptionDto.getReturnDesc());
        orderBean.setMerchantDesc(orderExceptionDto.getRemark());
        orderBean.setExceptionOrderId(orderExceptionDto.getExceptionOrderId());
        orderBean.setOrderTotal(Double.parseDouble(orderExceptionDto.getOrderMoney().toString()));
        orderBean.setFinalPay(Double.parseDouble(orderExceptionDto.getOrderMoney().toString()));

		 /* 商品信息 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            List<OrderProductBean> productList = new ArrayList<OrderProductBean>();
            Integer productNumber = 0; //商品数量
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto.getReturnPay())) continue;
                OrderProductBean orderProductBean = new OrderProductBean();
                orderProductBean.setProductId(orderReturnDto.getSpuCode());
                orderProductBean.setProductPicUrl(getProductImg(orderReturnDto.getSpuCode(), iProductDubboManageService));
                orderProductBean.setProductName(orderReturnDto.getShortName());
                orderProductBean.setSpec(orderReturnDto.getSpecification());
                orderProductBean.setUnit("");
                orderProductBean.setProductPrice(Double.parseDouble(orderReturnDto.getProductPrice().toString()));
                orderProductBean.setFactoryName(orderReturnDto.getManufactures());
                orderProductBean.setQuantity(orderReturnDto.getReturnCount());
                orderProductBean.setBatchNumber(orderReturnDto.getBatchNumber());
                productNumber += orderReturnDto.getReturnCount();
                productList.add(orderProductBean);
            }
            orderBean.setProductList(productList);
            orderBean.setVarietyNumber(orderExceptionDto.getOrderReturnList().size());
            orderBean.setProductNumber(productNumber);
        }
        return orderBean;
    }

    /**
     * 后台异常订单列表
     * @param pagination
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public Pagination<OrderExceptionDto> listPaginationOrderException(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) throws Exception {
        List<OrderExceptionDto> list = orderExceptionMapper.listPaginationOrderException(pagination, orderExceptionDto);
        if (!UtilHelper.isEmpty(list)) {
            if (OrderExceptionTypeEnum.RETURN.getType().equals(orderExceptionDto.getReturnType())) {  //退货
                for (OrderExceptionDto order : list) {
                    order.setOrderStatusName(SystemRefundOrderStatusEnum.getName(order.getOrderStatus()));
                }
            } else if (OrderExceptionTypeEnum.CHANGE.getType().equals(orderExceptionDto.getReturnType())) {  //换货
                for (OrderExceptionDto order : list) {
                    order.setOrderStatusName(SystemChangeGoodsOrderStatusEnum.getName(order.getOrderStatus()));
                }
            } else if (OrderExceptionTypeEnum.REPLENISHMENT.getType().equals(orderExceptionDto.getReturnType())) {  //补货
                for (OrderExceptionDto order : list) {
                    order.setOrderStatusName(SystemReplenishmentOrderStatusEnum.getName(order.getOrderStatus()));
                }
            } else if (OrderExceptionTypeEnum.REJECT.getType().equals(orderExceptionDto.getReturnType())) {  //拒收
                for (OrderExceptionDto order : list) {
                    order.setOrderStatusName(SystemOrderExceptionStatusEnum.getName(order.getOrderStatus()));
                }
            }
        }
        pagination.setResultList(list);
        return pagination;
    }

    /**
     * 后台拒收订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getRejectionOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        /* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay())) continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }
        orderExceptionDto.setOrderStatusName(SystemOrderExceptionStatusEnum.getName(orderExceptionDto.getOrderStatus()));
        return orderExceptionDto;
    }

    /**
     * 后台补货订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getReplenishmentOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getReplenishmentDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }
        orderExceptionDto.setOrderStatusName(SystemReplenishmentOrderStatusEnum.getName(orderExceptionDto.getOrderStatus()));
        return orderExceptionDto;
    }

    /**
     * 后台退货订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getRefundOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReturn(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getOrder().getBillType()));
		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }
        orderExceptionDto.setOrderStatusName(SystemRefundOrderStatusEnum.getName(orderExceptionDto.getOrderStatus()));
        return orderExceptionDto;
    }

    /**
     * 后台换货订单详情
     *
     * @param orderExceptionDto
     * @return
     * @throws Exception
     */
    public OrderExceptionDto getExchangeOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception {
        orderExceptionDto = orderExceptionMapper.getChangeGoodsOrderDetails(orderExceptionDto);
        if (UtilHelper.isEmpty(orderExceptionDto)) {
            return orderExceptionDto;
        }
        orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));

		/* 计算商品总额 */
        if (!UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())) {
            BigDecimal productPriceCount = new BigDecimal(0);
            for (OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()) {
                if (UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
                    continue;
                productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
            }
            orderExceptionDto.setProductPriceCount(productPriceCount);
        }
        orderExceptionDto.setOrderStatusName(SystemChangeGoodsOrderStatusEnum.getName(orderExceptionDto.getOrderStatus()));
        return orderExceptionDto;
    }

}