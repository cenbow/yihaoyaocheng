$(function(){
	//角色切换
	roleSwitching();
});
//表单验证
$.validator.setDefaults( {
	submitHandler: function () {
		alert( "submitted!" );
	}
} );
$( document ).ready( function () {
	$( "#signupForm" ).validate( {
		rules: {
			firstname: "required",
			oldername: "required",
			login_l:"required"
		},
		messages: {
			firstname: "请填写企业名称",
			oldername: "请填写曾用名",
			login_l:"请输入注册类型"
		},
		errorElement: "span",
		errorPlacement: function ( error, element ) {
			error.addClass( "help-block" );
			if ( element.prop( "type" ) === "checkbox" ) {
				error.insertAfter( element.parent( "label" ) );
			} else {
				error.insertAfter( element );
			}
		},//element出错时触发
		highlight: function (element, errorClass, validClass ) {
			$( element ).closest("div").addClass( "has-error" ).removeClass( "has-success" );
		},//element验证通过时触发
		unhighlight: function (element, errorClass, validClass) {
			$( element ).closest("div").addClass( "has-success" ).removeClass( "has-error" );
		}
	} );
} );

/*角色切换*/
var roleSwitching = function(){
	$("#choseuser_box h3 span").on("click",function(){
		var index = $(this).index();
		$(this).addClass("hovers").siblings("span").removeClass();
		$("#choseuser_box ul").eq(index).show().siblings("ul").hide();
		$(".lasttile").html($(this).html()+"审核所需模板文件");
		if(index != 0){
			$(".yljg").hide();
			$(".sfcj").show();
		}else{
			$(".yljg").show();
			$(".sfcj").hide();
		}
	});
	$(".one_tips a").on("click",function(){
		$(this).addClass("curs").siblings().removeClass();
	});
	$(".two_tips a").on("click",function(){
		$(this).toggleClass("curs");
	});
}
