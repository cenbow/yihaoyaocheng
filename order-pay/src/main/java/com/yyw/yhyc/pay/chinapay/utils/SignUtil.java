package com.yyw.yhyc.pay.chinapay.utils;

import com.chinapay.secss.SecssUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class SignUtil {

	private static Properties pPc = null;
	private static Properties pApp = null;
	private static Properties pB2b = null;
	private static String path = null;
	
	static {
		/*
		 * 初始化security.properties属性文件
		 * 
		 */
		
	   if(pPc==null){
		 InputStream is = PayUtil.class.getResourceAsStream("/com/chinapay/config/security.properties");
		 pPc = new Properties();
		 try {
			pPc.load(is);
			is.close();
			initPath(pPc);
			pPc.setProperty("verify.file", path + pPc.getProperty("verify.file"));
			pPc.setProperty("sign.file", path + pPc.getProperty("sign.file"));
			System.out.println(path);
			if(!new File(pPc.getProperty("sign.file")).exists()){
				createSignFile(pPc,path);
			}
		 } catch (IOException e) {
			e.printStackTrace();
		 }
	    }	
	   
	   if(pApp==null){
			 InputStream is = PayUtil.class.getResourceAsStream("/com/chinapay/config/securityForApp.properties");
			 pApp = new Properties();
			 try {
				pApp.load(is);
				is.close();
				initPath(pApp);
				pApp.setProperty("verify.file", path + pApp.getProperty("verify.file"));
				pApp.setProperty("sign.file", path + pApp.getProperty("sign.file"));
				System.out.println(path);
				if(!new File(pApp.getProperty("sign.file")).exists()){
					createSignAppFile(pApp,path);
				}
			 } catch (IOException e) {
				e.printStackTrace();
			 }
	   }
	   
	   if(pB2b==null){
			 InputStream is = PayUtil.class.getResourceAsStream("/com/chinapay/config/securityForB2b.properties");
			 pB2b = new Properties();
			 try {
				pB2b.load(is);
				is.close();
				initPath(pB2b);
				pB2b.setProperty("verify.file", path + pB2b.getProperty("verify.file"));
				pB2b.setProperty("sign.file", path + pB2b.getProperty("sign.file"));
				System.out.println(path);
				if(!new File(pB2b.getProperty("sign.file")).exists()){
					createSignB2bFile(pB2b,path);
				}
			 } catch (IOException e) {
				e.printStackTrace();
			 }
	   }
		   
	}
	
	

	public static String sign(Map signMap) {
		SecssUtil secssUtil = new SecssUtil();
		secssUtil.init(pPc);
		secssUtil.init();
		secssUtil.sign(signMap);

		System.out.println(secssUtil.getErrCode());
		System.out.println(secssUtil.getErrMsg());

		return secssUtil.getSign();
	}
	
	public static String signForApp(Map signMap) {
		SecssUtil secssAppUtil = new SecssUtil();
		secssAppUtil.init(pApp);
		secssAppUtil.init();
		secssAppUtil.sign(signMap);

		System.out.println(secssAppUtil.getErrCode());
		System.out.println(secssAppUtil.getErrMsg());

		return secssAppUtil.getSign();
	}
	
	public static String signForB2b(Map signMap) {
		SecssUtil secssB2bUtil = new SecssUtil();
		secssB2bUtil.init(pB2b);
		secssB2bUtil.init();
		secssB2bUtil.sign(signMap);

		System.out.println(secssB2bUtil.getErrCode());
		System.out.println(secssB2bUtil.getErrMsg());

		return secssB2bUtil.getSign();
	}
	
	

	public static String sign(String merId, String signData) {
		return null;
	}

	public synchronized static boolean verify(Map map) {
		SecssUtil secssUtil = new SecssUtil();
		secssUtil.init(pPc);
		secssUtil.init();
		secssUtil.verify(map);
		if ("00".equals(secssUtil.getErrCode())){
			return true;
		}
		SecssUtil secssAppUtil = new SecssUtil();
		secssAppUtil.init(pApp);
		secssAppUtil.init();
		secssAppUtil.verify(map);
		if ("00".equals(secssAppUtil.getErrCode())){
			return true;
		}	
		
		SecssUtil secssB2bUtil = new SecssUtil();
		secssB2bUtil.init(pB2b);
		secssB2bUtil.init();
		secssB2bUtil.verify(map);
		if ("00".equals(secssB2bUtil.getErrCode())){
			return true;
		}	
		return false;
	}

	public static String decode(String merId, String decData) {

		return null;
	}

	public static String decryptData(String encData) {
		SecssUtil secssUtil = new SecssUtil();
		secssUtil.init(pPc);
		secssUtil.init();
		secssUtil.decryptData(encData);
		return secssUtil.getSign();
	}
	
	/*
	 * 将包中的签名文件放在用户工作目录
	 */
	public static void createSignFile(Properties p, String outFileDirectory){
		
		File fileDirectory = new File(outFileDirectory);
		if(!fileDirectory.exists()){
			fileDirectory.mkdir();
		}

		InputStream inFile = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/yiyiaowangtrade.pfx");
		
		File outFile = new File(p.getProperty("sign.file"));

		try {
		       FileOutputStream outStream = new FileOutputStream(outFile);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile.read(buffer, 0, 8192)) != -1) {
				   outStream.write(buffer, 0, bytesRead);
			   }
			   outStream.close();
			   inFile.close();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }

		InputStream inFile1 = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/cp_test.cer");

		File outFile1 = new File(p.getProperty("verify.file"));

		try {
			   FileOutputStream outStream1 = new FileOutputStream(outFile1);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile1.read(buffer, 0, 8192)) != -1) {
				   outStream1.write(buffer, 0, bytesRead);
			   }
			   outStream1.close();
			   inFile1.close();
			  } catch (Exception e) {
			   e.printStackTrace();
		}
	}
	
	/*
	 * 将包中的签名文件放在用户工作目录
	 */
	public static void createSignAppFile(Properties p, String outFileDirectory){
		
		File fileDirectory = new File(outFileDirectory);
		if(!fileDirectory.exists()){
			fileDirectory.mkdir();
		}

		InputStream inFile = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/yiyiaowangtradeForApp.pfx");
		
		File outFile = new File(p.getProperty("sign.file"));

		try {
		       FileOutputStream outStream = new FileOutputStream(outFile);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile.read(buffer, 0, 8192)) != -1) {
				   outStream.write(buffer, 0, bytesRead);
			   }
			   outStream.close();
			   inFile.close();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }

		InputStream inFile1 = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/cp_test.cer");

		File outFile1 = new File(p.getProperty("verify.file"));
		if(!outFile1.exists()){
		   try {
			   FileOutputStream outStream1 = new FileOutputStream(outFile1);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile1.read(buffer, 0, 8192)) != -1) {
				   outStream1.write(buffer, 0, bytesRead);
			   }
			   outStream1.close();
			   inFile1.close();
			  } catch (Exception e) {
			   e.printStackTrace();
		    }
		}
	}
	
	
	/*
	 * 将包中的签名文件放在用户工作目录
	 */
	public static void createSignB2bFile(Properties p, String outFileDirectory){
		
		File fileDirectory = new File(outFileDirectory);
		if(!fileDirectory.exists()){
			fileDirectory.mkdir();
		}

		InputStream inFile = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/yiyiaowangtradeForB2b.pfx");
		
		File outFile = new File(p.getProperty("sign.file"));

		try {
		       FileOutputStream outStream = new FileOutputStream(outFile);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile.read(buffer, 0, 8192)) != -1) {
				   outStream.write(buffer, 0, bytesRead);
			   }
			   outStream.close();
			   inFile.close();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }

		InputStream inFile1 = PayUtil.class.getResourceAsStream("/com/chinapay/certificate/cp_test.cer");

		File outFile1 = new File(p.getProperty("verify.file"));
		
		if(!outFile1.exists()){

		  try {
			   FileOutputStream outStream1 = new FileOutputStream(outFile1);
			   int bytesRead = 0;
			   byte[] buffer = new byte[8192];
			   while ((bytesRead = inFile1.read(buffer, 0, 8192)) != -1) {
				   outStream1.write(buffer, 0, bytesRead);
			   }
			   outStream1.close();
			   inFile1.close();
			  } catch (Exception e) {
			   e.printStackTrace();
		  }
		
		}
	}
	
	private static void initPath(Properties p){
		
		if(path==null){
			String fs = System.getProperties().getProperty("file.separator") ;
			String path_home = System.getProperty("user.home");
			String sign_Path = p.getProperty("sign.filePath");
			path=path_home+fs+sign_Path+fs;	
		}
	}
}
