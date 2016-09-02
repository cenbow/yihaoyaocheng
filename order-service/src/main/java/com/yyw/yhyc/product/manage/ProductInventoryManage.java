package com.yyw.yhyc.product.manage;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void setProductInventoryMapper(ProductInventoryMapper productInventoryMapper) {
        this.productInventoryMapper = productInventoryMapper;
    }

    /**
     * 检查购物车库存数量
     * @param productInventory
     * @return
     * @throws Exception
     */
    public synchronized Map<String, Object> findInventoryNumber(ProductInventory productInventory) throws Exception {
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
     * @param productInventoryList
     * @return
     * @throws Exception
     */
    public synchronized Map<String, Object> findInventoryListNumber(List<ProductInventory> productInventoryList) throws Exception {
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
     * @throws Exception
     */
    public void frozenInventory() throws Exception{

    }


    /**
     * 释放冻结库存(取消)
     * @throws Exception
     */
    public void releaseInventory(List<OrderDetail> orderDetailList) throws Exception{

    }

    /**
     * 扣减冻结库存(发货)
     * @throws Exception
     */
    public void deductionInventory(List<OrderDetail> orderDetailList) throws Exception{

    }

}
