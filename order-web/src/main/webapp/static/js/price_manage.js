var arr = [];
$("#lefttable tbody tr").on("click",function(){
	var val = $(this).find("td:nth-child(1)").html();
	$(this).css({"background":"#d6f0fb"}).siblings("tr").css({"background":"#fff"});
	var isin = $.inArray(val, arr);
	$(".default_zi").remove();
	if(isin==-1 && arr.length<5){
		arr.push(val);
		$("#righttable").append("<tr><td><span>"+val+"</span><i class='fa fa-close'></i></td></tr>");
	}
});
var arr1 = [];
$("#lefttable1 tbody tr").on("click",function(){
	$(this).css({"background":"#d6f0fb"}).siblings("tr").css({"background":"#fff"});
	$(".default_zi").remove();
	var ar = '';
	for(var i=1;i<$(this).find('td').length+1;i++){
		ar+=$(this).find("td:nth-child("+i+")").html() +','; 
	}
	ar=ar.substring(0,ar.length-1);
	
	var isin = $.inArray(ar, arr1);
	if(isin==-1){
		arr1.push(ar);
		var str = ar.split(',');
		var stri = '';
		for(var i=0;i<str.length;i++){
			stri+= '<td>'+str[i]+'<a></a></td>';
		}
		$("#righttable1").append("<tr>"+stri+"<a>23232323</a></tr>");
	}
});

$("#righttable").on("click","tr",function(){
	$(this).closest("tr").remove();
	var val = $(this).prev("span").html();
	arr.splice($.inArray(val,arr),1);
});
$("#righttable1").on("click","tr",function(){
	$(this).remove();
	var ar = '';
	for(var i=1;i<$(this).find('td').length+1;i++){
		if(i==$(this).find('td').length+1){
			ar+=$(this).find("td:nth-child("+i+")").html() +','; 
		}else{
			ar+=$(this).find("td:nth-child("+i+")").html() +',';	
		}
	}
	ar=ar.substring(0,ar.length-1);
	arr1.splice($.inArray(arr1),1);
	console.log(arr1);
});
