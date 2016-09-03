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
package com.yyw.yhyc.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.product.bo.ProductAudit;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.product.mapper.ProductAuditMapper;

@Service("productAuditService")
public class ProductAuditService {

	private ProductAuditMapper	productAuditMapper;

	@Autowired
	public void setProductAuditMapper(ProductAuditMapper productAuditMapper)
	{
		this.productAuditMapper = productAuditMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ProductAudit getByPK(Integer primaryKey) throws Exception
	{
		return productAuditMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductAudit> list() throws Exception
	{
		return productAuditMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ProductAudit> listByProperty(ProductAudit productAudit)
			throws Exception
	{
		return productAuditMapper.listByProperty(productAudit);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ProductAudit> listPaginationByProperty(Pagination<ProductAudit> pagination, ProductAudit productAudit) throws Exception
	{
		List<ProductAudit> list = productAuditMapper.listPaginationByProperty(pagination, productAudit);

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
		return productAuditMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		productAuditMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param productAudit
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ProductAudit productAudit) throws Exception
	{
		return productAuditMapper.deleteByProperty(productAudit);
	}

	/**
	 * 保存记录
	 * @param productAudit
	 * @return
	 * @throws Exception
	 */
	public void save(ProductAudit productAudit) throws Exception
	{
		productAuditMapper.save(productAudit);
	}

	/**
	 * 更新记录
	 * @param productAudit
	 * @return
	 * @throws Exception
	 */
	public int update(ProductAudit productAudit) throws Exception
	{
		return productAuditMapper.update(productAudit);
	}

	/**
	 * 根据条件查询记录条数
	 * @param productAudit
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ProductAudit productAudit) throws Exception
	{
		return productAuditMapper.findByCount(productAudit);
	}
}