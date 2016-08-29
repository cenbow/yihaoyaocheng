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
package com.yyw.yhyc.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;

@Service("productInventoryService")
public class ProductInventoryService {

	private ProductInventoryMapper	productInventoryMapper;

	@Autowired
	public void setProductInventoryMapper(ProductInventoryMapper productInventoryMapper)
	{
		this.productInventoryMapper = productInventoryMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ProductInventory getByPK(Integer primaryKey) throws Exception
	{
		return productInventoryMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductInventory> list() throws Exception
	{
		return productInventoryMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductInventory> listByProperty(ProductInventory productInventory)
			throws Exception
	{
		return productInventoryMapper.listByProperty(productInventory);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ProductInventory> listPaginationByProperty(Pagination<ProductInventory> pagination, ProductInventory productInventory) throws Exception
	{
		List<ProductInventory> list = productInventoryMapper.listPaginationByProperty(pagination, productInventory);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return productInventoryMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		productInventoryMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ProductInventory productInventory) throws Exception
	{
		return productInventoryMapper.deleteByProperty(productInventory);
	}

	/**
	 * 保存记录
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public void save(ProductInventory productInventory) throws Exception
	{
		productInventoryMapper.save(productInventory);
	}

	/**
	 * 更新记录
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public int update(ProductInventory productInventory) throws Exception
	{
		return productInventoryMapper.update(productInventory);
	}

	/**
	 * 根据条件查询记录条数
	 * @param productInventory
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ProductInventory productInventory) throws Exception
	{
		return productInventoryMapper.findByCount(productInventory);
	}
}