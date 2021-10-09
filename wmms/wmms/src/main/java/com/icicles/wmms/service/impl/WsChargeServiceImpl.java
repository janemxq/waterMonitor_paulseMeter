package com.icicles.wmms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.icicles.wmms.entity.po.*;
import com.icicles.wmms.entity.param.WsChargeQueryParam;
import com.icicles.wmms.DAO.WsChargeMapper;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.service.WsChargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.WsVillageService;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户充值记录 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@Service
public class WsChargeServiceImpl extends ServiceImpl<WsChargeMapper, WsCharge> implements WsChargeService {

    private static final Logger logger = LoggerFactory.getLogger(WsChargeServiceImpl.class);

    @Resource
    WsChargeMapper wsChargeMapper;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private WsVillageService villageService;

    @Override
    public IPage<WsCharge> findPage(Page page, WsChargeQueryParam wsChargeQueryParam) throws ApiException {
        IPage<WsCharge> retPage;
        try {
            QueryWrapper<WsCharge> queryWrapper = wsChargeQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getUserSn()), "user_sn", wsChargeQueryParam.getUserSn());
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getUserName()), "user_name", wsChargeQueryParam.getUserName());
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getPhone()), "phone", wsChargeQueryParam.getPhone());
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getAccountNum()), "account_num", wsChargeQueryParam.getAccountNum());
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getBalance()), "balance", wsChargeQueryParam.getBalance());
            queryWrapper.eq(StringUtils.isNotBlank(wsChargeQueryParam.getVillageSn()), "village_sn", wsChargeQueryParam.getVillageSn());
            queryWrapper.lambda().orderByDesc(WsCharge::getId);
            retPage = this.page(page,queryWrapper);
            List<WsCharge> data = retPage.getRecords();
            this.getUserVillageGroupInfo(data);
            logger.debug("查询用户充值记录列表成功");
        } catch (Exception e) {
            logger.error("查询用户充值记录列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询用户充值记录列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsCharge wsCharge) throws ApiException {
        try {
            this.save(wsCharge);
            logger.debug("添加用户充值记录成功" + wsCharge.getId());
        } catch (ApiException e) {
            logger.error("添加用户充值记录错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加用户充值记录异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加用户充值记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除用户充值记录成功" + id);
        } catch (Exception e) {
            logger.error("删除用户充值记录异常", e);
            e.printStackTrace();
            throw new ApiException("删除用户充值记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsCharge wsCharge) throws ApiException {
        try {
            UpdateWrapper<WsCharge> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsCharge.getId());
            this.update(wsCharge,wrapper);
            logger.debug("更新用户充值记录成功" + wsCharge.getId());
        } catch (ApiException e) {
            logger.error("更新用户充值记录错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新用户充值记录异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新用户充值记录异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsCharge findById(String id) throws ApiException {
        WsCharge wsCharge;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsCharge = wsChargeMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询用户充值记录成功");
        } catch (Exception e) {
            logger.error("根据编号查询用户充值记录异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询用户充值记录异常", HttpStatus.BAD_REQUEST);
        }
        return wsCharge;
    }

    private List<WsCharge> getUserVillageGroupInfo(List<WsCharge> data){
        if(CollUtil.isEmpty(data)){
            return null;
        }
        //涉及到的村信息
        List<WsVillage> villageList=null;
        //涉及到的用户
        List<SysUser> userList=null;

        //所有村的标识
        List<String> villageSnList = data.stream().map(WsCharge::getVillageSn).collect(Collectors.toList());
        //所有用户的标识
        List<String> userSnList = data.stream().map(WsCharge::getUserSn).collect(Collectors.toList());
        //去重
        if(CollUtil.isNotEmpty(villageSnList)){
            villageSnList = villageSnList.stream().distinct().collect(Collectors.toList());
            villageList = villageService.getListBySns(villageSnList);
        }
        if(CollUtil.isNotEmpty(userSnList)){
            userSnList = userSnList.stream().distinct().collect(Collectors.toList());
            userList = sysUserService.getAllBySn(userSnList);
        }
        boolean vFlag = CollUtil.isNotEmpty(villageList);
        boolean uFlag = CollUtil.isNotEmpty(userList);
        for (WsCharge temp:data){
            if(uFlag){
                for (SysUser u:userList){
                    if(u.getSn().equals(temp.getUserSn())){
                        temp.setUserName(u.getName());
                    }
                }
            }
            if(vFlag){
                for (WsVillage v:villageList){
                    if(v.getSn().equals(temp.getVillageSn())){
                        temp.setVillageName(v.getVillageName());
                    }
                }
            }
        }
        return data;
    }
}
