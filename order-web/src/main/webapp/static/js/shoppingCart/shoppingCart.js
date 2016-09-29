function deleteShoppingCart(_shoppingCartId){
    if(_shoppingCartId == null || _shoppingCartId == "" || typeof _shoppingCartId == "undefined"){
        return;
    }
    var _data = {"list":[_shoppingCartId]};
    $.ajax({
        url:ctx + "/shoppingCart/delete",
        data:JSON.stringify(_data),
        type:"post",
        dataType:"json",   //返回参数类型
        contentType :"application/json",   //请求参数类型
        success:function(data){
            console.info(data);
        },
        error:function(){

        }
    })
}

function deleteSelectedShoppingCart(){
    var _shoppingCartIdList = new Array();
    $('.holder-list .cart-checkbox').each(function(){
        if($(this).hasClass('select-all')){
            var shoppingCartId = $(this).attr("shoppingCartId");
            if(shoppingCartId != null || shoppingCartId != '' && typeof shoppingCartId != 'undefined'){
                _shoppingCartIdList.push(shoppingCartId);
            }
        }
    });
    var _data = {"list":_shoppingCartIdList};
    sendDeleteAjaxRequest(_data);
}

function sendDeleteAjaxRequest(_data){
    $.ajax({
        url:ctx + "/shoppingCart/delete",
        data:JSON.stringify(_data),
        type:"post",
        dataType:"json",   //返回参数类型
        contentType :"application/json",   //请求参数类型
        async:false,
        success:function(data){
            console.info("删除成功!");
            window.location.reload();
        },
        error:function(){

        }
    })
}


function additem(e,value){
    var inputObject = $(e).parent().find('.its-buy-num');
    var oldValue = inputObject.val();
    //inputObject.val(Number(parseInt(oldValue)) + Number(upStep));
    inputObject.val(value);
    $(e).parent().find('.its-buy-num').attr("preValue",value);
}
function minusitem(e,value){
    var inputObject = $(e).parent().find('.its-buy-num');
    var oldValue = inputObject.val();
    if(oldValue<=1){
        return;
    }
    inputObject.val(value);
    $(e).parent().find('.its-buy-num').attr("preValue",value);
}

// 小计
function totalSub(e){
    var tdsumObject=$(e).parents('.holder-list').find('.td-sum span');
    var tdamount=Number($(e).parent().find('.its-buy-num').val());
    var tdprice=Number($(e).parents('.holder-list').find('.td-price span').html());
    var tdsum= tdamount*tdprice;
    tdsumObject.html(fmoney(tdsum,2));

    var productSettlementPriceObject=$(e).parents('.holder-list').find("input[name='productSettlementPrice']");
    // console.info("productSettlementPriceObject=" + productSettlementPriceObject + ",value="+productSettlementPriceObject.val());
    productSettlementPriceObject.val(tdsum.toFixed(2));

    var buyedOrderAmount = 0;
    $(e).parents('.order-holder').find("input[name='productSettlementPrice']").each(function (index,element) {
        // console.info("index="+index+",element="+element +",value=" + this.value);
        buyedOrderAmount += Number(this.value);
    });
    $(e).parents('.order-holder').find('.buy-price').html(fmoney(buyedOrderAmount));
    var orderSamountObject = $(e).parents('.order-holder').find("input[name='orderSamount']");
    var supplyId = orderSamountObject.attr("supplyId");
    orderSamountObject.attr("buyPrice",buyedOrderAmount.toFixed(2));

    var orderSamount = orderSamountObject.val();
    /* 如果在该供应商下购买的商品总额 超过 订单起售金额 ，则隐藏提示语。否则要提示用户 */
    if(Number(orderSamount) <= Number( buyedOrderAmount)){
        orderSamountObject.attr("needPrice",Number(0));
        $(e).parents('.order-holder').find('.need-price').html(fmoney(0));
        $(e).parents('.order-holder').find('.holder-top p').hide();
        // $(e).parents('.order-holder').find(".cart-checkbox").addClass('select-all');
    }else{
        var needPrice = (Number(orderSamount) - Number( buyedOrderAmount)).toFixed(2);
        orderSamountObject.attr("needPrice",needPrice);
        $(e).parents('.order-holder').find('.need-price').html(fmoney(needPrice));
        $(e).parents('.order-holder').find('.holder-top p').show();
        // $(e).parents('.order-holder').find(".cart-checkbox").removeClass('select-all');
    }
    getSelectedShoppingCart();
}
//商品总额
function totalSum(){
    var tdsum=0;
    $(".shopping-cart .holder-list").each(function(){
        if(!$(this).hasClass('no-stock')){
            if($(".cart-checkbox",this).hasClass('select-all')){
                $("input[name='productSettlementPrice']",this).each(function(){
                    tdsum += Number($(this).val());
                });
            }
        }
    });
    $('.total-price span').html(fmoney(tdsum,3));
}
//品种总计
function totalItem(){
    var tditem=0;
    $(".shopping-cart .holder-list").each(function(){
        if(!$(this).hasClass('no-stock')){
            if($(".cart-checkbox",this).hasClass('select-all')){
                $(".td-amount",this).each(function(){
                    tditem += 1;
                    //Number($('.its-buy-num', this).val()); //商品数量
                });
            }
        }
    });

    $('.total-item span').html(tditem);
}

//判断是满足购买(更改商品数量后，计算是否低于 订单起售金额)
function priceNeed(){
    $("input[name='orderSamount']").each(function(_index,_element){
        var needPrice = $(_element).attr("needPrice");
        if(Number(needPrice) > 0 ){
            $(_element).parents('.order-holder').find('.holder-top p').show();    
        }else{
            $(_element).parents('.order-holder').find('.holder-top p').hide();    
        }
    });
}

function showOrHideTip(_this){
    var needPrice = $(_this).parents('.order-holder').find("input[name='orderSamount']").attr("needPrice");
    if(Number(needPrice) > 0){
        $(_this).parents('.order-holder').find('.holder-top p').show();
    }else{
        $(_this).parents('.order-holder').find('.holder-top p').hide();
    }

}

/**
 * 更改(与订单起售金额的)提示信息内容
 * @param _this
 */
function changeOrderAmountPriceTip(_this){
    //计算当前供应商下，已购买的商品总和(不含缺货和下架的商品)
    var productSettlementPrice = Number(0);
    $(_this).parents('.order-holder').find(".holder-list").each(function(_i,_e){
        if(!$(_e).hasClass("no-stock")){
            $(_e).find("input[name='productSettlementPrice']").each(function(_index,_element){
                var val = $(_element).val();
                if(val != null && val != '' && typeof val != "undefined"){
                    productSettlementPrice += Number(val);
                }
            });
        }
    });
    // console.info("该供应商下已买的商品总额：" + productSettlementPrice);

    //比较订单起售金额 与 已购买的商品总和
    var orderSamount = $(_this).parents('.order-holder').find("input[name='orderSamount']").val();
    // console.info("该供应商设置订单起售金额：" + orderSamount);

    var needPrice = Number(orderSamount) - Number(productSettlementPrice);
    if(needPrice <= 0 ){
        needPrice = Number(0);
    }
    $(_this).parents('.order-holder').find("input[name='orderSamount']").attr("buyPrice",productSettlementPrice.toFixed(2));
    $(_this).parents('.order-holder').find("input[name='orderSamount']").attr("needPrice",needPrice.toFixed(2));
    $(_this).parents('.order-holder').find(".holder-top").find(".buy-price").html(fmoney(productSettlementPrice,2));
    $(_this).parents('.order-holder').find(".holder-top").find(".need-price").html(fmoney(needPrice,2));

    //更改提示信息内容
    $(_this).parents('.order-holder').find(".holder-top")
}

/**
 * 刷新购物车页面，计算每个供应商下的商品 是否低于各自的订单起售金额
 */
function updateOrderSaleAmount(){

    $('.order-holder').each(function(){
        var needPrice = $(this).find("input[name='orderSamount']").attr("buyPrice");

        var buyedOrderAmount = 0;
        $(this).find("input[name='productSettlementPrice']").each(function (index,element) {
            buyedOrderAmount += Number(this.value);
        });
        $(e).parents('.order-holder').find('.buy-price').html(fmoney(buyedOrderAmount));
        var orderSamountObject = $(e).parents('.order-holder').find("input[name='orderSamount']");
        var supplyId = orderSamountObject.attr("supplyId");
        orderSamountObject.attr("buyPrice",buyedOrderAmount.toFixed(2));

        var orderSamount = orderSamountObject.val();
        /* 如果在该供应商下购买的商品总额 超过 订单起售金额 ，则隐藏提示语。否则要提示用户 */
        if(Number(orderSamount) <= Number( buyedOrderAmount)){
            orderSamountObject.attr("needPrice",Number(0));
            $(e).parents('.order-holder').find('.need-price').html(fmoney(0));
            $(e).parents('.order-holder').find('.holder-top p').hide();
            // $(e).parents('.order-holder').find(".cart-checkbox").addClass('select-all');
        }else{
            var needPrice = (Number(orderSamount) - Number( buyedOrderAmount)).toFixed(2);
            orderSamountObject.attr("needPrice",needPrice);
            $(e).parents('.order-holder').find('.need-price').html(fmoney(needPrice));
            $(e).parents('.order-holder').find('.holder-top p').show();
            // $(e).parents('.order-holder').find(".cart-checkbox").removeClass('select-all');
        }
    });


    getSelectedShoppingCart();

}


/**
 * 更新购物车中数量
 * （只发送更新购物车中数量的请求，不做业务、页面样式的处理。这是个通用的方法，给其他方法调用）
 * @param _shoppingCartId
 * @param _value
 */
function updateNumInShoppingCart(_shoppingCartId,_value,_this,_type, _preValue){
    var reg = /^[1-9]\d*$/;
    if(_shoppingCartId == null || _shoppingCartId == "" || typeof _shoppingCartId == "undefined"){
        return;
    }
    if(_value == null || _value == "" || typeof _value == "undefined"){
        return;
    }
    if(!(""+_value).match(reg)){
        $(_this).parent().find('.its-buy-num').val(_preValue);
        return;
    }
    if(_value < 1){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14">购买数量不能小于1 ！</p>',
            cancel:'取消',
            ok:'确定'
        });
        return;
    }
    if(_value > 999999999){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14">购买数量不能大于999999999 ！</p>',
            cancel:'取消',
            ok:'确定'
        });
        return;
    }
    var _data = {"shoppingCartId":_shoppingCartId,"productCount":_value};
    $.ajax({
        url:ctx + "/shoppingCart/updateNum",
        data:JSON.stringify(_data),
        type:"post",
        contentType :"application/json",   //请求参数类型
        async:false,
        success:function(data){
            if(data.statusCode || data.message){
                // console.info("更新数量失败" );
                new Dialog({
                    title:'提示',
                    content:'<p class="mt60 f14">'+data.message+'</p>',
                    cancel:'取消',
                    ok:'确定'
                });
            }else{
                // console.info("更新数量成功" );
                $(_this).parent().find('.its-buy-num').val(_value);
                $(_this).parent().find('.its-buy-num').attr("preValue",_value);
                var productPrice = $(_this).parent().find('.its-buy-num').attr("productPrice");
                /* 商品小计 */
                var productTotalPrice = Number(productPrice) * Number(_value);
                $(_this).parents('.holder-list').find('.td-sum span').html(fmoney(productTotalPrice,3));
                $(_this).parents('.holder-list').find('.td-sum').find("input[name='productSettlementPrice']").val(productTotalPrice.toFixed(3));
                changeOrderAmountPriceTip(_this);
                showOrHideTip(_this);
                totalSum();
            }
        },
        error:function(data){
            var a = data;
            if(_type == 'add'){
                additem(_this,_value);
            }else if(_type == 'minusitem'){
                minusitem(_this,_value);
            }
            totalSub(_this);
            totalItem();
            totalSum();
            priceNeed();
        }
    });
}



$(function() {
    getSelectedShoppingCart();
    $('.its-buy-num').blur(function(){
        var shoppingCartId = $(this).parent().find('.its-buy-num').attr("shoppingCartId");
        // var saleStart = $(this).parent().find('.its-buy-num').attr("saleStart");//起批量
        var upStep = $(this).parent().find('.its-buy-num').attr("upStep");//最小可拆零包装量(用于递增、递减)
        if(upStep == '' || Number(upStep) <=0){
            upStep=1;
        }
        var value = 0;
        if(Number($(this).parent().find('.its-buy-num').val()) <= Number(upStep)){
            $(this).parent().find('.its-buy-num').val(upStep);
            value = upStep;
            $(this).addClass('its-btn-gray');
        }else{
            value = $(this).parent().find('.its-buy-num').val();
        }
        var _preValue = $(this).parent().find('.its-buy-num').attr("preValue");
        var _productInventory = $(this).parent().find('.its-buy-num').attr("productInventory");

        /* 控制用户输入的商品数量，是以最小可拆零包装量的整数倍进行递增或者递减 */
        value = convertValidNumber(value,_preValue,upStep,_productInventory);
        console.info("转换后的value=" + value);
        updateNumInShoppingCart(shoppingCartId,value,this,'updateText',_preValue);
    });
    //小计
    $('.its-btn-reduce').click(function(){

        var shoppingCartId = $(this).parent().find('.its-buy-num').attr("shoppingCartId");
        // var saleStart = $(this).parent().find('.its-buy-num').attr("saleStart");//起批量
        var upStep = $(this).parent().find('.its-buy-num').attr("upStep");//最小可拆零包装量(用于递增、递减)
        if(upStep == '' || Number(upStep) <=0){
            upStep=1;
        }
        // if(saleStart == '' || Number(saleStart) <=0){
        //     saleStart=1;
        // }
        // console.info("shoppingCartId=" + shoppingCartId );
        var value = 0;
        if(Number($(this).parent().find('.its-buy-num').val())-Number(upStep) <= Number(upStep)){
            $(this).parent().find('.its-buy-num').val(upStep);
            value = upStep;
            $(this).addClass('its-btn-gray');
        }else{
            value = Number($(this).parent().find('.its-buy-num').val()) - Number(upStep);
        }

        //minusitem(this,value);
        //totalSub(this);
        //totalItem();
        //totalSum();
        //priceNeed();
        var _preValue =   $(this).parent().find('.its-buy-num').attr("preValue");
        updateNumInShoppingCart(shoppingCartId,value,this,'minusitem');
    });
    $('.its-btn-add').click(function(){

        var shoppingCartId = $(this).parent().find('.its-buy-num').attr("shoppingCartId");
        // var saleStart = $(this).parent().find('.its-buy-num').attr("saleStart");//起批量
        var upStep = $(this).parent().find('.its-buy-num').attr("upStep"); //最小可拆零包装量(用于递增、递减)
        if(upStep == '' || Number(upStep) <=0){
            upStep=1;
        }
        // if(saleStart == '' || Number(saleStart) <=0){
        //     saleStart=1;
        // }
        // console.info("shoppingCartId=" + shoppingCartId );

        if(Number($(this).parent().find('.its-buy-num').val())+Number(upStep) > Number(upStep)){
            $(this).parent().find('.its-btn-reduce').removeClass('its-btn-gray');
        }
        var value =  Number($(this).parent().find('.its-buy-num').val()) + Number(upStep);
        //additem(this,upStep);
        //totalSub(this);
        //totalItem();
        //totalSum();
        //priceNeed();
        updateNumInShoppingCart(shoppingCartId,value,this,'add');
    });
    //单选
    $('.holder-list .cart-checkbox').click(function(){
        // var shoppingCartId = $(this).attr("shoppingCartId");
        // console.info("shoppingCartId=" + shoppingCartId );
        //判断 缺货 下架
        if($(this).hasClass('select-all')){
            $(this).removeClass('select-all');
            $(".th-chk .cart-checkbox").removeClass('select-all');
            $(this).parents('.order-holder').find('.holder-top .cart-checkbox').removeClass('select-all');
            totalItem();
            totalSum();
            removeSelectedShoppingCart();
        }else{
            //全选：是否符合订单起售金额
            // var obj = $(this).parents('.order-holder').find("input[name='orderSamount']");
            // var needPrice = obj.attr("needPrice");

            //全选:排除无库存
            // var holderTop = $(this).parents('.order-holder').find('.holder-list');
            // holderTop.each(function(){
            //     if( !$(this).hasClass("no-stock")){
            $(this).addClass('select-all');
            //     }
            // });
            totalItem();
            totalSum();
            getSelectedShoppingCart();
        }
    });
    //全选
    $(".th-chk").click(function(){
        if($(".cart-checkbox",this).hasClass('select-all')){
            $('.cart-checkbox').removeClass('select-all');
            removeSelectedShoppingCart();
        }else{
            $('.shopping-cart .cart-checkbox').each(function(){
                $(this).addClass('select-all');
            });
            getSelectedShoppingCart();
        }
        totalItem();
        totalSum();

    });
    //企业全选
    $(".holder-top .cart-checkbox").click(function(){
        if($(this).hasClass('select-all')){
            $(this).parent().parent().find('.cart-checkbox').removeClass('select-all');
            removeSelectedShoppingCart();
        }else{
            //排除无库存 全选
            var holderTop = $(this).parents('.order-holder').find('.holder-list');
            holderTop.each(function(){
                $(this).find('.cart-checkbox').addClass('select-all');
                $(this).parents('.order-holder').find('.holder-top .cart-checkbox').addClass('select-all');
            });
            getSelectedShoppingCart();
        }
        totalItem();
        totalSum();
    });
    // 删除选中商品
    $('.delete-items').click(function(){
        deleteSelectedShoppingCart();
        $(".order-holder").each(function(){
            var holderList = $('.holder-list',this);
            if($(holderList).find(".cart-checkbox").hasClass("select-all")){
                if(holderList.length == 1){
                    $(this).remove();
                }else{
                    $('.holder-list',this).remove();
                }
            }
        });
        totalItem();
        totalSum();
    });
    //清除失效商品（缺货、下架等）
    $(".clear-items").click(function(){
        var _shoppingCartIdList = new Array();
        $(".order-holder").each(function(_index,_element){
            $('.holder-list',this).each(function(index,element){
                if($(element).hasClass("no-stock")){
                    var _shoppingCartId = $(element).find(".cart-checkbox").attr("shoppingCartId");
                    if(_shoppingCartId != null || _shoppingCartId != '' && typeof _shoppingCartId != 'undefined'){
                        _shoppingCartIdList.push(_shoppingCartId);
                    }
                    $(element).remove();
                }
            });
            if($('.holder-list',this).length == 0 || $('.holder-list',this).length == 1){
                $(this).remove();
            }
        });
        var _data = {"list":_shoppingCartIdList};
        sendDeleteAjaxRequest(_data);
        totalItem();
        totalSum();
    });

    // 删除
    $('.td-op .btn-delete').click(function(){
        var orderHolder =$(this).parents('.order-holder');
        var holderList=$('.holder-list',orderHolder).length;
        if(holderList==1){
            $(this).parents('.order-holder').remove();
        }else{
            $(this).parents('.holder-list').remove();
        }
        totalItem();
        totalSum();
    });
    //删除 提示
    $('.td-op .btn-notice').click(function(){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14">确定删除选中商品 ！</p>',
            cancel:'取消',
            ok:'确定'
        });
    });
    //品种总计 商品总额
    totalItem();
    totalSum();
    priceNeed();

    //结算判断
    $('.os-btn-pay').click(function(){
        if($('.select-all').length<1){
            new Dialog({
                title:'提示',
                content:'<p class="mt60 f14 ">请选择相应的商品！</p>',
                ok:'确定',
                afterOk:function(){
                    console.log('111');
                },
                afterClose:function(){
                    console.log('222');
                }
            });
        }else if($('.order-holder .cart-checkbox').hasClass('select-all')){

            var canSubmit = true;
            $("input[name='orderSamount']").each(function(index,element){
                var needPrice = $(this).attr("needPrice");
                // console.info("needPrice =" + needPrice );
                if(Number(needPrice) > 0 ){
                    canSubmit = false;
                }
            });
            if(!canSubmit){
                new Dialog({
                    title:'提示',
                    content:'<p class="f14 mt40">你有部分商品金额低于供货商的发货标准，<br>此商品无法结算，是否继续？</p>',
                    cancel:'取消',
                    ok:'确定',
                    afterOk:function(){
                        submitCheckOrderPage();
                    },
                    afterClose:function(){
                        console.log('222');
                    }
                });
            }else{
                submitCheckOrderPage();
            }

        }else if($('.select-all').hasClass('checkbox-disable')){
            // 缺货 无库存
            new Dialog({
                title:'提示',
                content:'<p class="mt60 f14">商品失效，无法结算 ！</p>',
                ok:'确定',
                afterOk:function(){
                    console.log('111');
                },
                afterClose:function(){
                    console.log('222');
                }
            });
        }
    });

});

/**
 * 控制用户输入的商品数量，是以最小可拆零包装量的整数倍进行递增或者递减
 * @param _inputValue 用户在框中输入的商品数量
 * @param _preValue  原有的商品数量
 * @param _upStep  最小可拆零包装数量(以这个参数控制递增、递减)
 * @param _productInventory  库存数量
 */
function convertValidNumber(_inputValue, _preValue, _upStep,_productInventory) {
    console.info("_inputValue=" + _inputValue + ",_preValue=" + _preValue + ",_upStep=" + _upStep + ",_productInventory=" + _productInventory);
    /* 当库存低于最小可拆零包装数量，不让用户修改商品数量 */
    if(Number(_productInventory) < Number(_upStep)){
        return _preValue
    }
    //输入的数字低于最小可拆零包装数量，不修改
    if(Number(_inputValue) < Number(_upStep)){
        return _preValue;
    }
    //输入的数字高于库存数量，不修改
    if(Number(_inputValue) > Number(_productInventory)){
        return _preValue;
    }

    var mod = Number(_inputValue) % Number(_upStep);

    //递增逻辑
    if(Number(_inputValue) > Number(_preValue)){
        if(mod == 0){
            return _inputValue;
        }else{
            var finalValue = Number(_inputValue) - mod + Number(_upStep);
            return finalValue > Number(_productInventory) ? Number(_productInventory) : finalValue;
        }

        //递减逻辑
    }if(Number(_inputValue) < Number(_preValue)){
        if(mod == 0){
            return _inputValue;
        }else{
            var finalValue = Number(_inputValue) - mod;
            return finalValue < Number(_upStep) ? Number(_upStep) : finalValue;
        }
    }else{
        return _preValue;
    }
}



/* 移除页面已勾选的 checkBox，用于提交 */
function removeSelectedShoppingCart(){
    var array = new Array();
    $('.cart-checkbox').each(function(){
        if(!$(this).hasClass('select-all')){
            var shoppingCartId = $(this).attr("shoppingCartId");
            console.info("shoppingCartId=" + shoppingCartId );
            if(shoppingCartId != null || shoppingCartId != '' && typeof shoppingCartId != 'undefined'){
                array.push(shoppingCartId);
            }
        }
    });
    if(array == null || array.length == 0){
        return;
    }

    var selectedShoppingCartId = null;
    for(var i = 0 ;i < array.length ; i++){
        selectedShoppingCartId = "#selectedShoppingCart_" + array[i];
        $(selectedShoppingCartId).remove();
    }
}

/**
 *  获取页面已勾选的 checkBox，用于提交
 * @returns {boolean}
 */
function getSelectedShoppingCart(){
    var paramsHtml = "";
    $('.cart-checkbox').each(function(index,element){
        if($(this).hasClass('select-all')){
            var shoppingCartId = $(this).attr("shoppingCartId");
            var supplyId = $(this).attr("supplyId");
            // console.info("shoppingCartId=" + shoppingCartId );
            if(shoppingCartId != null || shoppingCartId != '' && typeof shoppingCartId != 'undefined'){
                // array.push(shoppingCartId);
                paramsHtml += "<input type='hidden' id='selectedShoppingCart_" + shoppingCartId + "' class='selectedShoppingCart' supplyId='" + supplyId + "' name='shoppingCartDtoList[" + index + "].shoppingCartId' value='" + shoppingCartId + "'>";
            }
        }
    });
    if(paramsHtml == ""){
        return false;
    }
    $("#submitCheckOrderPage").html("");
    $("#submitCheckOrderPage").html(paramsHtml);
    return true;

}

/**
 * 跳到检查订单页面
 */
function submitCheckOrderPage(){

    if(!getSelectedShoppingCart()){
        /* 如果没有选中任何商品 则不跳转到检查订单页面 */
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14 ">请选择相应的商品！</p>',
            ok:'确定',
            afterOk:function(){
                console.log('111');
            },
            afterClose:function(){
                console.log('222');
            }
        });
        return ;
    }

    var array = new Array();
    $("#submitCheckOrderPage").find(".selectedShoppingCart").each(function(){
        var shoppingCartId = $(this).val();
        var supplyId = $(this).attr("supplyId");
        // console.info("shoppingCartId= "+shoppingCartId +",supplyId=" + supplyId);
        if(shoppingCartId != null || shoppingCartId != '' && typeof shoppingCartId != 'undefined'){
            var _data = {"shoppingCartId":shoppingCartId,"supplyId":supplyId};
            array.push(_data);
        }
    });
    console.info("array="+array);

    if(array == null ){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14 ">请选择相应的商品！</p>',
            ok:'确定',
            afterOk:function(){
                console.log('111');
            },
            afterClose:function(){
                console.log('222');
            }
        });
        return ;
    }
    // console.info("_data="+_data);

    /*  检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
    $.ajax({
        url:ctx + "/shoppingCart/check",
        data:JSON.stringify(array),
        type:"post",
        dataType:"json",   //返回参数类型
        contentType :"application/json",   //请求参数类型
        async:false,
        success:function(data){
            console.info(data);
            if(data.result){
                $("#submitCheckOrderPage").attr({"action": ctx + "/order/checkOrderPage"});
                $("#submitCheckOrderPage").submit();
            }else{
                new Dialog({
                    title:'提示',
                    content:'<p class="mt60 f14">' + data.message  + '</p>',
                    ok:'确定',
                    afterOk:function(){
                        console.log('111');
                    },
                    afterClose:function(){
                        console.log('222');
                    }
                });
            }
        },
        error:function(){

        }
    });
}
