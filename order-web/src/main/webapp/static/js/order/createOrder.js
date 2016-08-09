/**
 * Created by lizhou on 2016/8/5.
 */

/**
 * 获取检查订单页数据
 */
function checkOrderPage(){
    $.ajax({
        "url" : ctx + "/order/checkOrder",
        "type":"post",
        "dataType" : "json",
        success:function(data){
            alert("创建订单成功！");
        },
        error:function (data) {
            alert("创建订单失败！");
        }
    });
}

//支付方式单选按钮
function selectPayTypeId(_supplyId,_payTypeId){
    var _supplyPayTypeId = "#" + _supplyId + "_payTypeId";
    $(_supplyPayTypeId).val(_payTypeId);

    $('.radio-select label').click(function(){
        $(this).parent('div').find('.radio-skin').removeClass('radio-skin-selected');
        $(this).find('i:first').addClass('radio-skin-selected');
    })
}

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
    $("input[name='supplyId']").each(function(_index,element){
        // console.info("_index=" + _index + ",element=" + element);
        var _supplyId = $(this).val();
        var _payTypeId = "#"+_supplyId +"_payTypeId";
        var payType = $(_payTypeId).val();

        if( !$(_payTypeId) || payType == null || payType == "" || typeof payType == "undefined"){
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

    
    // alert("发送创建订单请求！");
    

    // var productInfoArray = new Array();
    // $("input[name='productInfoList']").each(function(_index,element){
    //     var productId = $(this).attr("productId");
    //     var quantity = $(this).attr("quantity");
    //     var supplyId = $(this).attr("supplyId");
    //     var product={'productId':productId,'quantity':quantity,'supplyId':supplyId};
    //     productInfoArray.push(product);
    // });
    // var orderDtoList = new Array();
    // //TODO 遍历循环每个供应商
    // //foreach{
    //     orderDtoList.push(productInfoArray);
    //  // }
    //
    // var orderCreatDto = {custId:"123",receiveAddressId:"2",orderDtoList: orderDtoList};
    // $.ajax({
    //     "url" : ctx + "/order/createOrder",
    //     "type":"post",
    //     "dataType" : "json",
    //     "data" : JSON.stringify(orderCreatDto),
    //     success:function(data){
    //         alert("创建订单成功！");
    //     },
    //     error:function (data) {
    //         alert("创建订单失败！");
    //     }
    // });

    $("#createOrderForm").attr({"action": ctx + "/order/createOrder"});
    $("#createOrderForm").ajaxSubmit(function(resultJson) {
        var _targetUrl = ctx + resultJson;
        // window.open(_targetUrl,'_self');
        
        window.opener=null;window.open('','_self');window.close();
        window.open(_targetUrl,'_self');
        // window.history.forward(1);
        // window.location.href = _targetUrl;  
    });
    
}

