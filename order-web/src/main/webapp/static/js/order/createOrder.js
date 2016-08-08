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
    $("#createOrderForm").submit();
}

