package com.yyw.yhyc.product.manage;

import com.sun.tools.corba.se.idl.StringGen;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.bo.ProductInventoryLog;
import com.yyw.yhyc.product.enmu.ProductInventoryLogTypeEnum;
import com.yyw.yhyc.product.mapper.ProductInventoryLogMapper;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2016/9/2.
 */

@Service("productInventoryManage")
public class ProductInventoryManage {
    private static final Logger log = LoggerFactory.getLogger(ProductInventoryManage.class);
    private ProductInventoryMapper productInventoryMapper;
    private SystemDateMapper systemDateMapper;
    private ProductInventoryLogMapper productInventoryLogMapper;

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

    /**
     * 检查购物车库存数量
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public Map<String, Object> findInventoryNumber(ProductInventory productInventory) throws Exception {
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
     * @param orderDtoList
     * @throws Exception
     */
    public void frozenInventory(List<OrderDto> orderDtoList) throws Exception {
        try {
            if (!UtilHelper.isEmpty(orderDtoList)) {
                String nowTime = systemDateMapper.getSystemDate();
                for (OrderDto orderDto : orderDtoList) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 释放冻结库存(取消)
     *
     * @param orderDetailList
     * @param supplyName      供应商自己操作是 operator=supplyName
     * @param operator
     * @throws Exception
     */
    public void releaseInventory(List<OrderDetail> orderDetailList, String supplyName, String operator) throws Exception {
        try {
            if (!UtilHelper.isEmpty(orderDetailList)) {
                String nowTime = systemDateMapper.getSystemDate();
                for (OrderDetail orderDetail : orderDetailList) {
                    ProductInventory productInventory = new ProductInventory();
                    productInventory.setSpuCode(orderDetail.getSpuCode());
                    productInventory.setSupplyId(orderDetail.getSupplyId());
                    productInventory.setBlockedInventory(orderDetail.getProductCount());
                    productInventory.setUpdateUser(operator);
                    productInventory.setUpdateTime(nowTime);
                    productInventoryMapper.updateReleaseInventory(productInventory);
                    saveProductInventoryLog(orderDetail, ProductInventoryLogTypeEnum.release.getType(), nowTime, supplyName, operator);
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
    public void deductionInventory(List<OrderDetail> orderDetailList, String supplyName) throws Exception {
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
        productInventoryLog.setLogType(ProductInventoryLogTypeEnum.deduction.getType());
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
        productInventoryLog.setRemark(orderDetail.getOrderId() + ProductInventoryLogTypeEnum.getName(logType) + orderDetail.getProductCount());
        productInventoryLogMapper.save(productInventoryLog);
    }

}
