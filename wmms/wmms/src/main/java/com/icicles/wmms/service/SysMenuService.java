package com.icicles.wmms.service;

import com.icicles.wmms.entity.form.SysMenuQueryForm;
import com.icicles.wmms.entity.po.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.SysMenuQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
public interface SysMenuService extends IService<SysMenu> {
      /**
       * 根据角色所有菜单
       *
       * @param sysMenuQueryForm 查询条件
       * @param principal  登录信息
       * @return IPage<SysMenu>   列表（分页）
       */
       List<SysMenu> menus(SysMenuQueryForm sysMenuQueryForm, Principal principal) throws ApiException;

      /**
       * 返回菜单列表
       * @param page             分页对象
       * @param sysMenuQueryForm 查询条件
       * @return IPage<SysMenu>   列表（分页）
       */
      IPage<SysMenu> findPage(Page page,SysMenuQueryParam sysMenuQueryForm) throws ApiException;

      /**
       * 增加
       *
       * @param sysMenu 
       * @throws ApiException 异常信息
       */
      void add(SysMenu sysMenu) throws ApiException;

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
      * @param sysMenu 
      * @throws ApiException 异常信息
      */
      void refresh(SysMenu sysMenu) throws ApiException;

      /**
      * 根据编号查询
      *
      * @param id 编号
      * @return SysMenu 
      * @throws ApiException 异常信息
      */
      SysMenu findById(String id) throws ApiException;

      /**
       * 某个菜单的直接子菜单
       *
       * @param id 编号
       * @return SysMenu
       * @throws ApiException 异常信息
       */
      List<SysMenu> getChildMenu(Long id) throws ApiException;

      /**
       * 获取树形结构的菜单
       * @param sysMenuQueryForm 查询条件
       * @param principal        登录信息
       * @return SysMenu
       * @throws ApiException 异常信息
       */
      List<SysMenu> menusTree(SysMenuQueryForm sysMenuQueryForm, Principal principal) throws ApiException;

      /**
       * 通过一组id获取菜单
       *
       * @param ids 通过一组id获取菜单
       * @return SysMenu
       * @throws ApiException 异常信息
       */
      List<SysMenu> getListByIds(List<String> ids) throws ApiException;

      List<SysMenu> menusTreeByRoleId(String roleId,Principal principal) throws ApiException;
}
