/**
 * Created by shiyongxi on 2016/8/18.
 */

$(function(){
    $.fn.loadArea = function($province, $city, $area){
        if(!_areaMap) {
            alert("缺少area_data.js");
            return;
        }

        if($province.size()>0){
            var htmls = ['<option value="">--省份--</option>'];
            for (var i in _areaMap[0]){
                var o = _areaMap[0][i];
                htmls.push('<option value="'+ o.infoCode +'">'+ o.infoName +'</option>')
            }
            $province.html(htmls.join(""));

            if($city.size()>0) {
                $province.change(function () {
                    var value = $(this).val();
                    htmls = ['<option value="">--市/城--</option>'];
                    for (var i in _areaMap[value]) {
                        var o = _areaMap[value][i];
                        htmls.push('<option value="' + o.infoCode + '">' + o.infoName + '</option>')
                    }
                    $city.html(htmls.join(""));
                });
            }

            if($area.size()>0) {
                $city.change(function () {
                    var value = $(this).val();
                    htmls = ['<option value="">--县/区--</option>'];
                    for (var i in _areaMap[value]) {
                        var o = _areaMap[value][i];
                        htmls.push('<option value="' + o.infoCode + '">' + o.infoName + '</option>')
                    }
                    $area.html(htmls.join(""));
                });
            }
        }
    };
});
