package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.WsCharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsChargeQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 用户充值记录 服务类
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
public interface WsChargeService extends IService<WsCharge> {
      /**
       * 根据条件分页查询用户充值记录
       *
       * @param page 分页对象
       * @return IPage<WsCharge>   用户充值记录列表（分页）
       */
       IPage<WsCharge> findPage(Page page, WsChargeQueryParam wsChargeQueryParam) throws ApiException;

      /**
       * 增加用户充值记录
       *
       * @param wsCharge 用户充值记录
       * @throws ApiException 异常信息
       */
      void add(WsCharge wsCharge) throws ApiException;

      /**
      * 删除用户充值记录
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新用户充值记录
      *
      * @param wsCharge 用户充值记录
      * @throws ApiException 异常信息
      */
      void refresh(WsCharge wsCharge) throws ApiException;

      /**
      * 根据编号查询用户充值记录
      *
      * @param id 编号
      * @return WsCharge 用户充值记录
      * @throws ApiException 异常信息
      */
      WsCharge findById(String id) throws ApiException;
}
