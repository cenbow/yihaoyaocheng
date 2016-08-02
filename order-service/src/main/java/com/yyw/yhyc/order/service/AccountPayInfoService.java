/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.AccountPayInfo;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.AccountPayInfoMapper;

@Service("accountPayInfoService")
public class AccountPayInfoService {

	private AccountPayInfoMapper	accountPayInfoMapper;

	@Autowired
	public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper)
	{
		this.accountPayInfoMapper = accountPayInfoMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public AccountPayInfo getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return accountPayInfoMapper.getByPK(primaryKey);
	}
	/**
	 * 通过供应商custId查询
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public AccountPayInfo getByCustId(Integer custId) throws Exception {
		return accountPayInfoMapper.getByCustId(custId);
	}
	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<AccountPayInfo> list() throws Exception
	{
		return accountPayInfoMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<AccountPayInfo> listByProperty(AccountPayInfo accountPayInfo)
			throws Exception
	{
		return accountPayInfoMapper.listByProperty(accountPayInfo);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<AccountPayInfo> listPaginationByProperty(Pagination<AccountPayInfo> pagination, AccountPayInfo accountPayInfo) throws Exception
	{
		List<AccountPayInfo> list = accountPayInfoMapper.listPaginationByProperty(pagination, accountPayInfo);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return accountPayInfoMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		accountPayInfoMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param accountPayInfo
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(AccountPayInfo accountPayInfo) throws Exception
	{
		return accountPayInfoMapper.deleteByProperty(accountPayInfo);
	}

	/**
	 * 保存记录
	 * @param accountPayInfo
	 * @return
	 * @throws Exception
	 */
	public void save(AccountPayInfo accountPayInfo) throws Exception
	{
		accountPayInfoMapper.save(accountPayInfo);
	}

	/**
	 * 更新记录
	 * @param accountPayInfo
	 * @return
	 * @throws Exception
	 */
	public int update(AccountPayInfo accountPayInfo) throws Exception
	{
		return accountPayInfoMapper.update(accountPayInfo);
	}

	/**
	 * 根据条件查询记录条数
	 * @param accountPayInfo
	 * @return
	 * @throws Exception
	 */
	public int findByCount(AccountPayInfo accountPayInfo) throws Exception
	{
		return accountPayInfoMapper.findByCount(accountPayInfo);
	}
}