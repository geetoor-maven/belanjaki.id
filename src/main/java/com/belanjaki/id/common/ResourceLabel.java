package com.belanjaki.id.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:body-label.properties")
public class ResourceLabel {

    @Autowired
    private Environment environment;

    public String getBodyLabel(String key){
        return environment.getProperty(key);
    }
}
