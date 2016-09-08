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
package com.yyw.yhyc.usermanage.bo;

import com.yyw.yhyc.bo.Model;

public class UsermanageEnterprise extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键id
	  */
	private Integer id;

	/**
	  *	企业编号(单点登录方传入)
	  */
	private String enterpriseId;

	/**
	  *	企业类型（1：我是下游客户，2：我是生产企业，3：我是批发企业）
	  */
	private Byte roleType;

	/**
	  *	企业(单位)名称
	  */
	private String enterpriseName;

	/**
	  *	法定代表人
	  */
	private String legalPersonname;

	/**
	  *	省编码，通过字典表关联省的名称
	  */
	private String province;

	/**
	  *	市编码，通过字典表关联市的名称
	  */
	private String city;

	/**
	  *	区编码，通过字典表关联区的名称
	  */
	private String district;

	/**
	  *	省的名称
	  */
	private String provinceName;

	/**
	  *	市的名称
	  */
	private String cityName;

	/**
	  *	区的名称
	  */
	private String districtName;

	/**
	  *	注册地址
	  */
	private String registeredAddress;

	/**
	  *	手机号码
	  */
	private String enterpriseCellphone;

	/**
	  *	联系人姓名
	  */
	private String contactsName;

	/**
	  *	传真
	  */
	private String enterpriseFax;

	/**
	  *	固定电话
	  */
	private String enterpriseTelephone;

	/**
	  *	邮政编码
	  */
	private String enterprisePostcode;

	/**
	  *	开户银行
	  */
	private String bankName;

	/**
	  *	银行账号
	  */
	private String bankCode;

	/**
	  *	开户名
	  */
	private String accountName;

	/**
	  *	创建人
	  */
	private String createUser;

	/**
	  *	创建时间
	  */
	private String createTime;

	/**
	  *	最后更新时间
	  */
	private String updateTime;

	/**
	  *	最后更新人
	  */
	private String updateUser;

	/**
	  *	是否被删除（1：使用，0：删除）
	  */
	private Byte isUse;

	/**
	  *	是否审核通过（0：未审核，1：审核通过，2：审核未通过）
	  */
	private Byte isCheck;

	/**
	  *	
	  */
	private String enterpriseCode;

	/**
	  *	是否三证合一（1:是，0：否）
	  */
	private Byte is3merge1;


	/**
	  *	主键id
	  */
	public Integer getId()
	{
		return id;
	}
	
	/**
	  *	主键id
	  */
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	/**
	  *	企业编号(单点登录方传入)
	  */
	public String getEnterpriseId()
	{
		return enterpriseId;
	}
	
	/**
	  *	企业编号(单点登录方传入)
	  */
	public void setEnterpriseId(String enterpriseId)
	{
		this.enterpriseId = enterpriseId;
	}
	
	/**
	  *	企业类型（1：我是下游客户，2：我是生产企业，3：我是批发企业）
	  */
	public Byte getRoleType()
	{
		return roleType;
	}
	
	/**
	  *	企业类型（1：我是下游客户，2：我是生产企业，3：我是批发企业）
	  */
	public void setRoleType(Byte roleType)
	{
		this.roleType = roleType;
	}
	
	/**
	  *	企业(单位)名称
	  */
	public String getEnterpriseName()
	{
		return enterpriseName;
	}
	
	/**
	  *	企业(单位)名称
	  */
	public void setEnterpriseName(String enterpriseName)
	{
		this.enterpriseName = enterpriseName;
	}
	
	/**
	  *	法定代表人
	  */
	public String getLegalPersonname()
	{
		return legalPersonname;
	}
	
	/**
	  *	法定代表人
	  */
	public void setLegalPersonname(String legalPersonname)
	{
		this.legalPersonname = legalPersonname;
	}
	
	/**
	  *	省编码，通过字典表关联省的名称
	  */
	public String getProvince()
	{
		return province;
	}
	
	/**
	  *	省编码，通过字典表关联省的名称
	  */
	public void setProvince(String province)
	{
		this.province = province;
	}
	
	/**
	  *	市编码，通过字典表关联市的名称
	  */
	public String getCity()
	{
		return city;
	}
	
	/**
	  *	市编码，通过字典表关联市的名称
	  */
	public void setCity(String city)
	{
		this.city = city;
	}
	
	/**
	  *	区编码，通过字典表关联区的名称
	  */
	public String getDistrict()
	{
		return district;
	}
	
	/**
	  *	区编码，通过字典表关联区的名称
	  */
	public void setDistrict(String district)
	{
		this.district = district;
	}
	
	/**
	  *	省的名称
	  */
	public String getProvinceName()
	{
		return provinceName;
	}
	
	/**
	  *	省的名称
	  */
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	
	/**
	  *	市的名称
	  */
	public String getCityName()
	{
		return cityName;
	}
	
	/**
	  *	市的名称
	  */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	
	/**
	  *	区的名称
	  */
	public String getDistrictName()
	{
		return districtName;
	}
	
	/**
	  *	区的名称
	  */
	public void setDistrictName(String districtName)
	{
		this.districtName = districtName;
	}
	
	/**
	  *	注册地址
	  */
	public String getRegisteredAddress()
	{
		return registeredAddress;
	}
	
	/**
	  *	注册地址
	  */
	public void setRegisteredAddress(String registeredAddress)
	{
		this.registeredAddress = registeredAddress;
	}
	
	/**
	  *	手机号码
	  */
	public String getEnterpriseCellphone()
	{
		return enterpriseCellphone;
	}
	
	/**
	  *	手机号码
	  */
	public void setEnterpriseCellphone(String enterpriseCellphone)
	{
		this.enterpriseCellphone = enterpriseCellphone;
	}
	
	/**
	  *	联系人姓名
	  */
	public String getContactsName()
	{
		return contactsName;
	}
	
	/**
	  *	联系人姓名
	  */
	public void setContactsName(String contactsName)
	{
		this.contactsName = contactsName;
	}
	
	/**
	  *	传真
	  */
	public String getEnterpriseFax()
	{
		return enterpriseFax;
	}
	
	/**
	  *	传真
	  */
	public void setEnterpriseFax(String enterpriseFax)
	{
		this.enterpriseFax = enterpriseFax;
	}
	
	/**
	  *	固定电话
	  */
	public String getEnterpriseTelephone()
	{
		return enterpriseTelephone;
	}
	
	/**
	  *	固定电话
	  */
	public void setEnterpriseTelephone(String enterpriseTelephone)
	{
		this.enterpriseTelephone = enterpriseTelephone;
	}
	
	/**
	  *	邮政编码
	  */
	public String getEnterprisePostcode()
	{
		return enterprisePostcode;
	}
	
	/**
	  *	邮政编码
	  */
	public void setEnterprisePostcode(String enterprisePostcode)
	{
		this.enterprisePostcode = enterprisePostcode;
	}
	
	/**
	  *	开户银行
	  */
	public String getBankName()
	{
		return bankName;
	}
	
	/**
	  *	开户银行
	  */
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}
	
	/**
	  *	银行账号
	  */
	public String getBankCode()
	{
		return bankCode;
	}
	
	/**
	  *	银行账号
	  */
	public void setBankCode(String bankCode)
	{
		this.bankCode = bankCode;
	}
	
	/**
	  *	开户名
	  */
	public String getAccountName()
	{
		return accountName;
	}
	
	/**
	  *	开户名
	  */
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	
	/**
	  *	创建人
	  */
	public String getCreateUser()
	{
		return createUser;
	}
	
	/**
	  *	创建人
	  */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	
	/**
	  *	创建时间
	  */
	public String getCreateTime()
	{
		return createTime;
	}
	
	/**
	  *	创建时间
	  */
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	/**
	  *	最后更新时间
	  */
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	/**
	  *	最后更新时间
	  */
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	最后更新人
	  */
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	/**
	  *	最后更新人
	  */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	是否被删除（1：使用，0：删除）
	  */
	public Byte getIsUse()
	{
		return isUse;
	}
	
	/**
	  *	是否被删除（1：使用，0：删除）
	  */
	public void setIsUse(Byte isUse)
	{
		this.isUse = isUse;
	}
	
	/**
	  *	是否审核通过（0：未审核，1：审核通过，2：审核未通过）
	  */
	public Byte getIsCheck()
	{
		return isCheck;
	}
	
	/**
	  *	是否审核通过（0：未审核，1：审核通过，2：审核未通过）
	  */
	public void setIsCheck(Byte isCheck)
	{
		this.isCheck = isCheck;
	}
	
	/**
	  *	
	  */
	public String getEnterpriseCode()
	{
		return enterpriseCode;
	}
	
	/**
	  *	
	  */
	public void setEnterpriseCode(String enterpriseCode)
	{
		this.enterpriseCode = enterpriseCode;
	}
	
	/**
	  *	是否三证合一（1:是，0：否）
	  */
	public Byte getIs3merge1()
	{
		return is3merge1;
	}
	
	/**
	  *	是否三证合一（1:是，0：否）
	  */
	public void setIs3merge1(Byte is3merge1)
	{
		this.is3merge1 = is3merge1;
	}
	

	public String toString()
	{
		return "UsermanageEnterprise [" + 
					"id=" + id + 
					", enterpriseId=" + enterpriseId + 
					", roleType=" + roleType + 
					", enterpriseName=" + enterpriseName + 
					", legalPersonname=" + legalPersonname + 
					", province=" + province + 
					", city=" + city + 
					", district=" + district + 
					", provinceName=" + provinceName + 
					", cityName=" + cityName + 
					", districtName=" + districtName + 
					", registeredAddress=" + registeredAddress + 
					", enterpriseCellphone=" + enterpriseCellphone + 
					", contactsName=" + contactsName + 
					", enterpriseFax=" + enterpriseFax + 
					", enterpriseTelephone=" + enterpriseTelephone + 
					", enterprisePostcode=" + enterprisePostcode + 
					", bankName=" + bankName + 
					", bankCode=" + bankCode + 
					", accountName=" + accountName + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateTime=" + updateTime + 
					", updateUser=" + updateUser + 
					", isUse=" + isUse + 
					", isCheck=" + isCheck + 
					", enterpriseCode=" + enterpriseCode + 
					", is3merge1=" + is3merge1 + 
				"]";
	}
}

