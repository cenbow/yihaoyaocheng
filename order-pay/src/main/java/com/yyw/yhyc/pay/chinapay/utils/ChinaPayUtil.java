package com.yyw.yhyc.pay.chinapay.utils;

import java.util.HashMap;
import java.util.Map;

public class ChinaPayUtil {

	/* 银联B2C支付 */
	public static final String B2C="1";

	/* 银联无卡支付 */
	public static final String NOCARD="2";

	/* 银联B2B支付 */
	public static final String B2B="3";

	/* 银联手机支付 */
	public static final String MOBILE="4";

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
