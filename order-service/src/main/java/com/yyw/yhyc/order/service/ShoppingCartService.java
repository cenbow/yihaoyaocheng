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
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.List;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;

@Service("shoppingCartService")
public class ShoppingCartService {

	private ShoppingCartMapper	shoppingCartMapper;

	@Autowired
	public void setShoppingCartMapper(ShoppingCartMapper shoppingCartMapper)
	{
		this.shoppingCartMapper = shoppingCartMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> list() throws Exception
	{
		return shoppingCartMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> listByProperty(ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartMapper.listByProperty(shoppingCart);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart) throws Exception
	{
		List<ShoppingCart> list = shoppingCartMapper.listPaginationByProperty(pagination, shoppingCart);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		shoppingCartMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.deleteByProperty(shoppingCart);
	}

	/**
	 * 保存记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public void save(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartMapper.save(shoppingCart);
	}

	/**
	 * 更新记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int update(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.update(shoppingCart);
	}

	/**
	 * 根据条件查询记录条数
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.findByCount(shoppingCart);
	}

	public List<ShoppingCartListDto> listAllShoppingCart(ShoppingCart shoppingCart) {
		return shoppingCartMapper.listAllShoppingCart(shoppingCart);
	}

	/**
	 *
	 * @param shoppingCart
	 * @param userDto
     */
	public int updateNum(ShoppingCart shoppingCart, UserDto userDto) {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId())){
			return 0 ;
		}
		ShoppingCart oldShoppingCart =  shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			return 0;
		}
		ShoppingCart newShoppingCart = new ShoppingCart();
		newShoppingCart.setShoppingCartId(shoppingCart.getShoppingCartId());
		newShoppingCart.setProductCount(shoppingCart.getProductCount());
		newShoppingCart.setProductSettlementPrice( oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		newShoppingCart.setUpdateUser(userDto.getUserName());
		return shoppingCartMapper.update(newShoppingCart);
	}
}