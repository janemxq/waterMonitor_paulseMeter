package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.CommonResultShowDTO;
import com.icicles.wmms.entity.form.PadMeterInfoForm;
import com.icicles.wmms.entity.po.PadMeterInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.PadMeterInfoQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * pad端水表信息 服务类
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
public interface PadMeterInfoService extends IService<PadMeterInfo> {
      /**
       * 根据条件分页查询pad端水表信息
       *
       * @param page 分页对象
       * @return IPage<PadMeterInfo>   pad端水表信息列表（分页）
       */
       IPage<PadMeterInfo> findPage(Page page, PadMeterInfoQueryParam padMeterInfoQueryParam) throws ApiException;

      /**
       * 增加pad端水表信息
       *
       * @param padMeterInfo pad端水表信息
       * @throws ApiException 异常信息
       */
      void add(PadMeterInfo padMeterInfo) throws ApiException;

      /**
      * 删除pad端水表信息
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新pad端水表信息
      *
      * @param padMeterInfo pad端水表信息
      * @throws ApiException 异常信息
      */
      void refresh(PadMeterInfo padMeterInfo) throws ApiException;

      /**
      * 根据编号查询pad端水表信息
      *
      * @param id 编号
      * @return PadMeterInfo pad端水表信息
      * @throws ApiException 异常信息
      */
      PadMeterInfo findById(String id) throws ApiException;

      List<PadMeterInfo> getMetersByYardSn(String yardSn);

      /**
       * 水表初始化（配置当前码盘的码值、配置脉冲、配置累计用水量）
       * @param meterInfoForm
       */
      void init(PadMeterInfoForm meterInfoForm);
      /**
       * 上传数据到和村委端连接的设备
       * @param sns 水表的sn(一个或者多个)
       */
      void upload(List<String> sns);

      /**
       * 获取最新的设备读数
       * @param sns 一个或者多个设备
       */
      CommonResultShowDTO getMetersNum(List<String> sns);
}
