/**
 * Created By: XI
 * Created On: 2016-8-30 11:47:20
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.mapper;

import java.util.List;

import com.yyw.yhyc.product.bo.ProductAudit;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import org.apache.ibatis.annotations.Param;

public interface ProductAuditMapper extends GenericIBatisMapper<ProductAudit, Integer> {

	public List<ProductAudit> listPaginationByProperty(Pagination<ProductAudit> pagination, ProductAudit productAudit);

    public ProductAudit getProductcodeCompany(@Param("sellerCode") Integer sellerCode,@Param("productcodeCompany") String productcodeCompany);
}
