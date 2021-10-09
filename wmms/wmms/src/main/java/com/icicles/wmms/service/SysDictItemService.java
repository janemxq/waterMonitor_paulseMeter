package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.SysDictItemQueryParam;
import com.icicles.wmms.entity.po.SysDictItem;
import com.icicles.wmms.exception.ApiException;

import java.util.List;

/**
 * <p>
 * 字典项 服务类
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
public interface SysDictItemService extends IService<SysDictItem> {
      /**
       * 根据条件分页查询字典项
       *
       * @param page 分页对象
       * @return IPage<SysDictItem>   字典项列表（分页）
       */
       IPage<SysDictItem> findPage(Page page, SysDictItemQueryParam sysDictItemQueryParam) throws ApiException;

      /**
       * 增加字典项
       *
       * @param sysDictItem 字典项
       * @throws ApiException 异常信息
       */
      void add(SysDictItem sysDictItem) throws ApiException;

      /**
      * 删除字典项
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新字典项
      *
      * @param sysDictItem 字典项
      * @throws ApiException 异常信息
      */
      void refresh(SysDictItem sysDictItem) throws ApiException;

      /**
      * 根据编号查询字典项
      *
      * @param id 编号
      * @return SysDictItem 字典项
      * @throws ApiException 异常信息
      */
      SysDictItem findById(String id) throws ApiException;

      List<SysDictItem> findByDictCode(String dictCode) throws ApiException;

      /**
       * 根据字典代码和标签查询字典项详情
       * @param dictCode  字典代码
       * @param label     标签
       * @return          字典项
       * @throws ApiException
       */
      SysDictItem findByDictCode(String dictCode,String label) throws ApiException;
}
