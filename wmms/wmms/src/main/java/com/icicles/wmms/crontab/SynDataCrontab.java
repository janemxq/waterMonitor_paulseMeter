package com.icicles.wmms.crontab;

import cn.hutool.core.util.RandomUtil;
import com.icicles.wmms.config.AppRunSchema;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.service.impl.SysDataClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动同步数据
 * @author lisy
 */
@Component
@EnableScheduling
public class SynDataCrontab {
    @Resource
    private SysDataClient sysDataClient;
    @Resource
    private AppRunSchema appRunSchema;

    /**
     * 自动同步数据到集管端（每月5点）
     * 0 0 * * * ?
     */
    @Scheduled(cron="0 50 17 * * ?")
    public void fee(){
        try {
            //为了避免所有村委端集中发送数据，随机暂停10-50秒
            int sleepNum = RandomUtil.randomInt(10000,50000);
            Thread.sleep(sleepNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //只要不是集管端，就自动同步数据到集管端
        if(!appRunSchema.getSchema().equals(Constants.SCHEMA_CENTER)){
            sysDataClient.syn();
        }
    }
}
