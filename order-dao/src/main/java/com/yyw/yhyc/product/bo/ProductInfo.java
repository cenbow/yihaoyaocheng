/**
 * Created By: XI
 * Created On: 2016-8-1 14:52:12
 *
 * Amendment History:
 *
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.product.bo;

import com.yyw.yhyc.bo.Model;

public class ProductInfo extends Model{

    private static final long serialVersionUID = -8810101286294684186L;

    /**
     *	主键ID
     */
    private java.lang.Integer id;

    /**
     *	SPU编码
     */
    private java.lang.String spuCode;

    /**
     *	商品名
     */
    private java.lang.String productName;

    /**
     *	化学名
     */
    private java.lang.String productNameS;

    /**
     *	通用名
     */
    private java.lang.String shortName;

    /**
     *	商品条码
     */
    private java.lang.String productCode;

    /**
     *	一级类目
     */
    private java.lang.String firstCategory;

    /**
     *	二级类目
     */
    private java.lang.String secondCategory;

    /**
     *	三级类目
     */
    private java.lang.String thirdCategory;

    /**
     *	药品一级类别
     */
    private java.lang.String drugFirstCategory;

    /**
     *	药品二级类别
     */
    private java.lang.String drugSecondCategory;

    /**
     *	生产厂商
     */
    private java.lang.String factoryName;

    /**
     *	产地
     */
    private java.lang.String placeOrigin;

    /**
     *	规格
     */
    private java.lang.String spec;

    /**
     *	品牌名称
     */
    private java.lang.Long brandId;

    /**
     *	批准文号
     */
    private java.lang.String approvalNum;

    /**
     *	有效期
     */
    private java.lang.String startDate;

    /**
     *	失效期
     */
    private java.lang.String endDate;

    /**
     *	最小包装单位
     */
    private java.lang.String unit;

    /**
     *	剂型
     */
    private java.lang.String drugformType;

    /**
     *	保质期
     */
    private java.lang.Integer shelfLife;

    /**
     *	品种类别（进口/合资/国产）
     */
    private java.lang.String productType;

    /**
     *	国家社保目录类别
     */
    private java.lang.String nssCategory;

    /**
     *	运输条件
     */
    private java.lang.String transportationCondition;

    /**
     *	是否OTC
     */
    private java.lang.String isOtc;

    /**
     *	质量标准类别
     */
    private java.lang.String qualStandardsType;

    /**
     *	质量标准编号
     */
    private java.lang.String qualStandardsNum;

    /**
     *	创建时间
     */
    private java.lang.String createDate;

    /**
     *	更新时间
     */
    private java.lang.String updateDate;

    /**
     *	更新人（操作者）
     */
    private java.lang.String updateStaff;

    /**
     *	会员名称
     */
    private java.lang.String enterpriseName;

    /**
     *	会员编码
     */
    private java.lang.Integer enterpriseCode;

    /**
     *	主键ID
     */
    public java.lang.Integer getId()
    {
        return id;
    }

    /**
     *	主键ID
     */
    public void setId(java.lang.Integer id)
    {
        this.id = id;
    }

    /**
     *	SPU编码
     */
    public java.lang.String getSpuCode()
    {
        return spuCode;
    }

    /**
     *	SPU编码
     */
    public void setSpuCode(java.lang.String spuCode)
    {
        this.spuCode = spuCode;
    }

    /**
     *	商品名
     */
    public java.lang.String getProductName()
    {
        return productName;
    }

    /**
     *	商品名
     */
    public void setProductName(java.lang.String productName)
    {
        this.productName = productName;
    }

    /**
     *	化学名
     */
    public java.lang.String getProductNameS()
    {
        return productNameS;
    }

    /**
     *	化学名
     */
    public void setProductNameS(java.lang.String productNameS)
    {
        this.productNameS = productNameS;
    }

    /**
     *	通用名
     */
    public java.lang.String getShortName()
    {
        return shortName;
    }

    /**
     *	通用名
     */
    public void setShortName(java.lang.String shortName)
    {
        this.shortName = shortName;
    }

    /**
     *	商品条码
     */
    public java.lang.String getProductCode()
    {
        return productCode;
    }

    /**
     *	商品条码
     */
    public void setProductCode(java.lang.String productCode)
    {
        this.productCode = productCode;
    }

    /**
     *	一级类目
     */
    public java.lang.String getFirstCategory()
    {
        return firstCategory;
    }

    /**
     *	一级类目
     */
    public void setFirstCategory(java.lang.String firstCategory)
    {
        this.firstCategory = firstCategory;
    }

    /**
     *	二级类目
     */
    public java.lang.String getSecondCategory()
    {
        return secondCategory;
    }

    /**
     *	二级类目
     */
    public void setSecondCategory(java.lang.String secondCategory)
    {
        this.secondCategory = secondCategory;
    }

    /**
     *	三级类目
     */
    public java.lang.String getThirdCategory()
    {
        return thirdCategory;
    }

    /**
     *	三级类目
     */
    public void setThirdCategory(java.lang.String thirdCategory)
    {
        this.thirdCategory = thirdCategory;
    }

    /**
     *	药品一级类别
     */
    public java.lang.String getDrugFirstCategory()
    {
        return drugFirstCategory;
    }

    /**
     *	药品一级类别
     */
    public void setDrugFirstCategory(java.lang.String drugFirstCategory)
    {
        this.drugFirstCategory = drugFirstCategory;
    }

    /**
     *	药品二级类别
     */
    public java.lang.String getDrugSecondCategory()
    {
        return drugSecondCategory;
    }

    /**
     *	药品二级类别
     */
    public void setDrugSecondCategory(java.lang.String drugSecondCategory)
    {
        this.drugSecondCategory = drugSecondCategory;
    }

    /**
     *	生产厂商
     */
    public java.lang.String getFactoryName()
    {
        return factoryName;
    }

    /**
     *	生产厂商
     */
    public void setFactoryName(java.lang.String factoryName)
    {
        this.factoryName = factoryName;
    }

    /**
     *	产地
     */
    public java.lang.String getPlaceOrigin()
    {
        return placeOrigin;
    }

    /**
     *	产地
     */
    public void setPlaceOrigin(java.lang.String placeOrigin)
    {
        this.placeOrigin = placeOrigin;
    }

    /**
     *	规格
     */
    public java.lang.String getSpec()
    {
        return spec;
    }

    /**
     *	规格
     */
    public void setSpec(java.lang.String spec)
    {
        this.spec = spec;
    }

    /**
     *	品牌名称
     */
    public java.lang.Long getBrandId()
    {
        return brandId;
    }

    /**
     *	品牌名称
     */
    public void setBrandId(java.lang.Long brandId)
    {
        this.brandId = brandId;
    }

    /**
     *	批准文号
     */
    public java.lang.String getApprovalNum()
    {
        return approvalNum;
    }

    /**
     *	批准文号
     */
    public void setApprovalNum(java.lang.String approvalNum)
    {
        this.approvalNum = approvalNum;
    }

    /**
     *	有效期
     */
    public java.lang.String getStartDate()
    {
        return startDate;
    }

    /**
     *	有效期
     */
    public void setStartDate(java.lang.String startDate)
    {
        this.startDate = startDate;
    }

    /**
     *	失效期
     */
    public java.lang.String getEndDate()
    {
        return endDate;
    }

    /**
     *	失效期
     */
    public void setEndDate(java.lang.String endDate)
    {
        this.endDate = endDate;
    }

    /**
     *	最小包装单位
     */
    public java.lang.String getUnit()
    {
        return unit;
    }

    /**
     *	最小包装单位
     */
    public void setUnit(java.lang.String unit)
    {
        this.unit = unit;
    }

    /**
     *	剂型
     */
    public java.lang.String getDrugformType()
    {
        return drugformType;
    }

    /**
     *	剂型
     */
    public void setDrugformType(java.lang.String drugformType)
    {
        this.drugformType = drugformType;
    }

    /**
     *	保质期
     */
    public java.lang.Integer getShelfLife()
    {
        return shelfLife;
    }

    /**
     *	保质期
     */
    public void setShelfLife(java.lang.Integer shelfLife)
    {
        this.shelfLife = shelfLife;
    }

    /**
     *	品种类别（进口/合资/国产）
     */
    public java.lang.String getProductType()
    {
        return productType;
    }

    /**
     *	品种类别（进口/合资/国产）
     */
    public void setProductType(java.lang.String productType)
    {
        this.productType = productType;
    }

    /**
     *	国家社保目录类别
     */
    public java.lang.String getNssCategory()
    {
        return nssCategory;
    }

    /**
     *	国家社保目录类别
     */
    public void setNssCategory(java.lang.String nssCategory)
    {
        this.nssCategory = nssCategory;
    }

    /**
     *	运输条件
     */
    public java.lang.String getTransportationCondition()
    {
        return transportationCondition;
    }

    /**
     *	运输条件
     */
    public void setTransportationCondition(java.lang.String transportationCondition)
    {
        this.transportationCondition = transportationCondition;
    }

    /**
     *	是否OTC
     */
    public java.lang.String getIsOtc()
    {
        return isOtc;
    }

    /**
     *	是否OTC
     */
    public void setIsOtc(java.lang.String isOtc)
    {
        this.isOtc = isOtc;
    }

    /**
     *	质量标准类别
     */
    public java.lang.String getQualStandardsType()
    {
        return qualStandardsType;
    }

    /**
     *	质量标准类别
     */
    public void setQualStandardsType(java.lang.String qualStandardsType)
    {
        this.qualStandardsType = qualStandardsType;
    }

    /**
     *	质量标准编号
     */
    public java.lang.String getQualStandardsNum()
    {
        return qualStandardsNum;
    }

    /**
     *	质量标准编号
     */
    public void setQualStandardsNum(java.lang.String qualStandardsNum)
    {
        this.qualStandardsNum = qualStandardsNum;
    }

    /**
     *	创建时间
     */
    public java.lang.String getCreateDate()
    {
        return createDate;
    }

    /**
     *	创建时间
     */
    public void setCreateDate(java.lang.String createDate)
    {
        this.createDate = createDate;
    }

    /**
     *	更新时间
     */
    public java.lang.String getUpdateDate()
    {
        return updateDate;
    }

    /**
     *	更新时间
     */
    public void setUpdateDate(java.lang.String updateDate)
    {
        this.updateDate = updateDate;
    }

    /**
     *	更新人（操作者）
     */
    public java.lang.String getUpdateStaff()
    {
        return updateStaff;
    }

    /**
     *	更新人（操作者）
     */
    public void setUpdateStaff(java.lang.String updateStaff)
    {
        this.updateStaff = updateStaff;
    }

    /**
     *	会员名称
     */
    public java.lang.String getEnterpriseName()
    {
        return enterpriseName;
    }

    /**
     *	会员名称
     */
    public void setEnterpriseName(java.lang.String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    /**
     *	会员编码
     */
    public java.lang.Integer getEnterpriseCode()
    {
        return enterpriseCode;
    }

    /**
     *	会员编码
     */
    public void setEnterpriseCode(java.lang.Integer enterpriseCode)
    {
        this.enterpriseCode = enterpriseCode;
    }

    public String toString()
    {
        return "ProductInfo [" +
                "id=" + id +
                ", spuCode=" + spuCode +
                ", productName=" + productName +
                ", productNameS=" + productNameS +
                ", shortName=" + shortName +
                ", productCode=" + productCode +
                ", firstCategory=" + firstCategory +
                ", secondCategory=" + secondCategory +
                ", thirdCategory=" + thirdCategory +
                ", drugFirstCategory=" + drugFirstCategory +
                ", drugSecondCategory=" + drugSecondCategory +
                ", factoryName=" + factoryName +
                ", placeOrigin=" + placeOrigin +
                ", spec=" + spec +
                ", brandId=" + brandId +
                ", approvalNum=" + approvalNum +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", unit=" + unit +
                ", drugformType=" + drugformType +
                ", shelfLife=" + shelfLife +
                ", productType=" + productType +
                ", nssCategory=" + nssCategory +
                ", transportationCondition=" + transportationCondition +
                ", isOtc=" + isOtc +
                ", qualStandardsType=" + qualStandardsType +
                ", qualStandardsNum=" + qualStandardsNum +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", updateStaff=" + updateStaff +
                ", enterpriseName=" + enterpriseName +
                ", enterpriseCode=" + enterpriseCode +
                "]";
    }
}

