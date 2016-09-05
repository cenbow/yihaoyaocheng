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
import com.yyw.yhyc.product.dto.ProductInventoryDto;
import org.apache.ibatis.annotations.Param;

public interface ProductInventoryMapper extends GenericIBatisMapper<ProductInventory, Integer> {

	public List<ProductInventoryDto> listPaginationByProperty(Pagination<ProductInventoryDto> pagination, ProductInventoryDto productInventoryDto);

	public void updateInventory(ProductInventory productInventory);

	public ProductInventory findBySupplyIdSpuCode(@Param("supplyId") Integer supplyId,@Param("spuCode") String spuCode);

	/**
	 * 冻结库存
	 * @param productInventory
	 */
	public void updateFrozenInventory(ProductInventory productInventory);

	/**
	 * 释放库存
	 * @param productInventory
	 */
	public void updateReleaseInventory(ProductInventory productInventory);

	/**
	 * 扣减库存
	 * @param productInventory
	 */
	public void updateDeductionInventory(ProductInventory productInventory);
}
