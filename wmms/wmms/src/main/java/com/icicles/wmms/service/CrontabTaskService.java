package com.icicles.wmms.service;

/**
 * 定时任务
 */
public interface CrontabTaskService {
    /**
     * 自动抄表并充值
     */
    void readMeterAndFee();
}
