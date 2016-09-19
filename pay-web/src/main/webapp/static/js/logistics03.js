//表单验证
$.validator.setDefaults( {
	submitHandler: function () {
		alert( "submitted!" );
	}
} );
$( document ).ready( function () {
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {
		return this.optional(element) || isIdCardNo(value);
	}, "请正确输入您的身份证号码");
	$( "#signupForm01" ).validate( {
		rules: {
			scope: "required",
			mm1:{
				required: true,
				isIdCardNo:true
			},
			carnum:{
				required: true,
        		number: true
			},
			warehousearea:{
				required: true,
        		number: true
			}
		},
		messages: {
			scope: "请填写企业经营范围",
			mm1:{
				required:"必填",
				isIdCardNo:"22"
			},
			carnum:{
				required: "请输入车辆数",
        		number: "必须输入数字"
			},
			warehousearea:{
				required: "请输入总面积",
        		number: "必须输入数字"
			}
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
			$( element ).closest("div" ).addClass( "has-error" ).removeClass( "has-success" );
		},//element验证通过时触发
		unhighlight: function (element, errorClass, validClass) {
			$( element ).closest("div" ).addClass( "has-success" ).removeClass( "has-error" );
		}
	} );
} );
