/**
 * Created By: XI
 * Created On: 2016-8-29 11:23:12
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.mapper;

import java.util.List;

import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface ProductInventoryMapper extends GenericIBatisMapper<ProductInventory, Integer> {

	public List<ProductInventory> listPaginationByProperty(Pagination<ProductInventory> pagination, ProductInventory productInventory);
}
