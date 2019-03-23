package com.wuyue.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 说明： 一般线程池，用于处理一般业务
 */
@Configuration
public class ExecutorConfig {

    public final static String EXECUTOR_SERVICE_NAME = "executorService";

    @Bean
    public ExecutorService executorService(){
        return new ThreadPoolExecutor(
                20,
                250,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }
}
