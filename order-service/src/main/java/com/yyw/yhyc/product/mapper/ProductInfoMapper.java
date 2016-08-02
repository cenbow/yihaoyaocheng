package com.yyw.yhyc.product.mapper;

/**
 * Created by lizhou on 2016/8/1.
 */
import java.util.List;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.product.bo.ProductInfo;


public interface ProductInfoMapper extends GenericIBatisMapper<ProductInfo, Integer> {

    public List<ProductInfo> listPaginationByProperty(Pagination<ProductInfo> pagination, ProductInfo productInfo);
}
