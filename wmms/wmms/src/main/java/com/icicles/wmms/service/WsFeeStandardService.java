package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.WsFeeStandardDTO;
import com.icicles.wmms.entity.po.WsFeeStandard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsFeeStandardQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 水表的收费标准 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
public interface WsFeeStandardService extends IService<WsFeeStandard> {
      /**
       * 根据条件分页查询水表的收费标准
       *
       * @param page 分页对象
       * @return IPage<WsFeeStandard>   水表的收费标准列表（分页）
       */
       IPage<WsFeeStandardDTO> findPage(Page<WsFeeStandardDTO> page, WsFeeStandardQueryParam wsFeeStandardQueryParam) throws ApiException;

      /**
       * 增加水表的收费标准
       *
       * @param wsFeeStandard 水表的收费标准
       * @throws ApiException 异常信息
       */
      void add(WsFeeStandard wsFeeStandard) throws ApiException;

      /**
      * 删除水表的收费标准
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新水表的收费标准
      *
      * @param wsFeeStandard 水表的收费标准
      * @throws ApiException 异常信息
      */
      void refresh(WsFeeStandard wsFeeStandard) throws ApiException;

      /**
      * 根据编号查询水表的收费标准
      *
      * @param id 编号
      * @return WsFeeStandard 水表的收费标准
      * @throws ApiException 异常信息
      */
      WsFeeStandard findById(String id) throws ApiException;

      /**
       * 根据编号查询水表的收费标准
       *
       * @param id 编号
       * @return WsFeeStandard 水表的收费标准
       * @throws ApiException 异常信息
       */
      WsFeeStandardDTO findDetailById(String id) throws ApiException;

      List<WsFeeStandardDTO> getAll() throws ApiException;

      /**
       * 根据唯一编码查询水表的收费标准
       *
       * @param sn 唯一编码
       * @return WsFeeStandard 水表的收费标准
       * @throws ApiException 异常信息
       */
      WsFeeStandard findBySn(String sn) throws ApiException;
}
