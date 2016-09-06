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
package com.yyw.yhyc.product.facade;
import java.util.List;
import java.util.Map;
import com.yyw.yhyc.product.bo.ProductInventory;


public interface ProductInventoryFacade {

	/**
	 * 获取库存
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getProductInventory(ProductInventory productInventory) throws Exception;

	/**
	 * 根据供应商修改-保存、库存信息
	 * @param productInventoryList
	 * @return
	 * @throws Exception
	 */
	public List<ProductInventory> updateInventory(List<ProductInventory> productInventoryList) throws Exception;

}
