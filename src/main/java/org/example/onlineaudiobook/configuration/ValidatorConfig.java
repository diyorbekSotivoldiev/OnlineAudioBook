package org.example.onlineaudiobook.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}

