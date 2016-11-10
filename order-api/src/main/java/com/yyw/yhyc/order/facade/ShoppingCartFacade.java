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
package com.yyw.yhyc.order.facade;

import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.bo.Pagination;

public interface ShoppingCartFacade {
/*
	*//**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 *//*
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception;

	*//**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 *//*
	public List<ShoppingCart> list() throws Exception;

	*//**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 *//*
	public List<ShoppingCart> listByProperty(ShoppingCart shoppingCart)
			throws Exception;

	*//**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 *//*
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception;
	
	*//**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 *//*
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception;

	*//**
	 * 根据传入参数删除记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 *//*
	public int deleteByProperty(ShoppingCart shoppingCart) throws Exception;

	*//**
	 * 保存记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 *//*
	public void save(ShoppingCart shoppingCart) throws Exception;

	*//**
	 * 更新记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 *//*
	public int update(ShoppingCart shoppingCart) throws Exception;

	*//**
	 * 根据条件查询记录条数
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 *//*
	public int findByCount(ShoppingCart shoppingCart) throws Exception;
	
	*//**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 *//*
	public Pagination<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart) throws Exception;*/

	/**
	 * 查询进货单商品数量
	 * @param custId 企业ID
	 * @return
	 * @throws Exception
     */
	int findShoppingCartByCount(Integer custId);

	/**
	 * 加入进货单
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
     */
	@Deprecated
	Map<String, Object> addShoppingCart(ShoppingCart shoppingCart);

	/**
	 * 加入进货单
	 * @param shoppingCart 进货单对象
	 * @param sessionId 会话id
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 */
	Map<String, Object> addShoppingCart(ShoppingCart shoppingCart,String sessionId);
}
