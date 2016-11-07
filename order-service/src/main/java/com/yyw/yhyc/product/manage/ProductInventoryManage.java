package com.yyw.yhyc.product.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.bo.ProductInventoryLog;
import com.yyw.yhyc.product.enmu.ProductInventoryLogTypeEnum;
import com.yyw.yhyc.product.mapper.ProductInventoryLogMapper;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;

/**
 * Created by liqiang on 2016/9/2.
 */

@Service("productInventoryManage")
public class ProductInventoryManage {
    private static final Logger log = LoggerFactory.getLogger(ProductInventoryManage.class);
    private ProductInventoryMapper productInventoryMapper;
    private SystemDateMapper systemDateMapper;
    private ProductInventoryLogMapper productInventoryLogMapper;
    private OrderDetailMapper orderDetailMapper;
   
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    public void setProductInventoryMapper(ProductInventoryMapper productInventoryMapper) {
        this.productInventoryMapper = productInventoryMapper;
    }

    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    @Autowired
    public void setProductInventoryLogMapper(ProductInventoryLogMapper productInventoryLogMapper) {
        this.productInventoryLogMapper = productInventoryLogMapper;
    }

    @Autowired
    public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper)
    {
        this.orderDetailMapper = orderDetailMapper;
    }
    /**
     * 检查购物车库存数量
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public Map<String, Object> findInventoryNumber(ProductInventory productInventory) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductInventory productInventory1 = productInventoryMapper.findBySupplyIdSpuCode(productInventory.getSupplyId(), productInventory.getSpuCode());
        if (UtilHelper.isEmpty(productInventory1)) {
            resultMap.put("code", 0);
            resultMap.put("msg", "参数错误");
            return resultMap;
        }
        resultMap.put("frontInventory", productInventory1.getFrontInventory());
        if (productInventory.getFrontInventory() > productInventory1.getFrontInventory()) {
            resultMap.put("code", 1);
            resultMap.put("msg", "库存不足");
        } else {
            resultMap.put("code", 2);
            resultMap.put("msg", "库存正常");
        }
        return resultMap;
    }

    /**
     * 检查订单库存数量
     *
     * @param productInventoryList
     * @return
     * @throws Exception
     */
    public Map<String, Object> findInventoryListNumber(List<ProductInventory> productInventoryList) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (UtilHelper.isEmpty(productInventoryList)) {
            resultMap.put("code", 0);
            resultMap.put("msg", "参数错误");
            return resultMap;
        }
        List<ProductInventory> list = new ArrayList<ProductInventory>();
        for (ProductInventory productInventory : productInventoryList) {
            ProductInventory productInventory1 = productInventoryMapper.findBySupplyIdSpuCode(productInventory.getSupplyId(), productInventory.getSpuCode());
            if (UtilHelper.isEmpty(productInventory1)) {
                productInventory.setFrontInventory(0);
                list.add(productInventory);
            } else {
                if (productInventory.getFrontInventory() > productInventory1.getFrontInventory()) {
                    productInventory.setFrontInventory(productInventory1.getFrontInventory());
                    list.add(productInventory);
                }
            }
        }
        if (!UtilHelper.isEmpty(list)) {
            resultMap.put("code", 1);
            resultMap.put("productInventoryList", list);
            resultMap.put("msg", "库存不足");
            return resultMap;
        }
        resultMap.put("code", 2);
        resultMap.put("msg", "库存正常");
        return resultMap;
    }

    /**
     * 冻结库存（提交）
     *
     * @param orderDto
     * @throws Exception
     */
    public void frozenInventory(OrderDto orderDto){
        try {
            if (!UtilHelper.isEmpty(orderDto)) {
                String nowTime = systemDateMapper.getSystemDate();
                for (OrderDetail orderDetail : orderDto.getOrderDetailList()) {
                    ProductInventory productInventory = new ProductInventory();
                    productInventory.setSpuCode(orderDetail.getSpuCode());
                    productInventory.setSupplyId(orderDetail.getSupplyId());
                    productInventory.setBlockedInventory(orderDetail.getProductCount());
                    productInventory.setUpdateUser(orderDto.getSupplyName());
                    productInventory.setUpdateTime(nowTime);
                    productInventoryMapper.updateFrozenInventory(productInventory);
                    saveProductInventoryLog(orderDetail, ProductInventoryLogTypeEnum.frozen.getType(), nowTime, orderDto.getSupplyName(), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 释放冻结库存(取消)
     *
     * @param OrderId
     * @param supplyName      供应商自己操作是 operator=supplyName
     * @param operator
     * @param iPromotionDubboManageService 
     * @throws Exception
     */
    public void releaseInventory(int OrderId, String supplyName, String operator, IPromotionDubboManageService iPromotionDubboManageService){
        try {
            List<OrderDetail> list=orderDetailMapper.listOrderDetailInfoByOrderId(OrderId);
            if (!UtilHelper.isEmpty(list)) {
                String nowTime = systemDateMapper.getSystemDate();
                for (OrderDetail orderDetail : list) {
                    ProductInventory productInventory = new ProductInventory();
                    productInventory.setSpuCode(orderDetail.getSpuCode());
                    productInventory.setSupplyId(orderDetail.getSupplyId());
                    productInventory.setBlockedInventory(orderDetail.getProductCount());
                    productInventory.setUpdateUser(operator);
                    productInventory.setUpdateTime(nowTime);
                    productInventoryMapper.updateReleaseInventory(productInventory);
                    saveProductInventoryLog(orderDetail, ProductInventoryLogTypeEnum.release.getType(), nowTime, supplyName, operator);
                    
                    if((Integer)orderDetail.getPromotionId()!= null && orderDetail.getPromotionId()>0){
                    	Map params=new HashMap();
	      		      	params.put("spuCode", orderDetail.getSpuCode());
	      		      	params.put("promotionId", orderDetail.getPromotionId());
	      		      	params.put("productCount", orderDetail.getProductCount());
	      		      	
	      		      	Order order = orderMapper.getByPK(OrderId);
	      		      	params.put("buyerCode", order.getCustId());
	      		      	params.put("sellerCode", orderDetail.getSupplyId());
	      		      	log.info("活动库存释放"+params.toString());
	          			Map result = iPromotionDubboManageService.updateProductGroupInventroy(params);
	          			log.info("更新结果"+result.get("code")+" "+result.toString());
                    }
        			
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扣减冻结库存(发货)
     *
     * @param orderDetailList
     * @param supplyName
     * @throws Exception
     */
    public void deductionInventory(List<OrderDetail> orderDetailList, String supplyName) {
        try {
            if (!UtilHelper.isEmpty(orderDetailList)) {
                String nowTime = systemDateMapper.getSystemDate();
                for (OrderDetail orderDetail : orderDetailList) {
                    ProductInventory productInventory = new ProductInventory();
                    productInventory.setSpuCode(orderDetail.getSpuCode());
                    productInventory.setSupplyId(orderDetail.getSupplyId());
                    productInventory.setBlockedInventory(orderDetail.getProductCount());
                    productInventory.setUpdateUser(supplyName);
                    productInventory.setUpdateTime(nowTime);
                    productInventoryMapper.updateDeductionInventory(productInventory);
                    saveProductInventoryLog(orderDetail, ProductInventoryLogTypeEnum.deduction.getType(), nowTime, supplyName, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 库存日志
     *
     * @param orderDetail
     * @param logType
     * @param currentTime
     * @param supplyName
     * @param operator
     * @throws Exception
     */
    private void saveProductInventoryLog(OrderDetail orderDetail, Integer logType, String currentTime, String supplyName, String operator) throws Exception {
        ProductInventoryLog productInventoryLog = new ProductInventoryLog();
        productInventoryLog.setLogType(logType);
        productInventoryLog.setSpuCode(orderDetail.getSpuCode());
        productInventoryLog.setProductName(orderDetail.getProductName());
        productInventoryLog.setProductCount(orderDetail.getProductCount());
        productInventoryLog.setSupplyType(2);
        productInventoryLog.setSupplyId(orderDetail.getSupplyId());
        productInventoryLog.setSupplyName(supplyName);
        productInventoryLog.setCreateTime(currentTime);
        productInventoryLog.setUpdateTime(currentTime);
        if (!UtilHelper.isEmpty(operator)) {
            productInventoryLog.setCreateUser(operator);
            productInventoryLog.setUpdateUser(operator);
        } else {
            productInventoryLog.setCreateUser(supplyName);
            productInventoryLog.setUpdateUser(supplyName);
        }
        productInventoryLog.setRemark(orderDetail.getOrderId()+"的订单" + ProductInventoryLogTypeEnum.getName(logType) +"库存"+ orderDetail.getProductCount());
        productInventoryLogMapper.save(productInventoryLog);
    }

}
