package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import com.icicles.wmms.entity.po.PadMeterYard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.PadMeterYardQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * pad端表井 服务类
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
public interface PadMeterYardService extends IService<PadMeterYard> {
      /**
       * 根据条件分页查询pad端表井
       *
       * @param page 分页对象
       * @return IPage<PadMeterYard>   pad端表井列表（分页）
       */
       IPage<PadMeterYard> findPage(Page page, PadMeterYardQueryParam padMeterYardQueryParam) throws ApiException;

      /**
       * 增加pad端表井
       *
       * @param padMeterYard pad端表井
       * @throws ApiException 异常信息
       */
      void add(PadMeterYard padMeterYard) throws ApiException;

      /**
      * 删除pad端表井
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新pad端表井
      *
      * @param padMeterYard pad端表井
      * @throws ApiException 异常信息
      */
      void refresh(PadMeterYard padMeterYard) throws ApiException;

      /**
      * 根据编号查询pad端表井
      *
      * @param id 编号
      * @return PadMeterYard pad端表井
      * @throws ApiException 异常信息
      */
      PadMeterYard findById(String id) throws ApiException;
      /**
       * 根据水表sn值查询pad端表井
       *
       * @param sn 编号
       * @return PadMeterYard pad端表井
       * @throws ApiException 异常信息
       */
      PadMeterYard findBySn(String sn) throws ApiException;
      /**
       * 更新表井下的水表读数
       */
      CommonResultShowDTO getYardNum(String sn);

      /**
       * 上传数据
       * @param sns
       */
      void upload(List<String> sns);
}
