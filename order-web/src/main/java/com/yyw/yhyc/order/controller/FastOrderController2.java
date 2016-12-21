package com.yyw.yhyc.order.controller;

import static com.yyw.yhyc.order.inteceptor.GetUserInteceptor.CACHE_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.service.FastOrderService;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.utils.CacheUtil;

/**
 * Created by hedanjun on 2016/12/13
 * 极速下单业务处理
 */
@Controller
@RequestMapping(value = "/fastOrder2")
public class FastOrderController2 extends BaseJsonController {

    private static final Logger logger = LoggerFactory.getLogger(FastOrderController2.class);

    @Autowired
    private FastOrderService fastOrderService;

    @Reference
    private IProductDubboManageService iProductDubboManageService;

    @Autowired
    private HttpServletRequest request;

    @Reference
    private IPromotionDubboManageService iPromotionDubboManageService;

    /**
     * 添加商品到进货单
     * 请求数据格式如下：
	{
	    "custId": "33173",
	    "supplyId": "33182",
	    "spuCode": "SH00001CAAC80001",
	    "productId": "114225",
	    "productName": "dyy商品测试1103",
	    "productCount": "3",
	    "productPrice": "0.009",
	    "productCodeCompany": "bgs1103",
	    "specification": "12*12克",
	    "manufactures": "dyy生产企业001",
	    "promotionId": "12457",
	    "promotionName": "特价活动测试-何丹均",
	    "fromWhere": 1
	}
     * @param shoppingCart
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart) throws Exception {
    	 Map<String, Object> result = null;
    	 try{
			//1、获取登陆用户的企业信息 
	        UserDto userDto = getUserDto(request);
	        logger.info("start addShoppingCart for ["+shoppingCart+"]");
	        //2、执行新增操作
            shoppingCart.setCustId(userDto.getCustId());
            shoppingCart.setCreateUser(userDto.getUserName());
            shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
            result = fastOrderService.addShoppingCart(shoppingCart);
            logger.info("success addShoppingCart , result=" + result);
            //3、返回结果
            if( "S".equals(result.get("state").toString())) {
            	return ok("添加成功",result);
            }else{
            	throw new Exception("添加失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw  new Exception(e.getMessage());
        }
    }

    /**
     * 根据多条主键值删除记录
     * 请求参数：
     *      { "list":[110,111]}
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public  Map<String,Object>  delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception {
    	try{
	    	logger.info("start delete ShoppingCart");
	    	fastOrderService.deleteByPKeys(requestListModel.getList());
	    	logger.info("success delete ShoppingCart");
	        return ok("删除成功",null);
	    }catch (Exception e){
	        logger.error("exception when delete",e);
	        throw  new Exception(e.getMessage());
	    }
    }

    /**
     * 更新商品数量
     * 请求数据格式如下：
     {
	     "shoppingCartId": 117,
	     "productCount": 1
     }
     * @throws Exception
     */
    @RequestMapping(value = "/updateNum", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateNum(@RequestBody ShoppingCart shoppingCart) throws Exception {
    	try{
    		 UserDto userDto = getUserDto(request);
	        logger.info("start updateNum for ["+shoppingCart+"]");
	        shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
	        shoppingCart.setUpdateUser(userDto.getUserName());
	        fastOrderService.updateNum(shoppingCart);
	        logger.info("success updateNum for ["+shoppingCart+"]");
	        return ok("修改成功",null);
	    }catch (Exception e){
	        logger.error("exception when updateNum",e);
	        throw  new Exception(e.getMessage());
	    }
    }

	/**
     * 列表查询
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> list() throws Exception {
    	try{
    		UserDto userDto = getUserDto(request);
            logger.info("start list for [CustId="+userDto.getCustId()+"]");
            List<ShoppingCartListDto> allShoppingCart = fastOrderService.listShoppingCart(userDto.getCustId(),ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
            logger.info("success list , allShoppingCart=" + allShoppingCart);
            return ok("success",allShoppingCart);
	    }catch (Exception e){
	        logger.error("exception when list",e);
	        throw  new Exception(e.getMessage());
	    }
    }
    
	/**
     * 查询进货单列表
     * 
      {
	    "custId": "33173",
	    "supplyId": "33182",
	    "fromWhere": 1
	  } 
     * 
     * @return
     */
    @RequestMapping(value = "/listShoppingCart", method = RequestMethod.POST)
    @ResponseBody
    public List<ShoppingCartListDto> listShoppingCart(@RequestBody ShoppingCart shoppingCart) throws Exception {
    	try{
            logger.info("start listShoppingCart for ["+shoppingCart+"");
            List<ShoppingCartListDto> allShoppingCart = fastOrderService.listShoppingCart(shoppingCart);
            logger.info("success listShoppingCart , allShoppingCart=" + allShoppingCart);
            return allShoppingCart;
	    }catch (Exception e){
	        logger.error("exception when listShoppingCart",e);
	        throw  new Exception(e.getMessage());
	    }
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
        UserDto userDto = getUserDto(request);
        
        //1、参数校验
        if(UtilHelper.isEmpty(shoppingCartList)){
            throw new Exception("您没有选择商品！");
        }
        
        //2、查找出当前买家的进货单里面，有哪些供应商 
        List<ShoppingCart> custIdAndSupplyIdList = fastOrderService.listDistinctCustIdAndSupplyId(userDto.getCustId());
        if(UtilHelper.isEmpty(custIdAndSupplyIdList)){
            throw new Exception("您的进货单中没有商品！");
        }
        //3、按供应商分组，检查商品信息
		for(ShoppingCart custIdAndSupplyId : custIdAndSupplyIdList){
			//1、获取商品信息
        	List<ProductInfoDto>  productInfoDtoList = new ArrayList<>();
            for( ShoppingCart shoppingCart : shoppingCartList){
                if( custIdAndSupplyId.getSupplyId().equals(shoppingCart.getSupplyId()) ){
                	ProductInfoDto productInfoDto = new ProductInfoDto();
                    ShoppingCart temp = fastOrderService.getByPK(shoppingCart.getShoppingCartId());
                    if(UtilHelper.isEmpty(temp)){
                    	throw new Exception("极速订单数据有变化，请刷新页面！");
                    }
                    productInfoDto.setId(temp.getProductId());
                    productInfoDto.setSpuCode(temp.getSpuCode());
                    productInfoDto.setProductPrice(temp.getProductPrice());
                    productInfoDto.setProductCount(temp.getProductCount());
                    productInfoDto.setFromWhere(ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere());
                    productInfoDto.setPromotionId(temp.getPromotionId());
                    productInfoDto.setPromotionName(temp.getPromotionName());
                    productInfoDtoList.add(productInfoDto);
                }
            }
            OrderDto orderDto = new OrderDto();
            if(productInfoDtoList.isEmpty()){
            	continue;
            }
            orderDto.setProductInfoDtoList(productInfoDtoList);
        	
         	//2、商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 
            orderDto.setCustId(custIdAndSupplyId.getCustId());
            orderDto.setSupplyId(custIdAndSupplyId.getSupplyId());
            String message = fastOrderService.validateProducts(userDto, orderDto);
            //3、校验失败，直接返回失败原因
 			if( !"success".equals(message) ){
 				 throw new Exception(message);
 			}
		}

        //4、全部校验通过，返回成功
		Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",true);
        return ok("success",resultMap);
    }

    /**
     * 返回结果
     * @param message
     * @param data
     * @return
     */
    private Map<String,Object> ok(String message,Object data){
        Map<String,Object> result  = new HashMap<String,Object>();
        result.put("statusCode","200");
        result.put("message",message);
        result.put("data",data);
        return  result;
    }

    /**
     * 获取当前登陆的用户信息
     * @param request
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private UserDto getUserDto(HttpServletRequest request){
    	String sessionId = request.getHeader("mySessionId");
        String cacheUser = CacheUtil.getSingleton().get(CACHE_PREFIX + sessionId);
        logger.info("mySessionId=["+sessionId+"] , cacheUser=[" + cacheUser+"]");
        //用户信息
        if(!UtilHelper.isEmpty(cacheUser)) {
            Map userMap = JSONObject.parseObject(cacheUser, HashMap.class);
            UserDto userDto = new UserDto();
            userDto.setUser(userMap);
            userDto.setUserName(userMap.get("username")+"");
            userDto.setCustId(Integer.valueOf(userMap.get("enterprise_id")+""));
            return userDto;
        }
        return null;
    }
}
