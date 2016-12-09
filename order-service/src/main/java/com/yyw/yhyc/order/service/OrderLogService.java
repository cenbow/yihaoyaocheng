/**
 * Created By: XI
 * Created On: 2016-12-9 10:42:31
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.bo.OrderLog;
import com.yyw.yhyc.order.mapper.OrderLogMapper;

@Service("orderLogService")
public class OrderLogService {

	private OrderLogMapper	orderLogMapper;

	@Autowired
	public void setOrderLogMapper(OrderLogMapper orderLogMapper)
	{
		this.orderLogMapper = orderLogMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderLog getByPK(java.lang.Long primaryKey) throws Exception
	{
		return orderLogMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderLog> list() throws Exception
	{
		return orderLogMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderLog> listByProperty(OrderLog orderLog)
			throws Exception
	{
		return orderLogMapper.listByProperty(orderLog);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderLog> listPaginationByProperty(Pagination<OrderLog> pagination, OrderLog orderLog) throws Exception
	{
		List<OrderLog> list = orderLogMapper.listPaginationByProperty(pagination, orderLog);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Long primaryKey) throws Exception
	{
		return orderLogMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Long> primaryKeys) throws Exception
	{
		orderLogMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderLog orderLog) throws Exception
	{
		return orderLogMapper.deleteByProperty(orderLog);
	}

	/**
	 * 保存记录
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	public void save(OrderLog orderLog) throws Exception
	{
		orderLogMapper.save(orderLog);
	}

	/**
	 * 更新记录
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	public int update(OrderLog orderLog) throws Exception
	{
		return orderLogMapper.update(orderLog);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderLog orderLog) throws Exception
	{
		return orderLogMapper.findByCount(orderLog);
	}

	/**
	 * 
	 * @param request
	 * @param platform 
	 * @param orderCode 
	 * @param userid 
	 * @param platform 
	 */
	public void insertOrderLog(HttpServletRequest request,String type, int userid, String orderCode, int platform) {
		
		OrderLog orderLog = new OrderLog();
		
		Cookie[] cookies = request.getCookies();
		for(int i=0;cookies!=null && i<cookies.length;i++){  
            if("guid".equals(cookies[i].getName()))
            	orderLog.setGuid(cookies[i].getValue());
        }  
		orderLog.setUuid("");
		orderLog.setClientIp(request.getRemoteAddr());
		orderLog.setReferer(request.getHeader("Referer"));
		orderLog.setUserAgent(request.getHeader("User-Agent"));
		orderLog.setUserid(userid);
		orderLog.setOrderCode(orderCode);
		orderLog.setPlatform(String.valueOf(platform));
		orderLog.setType(Integer.valueOf(type));
		try {
			this.save(orderLog);
		} catch (Exception e) {
			Log.error("********插入orderLog失败*********", e);
		}
	}
	
	 
}