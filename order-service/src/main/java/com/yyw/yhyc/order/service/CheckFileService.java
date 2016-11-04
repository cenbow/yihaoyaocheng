package com.yyw.yhyc.order.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.CheckFileCollect;
import com.yyw.yhyc.order.bo.CheckFileDetail;
import com.yyw.yhyc.order.mapper.CheckFileCollectMapper;
import com.yyw.yhyc.order.mapper.CheckFileDetailMapper;
import com.yyw.yhyc.order.utils.FtpUtils;

@Service("checkFileService")
public class CheckFileService {
	private static final Logger log = LoggerFactory
			.getLogger(CheckFileService.class);
	private FTPClient ftp;

	private CheckFileCollectMapper	checkFileCollectMapper;
	private CheckFileDetailMapper	checkFileDetailMapper;
	@Autowired
	private OrderSettlementService orderSettlementService;
	
	public CheckFileCollectMapper getCheckFileCollectMapper() {
		return checkFileCollectMapper;
	}
	@Autowired
	public void setCheckFileCollectMapper(
			CheckFileCollectMapper checkFileCollectMapper) {
		this.checkFileCollectMapper = checkFileCollectMapper;
	}

	public CheckFileDetailMapper getCheckFileDetailMapper() {
		return checkFileDetailMapper;
	}
	@Autowired
	public void setCheckFileDetailMapper(CheckFileDetailMapper checkFileDetailMapper) {
		this.checkFileDetailMapper = checkFileDetailMapper;
	}

	public boolean downFile(String url, int port, String username,
			String password, String remoteremoteAdr) {
		FtpUtils utils = new FtpUtils();
		ftp = utils.getFtp();
		//ftp.sendCommand(command)
		boolean success = true;
		try {
			success = utils.login(url, port, username, password);
			if (!success) {
				return false;
			}
			ftp.changeWorkingDirectory(remoteremoteAdr);
			// FTPFile[] fs = ftp.listFiles();
			List<String> fileList = getFileList(remoteremoteAdr);
			for (String fileName : fileList) {
				readFile(fileName);
			}

		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			success = false;
		} finally {
			utils.disconnect();
		}
		//success = true;
		return success;

	}

	public void readFile(String fileName) {
		String merName = fileName.split("_")[0];
		InputStream ins = null;
		StringBuffer buf = new StringBuffer();
		BufferedReader reader = null;
		try {
			ins = ftp.retrieveFileStream(fileName);
			
		    if (fileName.indexOf("zip") != -1) {
		    	ZipInputStream zin = new ZipInputStream(ins);  
		    	ZipEntry ze; 
		    	if ((ze = zin.getNextEntry()) != null) {
		    		reader = new BufferedReader(  
                            new InputStreamReader(zin,"utf-8"));  
		    	}
		    } else {
			  reader = new BufferedReader(new InputStreamReader(
					ins, "utf-8"));
		    }
			String line;
			Integer pid = null;
			// 读取第一行
			if ((line = reader.readLine()) != null) {
				String[] firstInfo = line.split("\\|");
				CheckFileCollect collect = new CheckFileCollect();
				collect.setMerId(merName);
				collect.setFile_version(firstInfo[0]);
				collect.setCountPay(Integer.parseInt(firstInfo[1]));
				collect.setCountPayAmt(new BigDecimal(firstInfo[2]));
				collect.setCountPaySuc(Integer.parseInt(firstInfo[3]));
				collect.setCountPaySucAmt(new BigDecimal(firstInfo[4]));
				collect.setCountPayCancel(Integer.parseInt(firstInfo[5]));
				collect.setCountPayCancelAmt(new BigDecimal(firstInfo[6]));
				collect.setCountPayCancelSuc(Integer.parseInt(firstInfo[7]));
				collect.setCountPayCancelAmtSuc(new BigDecimal(firstInfo[8]));
				collect.setCountRefundSuc(Integer.parseInt(firstInfo[9]));
				collect.setCountRefundAmtSuc(new BigDecimal(firstInfo[10]));
				collect.setCountRefundCancel(Integer.parseInt(firstInfo[11]));
				collect.setCountRefundCancelAmt(new BigDecimal(firstInfo[12]));
				collect.setCountRefundCancelSuc(Integer.parseInt(firstInfo[13]));
				collect.setCountRefundCancelAmtSuc(new BigDecimal(firstInfo[14]));
				collect.setCreate_time(new Date());
				pid = saveCheckFileCollect(collect);
				//pid = collect.getId();
			}
			List<String> resList = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				// builder.append(line);
				resList.add(line);
				
			}
			resList.remove(resList.size() -1);
			for (String infoValue : resList) {
				String[] detailInfoFile = infoValue.split("\\|");
				CheckFileDetail detail = new CheckFileDetail();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				detail.setTranDate(sdf.parse(detailInfoFile[0]));
				detail.setAcqCode(detailInfoFile[1]);
				detail.setMerId(detailInfoFile[2]);
				detail.setTranReserved(detailInfoFile[3]);
				detail.setLiqDate(detailInfoFile[4]);
				detail.setMerOrderNo(detailInfoFile[5]);
				detail.setBankInstNo(Integer.parseInt(detailInfoFile[6]));
				detail.setCurryNo(detailInfoFile[7]);
				detail.setTranType(detailInfoFile[8]);
				detail.setBusiType(detailInfoFile[9]);
				detail.setOrderStatus(detailInfoFile[10]);
				detail.setChannelRespCode(detailInfoFile[11]);
				detail.setCompleteDate(detailInfoFile[12]);
				detail.setCompleteTime(detailInfoFile[13]);
				detail.setOrderAmt(new BigDecimal(detailInfoFile[14]) );
				detail.setAcqSeqId(detailInfoFile[15]);
				detail.setAcqDate(detailInfoFile[16]);
				detail.setAcqTime(detailInfoFile[17]);
				detail.setOriTranDate(detailInfoFile[18]);
				detail.setOriOrderNo(detailInfoFile[19]);
				detail.setDCMark(detailInfoFile[20]);
				detail.setMerResv(detailInfoFile[21]);
				detail.setCreate_time(new Date());
				detail.setPid(pid);
				changeSettlementStatus(detail);
				saveCheckFileDetail(detail);
				
			}
			reader.close();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			if (reader != null) {
			try {
				reader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
			}
		}
		
	}
	
	
	/**
	 * @param path
	 * @return function:改变结算状态
	 * @throws IOException
	 */
    public void changeSettlementStatus(CheckFileDetail detail) {
    	
    	if (detail.getOriOrderNo() != null && detail.getOrderStatus() != null && detail.getTranType() != null) {
    		 Integer status = null;
    		 if (!"0401".equals(detail.getTranType())  ) {
    			 orderSettlementService.updateSettlementByCheckFile(detail.getOriOrderNo(), 1);
    		 } else {
    			 orderSettlementService.updateSettlementByCheckFile(detail.getOriOrderNo(), 0);
    		 }
    		 //detail.get
    	}
    }
	/**
	 * @param path
	 * @return function:读取指定目录下的文件名
	 * @throws IOException
	 */
	public List<String> getFileList(String path) {
		// 获取昨天的日期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal
				.getTime());

		List<String> fileLists = new ArrayList<String>();
		// 获得指定目录下所有文件名
		FTPFile[] ftpFiles = null;
		try {
			ftpFiles = ftp.listFiles(path);
		} catch (IOException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}
		for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
			FTPFile file = ftpFiles[i];
			if (file.isFile()) {
				String[] nameInfo = file.getName().split("_");
				if (nameInfo[1].equals(yesterday)) {
					fileLists.add(file.getName());
				}
			} else if (file.isDirectory()) {
				List<String> childLists = getFileList(path + file.getName()
						+ "/");
				for (String childFileName : childLists) {

					fileLists.add(file.getName()+"/"+childFileName);
				}
			}

		}
		return fileLists;
	}
	
	 /* 保存汇总记录
	 * @param orderCombined
	 * @return
	 * @throws Exception
	 */
	 public int saveCheckFileCollect(CheckFileCollect collect) throws Exception
	{
		  return checkFileCollectMapper.insertCheckFileCollect(collect);
		//orderCombinedMapper.save(orderCombined);
	}
	 
	 public void  saveCheckFileDetail(CheckFileDetail collect) throws Exception
    {
		 checkFileDetailMapper.save(collect);
			//orderCombinedMapper.save(orderCombined);
    }
}
