package com.icicles.wmms.service.impl;

import cn.hutool.core.date.DateUtil;
import com.icicles.wmms.config.AppRunSchema;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.po.SysParam;
import com.icicles.wmms.entity.po.WsCrontabLog;
import com.icicles.wmms.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 定时任务
 * 1、自动抄表和缴费
 * @author lisy
 */
@Service
public class CrontabTaskServiceImpl implements CrontabTaskService {
    @Resource
    private MeterDealService meterDealService;
    @Resource
    private AppRunSchema appRunSchema;
    @Resource
    private SysParamService sysParamService;
    @Resource
    private WsFeeLogService feeFeeLogService;
    @Resource
    private WsCrontabLogService wsCrontabLogService;

    /**
     * 超过多久没有执行时，无论到不到设定的期限都执行
     */
    private long intervalDays = 31L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Async("task-queue")
    public void readMeterAndFee() {
        //只要不是集管端，就自动抄表
        if(!appRunSchema.getSchema().equals(Constants.SCHEMA_CENTER)){
            //判断是否执行
            SysParam param = sysParamService.findByKey(Constants.READ_METER_TIME);
            if(param!=null){
                //用户设定的日期
                String userSettingDate = param.getConfigValue();
                //如果今天的日期等于用户设置的日期或者超过了一定时间没有抄表了 都自动执行
                if(this.isStartForSetting(userSettingDate)||this.isStartForTooLongNoRun(intervalDays)){
                    //自动抄表
                    meterDealService.updateMetersDataCrontab();
                    //自动缴费
                    feeFeeLogService.autoFee();
                    /*添加记录*/
                    WsCrontabLog log = new WsCrontabLog();
                    log.setType(1);
                    log.setRunDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
                    log.setRemark("自动抄表和缴费");

                    wsCrontabLogService.add(log);
                }else{
                    logger.info("不需要处理抄表和缴费");
                }
            }else{
                logger.info("不需要处理抄表和缴费!");
            }
        }else{
            logger.info("不需要处理抄表和缴费!!!");
        }
    }

    /**
     * 判断今天的日期是不是等于用户设置的日期
     * @param userSettingDate 用户设置的日期
     * @return
     */
    private boolean isStartForSetting(String userSettingDate) {
        if(StringUtils.isBlank(userSettingDate)){
            return false;
        }
        int today = DateUtil.dayOfMonth(new Date());
        String nowDate = String.valueOf(today);
        if(nowDate.equals(userSettingDate)){
            return true;
        }else{
            if(today<=9){
                nowDate = "0"+nowDate;
                return nowDate.equals(userSettingDate);
            }
            return false;
        }
    }

    /**
     * 计算最后一次执行时间和当前时间的时间差，看看时间差是不是大于了某个值
     * @param days 时间差大于的某个值
     * @return
     */
    private boolean isStartForTooLongNoRun(long days){
        long howLong = wsCrontabLogService.getDaysNumFromLastDb();
        return howLong > days;
    }
}
