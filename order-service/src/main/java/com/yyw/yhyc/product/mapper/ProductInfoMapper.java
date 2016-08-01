package com.yyw.yhyc.product.mapper;

/**
 * Created by lizhou on 2016/8/1.
 */
import java.util.List;

import com.yyw.yhyc.product.bo.ProductInfo;
import com.yyw.yhyc.order.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.Pagination;

public interface ProductInfoMapper extends GenericIBatisMapper<ProductInfo, java.lang.Integer> {

    public List<ProductInfo> listPaginationByProperty(Pagination<ProductInfo> pagination, ProductInfo productInfo);
}
