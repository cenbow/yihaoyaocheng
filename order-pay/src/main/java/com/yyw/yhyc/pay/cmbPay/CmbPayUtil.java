package com.yyw.yhyc.pay.cmbPay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CmbPayUtil {

	private static Properties properties = null;

	public static final String CMB_PAY_URL_WITH_SIGNED = "CMB_PAY_URL_WITH_SIGNED";
	public static final String CMB_PAY_URL_WITHOUT_SIGNED = "CMB_PAY_URL_WITHOUT_SIGNED";


	public static final String MCHNBR = "MCHNBR";
	public static final String REFORD = "REFORD";
	public static final String CCYNBR = "CCYNBR";
	public static final String TRSAMT = "TRSAMT";
	public static final String CRTACC = "CRTACC";
	public static final String CRTNAM = "CRTNAM";
	public static final String CRTBNK = "CRTBNK";
	public static final String CRTPVC = "CRTPVC";
	public static final String CRTCTY = "CRTCTY";
	public static final String RETURL = "RETURL";


	/**
	 * 根据key值取得对应的value值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		try {
			if(properties==null){
			 InputStream is = CmbPayUtil.class.getResourceAsStream("/com/cmbPay/config/pay.properties");
			 properties = new Properties();
			 properties.load(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	

}
