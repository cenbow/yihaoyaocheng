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
package com.yyw.yhyc.product.facade.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.facade.ProductInventoryFacade;
import com.yyw.yhyc.product.service.ProductInventoryService;

@Service("productInventoryFacade")
public class ProductInventoryFacadeImpl implements ProductInventoryFacade {

	private ProductInventoryService productInventoryService;
	
	@Autowired
	public void setProductInventoryService(ProductInventoryService productInventoryService)
	{
		this.productInventoryService = productInventoryService;
	}

	/**
	 * 获取库存
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getProductInventory(ProductInventory productInventory) throws Exception{
		return  productInventoryService.getProductInventory(productInventory);
	}
}
