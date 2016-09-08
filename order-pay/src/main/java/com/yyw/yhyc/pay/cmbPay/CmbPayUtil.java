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
	public static final String SUBORD = "SUBORD";
	public static final String CCYNBR = "CCYNBR";
	public static final String TRSAMT = "TRSAMT";
	public static final String CRTACC = "CRTACC";
	public static final String CRTNAM = "CRTNAM";
	public static final String CRTBNK = "CRTBNK";
	public static final String CRTPVC = "CRTPVC";
	public static final String CRTCTY = "CRTCTY";
	public static final String RETURL = "RETURL";

	/*招行支付回调应答接口成功code*/
	public static final String CALLBACK_SUCCESS_CODE = "00000000";
	/*招行支付回调应接口答失败code*/
	public static final String CALLBACK_FAILURE_CODE = "11111111";
	/*招行支付回调应答接口数据格式模版*/
	public static final String CALLBACK_RESPONSE_TEMPATE =  "<?xml version=\"1.0\" encoding=\"ISO8859-1\"?><DATA><RESPONSE><STSCOD>%s</STSCOD><STSMSG>%s</STSMSG></RESPONSE></DATA>";



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
