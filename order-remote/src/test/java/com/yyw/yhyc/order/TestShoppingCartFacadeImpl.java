package com.yyw.yhyc.order;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.facade.ShoppingCartFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by shiyongxi on 2016/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class TestShoppingCartFacadeImpl extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private ShoppingCartFacade shoppingCartFacade;

    @Test
    public void findShoppingCartByCount(){
        int count = shoppingCartFacade.findShoppingCartByCount(6066);
        System.out.println("count-->" + count);
    }

    @Test
    public void addShoppingCart(){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCustId(6066);
        shoppingCart.setProductId(7);
        shoppingCart.setProductCount(5);
        shoppingCart.setProductName("硫唑嘌呤片");
        shoppingCart.setSpecification("50mg*100s");
        shoppingCart.setSupplyId(6065);
        shoppingCart.setSpuCode("010BAA3040003");
        shoppingCart.setManufactures("英葛—威康");
        shoppingCart.setProductPrice(new BigDecimal(120.000));
        shoppingCart.setProductSettlementPrice(new BigDecimal(120.000));
        shoppingCart.setCreateUser("xi");
        shoppingCart.setUpdateUser("xi");

        Map<String, Object> map = shoppingCartFacade.addShoppingCart(shoppingCart);
        System.out.println("map-->" + map);
    }
}
