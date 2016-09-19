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
package com.yyw.yhyc.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.product.bo.ProductInventoryLog;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.product.mapper.ProductInventoryLogMapper;

@Service("productInventoryLogService")
public class ProductInventoryLogService {

	private ProductInventoryLogMapper	productInventoryLogMapper;

	@Autowired
	public void setProductInventoryLogMapper(ProductInventoryLogMapper productInventoryLogMapper)
	{
		this.productInventoryLogMapper = productInventoryLogMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ProductInventoryLog getByPK(Integer primaryKey) throws Exception
	{
		return productInventoryLogMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductInventoryLog> list() throws Exception
	{
		return productInventoryLogMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductInventoryLog> listByProperty(ProductInventoryLog productInventoryLog)
			throws Exception
	{
		return productInventoryLogMapper.listByProperty(productInventoryLog);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ProductInventoryLog> listPaginationByProperty(Pagination<ProductInventoryLog> pagination, ProductInventoryLog productInventoryLog) throws Exception
	{
		List<ProductInventoryLog> list = productInventoryLogMapper.listPaginationByProperty(pagination, productInventoryLog);

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
		return productInventoryLogMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		productInventoryLogMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param productInventoryLog
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ProductInventoryLog productInventoryLog) throws Exception
	{
		return productInventoryLogMapper.deleteByProperty(productInventoryLog);
	}

	/**
	 * 保存记录
	 * @param productInventoryLog
	 * @return
	 * @throws Exception
	 */
	public void save(ProductInventoryLog productInventoryLog) throws Exception
	{
		productInventoryLogMapper.save(productInventoryLog);
	}

	/**
	 * 更新记录
	 * @param productInventoryLog
	 * @return
	 * @throws Exception
	 */
	public int update(ProductInventoryLog productInventoryLog) throws Exception
	{
		return productInventoryLogMapper.update(productInventoryLog);
	}

	/**
	 * 根据条件查询记录条数
	 * @param productInventoryLog
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ProductInventoryLog productInventoryLog) throws Exception
	{
		return productInventoryLogMapper.findByCount(productInventoryLog);
	}
}