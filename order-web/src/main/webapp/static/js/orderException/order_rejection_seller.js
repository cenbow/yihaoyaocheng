var params = undefined;

$(function(){
	//初始化分页插件
	fnInitPageUtil();
	//初始化查询数据
	pasretFormData();
	//获取数据
	doRefreshData(params);
	//绑定 搜索的click事件
	bindSearchBtn();
	//绑定切换tabs的事件
	bindChangeTabs();
})

function  bindChangeTabs() {
	$(".nav-tabs li").on("click",function () {
		var index = $(this).index();
		index = index+1;
		params.pageNo = 1;
		pasretFormData();
		var requestUrl = ctx+"/orderException/sellerRejcetOrderManage/list"+index;
		doRefreshData(params,requestUrl)
	})
}

function fnInitPageUtil(){
	$("#J_pager").pager();
}
//初始化数据为  {"pageNo":1 ,"pageSize":22 ,"param":{"province":11。。。。}}格式
function pasretFormData(){
	params = new Object();
	var p = $("form:first").serializeObject()
	params.pageNo = 1;
	params.pageSize = 15;
	params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn(){
	$("form .btn-info").on("click",function () {
		params.pageNo = 1;
		pasretFormData();
		var index  = $(".nav-tabs .active").index();
		index = index+1;
		var requestUrl = ctx+"/orderException/sellerRejcetOrderManage/list"+index;
		doRefreshData(params,requestUrl);
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

function doRefreshData(requestParam,requestUrl){
	if(!requestUrl || requestUrl == ''){
		requestUrl = ctx+"/orderException/sellerRejcetOrderManage/list1";
	}
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
			var pagination = data.pagination;
			var totalpage = pagination.totalPage;
			var nowpage = pagination.pageNo;
			var totalCount = pagination.total;
			$("#J_pager").attr("current",nowpage);
			$("#J_pager").attr("total",totalpage);
			$("#J_pager").attr("url",requestUrl);
			$("#J_pager").pager({
				data:requestParam,
				requestType:"post",
				asyn:1,
				contentType:'application/json;charset=UTF-8',
				callback:function(data,index){
					var nowpage = data.pagination.page;
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
	var list = data.pagination.resultList;
	$(".table-box tbody").html("");
	var trs = "";
	if(list && list.length>0){
		for (var i = 0; i < list.length; i++) {
			var oe = list[i];
			var operation = typeToOperate(oe.orderSettlementId);
			var tr = "<tr>";
			tr += "<td>" + oe.flowId + "</td>";
			tr += "<td>" + oe.createTime + "</td>";
			tr += "<td>" + oe.custName + "</td>";
			tr += "<td>" + oe.orderStatusName + "</td>";
			tr += "<td>" + oe.orderMoneyTotal + "</td>";
			tr += "<td>" +operation + "</td>";
			tr += "</tr>";
			trs += tr;
		}
	}
	$(".table-box tbody").append(trs);
	$(".nav-tabs li:eq(1) a").html("待确认("+data.orderExceptionDto.waitingConfirmCount+")");
	$(".nav-tabs li:eq(2) a").html("退款中("+data.orderExceptionDto.refundingCount+")");
	$("#orderCount").html(data.pagination.total);
	$("#orderTotalMoney").html(data.orderExceptionDto.orderMoneyTotal);
	changeColor();
	bindOperateBtn();
}
//类型 转换成操作
function typeToOperate(businessType,confirm,settlementId) {
	var result = '';

	return result;
}

function changeColor(){
	$(".table tr:not(:first):odd").css({background:"#f7f7f7"});
	$(".table tr:not(:first):even").css({background:"#fff"});
}

function bindOperateBtn() {
	$(".back-opreate").on("click",function () {
		//$("#myModalOperate").modal();
	});
	$(".back-detail").on("click",function () {
		var settlementId = $(this).attr("data-stmid");

		var requestUrl = "/order/orderSettlement/getByPK/"+settlementId;

		$.ajax({
			url : requestUrl,
			type : 'GET',
			dataType:'json',
			success : function(data) {
				$("#myModalDetail .form-group:eq(0) div" ).html(data.settlementMoney+"元")
				$("#myModalDetail .form-group:eq(1) div" ).html(data.refunSettlementMoney+"元")
				$("#myModalDetail .form-group:eq(2) div" ).html(data.differentMoney+"元")
				$("#myModalDetail .form-group:eq(3) div" ).html(data.remark)
				$("#myModalDetail").modal();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("退款详情信息错误",function(){
					closeAlert();
				});
			}
		});
	});
}


