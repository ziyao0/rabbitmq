package com.zzy.provider.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 15:27
 * @desc
 */
@Configuration
public class ThreadPololConfig {

    @Bean
    public ScheduledThreadPoolExecutor getPoolExecutor() {
        return new ScheduledThreadPoolExecutor(10);
    }
}
