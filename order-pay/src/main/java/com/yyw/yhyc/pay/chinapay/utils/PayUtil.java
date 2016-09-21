package com.yyw.yhyc.pay.chinapay.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 支付属性
 */
public class PayUtil {

	public static final String MerId = "MerId";

	public static final String TranType = "TranType";

	public static final String BusiType = "BusiType";

	public static final String Version = "Version";

	public static final String SplitType = "SplitType";

	public static final String SplitMethod = "SplitMethod";

	public static final String BankInstNo = "BankInstNo";

	public static final String PayTimeOut = "PayTimeOut";

	public static final String TimeStamp = "TimeStamp";

	public static final String CurryNo = "CurryNo";

	public static final String AccessType = "AccessType";

	public static final String AcqCode = "AcqCode";

	public static final String CommodityMsg = "CommodityMsg";

	public static final String MerPageUrl = "MerPageUrl";

	public static final String MerBgUrl = "MerBgUrl";

	public static final String MerResv = "MerResv";

	public static final String trans_BusiId = "trans_BusiId";

	public static final String trans_P1 = "trans_P1";

	public static final String trans_P2 = "trans_P2";

	public static final String trans_P3 = "trans_P3";

	public static final String trans_P4 = "trans_P4";

	public static final String trans_P5 = "trans_P5";

	public static final String trans_P6 = "trans_P6";

	public static final String trans_P7 = "trans_P7";

	public static final String trans_P8 = "trans_P8";

	public static final String trans_P9 = "trans_P9";

	public static final String trans_P10 = "trans_P10";

	private static Properties properties = null;
	
	private static Properties propertiesForApp = null;
	
	private static Properties propertiesForB2b = null;


	/**
	 * 根据key值取得对应的value值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		try {
			if(properties==null){
			 InputStream is = PayUtil.class.getResourceAsStream("pay.properties");
			 properties = new Properties();
			 properties.load(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	
	/**
	 * 根据key值取得对应的value值
	 * 
	 * @param key
	 * @return
	 */
	public static String getAppValue(String key) {
		try {
			if(propertiesForApp==null){
			 InputStream isApp = PayUtil.class.getResourceAsStream("/com/chinapay/config/payForApp.properties");
			 propertiesForApp = new Properties();
			 propertiesForApp.load(isApp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertiesForApp.getProperty(key);
	}
	
	/**
	 * 根据key值取得对应的value值
	 * 
	 * @param key
	 * @return
	 */
	public static String getB2bValue(String key) {
		try {
			if(propertiesForB2b==null){
			 InputStream isB2b = PayUtil.class.getResourceAsStream("/com/chinapay/config/payForB2b.properties");
			 propertiesForB2b = new Properties();
			 propertiesForB2b.load(isB2b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertiesForB2b.getProperty(key);
	}
}
