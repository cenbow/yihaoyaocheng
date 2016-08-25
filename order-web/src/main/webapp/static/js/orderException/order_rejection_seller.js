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
	//绑定省市区
	bindAreaData('province','city','area');
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

/**
 * 绑定省市区操作
 * @param prov
 * @param city
 * @param area
 */
function bindAreaData(prov,city,area) {
	var provinceList = getProvince();
	if (provinceList && provinceList.length > 0) {
		var provStr = '<option value="">省份</option>';
		for (var i = 0; i < provinceList.length; i++) {
			provStr += ' <option value="' + provinceList[i].infoCode + '">' + provinceList[i].infoName + '</option>';
		}
		$("#" + prov).html(provStr);
	}
	$("#" + prov).change(function () {
		var cityStr = '<option value="">城市</option>';
		var _prov = $(this).children('option:selected').val();
		if (_prov == '') {
			$("#" + city).html(cityStr);
			$("#" + area).html('<option value="">区/县</option>');
			return;
		}
		var cityList = getCity(_prov);
		if (cityList && cityList.length > 0) {
			for (var i = 0; i < cityList.length; i++) {
				cityStr += ' <option value="' + cityList[i].infoCode + '">' + cityList[i].infoName + '</option>';
			}
			$("#" + city).html(cityStr);
		}
	});
	$("#" + city).change(function () {
		var areaStr = '<option value="">区/县</option>';
		var _city = $(this).children('option:selected').val();
		if (_city == '') {
			$("#" + area).html(areaStr);
			return;
		}
		var areaList = getArea(_city);
		if (areaList && areaList.length > 0) {
			for (var i = 0; i < areaList.length; i++) {
				areaStr += ' <option value="' + areaList[i].infoCode + '">' + areaList[i].infoName + '</option>';
			}
			$("#" + area).html(areaStr);
		}
	});
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
			var operation = typeToOperate(oe.orderStatusName,oe.exceptionId);
			var tr = "<tr>";
			tr += "<td><a href='"+ctx+"/orderException/getDetails-2/" + oe.flowId + "' class='undeline'>"+oe.exceptionOrderId+"</a></td>";
			tr += "<td>" + oe.orderCreateTime + "</td>";
			tr += "<td>" + oe.custName + "</td>";
			tr += "<td>" + oe.orderStatusName + "</td>";
			tr += "<td>&yen" + fmoney(oe.orderMoney,2) + "<br/>" + oe.payTypeName + "</td>";
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
	//bindOperateBtn();
}
//类型 转换成操作
function typeToOperate(statusName,exceptionId) {
	var result = '';
	if(statusName&&statusName=='待确认'){
		result = '<a href="'+ctx+'/orderException/getRejectOrderDetails/'+exceptionId+'" target="_blank" class="btn btn-info btn-sm editbtn back-detail " data-stmid = "'+exceptionId+'">审核</a>';
	}
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
		var flowId = $(this).attr("data-stmid");
		alert(flowId);
		return;
		var requestUrl = "/order/orderSettlement/getByPK/"+settlementId;

		$.ajax({
			url : requestUrl,
			type : 'GET',
			dataType:'json',
			success : function(data) {

			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("退款详情信息错误",function(){
					closeAlert();
				});
			}
		});
	});
}


