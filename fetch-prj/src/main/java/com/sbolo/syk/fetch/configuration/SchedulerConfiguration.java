package com.sbolo.syk.fetch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sbolo.syk.fetch.scheduler.StartupScheduler;

@Configuration
public class SchedulerConfiguration {
	@Bean
    public StartupScheduler startupScheduler(){
        return new StartupScheduler();
    }
}
