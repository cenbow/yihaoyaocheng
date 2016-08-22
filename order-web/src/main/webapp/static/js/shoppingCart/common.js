(function($){
	$('.location').on('mouseenter',function(){
		$(this).children('.location-city').show();
		$(this).find('i').addClass('location-icon-hover');
	}).on('mouseleave',function(){
		$(this).children('.location-city').hide();
		$(this).find('i').removeClass('location-icon-hover');
	})
	$('.location-city a').on('click',function(){
		$('.location-text').text($(this).text());
		$(this).parents('.location-city').find('a').removeClass('location-city-hover');
		$(this).addClass('location-city-hover');
		$(this).parents('.location-city').hide();
	})
	/*输入框焦点*/
	function changeFocus(id,value){
		var sIinputVal = value;
		var $ulpasstips=$('.ul-pass-tips');
		$(id).focus(function(){
			if($(this).val() == sIinputVal){
				$(this).val('');
				$ulpasstips.text('');
			}
		}).blur(function(){
			if($(this).val() == ''){
				if($(this).hasClass('ul-pass')){
					$ulpasstips.text(sIinputVal);
				}else{
					$(this).val(sIinputVal);
				}
			}
		})
	}
	changeFocus('.s-input','药品名 | 拼音缩写 | 批准文号 | 厂家');
	changeFocus('.ul-user','请输入用户名');
	changeFocus('.ul-pass','请输入密码');
	changeFocus('.ul-barcode','请输入验证码');
	changeFocus('.sgbi-search input','搜索 供应商名称');
	changeFocus('.sgbi-search2 input','黑龙江葵花药业股份有限公司');
	/*搜索*/
	$('.search-top-tab a').on('click',function(){
		$(this).parent('.search-top-tab').find('a').removeClass('search-top-tab-hover');
		$(this).addClass('search-top-tab-hover');
	})
	/*
	 $('.header-search-tab').hover(function(){
	 $(this).addClass('hs-tab-hover');
	 $(this).children('.hst-arrow').addClass('hst-arrow-hover');
	 },function(){
	 $(this).removeClass('hs-tab-hover');
	 $(this).children('.hst-arrow').removeClass('hst-arrow-hover');
	 })
	 $('.header-search-tab li').hover(function(){
	 $(this).parent('ul').find('li').removeClass('hst-selected');
	 $(this).addClass('hst-selected');
	 },function(){
	 $(this).parent('ul').find('li').removeClass('hst-selected');
	 $(this).removeClass('hst-selected');
	 $('.header-search-tab li:first').addClass('hst-selected');
	 }).on('click',function(){
	 if($(this).index() == 1){
	 $(this).parent('ul').prepend($(this).get(0));
	 $(this).parents('.header-search-tab').removeClass('hs-tab-hover');
	 }
	 })*/
	$('.s-input').keyup(function(){
		$(this).siblings('.sc-main-w').show();
		$('.sc-size').height($('.search-component').height());
	})
	$('.search-component li').on('click',function(){
		$('.s-input').val($(this).text());
		$(this).parents('.sc-main-w').hide();
		$(this).parents('.sc-main-w').find('.sc-size').hide();
	})
	$('.search-component li').on('mouseover',function(){
		var $index = $(this).index();
		$(this).parents('.sc-main-w').find('.sc-size').hide().eq($index).show();
		$(this).parents('.sc-main-w').find('.search-component li').removeClass('add-sc-hover');
		$(this).addClass('add-sc-hover');
	})
	$('body').on('click',function(){
		$(this).find('.sc-main-w').hide();
	})
	/*导航*/
	$('.categorys').on('mouseenter',function(){
		$(this).find('.nav-con-inside').show();
	}).on('mouseleave',function(){
		$(this).find('.nav-con-inside').hide();
	})
	$('.nav-con li').on('mouseenter',function(){
		$(this).parent('.nav-con').find('.nav-con-list').hide();
		$(this).parent('.nav-con').find('.nav-con-hover').hide();
		$(this).children('.nav-con-list').show();
		$(this).children('.nav-con-hover').show();
	}).on('mouseleave',function(){
		$(this).children('.nav-con-list').hide();
		$(this).children('.nav-con-hover').hide();
		$(this).parent('.nav-con').find('.nav-con-list').hide();
		$(this).parent('.nav-con').find('.nav-con-hover').hide();
	})
	/*返回顶部*/
	$('.top-btn').click(function(){
		$('html,body').animate({scrollTop:'0px'})
	})
	var wHeight = $(window).height();
	$('.side-bar-height').animate({
		height : wHeight
	})
	function showElem(elem,newClass,content){
		$(elem).on('mouseenter',function(){
			$(this).addClass(newClass).children(content).show();
		}).on('mouseleave',function(){
			$(this).removeClass(newClass).children(content).hide();
		})
	}
	showElem('.sbc-qq','sbc-qq-hover','.sbc-qq-details');
	showElem('.sbc-contact','sbc-contact-hover','.sbc-c-details');
	showElem('.sbc-barcode','sbc-barcode-hover','.sbc-b-details');
	/*选项卡*/
	$('.main h3 a').click(function(){
		$(this).parent('h3').parent('.main-title').parent('.main').find('h3 a').siblings().removeClass('red');
		$(this).addClass('red');
		$(this).parents('.main').find('.main-content').hide().eq($(this).index()).show();
	})
	/*商品数量*/
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
	$('.commonjs .its-btn-reduce').click(function(){
		minusitem(this);
	});
	$('.commonjs .its-btn-add').click(function(){
		additem(this);
	});
	/*弹窗*/
	$('.dialog-add-btn').click(function(){
		new Dialog({
			title:'申请加入渠道',
			content:'<p class="mt60 f14">如需采购，请向卖家申请加入渠道！</p>',
			ok:'立即申请',
			cancel:'以后再说',
			afterOk:function(){
				console.log('111');
			},
			afterClose:function(){
				console.log('222');
			}
		})
	})
	/*弹窗*/
	$('.dialog-order-btn').click(function(){
		new Dialog({
			className:'product-dialog',
			title:'加入进货单',
			content:'<div class="pr clearfix"><i class="common-icon order-dialog-icon"></i><h3>货品已添加成功！</h3><p>进货单中共有 <span class="red">1</span> 个商品</p><p>金额总计： <span class="red">¥ 695.00</span></p></div>',
			ok:'继续采购',
			cancel:'结账',
			afterOk:function(){
				console.log('111');
			},
			afterClose:function(){
				console.log('222');
			}
		})
	})
})($);
$(document).ready(function(){
	/*右边浮动条*/
	$('.side-bar').animate({
		right : '0px'
	},800)
})

