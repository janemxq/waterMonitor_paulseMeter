package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.SysRoleQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
public interface SysRoleService extends IService<SysRole> {
      /**
       * 根据条件分页查询角色
       *
       * @param page 分页对象
       * @return IPage<SysRole>   角色列表（分页）
       */
       IPage<SysRole> findPage(Page page, SysRoleQueryParam sysRoleQueryParam) throws ApiException;

      /**
       * 增加角色
       *
       * @param sysRole 角色
       * @throws ApiException 异常信息
       */
      void add(SysRole sysRole) throws ApiException;

      /**
      * 删除角色
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新角色
      *
      * @param sysRole 角色
      * @throws ApiException 异常信息
      */
      void refresh(SysRole sysRole) throws ApiException;

      /**
      * 根据编号查询角色
      *
      * @param id 编号
      * @return SysRole 角色
      * @throws ApiException 异常信息
      */
      SysRole findById(String id) throws ApiException;

      /**
       * 根据账号查询角色
       *
       * @param account 账号
       * @return SysRole 角色
       * @throws ApiException 异常信息
       */
      List<SysRole> findByAccount(String account) throws ApiException;

}
