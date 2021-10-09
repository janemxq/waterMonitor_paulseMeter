package com.icicles.wmms.service.impl;

import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.param.SysRoleQueryParam;
import com.icicles.wmms.DAO.SysRoleMapper;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icicles.wmms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Resource
    SysRoleMapper sysRoleMapper;

    private SysUserService sysUserService;

    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    @Override
    public IPage<SysRole> findPage(Page page, SysRoleQueryParam sysRoleQueryParam) throws ApiException {
        IPage<SysRole> retPage;
        try {
            QueryWrapper<SysRole> queryWrapper = sysRoleQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleQueryParam.getRoleCode()), "role_code", sysRoleQueryParam.getRoleCode());
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleQueryParam.getRoleName()), "role_name", sysRoleQueryParam.getRoleName());
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleQueryParam.getIsValid()), "is_valid", sysRoleQueryParam.getIsValid());
            queryWrapper.eq(StringUtils.isNotBlank(sysRoleQueryParam.getRemark()), "remark", sysRoleQueryParam.getRemark());
            retPage = this.page(page,queryWrapper);
            logger.debug("查询角色列表成功");
        } catch (Exception e) {
            logger.error("查询角色列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询角色列表异常", HttpStatus.BAD_REQUEST);
        }
        if(retPage!=null){
            List<SysRole> data = retPage.getRecords();
            sysUserService.setUpdateUser(data);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRole sysRole) throws ApiException {
        try {
            this.save(sysRole);
            logger.debug("添加角色成功" + sysRole.getId());
        } catch (ApiException e) {
            logger.error("添加角色错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加角色异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加角色异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除角色成功" + id);
        } catch (Exception e) {
            logger.error("删除角色异常", e);
            e.printStackTrace();
            throw new ApiException("删除角色异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(SysRole sysRole) throws ApiException {
        try {
            UpdateWrapper<SysRole> wrapper = new UpdateWrapper();
            wrapper.eq("id",sysRole.getId());
            this.update(sysRole,wrapper);
            logger.debug("更新角色成功" + sysRole.getId());
        } catch (ApiException e) {
            logger.error("更新角色错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新角色异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新角色异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public SysRole findById(String id) throws ApiException {
        SysRole sysRole;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            sysRole = sysRoleMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询角色成功");
        } catch (Exception e) {
            logger.error("根据编号查询角色异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询角色异常", HttpStatus.BAD_REQUEST);
        }
        return sysRole;
    }

    @Override
    public List<SysRole> findByAccount(String account) throws ApiException {
        List<SysRole> sysRoleList = null;
        try {
            SysUser sysUser = sysUserService.findByAccount(account);
            if(sysUser!=null){
                QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.le("id", sysUser.getType());
                sysRoleList = sysRoleMapper.selectList(queryWrapper);
            }else{
                sysRoleList = this.list();
            }
            logger.debug("根据账号查询角色成功");
        } catch (Exception e) {
            logger.error("根据账号查询角色异常", e);
            e.printStackTrace();
            throw new ApiException("根据账号查询角色异常", HttpStatus.BAD_REQUEST);
        }
        return sysRoleList;
    }

}
