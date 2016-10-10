/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.mapper.SystemPayTypeMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.record.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.AccountPayInfo;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.AccountPayInfoMapper;

@Service("accountPayInfoService")
public class AccountPayInfoService {

    private static final Log logger = LogFactory.getLog(AccountPayInfoService.class);
    private AccountPayInfoMapper accountPayInfoMapper;
    private SystemPayTypeMapper systemPayTypeMapper;
    private SystemDateMapper systemDateMapper;
    @Autowired
    private UsermanageEnterpriseMapper usermanageEnterpriseMapper;

    @Autowired
    public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
        this.systemPayTypeMapper = systemPayTypeMapper;
    }

    @Autowired
    public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper) {
        this.accountPayInfoMapper = accountPayInfoMapper;
    }

    @Autowired
    public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
        this.systemDateMapper = systemDateMapper;
    }

    /**
     * 通过主键查询实体对象
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public AccountPayInfo getByPK(java.lang.Integer primaryKey) throws Exception {
        return accountPayInfoMapper.getByPK(primaryKey);
    }

    /**
     * 通过供应商custId查询线下支付帐号
     *
     * @param custId
     * @return
     * @throws Exception
     */
    public AccountPayInfo getByCustId(Integer custId) throws Exception {
        AccountPayInfo accountPayInfo = new AccountPayInfo();
        //设置支付类型ID
        accountPayInfo.setPayTypeId(OnlinePayTypeEnum.MerchantBank.getPayTypeId());
        //设置客户ID
        accountPayInfo.setCustId(custId);
        return accountPayInfoMapper.getByCustId(accountPayInfo);
    }

    /**
     * 查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<AccountPayInfo> list() throws Exception {
        return accountPayInfoMapper.list();
    }

    /**
     * 根据查询条件查询所有记录
     *
     * @return
     * @throws Exception
     */
    public List<AccountPayInfo> listByProperty(AccountPayInfo accountPayInfo)
            throws Exception {
        return accountPayInfoMapper.listByProperty(accountPayInfo);
    }

    /**
     * 根据查询条件查询分页记录
     *
     * @return
     * @throws Exception
     */
    public Pagination<AccountPayInfo> listPaginationByProperty(Pagination<AccountPayInfo> pagination, AccountPayInfo accountPayInfo) throws Exception {
        List<AccountPayInfo> list = accountPayInfoMapper.listPaginationByProperty(pagination, accountPayInfo);

        pagination.setResultList(list);

        return pagination;
    }

    /**
     * 根据主键删除记录
     *
     * @param primaryKey
     * @return
     * @throws Exception
     */
    public int deleteByPK(java.lang.Integer primaryKey) throws Exception {
        return accountPayInfoMapper.deleteByPK(primaryKey);
    }

    /**
     * 根据多个主键删除记录
     *
     * @param primaryKeys
     * @throws Exception
     */
    public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception {
        accountPayInfoMapper.deleteByPKeys(primaryKeys);
    }

    /**
     * 根据传入参数删除记录
     *
     * @param accountPayInfo
     * @return
     * @throws Exception
     */
    public int deleteByProperty(AccountPayInfo accountPayInfo) throws Exception {
        return accountPayInfoMapper.deleteByProperty(accountPayInfo);
    }

    /**
     * 保存记录
     *
     * @param accountPayInfo
     * @return
     * @throws Exception
     */
    public void save(AccountPayInfo accountPayInfo) throws Exception {
        accountPayInfoMapper.save(accountPayInfo);
    }

    /**
     * 更新记录
     *
     * @param accountPayInfo
     * @return
     * @throws Exception
     */
    public int update(AccountPayInfo accountPayInfo) throws Exception {
        return accountPayInfoMapper.update(accountPayInfo);
    }

    /**
     * 根据条件查询记录条数
     *
     * @param accountPayInfo
     * @return
     * @throws Exception
     */
    public int findByCount(AccountPayInfo accountPayInfo) throws Exception {
        return accountPayInfoMapper.findByCount(accountPayInfo);
    }

    /**
     * 新增修改支付账户信息
     *
     * @return
     */
    public Map<String, String> saveOrUpdateAccountPayInfo(List<AccountPayInfo> accountPayInfoList) {
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            logger.info("添加修改支付帐号接口：accountPayInfoList："+accountPayInfoList);
            if (UtilHelper.isEmpty(accountPayInfoList)) {
                resultMap.put("code", "1111");
                resultMap.put("msg", "入参集合不能为空");
                return resultMap;
            }
            /*过滤数据*/
            List<AccountPayInfo> payInfoList = this.fitterAccountPayInfo(accountPayInfoList);
            if (UtilHelper.isEmpty(payInfoList)) {
                resultMap.put("code", "1111");
                resultMap.put("msg", "参数错误");
                return resultMap;
            }
            String now = systemDateMapper.getSystemDate();
            String receiveAccountName="";
            for (AccountPayInfo accountPayInfo : payInfoList) {
                /*数据校验*/
                this.checkAccountPayInfo(accountPayInfo, resultMap);
                if (!resultMap.isEmpty())
                    return resultMap;

                AccountPayInfo ap = new AccountPayInfo();
                ap.setCustId(accountPayInfo.getCustId());
                ap.setPayTypeId(accountPayInfo.getPayTypeId());
                List<AccountPayInfo> accountPayInfos = accountPayInfoMapper.listByProperty(ap);
                if(!UtilHelper.isEmpty(accountPayInfos))
                    ap = accountPayInfos.get(0);
                ap.setReceiveAccountNo(accountPayInfo.getReceiveAccountNo());
                /*招行*/
                if (OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(accountPayInfo.getPayTypeId())) {
                    receiveAccountName=accountPayInfo.getReceiveAccountName();
                    ap.setReceiveAccountName(accountPayInfo.getReceiveAccountName());
                    ap.setSubbankName(accountPayInfo.getSubbankName());
                    ap.setProvinceName(accountPayInfo.getProvinceName());
                    ap.setCityName(accountPayInfo.getCityName());
                }
                if (UtilHelper.isEmpty(accountPayInfos)) {//新增
                    ap.setAccountStatus("1");
                    ap.setCreateTime(now);
                    ap.setCreateUser(accountPayInfo.getCreateUser());
                    ap.setReceiveAccountName(receiveAccountName);
                    accountPayInfoMapper.save(ap);
                } else {//修改
                    ap.setUpdateTime(now);
                    ap.setUpdateUser(accountPayInfo.getCreateUser());
                    ap.setReceiveAccountName(receiveAccountName);
                    accountPayInfoMapper.update(ap);
                }
            }
            resultMap.put("code", "0000");
            resultMap.put("msg", "操作成功");
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "1111");
            resultMap.put("msg", "操作异常:" + e.getMessage());
            logger.error("添加修改付款账户信息失败，" + e.getMessage());
        }
        if (resultMap.isEmpty()) {
            resultMap.put("code", "1111");
            resultMap.put("msg", "未知异常");
        }
        return resultMap;
    }

    /**
     * 过滤空数据
     *
     * @param accountPayInfoList
     * @return
     */
    private List<AccountPayInfo> fitterAccountPayInfo(List<AccountPayInfo> accountPayInfoList) {
        List<AccountPayInfo> list = new ArrayList<AccountPayInfo>();
        for (AccountPayInfo accountPayInfo : accountPayInfoList) {
            if (UtilHelper.isEmpty(accountPayInfo))
                continue;
            if (!OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    && !OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    && !OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    && !OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    )
                throw new RuntimeException("payTypeId不正确");
			/*银联b2c 或 银联无卡*/
            if (OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    || OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                    || OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(accountPayInfo.getPayTypeId()) ) {
                if (null!=accountPayInfo.getReceiveAccountNo())
                    list.add(accountPayInfo);
            }
			/*招行*/
            if (OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(accountPayInfo.getPayTypeId())) {
                if (null!=accountPayInfo.getReceiveAccountNo()
                        || !UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName())
                        || !UtilHelper.isEmpty(accountPayInfo.getSubbankName())
                        || !UtilHelper.isEmpty(accountPayInfo.getProvinceName())
                        || !UtilHelper.isEmpty(accountPayInfo.getCityName())
                        )
                    list.add(accountPayInfo);
            }

        }
        return list;
    }

    /**
     * 参数校验
     *
     * @param accountPayInfo
     * @param resultMap
     */
    private void checkAccountPayInfo(AccountPayInfo accountPayInfo, Map<String, String> resultMap) {
        if (UtilHelper.isEmpty(accountPayInfo.getCustId())) {
            resultMap.put("code", "1111");
            resultMap.put("msg", "custId不能为空");
            return;
        }
        if (!"1".equals(accountPayInfo.getAccountStatus())) {
            resultMap.put("code", "1111");
            resultMap.put("msg", "accountStatus不正确");
            return;
        }
        if (UtilHelper.isEmpty(accountPayInfo.getCreateUser())) {
            resultMap.put("code", "1111");
            resultMap.put("msg", "createUser不能为空");
            return;
        }
        if (!OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                && !OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                && !OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                && !OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                ) {
            resultMap.put("code", "1111");
            resultMap.put("msg", "payTypeId不正确");
            return;
        }
            /*银联b2c 或 银联无卡*/
        if (OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                || OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                || OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(accountPayInfo.getPayTypeId())
                ) {
            if (null==accountPayInfo.getReceiveAccountNo()) {
                resultMap.put("code", "1111");
                resultMap.put("msg", "receiveAccountNo不能为空");
                return;
            }
        }
			/*招行*/
        if (OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(accountPayInfo.getPayTypeId())) {
            if (null==accountPayInfo.getReceiveAccountNo()
                    || UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName())
                    || UtilHelper.isEmpty(accountPayInfo.getSubbankName())
                    || UtilHelper.isEmpty(accountPayInfo.getProvinceName())
                    || UtilHelper.isEmpty(accountPayInfo.getCityName())
                    ) {
                resultMap.put("code", "1111");
                resultMap.put("msg", "招行信息参数缺失");
                return;
            }
        }
    }
}