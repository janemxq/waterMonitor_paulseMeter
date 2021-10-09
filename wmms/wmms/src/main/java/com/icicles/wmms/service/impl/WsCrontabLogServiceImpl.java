package com.icicles.wmms.service.impl;

import cn.hutool.core.date.DateUtil;
import com.icicles.wmms.entity.po.WsCrontabLog;
import com.icicles.wmms.entity.param.WsCrontabLogQueryParam;
import com.icicles.wmms.DAO.WsCrontabLogMapper;
import com.icicles.wmms.service.WsCrontabLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.icicles.wmms.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 定时任务执行日志 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@Service
public class WsCrontabLogServiceImpl extends ServiceImpl<WsCrontabLogMapper, WsCrontabLog> implements WsCrontabLogService {

    private static final Logger logger = LoggerFactory.getLogger(WsCrontabLogServiceImpl.class);

    @Resource
    WsCrontabLogMapper wsCrontabLogMapper;

    @Override
    public IPage<WsCrontabLog> findPage(Page page, WsCrontabLogQueryParam wsCrontabLogQueryParam) throws ApiException {
        IPage<WsCrontabLog> retPage;
        try {
            QueryWrapper<WsCrontabLog> queryWrapper = wsCrontabLogQueryParam.build();
            queryWrapper.eq(StringUtils.isNotBlank(wsCrontabLogQueryParam.getType()), "type", wsCrontabLogQueryParam.getType());
            queryWrapper.eq(StringUtils.isNotBlank(wsCrontabLogQueryParam.getRunDate()), "run_date", wsCrontabLogQueryParam.getRunDate());
            queryWrapper.eq(StringUtils.isNotBlank(wsCrontabLogQueryParam.getRemark()), "remark", wsCrontabLogQueryParam.getRemark());
            queryWrapper.lambda().orderByDesc(WsCrontabLog::getId);
            retPage = this.page(page,queryWrapper);
            logger.debug("查询定时任务执行日志列表成功");
        } catch (Exception e) {
            logger.error("查询定时任务执行日志列表异常", e);
            e.printStackTrace();
            throw new ApiException("查询定时任务执行日志列表异常", HttpStatus.BAD_REQUEST);
        }
        return retPage;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WsCrontabLog wsCrontabLog) throws ApiException {
        try {
            this.save(wsCrontabLog);
            logger.debug("添加定时任务执行日志成功" + wsCrontabLog.getId());
        } catch (ApiException e) {
            logger.error("添加定时任务执行日志错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("添加定时任务执行日志异常", e1);
            e1.printStackTrace();
            throw new ApiException("添加定时任务执行日志异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws ApiException {
        try {
            this.removeById(id);
            logger.debug("删除定时任务执行日志成功" + id);
        } catch (Exception e) {
            logger.error("删除定时任务执行日志异常", e);
            e.printStackTrace();
            throw new ApiException("删除定时任务执行日志异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh(WsCrontabLog wsCrontabLog) throws ApiException {
        try {
            UpdateWrapper<WsCrontabLog> wrapper = new UpdateWrapper();
            wrapper.eq("id",wsCrontabLog.getId());
            this.update(wsCrontabLog,wrapper);
            logger.debug("更新定时任务执行日志成功" + wsCrontabLog.getId());
        } catch (ApiException e) {
            logger.error("更新定时任务执行日志错误:" + e.getMessage(), e);
            throw e;
        } catch (Exception e1) {
            logger.error("更新定时任务执行日志异常", e1);
            e1.printStackTrace();
            throw new ApiException("更新定时任务执行日志异常", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WsCrontabLog findById(String id) throws ApiException {
        WsCrontabLog wsCrontabLog;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            wsCrontabLog = wsCrontabLogMapper.selectOne(queryWrapper);
            logger.debug("根据编号查询定时任务执行日志成功");
        } catch (Exception e) {
            logger.error("根据编号查询定时任务执行日志异常", e);
            e.printStackTrace();
            throw new ApiException("根据编号查询定时任务执行日志异常", HttpStatus.BAD_REQUEST);
        }
        return wsCrontabLog;
    }

    @Override
    public long getDaysNumFromLastDb(int type) {
        QueryWrapper<WsCrontabLog> queryWrap = new QueryWrapper<> ();
        //查询最近一次记录
        queryWrap.lambda().eq(WsCrontabLog::getType,type).
                orderByDesc(WsCrontabLog::getId)
                .last("limit 1");
        WsCrontabLog log = wsCrontabLogMapper.selectOne(queryWrap);
        if (log == null) {
            return 0L;
        }
        String startTime = log.getCreateTime();
        if(StringUtils.isBlank(startTime)){
            startTime = log.getRunDate();
            if (StringUtils.isBlank(startTime)){
                return 0L;
            }
        }
        return DateUtil.betweenDay(DateUtil.date(),DateUtil.parse(startTime),true);
    }

    @Override
    public long getDaysNumFromLastDb() {
        return this.getDaysNumFromLastDb(1);
    }
}
