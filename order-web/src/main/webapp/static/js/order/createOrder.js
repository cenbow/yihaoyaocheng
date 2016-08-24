/**
 * Created by lizhou on 2016/8/5
 */

/**
 *   发送创建订单请求
 */
function createOrder(){
    $("#createOrderButton").removeAttr("onsubmit");

    var receiveAddressId = $("#receiveAddressId").val();
    if(receiveAddressId == null || receiveAddressId == '' || typeof  receiveAddressId == 'undefined'){
        alert("请选择收货地址！");
        return;
    }
    var billType = $("#billType").val();
    if(billType == null || billType == '' || typeof  billType == 'undefined'){
        alert("请选择发票类型！");
        return;
    }
    
    /* 校验所有供应商下的支付类型是否选中 */
    var allPayTypeSelected = true;
    var _supplyName = ""; 
    $(".payTypeId").each(function(_index,element){
        // console.info("_index=" + _index + ",element=" + element);
        var _payTypeId = $(this).val();

        if( !$(_payTypeId) || _payTypeId == null || _payTypeId == "" || typeof _payTypeId == "undefined"){
            console.info("_payTypeId=" + _payTypeId + ",_supplyName=" + _supplyName);
            _supplyName = $(this).attr("supplyName");
            allPayTypeSelected = false;
        }
    });
    if(!allPayTypeSelected){
        alert("供应商("+_supplyName+")下的商品未选择支付方式");
        return;
    }

    /* 检查购物车是否有商品 */
    var productIdArray = new Array();
    $("input[name='productId']").each(function(_index,element){
        var _productId = $(this).val();
        if( _productId != null && _productId != "" && typeof _productId != "undefined"){
            productIdArray[_index] = _productId;
        }
    });
    if(productIdArray.length == 0 ){
        alert("购物车中没有商品!");
        return;
    }


    $("#createOrderForm").attr({"action": ctx + "/order/createOrder"});
    $("#createOrderForm").ajaxSubmit(function(_resultJsonObj) {
        if(_resultJsonObj.message != null && _resultJsonObj.message != ""){
            console.info(_resultJsonObj.message );
            alert(_resultJsonObj.message);
            return;
        }

        try{
            if(_resultJsonObj.url == null || "" == _resultJsonObj.url || typeof _resultJsonObj.url == "undefined"){
                console.info();
                alert("服务器异常!");
            }

            /* 由于Java 后端代码使用登录拦截器，若登录拦截器异常，则返回的 _resultJsonObj 变量 是 登录页面的html代码 。所以此处校验返回的url是否为创建订单页的url */
            var existUrlStr = _resultJsonObj.url.indexOf("/order/createOrderSuccess?orderIds=") != -1;
            console.info("existUrlStr = " + existUrlStr);
            if( existUrlStr ){
                window.location.href = ctx + _resultJsonObj.url;
            }
        }catch (e){
            console.info(e.name + ": " + e.message);
            alert("服务器异常!");
        }
        
    });
    
}

