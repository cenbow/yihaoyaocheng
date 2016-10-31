package com.yyw.yhyc.job.order.service.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gangling.scheduler.AbstractJob;
import com.gangling.scheduler.ExecResult;
import com.gangling.scheduler.JobExecContext;
import com.yyw.yhyc.job.order.service.CheckFileJobService;
import com.yyw.yhyc.order.service.CheckFileService;
import com.yyw.yhyc.utils.PropertiesUtil;

@Service("checkFileJobService")
public class CheckFileJobServiceImpl extends AbstractJob implements CheckFileJobService{
    private static final Logger logger = LoggerFactory.getLogger(CheckFileJobServiceImpl.class);
	@Autowired
	private CheckFileService checkFileService;
	
	private String ftp_ip;
	
	private String ftp_username;
	
	private String ftp_password;
	private String ftp_path;
	
	
	public CheckFileService getCheckFileService() {
		return checkFileService;
	}


	public void setCheckFileService(CheckFileService checkFileService) {
		this.checkFileService = checkFileService;
	}


	public String getFtp_ip() {
		return ftp_ip;
	}


	public void setFtp_ip(String ftp_ip) {
		this.ftp_ip = ftp_ip;
	}


	public String getFtp_username() {
		return ftp_username;
	}


	public void setFtp_username(String ftp_username) {
		this.ftp_username = ftp_username;
	}


	public String getFtp_password() {
		return ftp_password;
	}


	public void setFtp_password(String ftp_password) {
		this.ftp_password = ftp_password;
	}


	public String getFtp_path() {
		return ftp_path;
	}


	public void setFtp_path(String ftp_path) {
		this.ftp_path = ftp_path;
	}


	@Override
	protected ExecResult doTask(JobExecContext arg0) {
		// TODO Auto-generated method stub
		long startmin = System.currentTimeMillis();
		logger.info("开始执行任务....");
		
		try {
			logger.info("读取配置信息....");
			Properties prop = PropertiesUtil.getConfig("myconfig.properties");
			ftp_ip = (String)prop.get("ftp_ip");
			ftp_username = (String)prop.get("ftp_username");
			ftp_password = (String)prop.get("ftp_password");
			ftp_path = (String)prop.get("ftp_path");
			checkFileService.downFile(ftp_ip, 21, ftp_username, ftp_password, ftp_path);
			long endmin = System.currentTimeMillis();
			logger.info("结束任务....,执行时间:"+ (endmin - startmin)/1000+"s");
		    return new ExecResult(0, "succeed!");
		} catch (Exception e) {
			 logger.error(e.getMessage(), e);
             return new ExecResult(4, "failed!");
		}
	}



//	public  void  execute() {
//		// TODO Auto-generated method stub
//		long startmin = System.currentTimeMillis();
//		logger.info("开始执行任务....");
//		try {
//			Properties prop = PropertiesUtil.getConfig("myconfig.properties");
//			ftp_ip = (String)prop.get("ftp_ip");
//			ftp_username = (String)prop.get("ftp_username");
//			ftp_password = (String)prop.get("ftp_password");
//			ftp_path = (String)prop.get("ftp_path");
//			checkFileService.downFile(ftp_ip, 21, ftp_username, ftp_password, ftp_path);
//			long endmin = System.currentTimeMillis();
//			logger.info("结束任务....,执行时间:"+ (endmin - startmin)/1000+"s");
//
//		}catch (Exception ex) {
//	            logger.error(ex.getMessage(), ex);
//	            //return new ExecResult(4, "failed!");
//	    }
//	}

}
