package com.sbolo.syk.common.mvc.configuration;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.sbolo.syk.common.tools.ConfigUtil;

@Configuration
public class ValidationConfiguration {
	public ResourceBundleMessageSource getMessageSource() throws Exception {
		String local = ConfigUtil.getPropertyValue("i18n.local");
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setDefaultEncoding("UTF-8");
        rbms.setBasenames("i18n/validation/ValidationMessages_"+local);
        return rbms;
    }

    @Bean
    public Validator getValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(getMessageSource());
        return validator;
    }
}
