package com.icicles.wmms.service;

import com.icicles.wmms.entity.form.SysRoleMenusForm;
import com.icicles.wmms.entity.po.SysRoleMenus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.SysRoleMenusQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
public interface SysRoleMenusService extends IService<SysRoleMenus> {
      /**
       * 根据条件分页查询
       *
       * @param page 分页对象
       * @return IPage<SysRoleMenus>   列表（分页）
       */
       IPage<SysRoleMenus> findPage(Page page, SysRoleMenusQueryParam sysRoleMenusQueryParam) throws ApiException;

      /**
       * 增加
       *
       * @param sysRoleMenus 
       * @throws ApiException 异常信息
       */
      void add(SysRoleMenusForm sysRoleMenus,String updateUserId) throws ApiException;

      /**
      * 删除
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新
      *
      * @param sysRoleMenus 
      * @throws ApiException 异常信息
      */
      void refresh(SysRoleMenusForm sysRoleMenus,String updateUserId) throws ApiException;

      /**
      * 根据编号查询
      *
      * @param id 编号
      * @return SysRoleMenus 
      * @throws ApiException 异常信息
      */
      SysRoleMenus findById(String id) throws ApiException;

      /**
       * 根据用户名获取角色对应的菜单id
       * @param loginUserName 用户名(即登录名login_account)
       * @return 返回角色对应的id
       * @throws ApiException
       */
      Set<String> getMenuIds(String loginUserName) throws ApiException;

      /**
       * 根据角色id获取菜单的id
       * @param roleId 角色id
       * @return 返回角色对应的id
       * @throws ApiException
       */
      List<String> getMenuIdsByRoleId(String roleId) throws ApiException;
      /**
       * 根据角色和菜单的对应对象
       * @param roleId 角色id
       * @return 返回角色对应的id
       * @throws ApiException
       */
      List<SysRoleMenus> getSysRoleMenusByRoleId(String roleId) throws ApiException;
}
