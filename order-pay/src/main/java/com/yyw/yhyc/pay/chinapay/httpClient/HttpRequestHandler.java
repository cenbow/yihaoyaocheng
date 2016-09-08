package com.yyw.yhyc.pay.chinapay.httpClient;

import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import com.yyw.yhyc.pay.chinapay.utils.SignUtil;
import com.yyw.yhyc.pay.chinapay.utils.StringUtil;
import com.yyw.yhyc.utils.HttpClientUtil;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/*
 * 主要实现通过http后台以post方式
 * 向银联发送请求，关对请求的参数
 */

public class HttpRequestHandler {

	private static final String transResvered = "trans_";
	private static final String cardResvered = "card_";
	private static final String transResveredKey = "TranReserved";
	private static final String cardResveredKey = "CardTranData";
	private static final String signatureField = "Signature";
	private static final String queryKeys=",Version,MerId,MerOrderNo,TranDate,TranTime,TranType,BusiType,TranReserved,Signature,";
	

	public HttpRequestHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 执行Http post 请求
	 * 
	 */
	public static Map<String, String> executePostHttpRequest(Map<String, Object> params, String action) {
		System.out.println("银联请求参数："+params.toString());
		HttpClient client = HttpClientUtil.getHttpClient();
		PostMethod post = new PostMethod(action);
		post.addRequestHeader("User-Agent", "Mozilla/4.0");
		NameValuePair[] nameValuePair = getRequestParams(params);
		post.setRequestBody(nameValuePair);
		String returnStr = "";
		String returnStrOld = "";
		try {
			// 对执行状态需做判定
			int status = client.executeMethod(post);
			returnStrOld = post.getResponseBodyAsString();
			returnStr = URLDecoder.decode(returnStrOld, "utf-8");
			
			//后台http请求状态
			if(status!=200){
			  System.out.println("银联响应异常结果："+returnStr);
			   return getErrorMap();
			}else{
			  System.out.println("银联响应结果："+returnStr);
			}
		} catch (HttpException e) {
			System.out.println("银联响应异常结果："+returnStr);
			e.printStackTrace();
			return getErrorMap();
		} catch (IOException e) {
			System.out.println("银联响应异常结果："+returnStr);
			e.printStackTrace();
			return getErrorMap();
		}
		

		//验证返回的签名
		Map<String, String> reponse= StringUtil.paserStrtoMap(returnStr);
		Map<String, String> reponseOld=StringUtil.paserStrtoMap(returnStrOld);
		if(SignUtil.verify(reponseOld)){
			System.out.println("银联响签名结果：true");
			return reponse;
		}else{
			System.out.println("银联响签名结果：false");
		}
		
		return reponse;
	}

	/*
	 * 设置后台发送请求的参数
	 */
	public static NameValuePair[] getRequestParams(Map<String, Object> rparam) {
		// 将请求参数签名及加密
		Map<String, Object> sendMap = getSignMap(rparam);
		Set<String> keySets = sendMap.keySet();
		NameValuePair[] nameValuePair = new NameValuePair[keySets.size()];
		int i = 0;
		for (String key : keySets) {
			nameValuePair[i] = new NameValuePair(key, (String) sendMap.get(key));
			i++;
		}
		return nameValuePair;
	}

	/*
	 * 把请求的form表单数据进行敏感信息加密及签名
	 */
	public synchronized static Map<String, Object> getSignMap(Map<String, Object> rparam) {

		// 交易扩展域信息
		JSONObject transResvedJson = new JSONObject();
		// 有卡信息域信息-需要签名加密
		JSONObject cardInfoJson = new JSONObject();
		Set<String> keySets = rparam.keySet();
		Map<String, Object> sendMap = new HashMap<String, Object>();
		for (String key : keySets) {
			String value = (String) rparam.get(key);

			// 过滤空值
			if (StringUtil.isEmpty(value)) {
				continue;
			}

			if (key.startsWith(transResvered)) {
				// 组装交易扩展域
				key = key.substring(transResvered.length());
				transResvedJson.put(key, value);
			} else if (key.startsWith(cardResvered)) {
				// 组装有卡交易信息域
				key = key.substring(cardResvered.length());
				cardInfoJson.put(key, value);
			} else {
				sendMap.put(key, value);
			}
		}
		String transResvedStr = null;
		String cardResvedStr = null;
		if (cardInfoJson != null)
			cardResvedStr = cardInfoJson.toString();
		if (transResvedJson != null)
			transResvedStr = transResvedJson.toString();
		// 敏感信息加密处理
		if (StringUtil.isNotEmpty(cardResvedStr)) {
			cardResvedStr = SignUtil.decryptData(cardResvedStr);
			if(StringUtil.isNotEmpty(cardResvedStr)){
				sendMap.put(cardResveredKey, cardResvedStr);
			}
		}
		//if (StringUtil.isNotEmpty(transResvedStr)) {
			//sendMap.put(transResveredKey, transResvedStr);
		//}
		// 商户签名
		String busiType=(String)sendMap.get(PayUtil.TranType);
		String MerBgUrls="";
		if(busiType.equals("9908")){
			MerBgUrls=(String)sendMap.get(PayUtil.MerBgUrl);
			sendMap.remove(PayUtil.MerBgUrl);
		}
		//fromWhere 1表示是pc 2 代表是app 发起的支付 不同支付签名不一样
		sendMap=getInitMap(sendMap);
		
		String signature ="";
		 if(!StringUtil.isEmpty((String)sendMap.get("fromWhere"))&&sendMap.get("fromWhere").equals(ChinaPayUtil.B2C)){
			 sendMap.remove("fromWhere");
			 signature = SignUtil.sign(sendMap); 
		 }else if(!StringUtil.isEmpty((String)sendMap.get("fromWhere"))&&sendMap.get("fromWhere").equals(ChinaPayUtil.NOCARD)){
			 if(busiType.equals("9908")||busiType.equals("0401")){
			    sendMap.remove("BankInstNo");
			 }
			/* if(busiType.equals("0001")){
				 sendMap.put("MerPageUrl", PayUtil.getValue("payReturnHost") + "/thirdpay/app_submit_success.html");
			  }*/
			 sendMap.remove("fromWhere");
			 signature = SignUtil.signForApp(sendMap);
		 }else{
			 sendMap.remove("fromWhere");
			 signature = SignUtil.signForB2b(sendMap);
		 }
		 
		 
		sendMap.put(signatureField, signature);
		if(busiType.equals("9908")){
			sendMap.put(PayUtil.MerBgUrl, MerBgUrls);
		}
		System.out.print("银联支付请求参数："+sendMap);
		return sendMap;
	}

	/*
	 *截取中文字符
	 */
	public static String bSubstring(String s, int length)
	{

		try{
			byte[] bytes = s.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			int i = 2; // 前两个字节是标志位，bytes[0] = -2，bytes[1] = -1。所以从第3位开始截取。

			for (; i < bytes.length && n < length; i++)
			{
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1)
				{
					n++; // 在UCS2第二个字节时n加1
				}
				else
				{
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0)
					{
						n++;
					}
				}
			}
			// 如果i为奇数时，处理成偶数
			if (i % 2 == 1)
			{
				// 该UCS2字符是汉字时，去掉这个截一半的汉字
				if (bytes[i - 1] != 0)
					i = i - 1;
					// 该UCS2字符是字母或数字，则保留该字符
				else
					i = i + 1;
			}
			if(i>=length){
				return new String(bytes, 0, i, "Unicode")+"...等";
			}
			return new String(bytes, 0, i, "Unicode");

		} catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * 发起支付时，当分账信息中所有的分账号都是一号药业时
	 * 实行实时 支付
	 */
	private static Map<String, Object> getInitMap(Map<String, Object> sendMap){
		String TranType=(String)sendMap.get("TranType");
		if(!TranType.equals("0002")&&!TranType.equals("0001")){
			return sendMap;
		}
		/*
		if(isBysystemReceiveMoney(sendMap)){
			sendMap.put("SplitType", "0001");
		}*/
		return sendMap;
	}
	
	/*
	 * 判断当前分账是否全部都是由一号药业代收货款
	 */
	public static boolean isBysystemReceiveMoney(Map<String, Object> sendMap){
		boolean is=false;
		String MerSplitMsg=(String)sendMap.get("MerSplitMsg");
		if(StringUtil.isEmpty(MerSplitMsg)){
			return is;
		}
		int len=MerSplitMsg.split(";").length;
		Map<String, String> ac=ChinaPayUtil.ac;
		Collection<String> accounts=ac.values();
		for(String acc:accounts){
		    String[] arr = (","+MerSplitMsg.toLowerCase()+",").split(acc);
		    if(len==arr.length-1){
		    	is=true;
		    	break;
		    }    
		}
		return is;
	}

	/*
	 * 将前台请求的数据转换map
	 */
	public static Map<String, Object> getRequestMap(HttpServletRequest request) {

		Map<String, Object> sendMap = new HashMap<String, Object>();
		Enumeration<String> requestNames = request.getParameterNames();
		while (requestNames.hasMoreElements()) {
			String key = requestNames.nextElement();
			String value = request.getParameter(key);

			// 过滤空值
			if (StringUtil.isEmpty(value)) {
				continue;
			}

			sendMap.put(key, value);

		}

		return sendMap;
	}

	/*
	 * 提交银联的退款交易参数初始化
	 * 
	 */
	public static Map<String, Object> getTradeRefundsMap(Map<String, Object> params) {
		if (params != null && params.keySet().size() > 0) {
			if (!params.containsKey(PayUtil.TranType)){
				params.put(PayUtil.SplitType, "0001");
				params.put(PayUtil.SplitMethod, "0");
				params.put(PayUtil.TranType, "0401");
			}
				
		}

		return getSubmitFormMap(params);

	}

	/*
	 * 提交银联的交易成功的通知分账参数初始化
	 * 
	 */
	public static Map<String, Object> getTradeDoneMap(Map<String, Object> params) {
		if (params != null && params.keySet().size() > 0) {
			if (!params.containsKey(PayUtil.TranType))
				params.put(PayUtil.TranType, "9908");
		}

		return getSubmitFormMap(params);

	}
	
	/*
	 * 提交银联的查询交易参数初始化
	 * 
	 */
	public static Map<String, Object> getTradeQueryMap(Map<String, Object> params) {
		if (params != null && params.keySet().size() > 0) {
			if (!params.containsKey(PayUtil.TranType))
				params.put(PayUtil.TranType, "0502");
		}
		
		Map<String, Object> paramsN=new HashMap();
		paramsN.put("TranType", params.get("TranType"));
		paramsN.put("TranDate", params.get("TranDate"));
		paramsN.put("fromWhere", params.get("fromWhere"));
		paramsN.put("MerOrderNo", params.get("MerOrderNo"));
		paramsN=getSubmitFormMap(paramsN);
		Iterator<String> it = paramsN.keySet().iterator();
		while(it.hasNext()) {
			 Object key = it.next();
			if(!queryKeys.contains(","+key+",")&&!"fromWhere".equals(key)){
				it.remove();
			}
	     }
		return paramsN;

	}

	/*
	 * 提交银联的form表彰参数初始化
	 * 
	 */
	public static Map<String, Object> getSubmitFormMap(Map<String, Object> params) {

		 if (params != null && params.keySet().size() > 0) {
			 
		  if(!StringUtil.isEmpty((String)params.get("fromWhere"))&&params.get("fromWhere").equals(ChinaPayUtil.B2C)){

			if (!params.containsKey(PayUtil.MerId) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.MerId)))
				params.put(PayUtil.MerId, PayUtil.getValue(PayUtil.MerId));

			if (!params.containsKey(PayUtil.TranType) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.TranType)))
				params.put(PayUtil.TranType, PayUtil.getValue(PayUtil.TranType));

			if (!params.containsKey(PayUtil.BusiType) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.BusiType)))
				params.put(PayUtil.BusiType, PayUtil.getValue(PayUtil.BusiType));

			if (!params.containsKey(PayUtil.Version) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.Version)))
				params.put(PayUtil.Version, PayUtil.getValue(PayUtil.Version));

			if (!params.containsKey(PayUtil.SplitType) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.SplitType)))
				params.put(PayUtil.SplitType, PayUtil.getValue(PayUtil.SplitType));

			if (!params.containsKey(PayUtil.SplitMethod) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.SplitMethod)))
				params.put(PayUtil.SplitMethod, PayUtil.getValue(PayUtil.SplitMethod));

			if (!params.containsKey(PayUtil.BankInstNo) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.BankInstNo)))
				params.put(PayUtil.BankInstNo, PayUtil.getValue(PayUtil.BankInstNo));

			if (!params.containsKey(PayUtil.PayTimeOut) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.PayTimeOut)))
				params.put(PayUtil.PayTimeOut, PayUtil.getValue(PayUtil.PayTimeOut));

			if (!params.containsKey(PayUtil.TimeStamp) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.TimeStamp)))
				params.put(PayUtil.TimeStamp, PayUtil.getValue(PayUtil.TimeStamp));

			if (!params.containsKey(PayUtil.CurryNo) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.CurryNo)))
				params.put(PayUtil.CurryNo, PayUtil.getValue(PayUtil.CurryNo));

			if (!params.containsKey(PayUtil.AccessType) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.AccessType)))
				params.put(PayUtil.AccessType, PayUtil.getValue(PayUtil.AccessType));

			if (!params.containsKey(PayUtil.AcqCode) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.AcqCode)))
				params.put(PayUtil.AcqCode, PayUtil.getValue(PayUtil.AcqCode));

			if (!params.containsKey(PayUtil.CommodityMsg) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.CommodityMsg)))
				params.put(PayUtil.CommodityMsg, PayUtil.getValue(PayUtil.CommodityMsg));

			if (!params.containsKey(PayUtil.MerPageUrl) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.MerPageUrl)))
				params.put(PayUtil.MerPageUrl, PayUtil.getValue(PayUtil.MerPageUrl));

			if (!params.containsKey(PayUtil.MerBgUrl) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.MerBgUrl)))
				params.put(PayUtil.MerBgUrl, PayUtil.getValue(PayUtil.MerBgUrl));

			if (!params.containsKey(PayUtil.MerResv) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.MerResv)))
				params.put(PayUtil.MerResv, PayUtil.getValue(PayUtil.MerResv));

			if (!params.containsKey(PayUtil.trans_BusiId) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_BusiId)))
				params.put(PayUtil.trans_BusiId, PayUtil.getValue(PayUtil.trans_BusiId));

			if (!params.containsKey(PayUtil.trans_P1) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P1)))
				params.put(PayUtil.trans_P1, PayUtil.getValue(PayUtil.trans_P1));

			if (!params.containsKey(PayUtil.trans_P2) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P2)))
				params.put(PayUtil.trans_P2, PayUtil.getValue(PayUtil.trans_P2));

			if (!params.containsKey(PayUtil.trans_P3) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P3)))
				params.put(PayUtil.trans_P3, PayUtil.getValue(PayUtil.trans_P3));

			if (!params.containsKey(PayUtil.trans_P4) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P4)))
				params.put(PayUtil.trans_P4, PayUtil.getValue(PayUtil.trans_P4));

			if (!params.containsKey(PayUtil.trans_P5) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P5)))
				params.put(PayUtil.trans_P5, PayUtil.getValue(PayUtil.trans_P5));

			if (!params.containsKey(PayUtil.trans_P6) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P6)))
				params.put(PayUtil.trans_P6, PayUtil.getValue(PayUtil.trans_P6));

			if (!params.containsKey(PayUtil.trans_P7) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P7)))
				params.put(PayUtil.trans_P7, PayUtil.getValue(PayUtil.trans_P7));

			if (!params.containsKey(PayUtil.trans_P8) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P8)))
				params.put(PayUtil.trans_P8, PayUtil.getValue(PayUtil.trans_P8));

			if (!params.containsKey(PayUtil.trans_P9) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P9)))
				params.put(PayUtil.trans_P9, PayUtil.getValue(PayUtil.trans_P9));

			if (!params.containsKey(PayUtil.trans_P10) && !StringUtil.isEmpty(PayUtil.getValue(PayUtil.trans_P10)))
				params.put(PayUtil.trans_P10, PayUtil.getValue(PayUtil.trans_P10));
			
		  }else if(!StringUtil.isEmpty((String)params.get("fromWhere"))&&params.get("fromWhere").equals(ChinaPayUtil.NOCARD)){
			 
			  if (!params.containsKey(PayUtil.MerId) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.MerId)))
					params.put(PayUtil.MerId, PayUtil.getAppValue(PayUtil.MerId));

				if (!params.containsKey(PayUtil.TranType) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.TranType)))
					params.put(PayUtil.TranType, PayUtil.getAppValue(PayUtil.TranType));

				if (!params.containsKey(PayUtil.BusiType) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.BusiType)))
					params.put(PayUtil.BusiType, PayUtil.getAppValue(PayUtil.BusiType));

				if (!params.containsKey(PayUtil.Version) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.Version)))
					params.put(PayUtil.Version, PayUtil.getAppValue(PayUtil.Version));

				if (!params.containsKey(PayUtil.SplitType) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.SplitType)))
					params.put(PayUtil.SplitType, PayUtil.getAppValue(PayUtil.SplitType));

				if (!params.containsKey(PayUtil.SplitMethod) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.SplitMethod)))
					params.put(PayUtil.SplitMethod, PayUtil.getAppValue(PayUtil.SplitMethod));

				if (!params.containsKey(PayUtil.BankInstNo) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.BankInstNo)))
					params.put(PayUtil.BankInstNo, PayUtil.getAppValue(PayUtil.BankInstNo));

				if (!params.containsKey(PayUtil.PayTimeOut) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.PayTimeOut)))
					params.put(PayUtil.PayTimeOut, PayUtil.getAppValue(PayUtil.PayTimeOut));

				if (!params.containsKey(PayUtil.TimeStamp) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.TimeStamp)))
					params.put(PayUtil.TimeStamp, PayUtil.getAppValue(PayUtil.TimeStamp));

				if (!params.containsKey(PayUtil.CurryNo) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.CurryNo)))
					params.put(PayUtil.CurryNo, PayUtil.getAppValue(PayUtil.CurryNo));

				if (!params.containsKey(PayUtil.AccessType) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.AccessType)))
					params.put(PayUtil.AccessType, PayUtil.getAppValue(PayUtil.AccessType));

				if (!params.containsKey(PayUtil.AcqCode) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.AcqCode)))
					params.put(PayUtil.AcqCode, PayUtil.getAppValue(PayUtil.AcqCode));

				if (!params.containsKey(PayUtil.CommodityMsg) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.CommodityMsg)))
					params.put(PayUtil.CommodityMsg, PayUtil.getAppValue(PayUtil.CommodityMsg));

				if (!params.containsKey(PayUtil.MerPageUrl) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.MerPageUrl)))
					params.put(PayUtil.MerPageUrl, PayUtil.getAppValue(PayUtil.MerPageUrl));

				if (!params.containsKey(PayUtil.MerBgUrl) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.MerBgUrl)))
					params.put(PayUtil.MerBgUrl, PayUtil.getAppValue(PayUtil.MerBgUrl));

				if (!params.containsKey(PayUtil.MerResv) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.MerResv)))
					params.put(PayUtil.MerResv, PayUtil.getAppValue(PayUtil.MerResv));

				if (!params.containsKey(PayUtil.trans_BusiId) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_BusiId)))
					params.put(PayUtil.trans_BusiId, PayUtil.getAppValue(PayUtil.trans_BusiId));

				if (!params.containsKey(PayUtil.trans_P1) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P1)))
					params.put(PayUtil.trans_P1, PayUtil.getAppValue(PayUtil.trans_P1));

				if (!params.containsKey(PayUtil.trans_P2) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P2)))
					params.put(PayUtil.trans_P2, PayUtil.getAppValue(PayUtil.trans_P2));

				if (!params.containsKey(PayUtil.trans_P3) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P3)))
					params.put(PayUtil.trans_P3, PayUtil.getAppValue(PayUtil.trans_P3));

				if (!params.containsKey(PayUtil.trans_P4) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P4)))
					params.put(PayUtil.trans_P4, PayUtil.getAppValue(PayUtil.trans_P4));

				if (!params.containsKey(PayUtil.trans_P5) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P5)))
					params.put(PayUtil.trans_P5, PayUtil.getAppValue(PayUtil.trans_P5));

				if (!params.containsKey(PayUtil.trans_P6) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P6)))
					params.put(PayUtil.trans_P6, PayUtil.getAppValue(PayUtil.trans_P6));

				if (!params.containsKey(PayUtil.trans_P7) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P7)))
					params.put(PayUtil.trans_P7, PayUtil.getAppValue(PayUtil.trans_P7));

				if (!params.containsKey(PayUtil.trans_P8) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P8)))
					params.put(PayUtil.trans_P8, PayUtil.getAppValue(PayUtil.trans_P8));

				if (!params.containsKey(PayUtil.trans_P9) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P9)))
					params.put(PayUtil.trans_P9, PayUtil.getAppValue(PayUtil.trans_P9));

				if (!params.containsKey(PayUtil.trans_P10) && !StringUtil.isEmpty(PayUtil.getAppValue(PayUtil.trans_P10)))
					params.put(PayUtil.trans_P10, PayUtil.getAppValue(PayUtil.trans_P10));
			  
		  }else{
			  
			  if (!params.containsKey(PayUtil.MerId) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.MerId)))
					params.put(PayUtil.MerId, PayUtil.getB2bValue(PayUtil.MerId));

				if (!params.containsKey(PayUtil.TranType) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.TranType)))
					params.put(PayUtil.TranType, PayUtil.getB2bValue(PayUtil.TranType));

				if (!params.containsKey(PayUtil.BusiType) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.BusiType)))
					params.put(PayUtil.BusiType, PayUtil.getB2bValue(PayUtil.BusiType));

				if (!params.containsKey(PayUtil.Version) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.Version)))
					params.put(PayUtil.Version, PayUtil.getB2bValue(PayUtil.Version));

				if (!params.containsKey(PayUtil.SplitType) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.SplitType)))
					params.put(PayUtil.SplitType, PayUtil.getB2bValue(PayUtil.SplitType));

				if (!params.containsKey(PayUtil.SplitMethod) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.SplitMethod)))
					params.put(PayUtil.SplitMethod, PayUtil.getB2bValue(PayUtil.SplitMethod));

				if (!params.containsKey(PayUtil.BankInstNo) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.BankInstNo)))
					params.put(PayUtil.BankInstNo, PayUtil.getB2bValue(PayUtil.BankInstNo));

				if (!params.containsKey(PayUtil.PayTimeOut) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.PayTimeOut)))
					params.put(PayUtil.PayTimeOut, PayUtil.getB2bValue(PayUtil.PayTimeOut));

				if (!params.containsKey(PayUtil.TimeStamp) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.TimeStamp)))
					params.put(PayUtil.TimeStamp, PayUtil.getB2bValue(PayUtil.TimeStamp));

				if (!params.containsKey(PayUtil.CurryNo) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.CurryNo)))
					params.put(PayUtil.CurryNo, PayUtil.getB2bValue(PayUtil.CurryNo));

				if (!params.containsKey(PayUtil.AccessType) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.AccessType)))
					params.put(PayUtil.AccessType, PayUtil.getB2bValue(PayUtil.AccessType));

				if (!params.containsKey(PayUtil.AcqCode) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.AcqCode)))
					params.put(PayUtil.AcqCode, PayUtil.getB2bValue(PayUtil.AcqCode));

				if (!params.containsKey(PayUtil.CommodityMsg) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.CommodityMsg)))
					params.put(PayUtil.CommodityMsg, PayUtil.getB2bValue(PayUtil.CommodityMsg));

				if (!params.containsKey(PayUtil.MerPageUrl) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.MerPageUrl)))
					params.put(PayUtil.MerPageUrl, PayUtil.getB2bValue(PayUtil.MerPageUrl));

				if (!params.containsKey(PayUtil.MerBgUrl) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.MerBgUrl)))
					params.put(PayUtil.MerBgUrl, PayUtil.getB2bValue(PayUtil.MerBgUrl));

				if (!params.containsKey(PayUtil.MerResv) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.MerResv)))
					params.put(PayUtil.MerResv, PayUtil.getB2bValue(PayUtil.MerResv));

				if (!params.containsKey(PayUtil.trans_BusiId) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_BusiId)))
					params.put(PayUtil.trans_BusiId, PayUtil.getB2bValue(PayUtil.trans_BusiId));

				if (!params.containsKey(PayUtil.trans_P1) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P1)))
					params.put(PayUtil.trans_P1, PayUtil.getB2bValue(PayUtil.trans_P1));

				if (!params.containsKey(PayUtil.trans_P2) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P2)))
					params.put(PayUtil.trans_P2, PayUtil.getB2bValue(PayUtil.trans_P2));

				if (!params.containsKey(PayUtil.trans_P3) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P3)))
					params.put(PayUtil.trans_P3, PayUtil.getB2bValue(PayUtil.trans_P3));

				if (!params.containsKey(PayUtil.trans_P4) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P4)))
					params.put(PayUtil.trans_P4, PayUtil.getB2bValue(PayUtil.trans_P4));

				if (!params.containsKey(PayUtil.trans_P5) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P5)))
					params.put(PayUtil.trans_P5, PayUtil.getB2bValue(PayUtil.trans_P5));

				if (!params.containsKey(PayUtil.trans_P6) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P6)))
					params.put(PayUtil.trans_P6, PayUtil.getB2bValue(PayUtil.trans_P6));

				if (!params.containsKey(PayUtil.trans_P7) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P7)))
					params.put(PayUtil.trans_P7, PayUtil.getB2bValue(PayUtil.trans_P7));

				if (!params.containsKey(PayUtil.trans_P8) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P8)))
					params.put(PayUtil.trans_P8, PayUtil.getB2bValue(PayUtil.trans_P8));

				if (!params.containsKey(PayUtil.trans_P9) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P9)))
					params.put(PayUtil.trans_P9, PayUtil.getB2bValue(PayUtil.trans_P9));

				if (!params.containsKey(PayUtil.trans_P10) && !StringUtil.isEmpty(PayUtil.getB2bValue(PayUtil.trans_P10)))
					params.put(PayUtil.trans_P10, PayUtil.getB2bValue(PayUtil.trans_P10));
		  }

			return params;
		}

		return null;
	}
	
	/*
	 * 后台提交失败，统一返回
	 */
	public static Map<String, String> getErrorMap(){
		Map<String, String> errorMap=new HashMap<String, String>();
		errorMap.put("respCode", "9999");
		errorMap.put("respMsg", "后台提交银联支付失败！");
		return errorMap;
	}
	
	/*
	 * 后台返回数据，签名验证不通过，统一返回
	 */
	public static Map<String, String> getVerifyErrorMap(){
		Map<String, String> errorMap=new HashMap<String, String>();
		errorMap.put("respCode", "9998");
		errorMap.put("respMsg", "签名验证不通过");
		return errorMap;
	}

}
