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
//检查订单页
(function($){
    function payment(id){
        $(id).click(function(){
            $(''+id+' b').hide();
            if($('.goi-selected')){
                $(id).removeClass('goi-selected');
            }
            $(this).find('b').show();
            $(this).addClass('goi-selected');
        })
    }
    payment('.goi-con li');
    payment('.goi-con-bill li');

    //单选按钮
    $('.radio-select label').click(function(){
        $('.radio-skin').removeClass('radio-skin-selected');
        $(this).find('i:first').addClass('radio-skin-selected');
    })

    //查看更多
    $('.goi-table').css('height','101');
    var extend = true,
        $table = $('.common-table'),
        $showtr = $table.find('tr').eq(0).height()+$table.find('tr').eq(1).height()+$table.find('tr').eq(2).height();
    $showtr += 1;
    $('.goi-table').css('height', $showtr);
    $('.goi-mover-btn').click(function(){
        if(extend){
            $('.goi-table').animate({height:$table.height()+1});
            extend = false;
            $(this).html('<i class="inside-icon goi-arrow goi-arrow-click"></i>收起');
        } else{
            $('.goi-table').animate({height:$showtr});
            $(this).html('<i class="inside-icon goi-arrow "></i>查看更多');
            extend = true;
        }
    })

})($);





