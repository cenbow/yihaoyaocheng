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
    $('.total-price span').html(fmoney(tdsum,2));
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

//判断是满足购买
function priceNeed(){
    var fromPrice =Number($('.from-price').html());
    var buyPrice=0;
    $(".order-holder:first .holder-list").each(function(){
        if(!$(this).hasClass('no-stock')){
            if($(".cart-checkbox",this).hasClass('select-all')){
                $(".td-sum",this).each(function(){
                    buyPrice += Number($('span',this).html());
                });
            }
        }
    });
    if(fromPrice>buyPrice){
        $('.order-holder:first .holder-top p').show();
        $('.buy-price').html(buyPrice.toFixed(2));
        var needPrice = fromPrice - buyPrice;
        $('.need-price').html(needPrice.toFixed(2));
    }else if(fromPrice>=buyPrice){
        $('.buy-price').html(buyPrice.toFixed(2));
        var needPrice = fromPrice - buyPrice;
        $('.need-price').html(needPrice.toFixed(2));
        $('.order-holder:first .holder-top p').hide();
    }
}

/**
 * 更新购物车中数量（当用户手动输入商品数量的场景使用）
 * @param _shoppingCartId
 * @param _this
 */
function updateNum(_shoppingCartId,_this){
    var _productCountInput = $(_this);
    var _productCountAttr = _productCountInput.attr("productCount");
    if(_productCountInput.val() < 1){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14">购买数量不能小于1 ！</p>',
            cancel:'取消',
            ok:'确定'
        });
        return;
    }
    if(_productCountInput.val() > 999999999){
        new Dialog({
            title:'提示',
            content:'<p class="mt60 f14">购买数量不能大于999999999 ！</p>',
            cancel:'取消',
            ok:'确定'
        });
        return;
    }
    console.info("_shoppingCartId=" + _shoppingCartId +",_productCountInput.val()=" + _productCountInput.val() +",_productCountAttr=" + _productCountAttr);
    return;

    /* 小计 */
    var tdsumObject=$(_this).parents('.holder-list').find('.td-sum span');
    var tdamount = Number(_productCountInput.val());
    var tdprice=Number($(_this).parents('.holder-list').find('.td-price span').html());
    var tdsum= tdamount*tdprice;
    tdsumObject.html(tdsum.toFixed(2));

    //品种总计
    totalItem();

    //商品总额
    totalSum();

    //判断是满足购买
    priceNeed();

    //发送请求：更新购物车中数量
    updateNumInShoppingCart(_shoppingCartId,_value);
}


/**
 * 更新购物车中数量
 * （只发送更新购物车中数量的请求，不做业务、页面样式的处理。这是个通用的方法，给其他方法调用）
 * @param _shoppingCartId
 * @param _value
 */
function updateNumInShoppingCart(_shoppingCartId,_value,_this,_type, _preValue){
    if(_shoppingCartId == null || _shoppingCartId == "" || typeof _shoppingCartId == "undefined"){
        return;
    }
    if(_value == null || _value == "" || typeof _value == "undefined"){
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
        dataType:"json",   //返回参数类型
        contentType :"application/json",   //请求参数类型
        success:function(data){
            if(data.statusCode || data.message){
                new Dialog({
                    title:'提示',
                    content:'<p class="mt60 f14">'+data.message+'</p>',
                    cancel:'取消',
                    ok:'确定'
                });
                if(_type == 'updateText')
                $(_this).parent().find('.its-buy-num').val(_preValue);
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
        var _preValue =   $(this).parent().find('.its-buy-num').attr("preValue");
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
            $(this).addClass('select-all');
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
    //删除选中商品
    $('.delete-items').click(function(){
        $('.cart-checkbox').removeClass('select-all');
        totalItem();
        totalSum();
    });
    //删除
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
        }else if($('.order-holder:first .cart-checkbox').hasClass('select-all')){
            if(Number($('.buy-price').html())<100){
                new Dialog({
                    title:'提示',
                    content:'<p class="f14 mt40">你有部分商品金额低于供货商的发货标准，<br>此商品无法结算，是否继续？</p>',
                    cancel:'取消',
                    ok:'确定',
                    afterOk:function(){
                        console.log('111');
                    },
                    afterClose:function(){
                        console.log('222');
                    }
                });
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


function getSelectedShoppingCart(){
    var array = new Array();
    $('.cart-checkbox').each(function(){
        if($(this).hasClass('select-all')){
            var shoppingCartId = $(this).attr("shoppingCartId");
            // console.info("shoppingCartId=" + shoppingCartId );
            if(shoppingCartId != null || shoppingCartId != '' && typeof shoppingCartId != 'undefined'){
                array.push(shoppingCartId);
            }
        }
    });
    if(array != null && array.length > 0){
        var paramsHtml = "";
        for(var i = 0 ;i < array.length ; i++){
            paramsHtml += "<input type='hidden' id='selectedShoppingCart_"+array[i]+"' name='shoppingCartDtoList["+i+"].shoppingCartId' value='"+array[i]+"'>";
        }
        if(paramsHtml == ""){
            return false;
        }
        $("#submitCheckOrderPage").html("");
        $("#submitCheckOrderPage").html(paramsHtml);
        return true;
    }else{
        return false;
    }
}

/**
 * 跳到检查订单页面
 */
function submitCheckOrderPage(){

    if(!getSelectedShoppingCart()){
        /* 如果没有选中任何商品 则不跳转到检查订单页面 */
        return ;
    }
    
    //TODO AJAX 检验商品上架、下架状态
    // $.ajax({
    //     url:ctx + "/shoppingCart/check",
    //     data:JSON.stringify(_data),
    //     type:"post",
    //     dataType:"json",   //返回参数类型
    //     contentType :"application/json",   //请求参数类型
    //     success:function(data){
    //         console.info(data);
    //     },
    //     error:function(){
    //
    //     }
    // })
    $("#submitCheckOrderPage").attr({"action": ctx + "/order/checkOrderPage"});
    $("#submitCheckOrderPage").submit();
}
