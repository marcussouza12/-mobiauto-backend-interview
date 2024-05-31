package com.mobiauto;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("api")
public class ConfigValues {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}