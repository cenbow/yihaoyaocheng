function submitForm(){
    var refunSettlementMoney=$("#refunSettlementMoney").val().trim();
    var remark=$("#remark").val().trim();
    var reg = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
    if(!refunSettlementMoney.match(reg) || null==refunSettlementMoney || ""==refunSettlementMoney){
        alert("实际收款金额输入有误，请重新输入");
        return false;
    }
    var p = $("#confirmForm").serializeObject();
    if (window.confirm("确认收款金额为"+refunSettlementMoney+"元？")) {
        $.ajax({
            url: ctx+"/order/addForConfirmMoney",
            type: 'POST',
            data: JSON.stringify(p),
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if(data.statusCode || data.message){
                    alertModal(data.message);
                    return;
                }
                alert("确认收款成功");
                window.location.href=ctx+"/order/sellerOrderManage";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModal("确认收款失败");
            }
        });
    }

}

function changediffMoney(){
    var orgTotal = $("#orgTotalMoney").val();
    var refunSettlementMoney=numSub(Number($("#refunSettlementMoney").val()) , Number(orgTotal));
    if(IsNum(refunSettlementMoney))
        $("#diffMoney").text(refunSettlementMoney);
    else
        $("#diffMoney").text("");
}
function IsNum(s)
{
    if (s!=null && s!="")
    {
        return !isNaN(s);
    }
    return false;
}
/**
 * 减法运算，避免数据相减小数点后产生多位数和计算精度损失。
 *
 * @param num1被减数 | num2减数
 */
function numSub(num1, num2) {
    var baseNum, baseNum1, baseNum2;
    var precision;// 精度
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
    precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
    return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
};
//表单转换成 josn
$.fn.serializeObject = function () {
    var json = {};
    var arrObj = this.serializeArray();
    $.each(arrObj, function () {
        if (json[this.name]) {
            if (!json[this.name].push) {
                json[this.name] = [json[this.name]];
            }
            json[this.name].push(this.value || '');
        } else {
            json[this.name] = this.value || '';
        }
    });
    return json;
};

function goSubmit()
{
    if(window.event.keyCode == 13)
    {
        return false;
    }
}
//禁用Enter键表单自动提交
document.onkeydown = function(event) {
    var target, code, tag;
    if (!event) {
        event = window.event; //针对ie浏览器
        target = event.srcElement;
        code = event.keyCode;
        if (code == 13) {
            tag = target.tagName;
            if (tag == "TEXTAREA") { return true; }
            else { return false; }
        }
    }
    else {
        target = event.target; //针对遵循w3c标准的浏览器，如Firefox
        code = event.keyCode;
        if (code == 13) {
            tag = target.tagName;
            if (tag == "INPUT") { return false; }
            else { return true; }
        }
    }
};