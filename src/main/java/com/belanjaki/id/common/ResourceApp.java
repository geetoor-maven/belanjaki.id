package com.belanjaki.id.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class ResourceApp {

    @Autowired
    private Environment environment;

    public String getValue(String key){
        return environment.getProperty(key);
    }

}
