  /*轮播图*/
  var mySwiper = new Swiper('.swiper-container',{
  	pagination: '.pagination',
  	paginationClickable: true,
  	autoplay: 2500,
  	loop: true
  });
  (function($){
  	/*登陆*/
  	$('.ls-barcode').click(function(){
  		$(this).hide();
  		$('.user-login').hide();
  		$('.ls-login').show();
  		$('.barcode-login').show();
  	})
  	$('.ls-login').click(function(){
  		$(this).hide();
  		$('.barcode-login').hide();
  		$('.ls-barcode').show();
  		$('.user-login').show();
  	})
	/*品牌专场*/
	$('.main-pro-tab li').on('mouseenter',function(){
		$(this).eq($(this).index()).removeClass('mrt-hover').children('.main-icon').addClass('none');
		$(this).addClass('mrt-hover').siblings().removeClass('mrt-hover');
		$(this).children('.main-icon').removeClass('none');
		$(this).siblings().children('.main-icon').addClass('none');
		$('.main-pro-list').hide().eq($(this).index()).show();
	})
  })($);
