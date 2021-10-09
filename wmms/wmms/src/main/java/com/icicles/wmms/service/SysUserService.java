package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.dto.UseDefaultPwdResultDTO;
import com.icicles.wmms.entity.param.SysUserQueryParam;
import com.icicles.wmms.entity.po.BasePo;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.exception.ApiException;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 人员信息 服务类
 * </p>
 *
 * @author yanwei
 * @since 2020-03-09
 */
public interface SysUserService extends IService<SysUser> {
      /**
       * 根据条件分页查询人员信息
       *
       * @param page 分页对象
       * @return IPage<SysUser>   人员信息列表（分页）
       */
      IPage<SysUser> findPage(Page page, SysUserQueryParam sysUserQueryParam,String account) throws ApiException;

      /**
       * 增加人员信息
       *
       * @param sysUser 人员信息
       * @throws ApiException 异常信息
       */
      void add(SysUser sysUser) throws ApiException;

      /**
      * 删除人员信息
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新人员信息
      *
      * @param sysUser 人员信息
      * @throws ApiException 异常信息
      */
      void refresh(SysUser sysUser) throws ApiException;

      /**
       * 根据账号查询用户信息
       *
       * @param account 账号
       * @return SysUser   用户信息
       */
      SysUser findByAccount(String account) throws ApiException;

      /**
       * 检查用户使用的密码是否是默认密码,如果默认用户密码与默认密码相同，返回用户名和默认密码
       * @param sn 登录账号
       * @return 带有默认密码和登录账号等信息的对象
       * @throws ApiException
       */
      UseDefaultPwdResultDTO checkDefaultPwd(String sn) throws ApiException;

      /**
       * 根据编号查询用水监测管理平台用户信息表
       *
       * @param id 编号
       * @return SysUser 用水监测管理平台用户信息表
       * @throws ApiException 异常信息
       */
      SysUser findById(String id) throws ApiException;

      /**
       * 获取所有内容
       * @return
       * @throws ApiException
       */
      List<SysUser> getAll() throws ApiException;

      /**
       * 根据用户标识查询用户新
       *
       * @param userSn     用户标识
       * @return SysUser   用户信息
       */
      SysUser findByUserSn(String userSn) throws ApiException;

      /**
       * 实时显示用户列表关联的村
       *
       * @param data     用户标识
       * @return SysUser   用户信息
       */
      List<SysUser> setVillageName(List<SysUser> data) throws ApiException;

      /**
       * 根据用户的sn返回用户信息
       * @param sn 所有的sn
       * @return
       * @throws ApiException
       */
      List<SysUser> getAllBySn(Collection<String> sn) throws ApiException;

      /**
       * 查询pojo中的涉及到的updateUserId的信息，并替换为用户名
       * @param data 实体类
       * @param <T> 实体类
       */
      <T extends BasePo> void setUpdateUser(List<T> data);

      /**
       * 通过登录账号获取角色
       * @param account 登录账号
       * @return
       */
      SysRole getRoleByAccount(String account);
      /**
       * 通过登录账号信息获取角色
       * @param principal 登录信息
       * @return
       */
      SysRole getRoleByAccount(Principal principal);

      /**
       * 通过用户标识修改用户登录密码
       * @param sn       用户标识
       * @param password 用户密码
       */
      void changePwd(String sn,String password);

      /**
       * 添加登录次数
       * @param username     用户id
       */
      void addLoginNum(String username);

      /**
       * 删除多个用户
       * @param ids  多个用户id
       */
      void multiDel(List<String> ids);
}
