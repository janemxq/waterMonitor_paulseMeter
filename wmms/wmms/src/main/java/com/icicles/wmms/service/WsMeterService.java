package com.icicles.wmms.service;

import com.icicles.wmms.entity.dto.*;
import com.icicles.wmms.entity.form.WsMeterQueryForm;
import com.icicles.wmms.entity.form.WsMeterSearchForm;
import com.icicles.wmms.entity.po.WsMeter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsMeterQueryParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icicles.wmms.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 水表设备 服务类
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
public interface WsMeterService extends IService<WsMeter> {
      /**
       * 根据条件分页查询水表设备
       *
       * @param page 分页对象
       * @return IPage<WsMeter>   水表设备列表（分页）
       */
       IPage<WsMeter> findPage(Page page, WsMeterQueryParam wsMeterQueryParam) throws ApiException;

      /**
       * 增加水表设备
       *
       * @param wsMeter 水表设备
       * @throws ApiException 异常信息
       */
      void add(WsMeter wsMeter) throws ApiException;

      /**
      * 删除水表设备
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新水表设备
      *
      * @param wsMeter 水表设备
      * @throws ApiException 异常信息
      */
      void refresh(WsMeter wsMeter) throws ApiException;

      /**
      * 根据编号查询水表设备
      *
      * @param id 编号
      * @return WsMeter 水表设备
      * @throws ApiException 异常信息
      */
      WsMeter findById(String id) throws ApiException;

      /**
       * 分页查询
       * @param wsMeterQueryForm 查询实体
       * @return
       */
      IPage<WsMeterDTO> getList(WsMeterQueryForm wsMeterQueryForm, Principal principal);

      /**
       * 根据用户名查询水表信息
       * @param wsMeterSearchForm 查询表单
       * @return
       */
      IPage<MeterFeeDTO> getMetersFeeInfo(WsMeterSearchForm wsMeterSearchForm,Principal principal);

      WsMeter findBySn(String sn) throws ApiException;

      /**
       * 导出excel
       */
      void exportExcel(List<String> items, HttpServletResponse response,String villageSn) throws IOException;

      /**
       * 导入excel
       * @param file  文件
       * @return ExcelResultDTO
       */
      ExcelResultDTO importExcel(MultipartFile file);

      /**
       * 添加缴费金额
       * @param meterSn  设备标识
       * @param account  金额
       * @throws ApiException 异常
       */
      void decAccount(String meterSn, BigDecimal account) throws ApiException;

      /**
       * 添加缴费金额
       * @param meterSn  设备标识
       * @param account  金额
       * @throws ApiException 异常
       */
      void incAccount(String meterSn, BigDecimal account) throws ApiException;

      /**
       * 用来处理水表设备村和表井的名称显示
       * @param data 设备列表
       * @return
       */
      List<WsMeter> setVillageNameAndYardName(List<WsMeter> data);
}
