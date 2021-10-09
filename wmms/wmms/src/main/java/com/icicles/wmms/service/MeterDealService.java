package com.icicles.wmms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icicles.wmms.exception.ApiException;

/**
 * <p>
 * 系统参数 服务类
 * </p>
 *
 * @author auto
 * @since 2020-04-27
 */
public interface MeterDealService {

      /**
       * 根据水表编号更新数据
       *
       * @param id 水表编号
       * @throws ApiException 异常信息
       */
      boolean updateMeterData(String id) throws ApiException;

      /**
       * 配置终端设备地址码
       *
       * @param dAddr 目的地址
       * @param nAddr 新地址
       * @throws ApiException 异常信息
       */
      boolean configAddr(String dAddr,String nAddr) throws ApiException;

      /**
       * 配置累计用水量
       *
       * @param dAddr 目的地址
       * @param index 通道
       * @param val 脉冲表累计用水量
       * @throws ApiException 异常信息
       */
      boolean configVal(String dAddr,String index,String val) throws ApiException;

      /**
       * 配置脉冲
       *
       * @param dAddr 目的地址
       * @param index 通道
       * @param pulse 脉冲
       * @throws ApiException 异常信息
       */
      boolean configPulse(String dAddr,String index,String pulse) throws ApiException;


      /**
       * 配置表井下所有水表脉冲
       *
       * @param dAddr 目的地址
       * @param pulse 脉冲
       * @throws ApiException 异常信息
       */
      boolean configPulse(String dAddr,String pulse) throws ApiException;

      /**
       * 配置当前码盘的码值
       *
       * @param dAddr 目的地址
       * @param index 通道
       * @param codeVal 码盘值
       * @throws ApiException 异常信息
       */
      boolean configCodeVal(String dAddr,String index,String codeVal) throws ApiException;

      /**
       * 读取终端设备数据
       *
       * @param dAddr 目的地址
       * @param index 通道
       * @throws ApiException 异常信息
       */
      JSONObject readSingle(String dAddr, String index) throws ApiException;

      /**
       * 读取终端设备数据(表井下所有表)
       *
       * @param dAddr 目的地址
       * @throws ApiException 异常信息
       */
      JSONArray read(String dAddr) throws ApiException;

      /**
       * 更新所有水表数据数据
       */
      void updateMetersDataCrontab();

      /**
       * 更新指定水表数据数据并自动缴费
       * @param meterId 水表id
       */
      void updateMetersDateCrontab(String meterId);
}
