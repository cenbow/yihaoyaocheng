/**
 * Created By: XI
 * Created On: 2016-8-29 11:24:18
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.mapper;

import java.util.List;

import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.product.bo.ProductInventoryLog;

public interface ProductInventoryLogMapper extends GenericIBatisMapper<ProductInventoryLog, Integer> {

	public List<ProductInventoryLog> listPaginationByProperty(Pagination<ProductInventoryLog> pagination, ProductInventoryLog productInventoryLog);
}
