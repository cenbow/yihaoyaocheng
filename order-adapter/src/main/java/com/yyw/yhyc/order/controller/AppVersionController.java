package com.yyw.yhyc.order.controller;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.AppVersion;
import com.yyw.yhyc.order.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/version", headers = "Accept=application/json;")
public class AppVersionController extends BaseController {
  
	@Autowired
	private AppVersionService appVersionService;
	
	private final static String SUCCESS_MESSAGE = "成功";

	
	@RequestMapping(value = "/getAppNewVersion")
	public Map<String, Object> getAppNewVersion(@RequestParam("versionCode") int  versionCode, HttpServletRequest request){
		Map<String, Object> restultMap = new HashMap<String, Object>();
		restultMap.put("statusCode", "0");
	    restultMap.put("message", SUCCESS_MESSAGE);

		String versionType ;
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.indexOf("iOS") >= 0 && ( userAgent.indexOf("iPhone") >= 0 || userAgent.indexOf("iPad") >= 0 )){
			versionType = "2";
		}else{
			versionType = "1";
		}
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("updateVersion","N" );

			AppVersion condition = new AppVersion();
			condition.setVersionType(versionType);
			List<AppVersion> list = appVersionService.listByProperty(condition);
			if(!UtilHelper.isEmpty(list)){
				AppVersion appVersion = list.get(0);
				String code = appVersion.getVersionCode();
				String updateFag = appVersion.getUpdateFag();
				String remark   = appVersion.getRemark();
				if(Integer.parseInt(code) > versionCode){
					map.put("versionCode", code);
					map.put("updateFag", updateFag);
					map.put("remark", remark);
					map.put("versionPath", appVersion.getVersionPath());
					map.put("updateVersion","Y" );
				}else{
				    restultMap.put("message", "无最新版本");
				}
			}else{
			    restultMap.put("message", "无最新版本");
			}
			
			restultMap.put("data", map);
		} catch (Exception e) {
			restultMap.put("data", null);
			restultMap.put("statusCode", "-3");
		    restultMap.put("message", e.getMessage());
		}
		
		return restultMap;
	}
}
