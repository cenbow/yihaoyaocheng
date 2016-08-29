package com.yyw.yhyc.pay.chinapay.utils;

import java.util.HashMap;
import java.util.Map;

public class ChinaPayUtil {
	
	public static final String B2C="1";
	
	public static final String NOCARD="2";
	
	public static final String B2B="3";
	
	public static final int PAYINFOTYPE_ZF=1;//表示支付
 
	public static final int PAYINFOTYPE_FZ=2;//表示分账
	
	public static final int PAYINFOTYPE_TK=2;//表示退款
	
	public static Map<String, String> ac=null;
	
	static{
		if(ac==null){
			   ac=new HashMap<String, String>();
			   ac.put("b2c", "999901511180004");
			   ac.put("app", "999901511181004");
			   ac.put("b2b", "999901511182004");
		}
	}
	
}
