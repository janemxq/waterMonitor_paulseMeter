package com.icicles.wmms.service.impl;

import com.icicles.wmms.service.CrontabTaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 应用启动后自动执行任务
 * 1、判断是不是很长时间没有自动抄表和缴费了
 * @author lisy
 */
@Component
public class StartupRunnerServiceImpl implements CommandLineRunner {
    @Resource
    private CrontabTaskService crontabTaskService;
    @Override
    public void run(String... args) {
        crontabTaskService.readMeterAndFee();
    }
}
