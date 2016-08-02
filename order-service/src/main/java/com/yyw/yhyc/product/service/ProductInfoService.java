package com.yyw.yhyc.product.service;

/**
 * Created by lizhou on 2016/8/1
 */

import java.util.List;

import com.yyw.yhyc.bo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.product.bo.ProductInfo;

import com.yyw.yhyc.product.mapper.ProductInfoMapper;

@Service("productInfoService")
public class ProductInfoService {

    private ProductInfoMapper	productInfoMapper;

    @Autowired
    public void setProductInfoMapper(ProductInfoMapper productInfoMapper)
    {
        this.productInfoMapper = productInfoMapper;
    }

    /**
     * 通过主键查询实体对象
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public ProductInfo getByPK(java.lang.Integer primaryKey) throws Exception
    {
        return productInfoMapper.getByPK(primaryKey);
    }

    /**
     * 查询所有记录
     * @return
     * @throws Exception
     */
    public List<ProductInfo> list() throws Exception
    {
        return productInfoMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     * @return
     * @throws Exception
     */
    public List<ProductInfo> listByProperty(ProductInfo productInfo)
            throws Exception
    {
        return productInfoMapper.listByProperty(productInfo);
    }

    /**
     * 根据查询条件查询分页记录
     * @return
     * @throws Exception
     */
    public Pagination<ProductInfo> listPaginationByProperty(Pagination<ProductInfo> pagination, ProductInfo productInfo) throws Exception
    {
        List<ProductInfo> list = productInfoMapper.listPaginationByProperty(pagination, productInfo);

        pagination.setResultList(list);

        return pagination;
    }

    /**
     * 根据主键删除记录
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public int deleteByPK(java.lang.Integer primaryKey) throws Exception
    {
        return productInfoMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
    {
        productInfoMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     * @param productInfo
     * @return
     * @throws Exception
     */
    public int deleteByProperty(ProductInfo productInfo) throws Exception
    {
        return productInfoMapper.deleteByProperty(productInfo);
    }

    /**
     * 保存记录
     * @param productInfo
     * @return
     * @throws Exception
     */
    public void save(ProductInfo productInfo) throws Exception
    {
        productInfoMapper.save(productInfo);
    }

    /**
     * 更新记录
     * @param productInfo
     * @return
     * @throws Exception
     */
    public int update(ProductInfo productInfo) throws Exception
    {
        return productInfoMapper.update(productInfo);
    }

    /**
     * 根据条件查询记录条数
     * @param productInfo
     * @return
     * @throws Exception
     */
    public int findByCount(ProductInfo productInfo) throws Exception
    {
        return productInfoMapper.findByCount(productInfo);
    }
}