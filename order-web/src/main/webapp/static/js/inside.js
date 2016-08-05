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
})($)

