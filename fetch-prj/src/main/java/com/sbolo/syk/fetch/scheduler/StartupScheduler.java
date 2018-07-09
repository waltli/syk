package com.sbolo.syk.fetch.scheduler;

import java.lang.reflect.Field;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import com.sbolo.syk.common.tools.ConfigUtil;

@Order(1)
public class StartupScheduler implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupScheduler.class);
    
    @Value("${jlibtorrent.jni}")
    private String jlibtorrent;

    @Override
    public void run(String... args) throws Exception {
    	this.setLibPath();
    }
    
    private void setLibPath() throws Exception {
    	if(StringUtils.isBlank(jlibtorrent)) {
    		log.warn("the jlibtorrent jni is not exist, when set jlibtorrent.jni.path in StartupScheduler!");
    		return;
    	}
		System.setProperty("jlibtorrent.jni.path", jlibtorrent);
    }
    
}
