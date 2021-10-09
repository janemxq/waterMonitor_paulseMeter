package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.WsCrontabLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.entity.param.WsCrontabLogQueryParam;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 定时任务执行日志 服务类
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
public interface WsCrontabLogService extends IService<WsCrontabLog> {
      /**
       * 根据条件分页查询定时任务执行日志
       *
       * @param page 分页对象
       * @return IPage<WsCrontabLog>   定时任务执行日志列表（分页）
       */
       IPage<WsCrontabLog> findPage(Page page, WsCrontabLogQueryParam wsCrontabLogQueryParam) throws ApiException;

      /**
       * 增加定时任务执行日志
       *
       * @param wsCrontabLog 定时任务执行日志
       * @throws ApiException 异常信息
       */
      void add(WsCrontabLog wsCrontabLog) throws ApiException;

      /**
      * 删除定时任务执行日志
      *
      * @param id 编号
      * @throws ApiException 异常信息
      */
      void delete(String id) throws ApiException;

      /**
      * 更新定时任务执行日志
      *
      * @param wsCrontabLog 定时任务执行日志
      * @throws ApiException 异常信息
      */
      void refresh(WsCrontabLog wsCrontabLog) throws ApiException;

      /**
      * 根据编号查询定时任务执行日志
      *
      * @param id 编号
      * @return WsCrontabLog 定时任务执行日志
      * @throws ApiException 异常信息
      */
      WsCrontabLog findById(String id) throws ApiException;

      /**
       * 数据库中最新的记录，距离现在有多少天
       * @param type 类型
       * @return
       */
      long getDaysNumFromLastDb(int type);
      /**
       * 数据库中最新的记录，距离现在有多少天（有默认类型）
       * @return
       */
      long getDaysNumFromLastDb();
}
