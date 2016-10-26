/**
 * Created By: XI
 * Created On: 2016-8-29 11:23:12
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 */
package com.yyw.yhyc.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.product.bo.ProductAudit;
import com.yyw.yhyc.product.bo.ProductInventoryLog;
import com.yyw.yhyc.product.dto.ProductInventoryDto;
import com.yyw.yhyc.product.enmu.ProductInventoryLogTypeEnum;
import com.yyw.yhyc.product.mapper.ProductAuditMapper;
import com.yyw.yhyc.product.mapper.ProductInventoryLogMapper;
import com.yyw.yhyc.utils.ExcelUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.record.formula.IntPtg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;

@Service("productInventoryService")
public class ProductInventoryService {

    private Log log = LogFactory.getLog(ProductInventoryService.class);

    private ProductInventoryMapper productInventoryMapper;

    private ProductAuditMapper productAuditMapper;

    private SystemDateMapper systemDateMapper;

    private ProductInventoryLogMapper productInventoryLogMapper;

    @Autowired
    public void setProductInventoryMapper(ProductInventoryMapper productInventoryMapper) {
        this.productInventoryMapper = productInventoryMapper;
    }

    @Autowired
    public void setProductAuditMapper(ProductAuditMapper productAuditMapper) {
        this.productAuditMapper = productAuditMapper;
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
     * 通过主键查询实体对象
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public ProductInventory getByPK(Integer primaryKey) throws Exception {
        return productInventoryMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<ProductInventory> list() throws Exception {
        return productInventoryMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<ProductInventory> listByProperty(ProductInventory productInventory)
            throws Exception {
        return productInventoryMapper.listByProperty(productInventory);
    }

    /**
     * 根据查询条件查询分页记录
     *
     * @return
     * @throws Exception
     */
    public Pagination<ProductInventoryDto> listPaginationByProperty(Pagination<ProductInventoryDto> pagination, ProductInventoryDto productInventoryDto) throws Exception {
        List<ProductInventoryDto> list = productInventoryMapper.listPaginationByProperty(pagination, productInventoryDto);

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
        return productInventoryMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     *
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<Integer> primaryKeys) throws Exception {
        productInventoryMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public int deleteByProperty(ProductInventory productInventory) throws Exception {
        return productInventoryMapper.deleteByProperty(productInventory);
    }

    /**
     * 保存记录
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public void save(ProductInventory productInventory) throws Exception {
        productInventoryMapper.save(productInventory);
    }

    /**
     * 更新
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public int update(ProductInventory productInventory) throws Exception {
        return productInventoryMapper.update(productInventory);
    }

    /**
     * 根据条件查询记录条数
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public int findByCount(ProductInventory productInventory) throws Exception {
        return productInventoryMapper.findByCount(productInventory);
    }

    /**
     * 更新-保存库存信息
     *
     * @param productInventoryDto
     * @return
     * @throws Exception
     */
    public void updateInventory(ProductInventoryDto productInventoryDto) throws Exception {
        if (UtilHelper.isEmpty(productInventoryDto.getProductcodeCompany()) || UtilHelper.isEmpty(productInventoryDto.getCurrentInventory())) {
            log.info("参数异常");
            throw new RuntimeException("参数异常");
        }
        ProductAudit productAudit = productAuditMapper.getProductcodeCompany(productInventoryDto.getSupplyId(), productInventoryDto.getProductcodeCompany());
        if (UtilHelper.isEmpty(productAudit)) {
            log.info("产品编码不存在或者商品没有审核通过");
            throw new RuntimeException("产品编码不存在或者商品没有审核通过");
        }

        String now = systemDateMapper.getSystemDate();
        ProductInventory productInventory = new ProductInventory();
        ProductInventoryLog productInventoryLog = new ProductInventoryLog();

        productInventory.setCurrentInventory(productInventoryDto.getCurrentInventory());    //当前库存
        productInventory.setWarningInventory(productInventoryDto.getWarningInventory());   //预警库存
        productInventory.setUpdateTime(now);
        productInventory.setUpdateUser(productInventoryDto.getSupplyName());
        if (UtilHelper.isEmpty(productInventoryDto.getId())) {  //为空的时候添加库存
            productInventory.setSupplyName(productInventoryDto.getSupplyName());
            productInventory.setCreateTime(now);
            productInventory.setCreateUser(productInventoryDto.getSupplyName());
            productInventory.setSpuCode(productAudit.getSpuCode());
            productInventory.setBlockedInventory(0);
            productInventory.setFrontInventory(productInventoryDto.getCurrentInventory());
            productInventory.setSupplyId(productInventoryDto.getSupplyId());
            productInventory.setSupplyType(productInventoryDto.getSupplyType());
            log.info("添加库存");
            productInventoryMapper.save(productInventory);
            productInventoryLog.setRemark("初始库存");
            productInventoryLog.setLogType(ProductInventoryLogTypeEnum.addto.getType());
        } else { //存在则修改
            log.info("编辑库存");
            productInventory.setId(productInventoryDto.getId());
            productInventoryMapper.updateInventory(productInventory);
            productInventoryLog.setRemark("修改库存");
            productInventoryLog.setLogType(ProductInventoryLogTypeEnum.modify.getType());
        }
        productInventoryLog.setSpuCode(productAudit.getSpuCode());
        productInventoryLog.setProductName(productInventoryDto.getProductName());
        productInventoryLog.setProductCount(productInventoryDto.getCurrentInventory());
        productInventoryLog.setSupplyType(productInventoryDto.getSupplyType());
        productInventoryLog.setSupplyId(productInventoryDto.getSupplyId());
        productInventoryLog.setSupplyName(productInventoryDto.getSupplyName());
        productInventoryLog.setCreateUser(productInventoryDto.getSupplyName());
        productInventoryLog.setCreateTime(now);
        productInventoryLog.setUpdateUser(productInventoryDto.getSupplyName());
        productInventoryLog.setUpdateTime(now);
        productInventoryLogMapper.save(productInventoryLog);

    }

    /**
     * 批量更新-插入库存信息
     *
     * @param supplyId
     * @param supplyName
     * @param path
     * @param fileName
     * @return
     * @throws Exception
     */
    public Map<String, Object> UpdateExcelInventory(Integer supplyId, String supplyName, String path, String fileName) throws Exception {
        List<Map<String, String>> li = ExcelUtil.readExcel(path + fileName);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (li != null && li.size() > 0) {
            li.remove(0);
        } else {
            resultMap.put("code", 0);
            resultMap.put("msg", "读取文件错误");
            return resultMap;
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map<String, String> map : li) {
            StringBuffer stringBuffer = new StringBuffer();
            String productcodeCompany = map.get("0");       //本公司商品编码
            String currentInventory = map.get("1");         //当前库存
            String warningInventory = map.get("2");         //预警库存
            String productName = map.get("4");              //商品名
            if (UtilHelper.isEmpty(productcodeCompany) && UtilHelper.isEmpty(currentInventory)) {
                continue;
            }
            if (UtilHelper.isEmpty(productcodeCompany)) {
                stringBuffer.append("本公司商品编码不能为空、");
            }
            if (UtilHelper.isEmpty(currentInventory)) {
                stringBuffer.append("库存数量不能为空、");
            } else {
                if (!isIntNumber(currentInventory)) {
                    stringBuffer.append("库存数量只能为数子、");
                }
            }
            if (!UtilHelper.isEmpty(warningInventory)) {
                if (!isIntNumber(warningInventory)) {
                    stringBuffer.append("预警库存只能为数子、");
                } else {
                    if (Integer.parseInt(warningInventory) <= 0) {
                        stringBuffer.append("预警库存必须大于0、");
                    }
                }
            }
            if (stringBuffer.length() > 0) {
                map.put("7", stringBuffer.toString());
                list.add(map);
                continue;
            }
            ProductAudit productAudit = productAuditMapper.getProductcodeCompany(supplyId, productcodeCompany);
            if (UtilHelper.isEmpty(productAudit)) {
                map.put("7", "该商品编码不存在或者商品没有审核通过");
                list.add(map);
                continue;
            }
            ProductInventory product = null;
            try {
                product = productInventoryMapper.findBySupplyIdSpuCode(supplyId, productAudit.getSpuCode());
            } catch (Exception e) {
                map.put("7", "该商品编码库存信息存在多条");
                list.add(map);
                continue;
            }
            String now = systemDateMapper.getSystemDate();
            ProductInventory productInventory = new ProductInventory();

            ProductInventoryLog productInventoryLog = new ProductInventoryLog();

            productInventory.setCurrentInventory(Integer.parseInt(currentInventory));       //当前库存
            if (!UtilHelper.isEmpty(warningInventory)) {
                productInventory.setWarningInventory(Integer.parseInt(warningInventory));   //预警库存
            }
            productInventory.setUpdateTime(now);
            productInventory.setUpdateUser(supplyName);
            try {
                if (UtilHelper.isEmpty(product)) {  //为空的时候添加库存
                    productInventory.setSupplyName(supplyName);
                    productInventory.setCreateTime(now);
                    productInventory.setCreateUser(supplyName);
                    productInventory.setSpuCode(productAudit.getSpuCode());
                    productInventory.setBlockedInventory(0);
                    productInventory.setFrontInventory(Integer.parseInt(currentInventory));
                    productInventory.setSupplyId(supplyId);
                    productInventory.setSupplyType(2);
                    log.info("添加库存");
                    productInventoryMapper.save(productInventory);
                    productInventoryLog.setRemark("初始库存");
                    productInventoryLog.setLogType(ProductInventoryLogTypeEnum.addto.getType());
                } else { //存在则修改
                    log.info("编辑库存");
                    productInventory.setId(product.getId());
                    productInventoryMapper.updateInventory(productInventory);
                    productInventoryLog.setRemark("修改库存");
                    productInventoryLog.setLogType(ProductInventoryLogTypeEnum.modify.getType());
                }
                if (!UtilHelper.isEmpty(productName)) {
                    productInventoryLog.setProductName(productName);
                }
                productInventoryLog.setSpuCode(productAudit.getSpuCode());
                productInventoryLog.setProductCount(Integer.parseInt(currentInventory));
                productInventoryLog.setSupplyType(2);
                productInventoryLog.setSupplyId(supplyId);
                productInventoryLog.setSupplyName(supplyName);
                productInventoryLog.setCreateUser(supplyName);
                productInventoryLog.setCreateTime(now);
                productInventoryLog.setUpdateUser(supplyName);
                productInventoryLog.setUpdateTime(now);
                productInventoryLogMapper.save(productInventoryLog);

            } catch (Exception e) {
                map.put("7", "数据异常");
                list.add(map);
                continue;
            }
        }
        Integer failNumber = 0;
        String filePath = "";
        if (list != null && list.size() > 0) {        //错误数据
            failNumber = list.size();
            filePath = writeExcel(list, path);
        }
        resultMap.put("code", 1);
        resultMap.put("successNumber", li.size() - failNumber);
        resultMap.put("failNumber", failNumber);
        resultMap.put("filePath", path + filePath);
        resultMap.put("msg", "批量导入成功");
        return resultMap;
    }


    /**
     * 失败信息写入Excel
     *
     * @param list
     * @param path
     */
    public static String writeExcel(List<Map<String, String>> list, String path) {
        //创建路径
        String[] headers = new String[]{"本公司产品编码", "库存数量", "预警库存", "通用名", "商品名", "规格", "厂商", "失败原因"};
        List<Object[]> dataset = new ArrayList<Object[]>();
        for (Map<String, String> map : list) {
            dataset.add(new Object[]{
                    map.get("0"), map.get("1"), map.get("2"), map.get("3"), map.get("4"), map.get("5"), map.get("6"), map.get("7"),
            });
        }
        String fileName = ExcelUtil.downloadExcel("商品库存信息导入", headers, dataset, path);
        return fileName;
    }

    /**
     * 判断一个字符串是否是int
     *
     * @param str
     * @return
     */
    public static Boolean isIntNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

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
     * 获取商品库存
     *
     * @param productInventory
     * @return
     * @throws Exception
     */
    public Map<String, Object> getProductInventory(ProductInventory productInventory) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(productInventory.getSupplyId(), productInventory.getSpuCode());
        if (UtilHelper.isEmpty(product)) {
            resultMap.put("isSuccessful", 0);
            resultMap.put("frontInventory", 0);
            resultMap.put("message", "该商品没有库存信息");
            return resultMap;
        }
        resultMap.put("isSuccessful", 1);
        resultMap.put("frontInventory", product.getFrontInventory());
        resultMap.put("currentInventory", product.getCurrentInventory());
        resultMap.put("message", "库存正常");
        return resultMap;
    }

    /**
     * 根据供应商修改-保存、库存信息
     *
     * @param productInventoryList（spuCode 为 productcodeCompany 本公司商品编码、对接公司商品编码）
     * @throws Exception
     */
    public List<ProductInventory> updateSupplyIdInventory(List<ProductInventory> productInventoryList) throws Exception {
        if (!UtilHelper.isEmpty(productInventoryList)) {
            List<ProductInventory> list = new ArrayList<ProductInventory>();
            String now = systemDateMapper.getSystemDate();
            for (ProductInventory productInventory : productInventoryList) {
                if (!UtilHelper.isEmpty(productInventory.getSpuCode()) && !UtilHelper.isEmpty(productInventory.getSupplyId()) && !UtilHelper.isEmpty(productInventory.getCurrentInventory()) && !UtilHelper.isEmpty(productInventory.getSupplyName())) {
                    ProductAudit productAudit = productAuditMapper.getProductcodeCompany(productInventory.getSupplyId(), productInventory.getSpuCode());    //erp 需要的方法
//                	ProductAudit productAudit = productAuditMapper.getProductAudit(productInventory.getSupplyId(), productInventory.getSpuCode());  备注 库存需要的代码
                    if (UtilHelper.isEmpty(productAudit)) {
                        productInventory.setRemark("该商品编码不存在或者商品没有审核通过");
                        list.add(productInventory);
                        continue;
                    }
                    ProductInventoryLog productInventoryLog = new ProductInventoryLog();
                    ProductInventory product = null;
                    try {
                          product = productInventoryMapper.findBySupplyIdSpuCode(productInventory.getSupplyId(), productAudit.getSpuCode());
                    }catch (Exception e){
                        productInventory.setRemark("该商品编码库存信息存在多条");
                        list.add(productInventory);
                        continue;
                    }
                    productInventory.setSpuCode(productAudit.getSpuCode());
                    productInventory.setUpdateTime(now);
                    productInventory.setUpdateUser(productInventory.getSupplyName());
                    if (UtilHelper.isEmpty(productInventory.getSupplyType())) {
                        productInventory.setSupplyType(2);
                        productInventoryLog.setSupplyType(2);
                    } else {
                        productInventoryLog.setSupplyType(productInventory.getSupplyType());
                    }
                    if (UtilHelper.isEmpty(product)) {  //添加
                        productInventory.setCreateTime(now);
                        productInventory.setCreateUser(productInventory.getSupplyName());
                        productInventory.setBlockedInventory(0);
                        productInventory.setFrontInventory(productInventory.getCurrentInventory());
                        productInventoryMapper.save(productInventory);
                        productInventoryLog.setRemark("初始库存");
                        productInventoryLog.setLogType(ProductInventoryLogTypeEnum.addto.getType());
                    } else {                          //修改
                        productInventory.setId(product.getId());
                        productInventoryMapper.updateInventory(productInventory);
                        productInventoryLog.setRemark("修改库存");
                        productInventoryLog.setLogType(ProductInventoryLogTypeEnum.modify.getType());
                    }
                    productInventoryLog.setSpuCode(productInventory.getSpuCode());
                    productInventoryLog.setProductCount(productInventory.getCurrentInventory());
                    productInventoryLog.setSupplyId(productInventory.getSupplyId());
                    productInventoryLog.setSupplyName(productInventory.getSupplyName());
                    productInventoryLog.setCreateUser(productInventory.getSupplyName());
                    productInventoryLog.setCreateTime(now);
                    productInventoryLog.setUpdateUser(productInventory.getSupplyName());
                    productInventoryLog.setUpdateTime(now);
                    productInventoryLogMapper.save(productInventoryLog);
                } else {
                    productInventory.setRemark("参数错误");
                    list.add(productInventory);
                }
            }
            return list;
        }
        return null;
    }

}