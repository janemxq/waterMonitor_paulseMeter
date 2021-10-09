package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.po.WsMeterReader;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsMeterReaderQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 水表读数 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
public interface WsMeterReaderService extends IService<WsMeterReader> {
      /**
       * 根据条件分页查询水表读数
       *
       * @param page 分页对象
       * @return IPage<WsMeterReader>   水表读数列表（分页）
       */
       IPage<WsMeterReader> findPage(Page page, WsMeterReaderQueryParam wsMeterReaderQueryParam) throws ApiException;

      /**
       * 增加水表读数
       *
       * @param wsMeterReader 水表读数
       * @throws ApiException 异常信息
       */
      void add(WsMeterReader wsMeterReader) throws ApiException;

      /**
      * 删除水表读数
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新水表读数
      *
      * @param wsMeterReader 水表读数
      * @throws ApiException 异常信息
      */
      void refresh(WsMeterReader wsMeterReader) throws ApiException;

      /**
      * 根据编号查询水表读数
      *
      * @param id 编号
      * @return WsMeterReader 水表读数
      * @throws ApiException 异常信息
      */
      WsMeterReader findById(String id) throws ApiException;

      /**
       * 查询月用水量，userSn为空时查询平台用水量
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getWaterSumByMonth(String year,String userSn,String villageSn,String meterSn);
      /**
       * 查询月用水量，userSn为空时查询平台用水量
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getWaterSumByYear(String userSn,String villageSn,String meterSn);

      /**
       * 查询季度用水量，userSn为空时查询平台用水量
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getWaterSumBySeason(String year,String userSn,String villageSn,String meterSn);

      /**
       * 查询月用水量，userSn为空时查询平台用水量
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response     响应
       */
      void exportWaterSumByMonth(String year, String userSn, String villageSn, String meterSn, HttpServletResponse response) throws IOException;

      /**
       * 查询季度用水量，userSn为空时查询平台用水量
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response     响应
       */
      void exportWaterSumBySeason(String year,String userSn,String villageSn,String meterSn,HttpServletResponse response) throws IOException;

      /**
       * 查询年用水量，userSn为空时查询平台用水量
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response     响应
       */
      void exportWaterSumByYear(String userSn,String villageSn,String meterSn,HttpServletResponse response) throws IOException;
}
