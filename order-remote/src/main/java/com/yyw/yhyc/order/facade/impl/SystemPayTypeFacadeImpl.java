/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.facade.SystemPayTypeFacade;
import com.yyw.yhyc.order.service.SystemPayTypeService;

@Service("systemPayTypeFacade")
public class SystemPayTypeFacadeImpl implements SystemPayTypeFacade {

	private SystemPayTypeService systemPayTypeService;
	
	@Autowired
	public void setSystemPayTypeService(SystemPayTypeService systemPayTypeService)
	{
		this.systemPayTypeService = systemPayTypeService;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public SystemPayType getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return systemPayTypeService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<SystemPayType> list() throws Exception
	{
		return systemPayTypeService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<SystemPayType> listByProperty(SystemPayType systemPayType)
			throws Exception
	{
		return systemPayTypeService.listByProperty(systemPayType);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<SystemPayType> listPaginationByProperty(Pagination<SystemPayType> pagination, SystemPayType systemPayType)
			throws Exception
	{
		return systemPayTypeService.listPaginationByProperty(pagination, systemPayType);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return systemPayTypeService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		systemPayTypeService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param systemPayType
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(SystemPayType systemPayType) throws Exception
	{
		return systemPayTypeService.deleteByProperty(systemPayType);
	}

	/**
	 * 保存记录
	 * @param systemPayType
	 * @return
	 * @throws Exception
	 */
	public void save(SystemPayType systemPayType) throws Exception
	{
		systemPayTypeService.save(systemPayType);
	}

	/**
	 * 更新记录
	 * @param systemPayType
	 * @return
	 * @throws Exception
	 */
	public int update(SystemPayType systemPayType) throws Exception
	{
		return systemPayTypeService.update(systemPayType);
	}

	/**
	 * 根据条件查询记录条数
	 * @param systemPayType
	 * @return
	 * @throws Exception
	 */
	public int findByCount(SystemPayType systemPayType) throws Exception
	{
		return systemPayTypeService.findByCount(systemPayType);
	}
}
