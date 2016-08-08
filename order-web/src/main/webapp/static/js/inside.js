//订单提交成功页-显示隐藏效果
(function($){
    var timer = null;
    $('.query-icon').hover(
        function(){
            clearTimeout(timer);
            $(this).children('.tips-frame').show();
        },function(){
            var that = $(this);
            timer = setTimeout(function(){
                $(that).children('.tips-frame').hide();
            },200)
        }
    );
})($);


function changeReceiveAddress(id){
    $(id).click(function(){
        $(''+id+' b').hide();
        if($('.goi-selected')){
            $(id).removeClass('goi-selected');
        }
        $(this).find('b').show();
        $(this).addClass('goi-selected');
        var _value = $(this).attr("receiveAddressId");
        var _node = "#" + "receiveAddressId";
        $(_node).val(_value)
    })
}
function changeBillType(id,_node){
    $(id).click(function(){
        $(''+id+' b').hide();
        if($('.goi-selected')){
            $(id).removeClass('goi-selected');
        }
        $(this).find('b').show();
        $(this).addClass('goi-selected');
        var _value = $(this).attr("billType");
        var _node = "#" + "billType";
        $(_node).val(_value)
    })
}





//检查订单页
(function($){
    /* 选择收货地址 */
    changeReceiveAddress('.goi-con li');

    /* 选择发票类型 */
    changeBillType('.goi-con-bill li');
    
    //查看更多
    var extend = true, $table = $('.common-table'),
        $showtr = $table.find('tr').eq(0).height()+$table.find('tr').eq(1).height()+$table.find('tr').eq(2).height();
    $showtr += 1;
    $('.goi-table').css('height', $showtr);
    $('.goi-product-list .goi-mover-btn').click(function(){
        if(extend){
            $(this).siblings('.goi-table').animate({height:$table.height()+1});
            extend = false;
            $(this).html('<i class="inside-icon goi-arrow goi-arrow-click"></i>收起');
        } else{
            $(this).siblings('.goi-table').animate({height:$showtr});
            $(this).html('<i class="inside-icon goi-arrow "></i>查看更多');
            extend = true;
        }
    })

})($);





