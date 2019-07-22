package com.monitoring.data_manipulation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 17:41
 * @Description: 配置定时任务
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(setTaskExecutors());
    }

    /**
     * 开启三个线程来处理任务
     * @return
     */
    @Bean
    public Executor setTaskExecutors(){
        return Executors.newScheduledThreadPool(3);
    }
}
