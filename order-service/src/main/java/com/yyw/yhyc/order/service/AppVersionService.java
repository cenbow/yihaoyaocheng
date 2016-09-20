/**
 *
 * Amendment History:
 *
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.bo.AppVersion;
import com.yyw.yhyc.order.mapper.AppVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appVersionService")
public class AppVersionService {

	private AppVersionMapper appVersionMapper;

	@Autowired
	public void setAppVersionMapper(AppVersionMapper appVersionMapper)
	{
		this.appVersionMapper = appVersionMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public AppVersion getByPK(java.lang.Long primaryKey) throws Exception
	{
		return appVersionMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<AppVersion> list() throws Exception
	{
		return appVersionMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<AppVersion> listByProperty(AppVersion appVersion)
			throws Exception
	{
		return appVersionMapper.listByProperty(appVersion);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<AppVersion> listPaginationByProperty(Pagination<AppVersion> pagination, AppVersion appVersion)
			throws Exception
	{
		List<AppVersion> list = appVersionMapper.listPaginationByProperty(pagination, appVersion);

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
		return appVersionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Long> primaryKeys) throws Exception
	{
		appVersionMapper.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(AppVersion appVersion) throws Exception
	{
		return appVersionMapper.deleteByProperty(appVersion);
	}

	/**
	 * 保存记录
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public void save(AppVersion appVersion) throws Exception
	{
		appVersionMapper.save(appVersion);
	}

	/**
	 * 更新记录
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public int update(AppVersion appVersion) throws Exception
	{
		return appVersionMapper.update(appVersion);
	}

	/**
	 * 根据条件查询记录条数
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	public int findByCount(AppVersion appVersion) throws Exception
	{
		return appVersionMapper.findByCount(appVersion);
	}
}