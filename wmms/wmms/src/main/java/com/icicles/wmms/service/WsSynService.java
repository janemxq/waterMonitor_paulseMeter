package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.WsSyn;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsSynQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 同步记录 服务类
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
public interface WsSynService extends IService<WsSyn> {
      /**
       * 根据条件分页查询同步记录
       *
       * @param page 分页对象
       * @return IPage<WsSyn>   同步记录列表（分页）
       */
       IPage<WsSyn> findPage(Page page, WsSynQueryParam wsSynQueryParam) throws ApiException;

      /**
       * 增加同步记录
       *
       * @param wsSyn 同步记录
       * @throws ApiException 异常信息
       */
      void add(WsSyn wsSyn) throws ApiException;

      /**
      * 删除同步记录
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新同步记录
      *
      * @param wsSyn 同步记录
      * @throws ApiException 异常信息
      */
      void refresh(WsSyn wsSyn) throws ApiException;

      /**
      * 根据编号查询同步记录
      *
      * @param id 编号
      * @return WsSyn 同步记录
      * @throws ApiException 异常信息
      */
      WsSyn findById(String id) throws ApiException;

      WsSyn findByType(String type) throws ApiException;
}
