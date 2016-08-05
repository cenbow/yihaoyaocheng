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
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;

public interface ShoppingCartMapper extends GenericIBatisMapper<ShoppingCart, Integer> {

	public List<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart);

	List<ShoppingCartListDto> listAllShoppingCart(ShoppingCart shoppingCart);
	List<ShoppingCartDto> listShoppingCartDtoByProperty(ShoppingCart shoppingCart);

}
