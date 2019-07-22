package com.monitoring.data_manipulation.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 17:46
 * @Description: 定时任务
 */
@Component
public class TimingTask {

    @Scheduled(cron = "0/10 * * * * ?")
    private void process(){
        // TODO 定时任务逻辑 10秒
        System.out.println("-------------" + new Date());
    }

}
