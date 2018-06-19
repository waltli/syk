package com.sbolo.syk.common.mvc.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.sbolo.syk.common.converter.RequestResultEspecialMessageConverter;

@Configuration
public class MessageConvertersConfiguration {
	
	@SuppressWarnings("rawtypes")
	@Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> requestResultEspecial = new RequestResultEspecialMessageConverter();
        return new HttpMessageConverters(requestResultEspecial);
    }
}
