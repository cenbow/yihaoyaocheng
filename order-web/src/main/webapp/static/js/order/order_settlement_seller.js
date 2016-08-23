var params = undefined;

$(function(){
	//初始化区域信息
	initArea();
	//初始化分页插件
	fnInitPageUtil();
	//初始化时间 按钮选择
	initDateSel();
	//初始化查询数据
	pasretFormData();
	//获取数据
	doRefreshData(params);
	//绑定 搜索的click事件
	bindSearchBtn();
})


function  initArea() {
	$.fn.loadArea($("#province"), $("#city"), $("#area"))
}

function initDateSel(){
	$(".padding-t-10 a").on("click",function () {
		var num = -3;
		var curIndex = $(this).index();
		if( curIndex==0){
			num = -3;
		}else if(curIndex==1){
			num = -7;
		}else if(curIndex ==2){
			num = -30;
		}
		selectDate(num)
	})
}
function fnInitPageUtil(){
	$("#J_pager").pager();
}
//初始化数据为  {"pageNo":1 ,"pageSize":22 ,"param":{"province":11。。。。}}格式
function pasretFormData(){
	params = new Object();
	var p = $("form").serializeObject()
	params.pageNo = 1;
	params.pageSize = 15;
	params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn(){
	$("#searchForm .btn-info").on("click",function () {
		params.pageNo = 1;
		pasretFormData();
		doRefreshData(params);
	})
}

function fillPagerUtil(data,requestParam) {
	var totalpage = data.totalPage;
	var nowpage = data.pageNo;
	var totalCount = data.total;
	$("#J_pager").attr("current",nowpage);
	$("#J_pager").attr("total",totalpage);
	$("#J_pager").attr("url",requestUrl);
	$("#J_pager").pager({
		data:requestParam,
		requestType:"post",
		asyn:1,
		callback:function(data,index){
			var nowpage = data.page;
			$("#nowpageedit").val(nowpage);
			fillTableJson(data);
		}});
}

function doRefreshData(requestParam){
	var requestUrl = ctx+"/order/orderSettlement/listPg/t1";
	$.ajax({
		url : requestUrl,
		data : JSON.stringify(requestParam),
		type : 'POST',
		dataType:'json',
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			//填充表格数据
			fillTableJson(data);
			//设置分页组件参数
			// fillPagerUtil(data,requestParam);
			var totalpage = data.totalPage;
			var nowpage = data.pageNo;
			var totalCount = data.total;
			$("#J_pager").attr("current",nowpage);
			$("#J_pager").attr("total",totalpage);
			$("#J_pager").attr("url",requestUrl);
			$("#J_pager").pager({
				data:requestParam,
				requestType:"post",
				asyn:1,
				contentType:'application/json;charset=UTF-8',
				callback:function(data,index){
					var nowpage = data.page;
					$("#nowpageedit").val(nowpage);
					fillTableJson(data);
				}});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alertModal("查询结算列表错误",function(){
				closeAlert();
			});
		}
	});
}

//表单转换成 josn
$.fn.serializeObject = function() {
	var json = {};
	var arrObj = this.serializeArray();
	$.each(arrObj, function() {
		if (json[this.name]) {
			if (!json[this.name].push) {
				json[this.name] = [ json[this.name] ];
			}
			json[this.name].push(this.value || '');
		} else {
			json[this.name] = this.value || '';
		}
	});
	return json;
};


/**
 * 填充表格数据
 * @param data
 */
function fillTableJson(data) {
	var indexNum = 1 ;
	var list = data.resultList;
	$(".table-box tbody").html("");
	var trs = "";
	if(list && list.length>0){

		for (var i = 0; i < list.length; i++) {
			var orderSettlemnt = list[i];
			var operation = typeToOperate(orderSettlemnt.businessType,orderSettlemnt.confirmSettlement,orderSettlemnt.orderSettlementId);
			var tr = "<tr>";
			tr += "<td>" + orderSettlemnt.flowId + "</td>";
			tr += "<td>" + orderSettlemnt.payTypeName + "</td>";
			tr += "<td>" + orderSettlemnt.businessTypeName + "</td>";
			tr += "<td>" + orderSettlemnt.supplyName + "</td>";
			tr += "<td>" + orderSettlemnt.orderTime + "</td>";
			tr += "<td>" + orderSettlemnt.settlementTime + "</td>";
			tr += "<td>" + orderSettlemnt.settlementMoney + "</td>";
			tr += "<td>" + orderSettlemnt.confirmSettlementName + "</td>";
			tr += "<td>" +operation + "</td>";
			tr += "</tr>";
			trs += tr;
		}
	}
	$(".table-box tbody").append(trs);
	changeColor();
	bindOperateBtn();
}
//类型 转换成操作
function typeToOperate(businessType,confirm,settlementId) {
	var result = '';
	if(businessType==2){//只有退款有操作
		if(confirm ==0){
			result = '<button type="button" class="btn btn-info btn-sm editbtn back-opreate" data-stmid = "'+settlementId+'">退款结算</button>';
		}else  if(confirm ==1){
			result = '<button type="button" class="btn btn-info btn-sm editbtn back-detail " data-stmid = "'+settlementId+'">退款详情</button>';
		}
	}
	return result;
}

function changeColor(){
	$(".table tr:not(:first):odd").css({background:"#f7f7f7"});
	$(".table tr:not(:first):even").css({background:"#fff"});
}

function  bindSettlementOperate() {
	/*$('#myModalOperate input[name="refunSettlementMoney"]').on("afterpaste",function(){
		console.info(this.value);
		$(this).val(this.value.replace(/\D/g,''));
	});
	$('#myModalOperate input[name="refunSettlementMoney"]').on("keyup",function(){
		console.info(this.value);
		$(this).val(this.value.replace(/\D/g,''));
	})*/

	$('#myModalOperate input[name="refunSettlementMoney"]').blur(function () {
		var money = $(this).val();
		var orderMoney = $("#myModalOperate .form-group:eq(0) div:first").html().replace("元","").trim();
		var regStr = /\d+(\.\d+){0,1}/g;
		if(regStr.test(money)){
			var differentMoney = (parseFloat(money)-parseFloat(orderMoney));
			$("#myModalOperate .form-group:eq(2) div:first").html(differentMoney+"元")
		}else{
			if(money&&money!=""){
				alertModal("请输入数字")
			}
		}
	});
	$("#myModalOperate .btn-danger").click(function () {
		var refunSettlementMoney = $("[name='refunSettlementMoney']").val();
		var remark = $("[name='remark']").val();
		if(!refunSettlementMoney || refunSettlementMoney ==""){
			alertModal("请输入结算金额");
			return;
		}
		if(!remark || remark ==""){
			alertModal("请填写备注信息");
			return;
		}
		var param = $("#upForm").serializeObject();
		var requestUrl = ctx+"/order/orderSettlement/refundSettlement";
		$.ajax({
			url : requestUrl,
			data : JSON.stringify(param),
			type : 'POST',
			dataType:'json',
			contentType : "application/json;charset=UTF-8",
			success : function(data) {
				if(data){
                    //Maskremove();
					$("#myModalOperate").modal("hide");
					alert("保存成功");
				};
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("保存失败",function(){
					closeAlert();
				});
			}
		});
	});

}

/**
 * 初始化弹窗内容
 */
function initSettlementOperateModal(formId) {
	$("#"+formId+" .form-group:eq(1) input").val("");
	$("#"+formId+" .form-group:eq(2) div").html("");
	$("#"+formId+" .form-group:eq(3) textarea").val("");

}

function initSettlementDetailModal(id) {
	$("#"+id+" .form-group div").html("");
}

function bindOperateBtn() {
	$(".back-opreate").on("click",function () {
		//$("#myModalOperate").modal();
		initSettlementOperateModal("upForm");
		var settlementId = $(this).attr("data-stmid");

		var requestUrl = ctx+"/order/orderSettlement/getByPK/"+settlementId;

		$.ajax({
			url : requestUrl,
			type : 'GET',
			dataType:'json',
			success : function(data) {
				$("#myModalOperate .form-group:eq(0) div" ).html(data.settlementMoney+"元")
				$('#myModalOperate input[name="orderSettlementId"]').val(settlementId);
				$("#myModalOperate").modal();
				bindSettlementOperate();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("退款详情信息错误",function(){
					closeAlert();
				});
			}
		});
	});
	$(".back-detail").on("click",function () {

		initSettlementDetailModal("myModalDetail");
		var settlementId = $(this).attr("data-stmid");

		var requestUrl = ctx+"/order/orderSettlement/getByPK/"+settlementId;

		$.ajax({
			url : requestUrl,
			type : 'GET',
			dataType:'json',
			success : function(data) {
				$("#myModalDetail .form-group:eq(0) div" ).html(data.settlementMoney+"元");
				$("#myModalDetail .form-group:eq(1) div" ).html(data.refunSettlementMoney+"元");
				$("#myModalDetail .form-group:eq(2) div" ).html(data.differentMoney+"元");
				$("#myModalDetail .form-group:eq(3) div" ).html(data.remark);
				$("#myModalDetail").modal();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("查询结算列表错误",function(){
					closeAlert();
				});
			}
		});
	});
}


function selectDate(day){
	var now = new Date();
	var pre;
	pre = now.valueOf()
	pre = pre + day * 24 * 60 * 60 * 1000
	pre = new Date(pre);

	$("input[name='startTime']").val(format(pre));
	$("input[name='endTime']").val(format(now));
}

function format(date){
	var year  = date.getFullYear();
	var month  = date.getMonth()+1;
	var day  = date.getDate();
	return year+'-'+month+'-'+day;
}