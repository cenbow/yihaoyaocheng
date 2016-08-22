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

function additem(e){
    var inputObject = $(e).parent().find('.its-buy-num');
    var oldValue = inputObject.val();
    inputObject.val(parseInt(oldValue) + 1);
}
function minusitem(e){
    var inputObject = $(e).parent().find('.its-buy-num');
    var oldValue = inputObject.val();
    if(oldValue<=1){
        return;
    }
    inputObject.val(parseInt(oldValue) - 1);
}

// 小计
function totalSub(e){
    var tdsumObject=$(e).parents('.holder-list').find('.td-sum span');
    var tdamount=Number($(e).parent().find('.its-buy-num').val());
    var tdprice=Number($(e).parents('.holder-list').find('.td-price span').html());
    var tdsum= tdamount*tdprice;
    tdsumObject.html(tdsum.toFixed(2));
}
//商品总额
function totalSum(){
    var tdsum=0;
    $(".shopping-cart .holder-list").each(function(){
        if(!$(this).hasClass('no-stock')){
            if($(".cart-checkbox",this).hasClass('select-all')){
                $(".td-sum",this).each(function(){
                    tdsum += Number($('span',this).html());
                });
            }
        }
    });
    $('.total-price span').html(tdsum.toFixed(2));
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
 * 更新购物车中数量
 * @param _shoppingCartId
 * @param _value
 */
function updateNumInShoppingCart(_shoppingCartId,_value){
    if(_shoppingCartId == null || _shoppingCartId == "" || typeof _shoppingCartId == "undefined"){
        return;
    }
    if(_value == null || _value == "" || typeof _value == "undefined"){
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
            console.info(data);
        },
        error:function(){

        }
    })
}





$(function() {
    getSelectedShoppingCart();

    //小计
    $('.its-btn-reduce').click(function(){
        
        var shoppingCartId = $(this).parent().find('.its-buy-num').attr("shoppingCartId");
        // console.info("shoppingCartId=" + shoppingCartId );
        
        if($(this).parent().find('.its-buy-num').val() <= 1){
            $(this).addClass('its-btn-gray');
            return;
        }
        minusitem(this);
        totalSub(this);
        totalItem();
        totalSum();
        priceNeed();
        updateNumInShoppingCart(shoppingCartId,$(this).parent().find('.its-buy-num').val())
    });
    $('.its-btn-add').click(function(){
        
        var shoppingCartId = $(this).parent().find('.its-buy-num').attr("shoppingCartId");
        // console.info("shoppingCartId=" + shoppingCartId );
        
        if($(this).parent().find('.its-buy-num').val() > 1){
            $(this).parent().find('.its-btn-reduce').removeClass('its-btn-gray');
        }
        additem(this);
        totalSub(this);
        totalItem();
        totalSum();
        priceNeed();
        updateNumInShoppingCart(shoppingCartId,$(this).parent().find('.its-buy-num').val())
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
                content:'<p class="mt60 f14 ">请选择相应商品！</p>',
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
            alert("请选择要提交的商品");
            return;
        }
        $("#submitCheckOrderPage").html("");
        $("#submitCheckOrderPage").html(paramsHtml);
    }else{
        alert("请选择要提交的商品");
        return;
    }
}

/**
 * 跳到检查订单页面
 */
function submitCheckOrderPage(){

    getSelectedShoppingCart();

    
    //TODO AJAX 检验商品上架、下架状态
    
    
    $("#submitCheckOrderPage").attr({"action": ctx + "/order/checkOrderPage"});
    $("#submitCheckOrderPage").submit();
}