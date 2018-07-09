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
    	URL resource = StartupScheduler.class.getResource("/dll");
    	if(resource == null) {
    		log.warn("the folder \"dll\" is not exist, when set java.library.path!");
    		return;
    	}
		String path = resource.getPath().replaceAll("^(file:)", "");
		System.setProperty("jlibtorrent.jni.path", path+"/"+jlibtorrent);
    }
    
}
