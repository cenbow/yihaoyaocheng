package com.yyw.yhyc.order.utils;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(FtpUtils.class);
	private FTPClient ftp;

	public FtpUtils() {
		ftp = new FTPClient();
	}
    
	
	public FTPClient getFtp() {
		return ftp;
	}


	public void setFtp(FTPClient ftp) {
		this.ftp = ftp;
	}


	/**
	 * 用户FTP账号登录
	 * 
	 * @param url
	 *            FTP地址
	 * @param port
	 *            FTP端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密 码
	 * @return true/false 成功/失败
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean login(String url, int port, String username, String password)
			throws SocketException, IOException {

		int reply;
		ftp.connect(url, port);
		ftp.setControlEncoding("UTF-8");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		ftp.login(username, password);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			logger.error(">>>>>>>>>>>>>>>>连接服务器失败!");
			return false;
		}
		logger.info(">>>>>>>>>>>>>>>>>登陆服务器成功!");
		return true;

	}

	/**
	 * 释放FTP
	 */
	public void disconnect() {
		if (ftp.isAvailable()) {
			try {
				ftp.logout(); // 退出FTP
			} catch (IOException e) {
				logger.error("FTP登录退出异常:" + e.getMessage());
		
			}
		}
		if (ftp.isConnected()) {
			try {
				// 断开连接
				ftp.disconnect();
			} catch (IOException e) {
				logger.error("FTP断开连接异常:" + e.getMessage());
				
		}
	}
 }
	

	
}
