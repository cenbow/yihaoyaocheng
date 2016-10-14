package com.yyw.yhyc.product.mapper;

/**
 * Created by lizhou on 2016/8/1.
 */
import java.util.List;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.product.bo.ProductInfo;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import org.apache.ibatis.annotations.Param;


public interface ProductInfoMapper extends GenericIBatisMapper<ProductInfo, Integer> {

    public List<ProductInfo> listPaginationByProperty(Pagination<ProductInfo> pagination, ProductInfo productInfo);

    /**
     * 根据 厂家id 查找生产厂家信息
     * @param manufacturesId
     * @return
     */
    ProductInfoDto getFactory(@Param("manufacturesId") int manufacturesId);
}
