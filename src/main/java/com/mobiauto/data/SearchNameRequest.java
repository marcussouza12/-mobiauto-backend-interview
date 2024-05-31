package com.mobiauto.data;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class SearchNameRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
