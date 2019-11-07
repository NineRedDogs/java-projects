package com.okta.examples.originexample;

import com.okta.examples.originexample.config.RequestContextUser;
import com.okta.examples.originexample.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class OriginExampleApplication {

    private static final Logger log = LoggerFactory.getLogger(OriginExampleApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(OriginExampleApplication.class, args);
	}

    @Bean
    @Scope("request")
    public User user() {
        log.error("=====================>>>> AJG-1");
        return RequestContextUser.findUser();
    }
}
