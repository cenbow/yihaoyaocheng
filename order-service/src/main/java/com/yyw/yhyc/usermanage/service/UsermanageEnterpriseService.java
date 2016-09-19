/**
 * Created By: XI
 * Created On: 2016-8-2 15:49:20
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;

@Service("usermanageEnterpriseService")
public class UsermanageEnterpriseService {

	private UsermanageEnterpriseMapper	usermanageEnterpriseMapper;

	@Autowired
	public void setUsermanageEnterpriseMapper(UsermanageEnterpriseMapper usermanageEnterpriseMapper)
	{
		this.usermanageEnterpriseMapper = usermanageEnterpriseMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public UsermanageEnterprise getByPK(Integer primaryKey) throws Exception
	{
		return usermanageEnterpriseMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<UsermanageEnterprise> list() throws Exception
	{
		return usermanageEnterpriseMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<UsermanageEnterprise> listByProperty(UsermanageEnterprise usermanageEnterprise)
			throws Exception
	{
		return usermanageEnterpriseMapper.listByProperty(usermanageEnterprise);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<UsermanageEnterprise> listPaginationByProperty(Pagination<UsermanageEnterprise> pagination, UsermanageEnterprise usermanageEnterprise) throws Exception
	{
		List<UsermanageEnterprise> list = usermanageEnterpriseMapper.listPaginationByProperty(pagination, usermanageEnterprise);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return usermanageEnterpriseMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		usermanageEnterpriseMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param usermanageEnterprise
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(UsermanageEnterprise usermanageEnterprise) throws Exception
	{
		return usermanageEnterpriseMapper.deleteByProperty(usermanageEnterprise);
	}

	/**
	 * 保存记录
	 * @param usermanageEnterprise
	 * @return
	 * @throws Exception
	 */
	public void save(UsermanageEnterprise usermanageEnterprise) throws Exception
	{
		usermanageEnterpriseMapper.save(usermanageEnterprise);
	}

	/**
	 * 更新记录
	 * @param usermanageEnterprise
	 * @return
	 * @throws Exception
	 */
	public int update(UsermanageEnterprise usermanageEnterprise) throws Exception
	{
		return usermanageEnterpriseMapper.update(usermanageEnterprise);
	}

	/**
	 * 根据条件查询记录条数
	 * @param usermanageEnterprise
	 * @return
	 * @throws Exception
	 */
	public int findByCount(UsermanageEnterprise usermanageEnterprise) throws Exception
	{
		return usermanageEnterpriseMapper.findByCount(usermanageEnterprise);
	}


	public  UsermanageEnterprise getByEnterpriseId (String enterpriseId)  {
		return usermanageEnterpriseMapper.getByEnterpriseId(enterpriseId);
	}
}