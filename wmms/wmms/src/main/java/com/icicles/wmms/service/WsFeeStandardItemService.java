package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.WsFeeStandardItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsFeeStandardItemQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 水表的收费标准子项 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-11
 */
public interface WsFeeStandardItemService extends IService<WsFeeStandardItem> {
      /**
       * 根据条件分页查询水表的收费标准子项
       *
       * @param page 分页对象
       * @return IPage<WsFeeStandardItem>   水表的收费标准子项列表（分页）
       */
       IPage<WsFeeStandardItem> findPage(Page page, WsFeeStandardItemQueryParam wsFeeStandardItemQueryParam) throws ApiException;

      /**
       * 增加水表的收费标准子项
       *
       * @param wsFeeStandardItem 水表的收费标准子项
       * @throws ApiException 异常信息
       */
      void add(WsFeeStandardItem wsFeeStandardItem) throws ApiException;

      /**
      * 删除水表的收费标准子项
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新水表的收费标准子项
      *
      * @param wsFeeStandardItem 水表的收费标准子项
      * @throws ApiException 异常信息
      */
      void refresh(WsFeeStandardItem wsFeeStandardItem) throws ApiException;

      /**
      * 根据编号查询水表的收费标准子项
      *
      * @param id 编号
      * @return WsFeeStandardItem 水表的收费标准子项
      * @throws ApiException 异常信息
      */
      WsFeeStandardItem findById(String id) throws ApiException;
}
