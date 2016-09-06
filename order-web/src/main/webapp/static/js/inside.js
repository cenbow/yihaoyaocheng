//订单提交成功页-显示隐藏效果
(function($){
    var timer = null;
    $('.query-icon').hover(function(){
        clearTimeout(timer);
        $('.tips-frame').hide();
        $(this).children('.tips-frame').show();
    },function(){
        var that = $(this);
        timer = setTimeout(function(){
            $(that).children('.tips-frame').hide();
        },100)
    });
    $('.order-sucess .os-btn-pay,.order-to-pay .os-btn-pay').click(function(){
        if($('.radio-skin-selected').length<1){
            new Dialog({
                title:'提示',
                content:'<p class="mt60 f14">请选择支付方式！</p>',
                ok:'确定',
                afterOk:function(){
                    console.log('111');
                }
            });
        }
    });
})($);

/* 选择收货地址 */
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

/* 选择发票类型 */
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

    /* 支付方式单选按钮 */
    $('.radio-select label').click(function(){
        
        /* 如果按钮被禁用掉，不能选中支付方式 */
        if(!$(this).hasClass('label-disabled')) {
            
            $(this).parent('div').find('.radio-skin').removeClass('radio-skin-selected');
            $(this).find('i:first').addClass('radio-skin-selected');
            var _supplyId = $(this).find('i:first').attr("supplyId");
            var _payTypeId = $(this).find('i:first').attr("payTypeId");

            $(this).parent('div').find('.payTypeId').val(_payTypeId);
        }
    });


    //查看更多
    var extend = true, $table = $('.common-table'),
        $showtr = $table.find('tr').eq(0).height()+$table.find('tr').eq(1).height();
    $showtr += 1;
    $('.goi-table').css('height', $showtr);
    $('.goi-product-list .goi-mover-btn').click(function(){
        if(extend){
            var closeTableHeight = $(this).siblings('.goi-table').find(".common-table").height()+1 ;
            $(this).siblings('.goi-table').animate({height:closeTableHeight});
            extend = false;
            $(this).html('<i class="inside-icon goi-arrow goi-arrow-click"></i>收起');
        } else{
            $(this).siblings('.goi-table').animate({height:$showtr});
            $(this).html('<i class="inside-icon goi-arrow "></i>查看更多');
            extend = true;
        }
    })

})($);

//搜索页
(function($){
    var iTure = true;
    /*筛选*/
    function searchTab(clickElem,isMark,isText){
        var $isText = '';
        $(clickElem).click(function(){
            var $that = $(this);
            var $sgBodySelected = $that.parents('.search-groups').find('.group1 .sg-body-selected');
            $that.parent('.sg-b-item-inner').find('a').removeClass('sg-b-i-i-hover');
            $isText = '<a class="sg-icon-tag '+isMark+'" href="javascript:;"><span class="sbii-text">'+isText+$that.text()+'</span><span class="inside-icon sg-icon-btn-x"></span></a>';

            var $a = $sgBodySelected.find('a');
            var ishas=true;

            if($a.length == 0){
                $($isText).appendTo('.sg-body-selected');
                $that.addClass('sg-b-i-i-hover');
            } else{
                $a.each(function(index,item){
                    var $item = $(item);
                    if($item.hasClass(isMark)){
                        if($item.text() == $that.text()){
                            $item.find('.sbii-text').text($that.text());
                            $that.addClass('sg-b-i-i-hover');
                        } else{
                            $item.remove();
                            $($isText).appendTo('.sg-body-selected');
                            $that.addClass('sg-b-i-i-hover');
                            ishas=true;
                            return false;
                        }
                        ishas=true;
                    } else  {
                        ishas=false;
                    }
                })
            }

            if(!ishas) {
                $($isText).appendTo('.sg-body-selected');
                $that.addClass('sg-b-i-i-hover');
            }
        })
    }
    searchTab('.group2 .sg-b-item-inner a','sg-mark2','供应商：');
    searchTab('.group3 .sg-b-item-inner a','sg-mark3','生产企业：');
    searchTab('.group4 .sg-b-item-inner a','sg-mark4','类别：');
    /*重置*/
    $('.sg-f-refresh').click(function(){
        $('.sg-b-item-inner a').removeClass('sg-b-i-i-hover');
        $('.group1 .sg-body').text('');
    })
    /*关闭*/
    $('.sg-body').on('click','.sg-icon-btn-x',function(){
        var aClassName= $(this).parent('a').attr('class');
        var aname=aClassName.split(' ')[1];
        $('div.sg-b-item-inner').each(function(index,item){
            var $item=$(this);
            if($item.hasClass(aname)){
                $item.children('a').each(function(k,v){
                    if($(v).hasClass('sg-b-i-i-hover')){
                        $(v).removeClass('sg-b-i-i-hover')
                    }
                })
            }
        })
        $(this).parent().remove();
    })
    /*展开*/
    $('.sgf-show').click(function(){
        var $that = $(this);
        if(iTure){
            $that.find('span').text('收起');
            $that.find('i').addClass('sg-show-arrow-hover');
            $(this).parents('.search-group').css({'overflow':'auto','height':'auto'});
            $(this).parents('.search-group').find('.sgbi-search').show();
            $(this).parents('.search-group').find('.sg-b-item-inner').css({'overflow-y':'scroll'});
            iTure = false;
        } else{
            $that.find('span').text('更多');
            $that.find('i').removeClass('sg-show-arrow-hover');
            $that.parents('.search-group').css({'overflow':'hidden','height':'22px'});
            $that.parents('.search-group').find('.sgbi-search').hide();
            $(this).parents('.search-group').find('.sg-b-item-inner').css({'overflow-y':'visible'});
            iTure = true;
        }
    })
    /*价格区间*/
    $('.tsrp-range input').click(function(){
        $(this).parents('.tsrp-range').addClass('tsrp-range-hover');
        $(this).siblings('button').show();
        $('.tsrp-range').css({'margin-top':'2px'})
    })
    $('.tsrp-range button').click(function(){
        $(this).parents('.tsrp-range').removeClass('tsrp-range-hover');
        $(this).siblings('button').hide();
        $('.tsrp-range').css({'margin-top':'0'})
    })
})($);
/*商品详情页*/
(function($){
    /*tab选项卡*/
    $('.product-tab-title a').click(function(){
        $(this).parents('.product-tab').find('h4 a').siblings().removeClass('ptt-hover');
        $(this).addClass('ptt-hover');
        $(this).parents('.product-tab').find('.product-tab-con').hide().eq($(this).index()).show();
    })
})($);






