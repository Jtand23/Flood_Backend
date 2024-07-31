package com.example.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务配置
 *
 * @author russ2
 */
@Configuration
public class TaskSchedulerConfig {

    @Bean
    @Primary
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 您可以根据需要调整线程池的大小
        scheduler.setPoolSize(16);
        scheduler.setThreadNamePrefix("FloodDataUpdate-");
        scheduler.initialize();
        return scheduler;
    }
}
