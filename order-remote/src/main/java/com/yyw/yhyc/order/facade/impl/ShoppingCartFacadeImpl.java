/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.ShoppingCartFacade;
import com.yyw.yhyc.order.service.ShoppingCartService;

@Service("shoppingCartFacade")
public class ShoppingCartFacadeImpl implements ShoppingCartFacade {

	private ShoppingCartService shoppingCartService;
	
	@Autowired
	public void setShoppingCartService(ShoppingCartService shoppingCartService)
	{
		this.shoppingCartService = shoppingCartService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> list() throws Exception
	{
		return shoppingCartService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> listByProperty(ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartService.listByProperty(shoppingCart);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartService.listPaginationByProperty(pagination, shoppingCart);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		shoppingCartService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.deleteByProperty(shoppingCart);
	}

	/**
	 * 保存记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public void save(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.save(shoppingCart);
	}

	/**
	 * 更新记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int update(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.update(shoppingCart);
	}

	/**
	 * 根据条件查询记录条数
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.findByCount(shoppingCart);
	}
}
