package com.yyw.yhyc.pay.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import com.yyw.yhyc.pay.alipay.util.UtilDate;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088521120786400";
	
	public static String partner_app = "2088521120786400";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;
	public static String seller_id_app = "2088521120786400";

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKIGcOPTpKDOgn/8BxsiqObR0ZPPwFCeCOOTuPo6ljdtJHUYwW6i+OS8SSLNfGwGaHAvtkmA3cLP7iMgMkPiN2q6fNqEqW/Nu8LNEycvAsO0qX6obMbhTF+jkj/6DpSCC0s3MP755XomugBcWWiajXIB+XsdD/o9wl/1N/dImTdlAgMBAAECgYBJECB+DCVTwmwErLDDosiJdZpNTkTJ1cnqXeXvKNYuXlAvYZ9wdZtJAkL0p7bwu58C3/ESikL2I2+edVlVVUBpb5S/ZvTmFBCd8yfZ2mkcfcK1Qzr4WRxUP/BxyIbDOxQ2Pz0Rrok1AMEv27oMq/lkPg/4w6JF10hHDuPdi9jzYQJBANWptpGVXhoL6Ap8RTEofQVwgsl5ZxWJu9w0Dk5FgkFHOZZffDtLH1ywqr+s0u9MG8E/aizxekeraeeeAwcpLukCQQDCIVhBdAW0bQYifhl9RuLcK4okitt35wKT6nmy/OIb1ZBoQAsuScxcv/wLI07e9ecgqlhsbJ84FmdfjGKQrU8dAkEAj2ppZ3MRRP509ITRlNuOf5Yz07Svot9ev8IZUCSL9/EtEGhrZQlZtcfH9Py2YXV+s8ozJJO59l1yqst/UNEt8QJAEbCgLja4Xlyg9nuvLu9KtO1yxSmyHhb68Seot1q77/ViF13epvUqnjkQaqNpCIA3844SSK8NQzsnaFmOkch5MQJAXX1ZclqGcOF1Ad51GIA0tegPDof7cU6DTyL8zwjLhdU05sIcdLWVcJsKAoaGbB2Nuzu1AyHHta2RvrbYKKFySA==";
	
	public static String private_key_app = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKIGcOPTpKDOgn/8BxsiqObR0ZPPwFCeCOOTuPo6ljdtJHUYwW6i+OS8SSLNfGwGaHAvtkmA3cLP7iMgMkPiN2q6fNqEqW/Nu8LNEycvAsO0qX6obMbhTF+jkj/6DpSCC0s3MP755XomugBcWWiajXIB+XsdD/o9wl/1N/dImTdlAgMBAAECgYBJECB+DCVTwmwErLDDosiJdZpNTkTJ1cnqXeXvKNYuXlAvYZ9wdZtJAkL0p7bwu58C3/ESikL2I2+edVlVVUBpb5S/ZvTmFBCd8yfZ2mkcfcK1Qzr4WRxUP/BxyIbDOxQ2Pz0Rrok1AMEv27oMq/lkPg/4w6JF10hHDuPdi9jzYQJBANWptpGVXhoL6Ap8RTEofQVwgsl5ZxWJu9w0Dk5FgkFHOZZffDtLH1ywqr+s0u9MG8E/aizxekeraeeeAwcpLukCQQDCIVhBdAW0bQYifhl9RuLcK4okitt35wKT6nmy/OIb1ZBoQAsuScxcv/wLI07e9ecgqlhsbJ84FmdfjGKQrU8dAkEAj2ppZ3MRRP509ITRlNuOf5Yz07Svot9ev8IZUCSL9/EtEGhrZQlZtcfH9Py2YXV+s8ozJJO59l1yqst/UNEt8QJAEbCgLja4Xlyg9nuvLu9KtO1yxSmyHhb68Seot1q77/ViF13epvUqnjkQaqNpCIA3844SSK8NQzsnaFmOkch5MQJAXX1ZclqGcOF1Ad51GIA0tegPDof7cU6DTyL8zwjLhdU05sIcdLWVcJsKAoaGbB2Nuzu1AyHHta2RvrbYKKFySA==";
	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	public static String alipay_public_key_app = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String notify_url = "http://tpay.yaoex.com/alipay/notify_url.jsp";
	public static String notify_url = PayUtil.getValue("alipay_notify_url");
	public static String notify_url_app = PayUtil.getValue("alipay_app_notify_url");
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String notify_url_refund = "http://tpay.yaoex.com/alipay/notify_url_refund.jsp";
	public static String notify_url_refund = PayUtil.getValue("alipay_notify_url_refund");

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String return_url = "http://tpay.yaoex.com/alipay/return_url.jsp";
	public static String return_url = PayUtil.getValue("alipay_return_url");

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "D:\\";
		
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";
	
	public static String service_app = "mobile.securitypay.pay";

	//退款日期 时间格式 yyyy-MM-dd HH:mm:ss
	public static String refund_date = UtilDate.getDateFormatter();

	// 调用的接口名，无需修改
	public static String servicerefund = "refund_fastpay_by_platform_pwd";
	
	//APP支付参数文档https://doc.open.alipay.com/doc2/detail?treeId=59&articleId=103663&docType=1
	// 设置未付款交易的超时时间
	// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
	// 取值范围：1m～15d。
	// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
	// 该参数数值不接受小数点，如1.5h，可转换为90m。 
	public static String it_b_pay = "30m";

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
//↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	// 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";
	
	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";
		
//↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
}

