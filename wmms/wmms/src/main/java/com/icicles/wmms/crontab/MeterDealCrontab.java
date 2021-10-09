package com.icicles.wmms.crontab;

import com.icicles.wmms.service.CrontabTaskService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动抄表,抄表之后，执行自动充值
 * @author lisy
 */
@Component
@EnableScheduling
public class MeterDealCrontab {
    @Resource
    private CrontabTaskService crontabTaskService;

    /**
     * 每天1点看看是不是需要抄表和自动缴费
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void fee(){
        crontabTaskService.readMeterAndFee();
    }
}
