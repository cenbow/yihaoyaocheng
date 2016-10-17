package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhou on 2016/10/12
 * 极速下单业务处理
 */
@Controller
@RequestMapping(value = "/fastOrder")
public class FastOrderController extends BaseJsonController {

    private static final Logger logger = LoggerFactory.getLogger(FastOrderController.class);

    @Autowired
    private ShoppingCartService shoppingCartService;


    @Autowired
    private OrderService orderService;


    @Reference
    private IProductDubboManageService iProductDubboManageService;
    @Reference
    private ICustgroupmanageDubbo iCustgroupmanageDubbo;

    @Reference
    private ProductSearchInterface productSearchInterface;

    /**
     * 添加商品到进货单
     * 请求数据格式如下：

     {
     "custId": 6066,                //采购商企业编号
     "supplyId": "6067",            //供应商企业编号
     "spuCode": "010BAA3040006",    // 商品SPU编码
     "productId": "7",             //商品id
     "productCount": 1,            //商品数量
     "productPrice": 12,           //商品单价
     "productCodeCompany": "3545451",  //商品的本公司编码
     "specification":"1g*12板"        //商品规格
     "manufactures" : "测试生产厂商"  //生产厂商名称
     "fromWhere" : 1                  //极速下单业务时，传固定值 ： 1
     }

     * @param shoppingCart
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart) throws Exception {
		/* 获取登陆用户的企业信息 */
        UserDto userDto = super.getLoginUser();
        logger.info("当前登陆的用户信息userDto=" + userDto);

        if(UtilHelper.isEmpty(shoppingCart)){
            throw  new Exception("非法参数");
        }
        if( ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() != shoppingCart.getFromWhere()) {
            throw  new Exception("非法参数");
        }

        Map<String, Object> result = null;
        try{
            shoppingCart.setCustId(userDto.getCustId());
            result = shoppingCartService.addShoppingCart(shoppingCart);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw  new Exception(e.getMessage());
        }

        if( UtilHelper.isEmpty(result) ||  !"S".equals(result.get("state")+"")) {
            throw new Exception("添加商品失败");
        }
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByProperty(shoppingCart);
        if(!UtilHelper.isEmpty(shoppingCartList) && shoppingCartList.size() == 1){
            ShoppingCart s = shoppingCartList.get(0);
            result.put("shoppingCartId",s.getShoppingCartId());
        }
        return ok("添加成功",result);
    }

//    /**
//     * 根据多条主键值删除记录
//     * @return
//     */
//    @RequestMapping(value = "/deleteShopCarts", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> deleteShopCarts(@RequestBody Map<String,List<Integer>> shoppingCartIdList) throws Exception {
//        UserDto userDto = super.getLoginUser();
//        int custId = userDto.getCustId();
//        return shoppingCartService.deleteShopCarts(custId,shoppingCartIdList.get("shoppingCartIdList"),iProductDubboManageService);
//    }

    /**
     * 根据多条主键值删除记录
     * 请求参数：
     *      { "list":[110,111]}
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public  Map<String,Object>  delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception {
        shoppingCartService.deleteByPKeys(requestListModel.getList());
        return ok("删除成功");
    }

    /**
     * 更极速下单中数量
     * 请求参数：
     *
     *
     {
     "shoppingCartId": 117,
     "productCount": 1
     }

     * @throws Exception
     */
    @RequestMapping(value = "/updateNum", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateNum(@RequestBody ShoppingCart shoppingCart) throws Exception {
        UserDto userDto = super.getLoginUser();
        logger.info("当前登陆的用户信息userDto=" + userDto);
        if(!UtilHelper.isEmpty(shoppingCart)){
            shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
        }
        shoppingCartService.updateNum(shoppingCart,userDto);
        return ok("修改成功");
    }

    /**
     * 列表查询
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> list() throws Exception {
        UserDto userDto = super.getLoginUser();
        logger.info("当前登陆的用户信息userDto=" + userDto);
        List<ShoppingCartListDto> allShoppingCart = shoppingCartService.listForFastOrder(userDto,iProductDubboManageService,ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
        logger.info("极速下单页面的商品数据，allShoppingCart=" + allShoppingCart);
        String message = UtilHelper.isEmpty(allShoppingCart) || allShoppingCart.size() == 0 ? "请添加商品" : "";
        return ok(message,allShoppingCart);
    }




    /**
     * 检查极速下单中的商品合法信息
     * 请求参数：
     [
     {"shoppingCartId":123,"supplyId":6065},
     {"shoppingCartId":1234,"supplyId":6548}
     ]
     * @param shoppingCartList
     * @throws Exception
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> check(@RequestBody List<ShoppingCart> shoppingCartList) throws Exception {
        UserDto userDto = super.getLoginUser();
        logger.info("当前登陆的用户信息userDto=" + userDto);

        Map<String,Object> resultMap = new HashMap<>();
        if(UtilHelper.isEmpty(shoppingCartList)){
            resultMap.put("result",false);
            resultMap.put("message","您的进货单中没有商品");
            return resultMap;
        }
		/* 查找出当前买家的进货单里面，有哪些供应商 */
        List<ShoppingCart> custIdAndSupplyIdList = shoppingCartService.listDistinctCustIdAndSupplyId(userDto.getCustId());
        if(UtilHelper.isEmpty(custIdAndSupplyIdList)){
            resultMap.put("result",false);
            resultMap.put("message","您的进货单中没有商品");
            return resultMap;
        }

        OrderDto orderDto = null;
        List<ProductInfoDto> productInfoDtoList = null;
        ProductInfoDto productInfoDto = null;
        for(ShoppingCart custIdAndSupplyId : custIdAndSupplyIdList){
            if(UtilHelper.isEmpty(custIdAndSupplyId)) continue;
            orderDto = new OrderDto();
            orderDto.setCustId(custIdAndSupplyId.getCustId());
            orderDto.setSupplyId(custIdAndSupplyId.getSupplyId());

			/* 遍历进货单中 选中的商品 */
            productInfoDtoList = new ArrayList<>();
            for( ShoppingCart shoppingCart : shoppingCartList){
                if(UtilHelper.isEmpty(shoppingCart)) continue;
                if(custIdAndSupplyId.getSupplyId().equals(shoppingCart.getSupplyId())){
                    productInfoDto = new ProductInfoDto();
                    ShoppingCart temp = shoppingCartService.getByPK(shoppingCart.getShoppingCartId());
                    if(UtilHelper.isEmpty(temp)) continue;
                    productInfoDto.setId(temp.getProductId());
                    productInfoDto.setSpuCode(temp.getSpuCode());
                    productInfoDto.setProductPrice(temp.getProductPrice());
                    productInfoDto.setProductCount(temp.getProductCount());
                    productInfoDto.setFromWhere(ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
                    productInfoDtoList.add(productInfoDto);
                }
            }
            if(productInfoDtoList.size() == 0) continue;
            orderDto.setProductInfoDtoList(productInfoDtoList);

			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
            resultMap = orderService.validateProducts(userDto, orderDto,iCustgroupmanageDubbo,iProductDubboManageService, productSearchInterface);
            boolean result = (boolean) resultMap.get("result");
            if(!result){
                return ok(resultMap);
            }
        }
        resultMap.put("result",true);
        return ok(resultMap);
    }







    private Map<String,Object> ok(Object data){
        Map<String,Object> result  = new HashMap<String,Object>();
        result.put("statusCode","200");
        result.put("message","");
        result.put("data",data);
        return  result;
    }
    private Map<String,Object> ok(String  message){
        Map<String,Object> result  = new HashMap<String,Object>();
        result.put("statusCode","200");
        result.put("message",message);
        result.put("data",null);
        return  result;
    }
    private Map<String,Object> ok(String  message,Object data){
        Map<String,Object> result  = new HashMap<String,Object>();
        result.put("statusCode","200");
        result.put("message",message);
        result.put("data",data);
        return  result;
    }

}
