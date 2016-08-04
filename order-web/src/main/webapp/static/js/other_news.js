$(function(){
	//安全中心的js操作
		/*$("#setpass").Validform({
			btnSubmit:"#btn_sub", 	
			btnReset:".btn_reset",	
			showAllError:true,	
			tiptype:3,
			label:".label",	
			ajaxPost:true,	
		});*/
		$('#editpassword').click(function(){
			$("#myModal2").modal("show");
		});
		$('#editpassword1').click(function(){
			$("#myModal1").modal("show");
		});
	//操作收货地址的js	
	    oper_address();
	    //表单验证
		var demo = $("#addaddress_form").Validform({
			btnSubmit:"#add_address",
			btnReset:"#cancel_consignee",
			showAllError:true,	
			tiptype:3,
			label:".label",
			beforeSubmit:function(curform){
				return false;
			},
			datatyp:{
			    "phone":function(gets,obj,curform,regxp){
			        //参数gets是获取到的表单元素值，
			        //obj为当前表单元素，
			        //curform为当前验证的表单，
			        //regxp为内置的一些正则表达式的引用。
			        
			        //return false表示验证出错，没有return或者return true表示验证通过，字符串表示验证错误并作为错误信息返回。
			    }
			}
		});
		//单独添加规则
		demo.addRule([
		{
			ele:"select",
			datatype:"*"
		}]);
		
		$("#add_address").on("click",function(){
			$("#addaddress_form").submit();
			if (!demo.check()) {
				return;
			}else{
				add_dress();
				demo.resetForm();
				$("#oindex").val("");
				$("select[name='city3']").prepend("<option value='' selected='selected'>-城市-</option>");
				$("select[name='area3']").prepend("<option value='' selected='selected'>-地区-</option>");
				return false;
			}
		});
		

});
var editaddress =  function(index){
	$("input[name='username']").val(obj[index]['username']);
	$("input[name='address']").val(obj[index]['address']);
	$("input[name='tel']").val(obj[index]['tel']);
	$("select[name='province3']").val(obj[index]['province3']);
	$("select[name='city3']").val(obj[index]['city3']);
	$("select[name='area3']").val(obj[index]['area3']);
	$("#oindex").val(index);
	$("#addaddress_form").submit();
	return false;
}
var oper_address = function (){
	$('.consignee_box').on("click",".address_close",function(){
    	$(this).closest('.dress_list').remove();
    	var i = $(this).attr("index");
    	delete obj[i];
    	$("#total_num").html($(".dress_list").length);
    });
    $('.consignee_box').on("click",".setdefault_address",function(){
    	$(".dress_list").siblings("div").find(".setdefault_address").html("设置为默认收货地址").removeClass("t_red");
    	$(this).html("默认收货地址").addClass('t_red');
    });
    //一共有多少个地址
    $("#total_num").html($(".dress_list").length);
    //添加收货地址动画
    $(".add_dress button").on("click",function(){
    	$("#consignee").slideToggle();
    });
    $("#cancel_consignee").on("click",function(){
    	$("#consignee").hide(500);
    });
    	//点击编辑进行
    $(".consignee_box").on("click",'.edit_address',function(){
    	
    });
}
var obj = [];
var index = 0;
var add_dress = function (){
	//一次获取表单所有
	var arr = [];
	x=$("#addaddress_form").serializeArray();
    $.each(x, function(i, field){
      	arr[field.name] = field.value;
    });
	if($("#oindex").val()){
		obj[$("#oindex").val()] = arr;
		var _this = $("#dress_list"+$("#oindex").val());
		_this.find("#goodsname").html(arr['username']);
		_this.find("#goodsarea").html(arr['province3']+"-"+arr['city3']+"-"+arr['area3']);
		_this.find("#goodsaddress").html(arr['address']);
		_this.find("#goodstel").html(arr['tel']);
		return;
	}else{
		index++;
		obj[index] = arr;
	}
	
	var data = { 
		index:index,
		goodsname:"联系人",
		goodsnameval:arr['username'],
		goodsarea:"所在地区",
		goodsareaval:arr['province3']+"-"+arr['city3']+"-"+arr['area3'],
		goodsaddress:"地址",
		goodsaddressval:arr['address'],
		goodstel:"联系电话",
		goodstelval:arr['tel']
	};
	var html = template('takegoods', data);
	$(".consignee_box").prepend(html);
	$("#total_num").html($(".dress_list").length);
}
