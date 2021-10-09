package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.WaterStatisticesDTO;
import com.icicles.wmms.entity.form.WsFeeLogExcelForm;
import com.icicles.wmms.entity.po.WsFeeLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsFeeLogQueryParam;
import com.icicles.wmms.entity.po.WsMeter;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 缴费记录 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
public interface WsFeeLogService extends IService<WsFeeLog> {
      /**
       * 根据条件分页查询缴费记录
       *
       * @param page 分页对象
       * @return IPage<WsFeeLog>   缴费记录列表（分页）
       */
       IPage<WsFeeLog> findPage(Page page, WsFeeLogQueryParam wsFeeLogQueryParam) throws ApiException;

      /**
       * 增加缴费记录
       * @param wsFeeLog 缴费记录
       * @throws ApiException 异常信息
       */
      WsFeeLog add(WsFeeLog wsFeeLog) throws ApiException;

      /**
      * 删除缴费记录
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新缴费记录
      *
      * @param wsFeeLog 缴费记录
      * @throws ApiException 异常信息
      */
      void refresh(WsFeeLog wsFeeLog) throws ApiException;

      /**
      * 根据编号查询缴费记录
      *
      * @param id 编号
      * @return WsFeeLog 缴费记录
      * @throws ApiException 异常信息
      */
      WsFeeLog findById(String id) throws ApiException;

      /**
       * 月统计
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getAccountSumByMonth(String year,String userSn,String villageSn,String meterSn);

      /**
       * 年统计
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getAccountSumByYear(String userSn,String villageSn,String meterSn);

      /**
       * 季度统计
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @return
       */
      List<WaterStatisticesDTO> getAccountSumBySeason(String year,String userSn,String villageSn,String meterSn);


      /**
       * 导出缴费记录月数据到excel
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response      响应类
       * @throws IOException
       */
      void exportMonthData(String year,String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException;

      /**
       * 导出缴费记录季度数据到excel
       * @param year         查询的年（默认当年）
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response      响应类
       * @throws IOException
       */
      void exportSeasonData(String year,String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException;

      /**
       * 导出缴费记录季度数据到excel
       * @param userSn       用户标识
       * @param villageSn    村标识
       * @param meterSn      设备标识
       * @param response      响应类
       * @throws IOException
       */
      void exportYearData(String userSn,String villageSn,String meterSn, HttpServletResponse response) throws IOException;

      /**
       * 导出缴费记录excel
       * @param wsFeeLogExcelForm  查询表单
       * @param response           响应类
       * @throws IOException
       */
      void exportFeeLogData(WsFeeLogExcelForm wsFeeLogExcelForm, HttpServletResponse response) throws IOException;
      /**
       * 自动用余额缴费
       */
      void autoFee();

      /**
       * 利用余额缴费
       * @param meter 水表设备pojo
       */
      void addFeeByBalance(WsMeter meter);
}
