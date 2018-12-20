package com.proserv.consumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Watch implements Serializable {

    private String name;
    private String watchText;

    public Watch() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getWatchText() {
        return watchText;
    }
    public void setWatchText(String watchText) {
        this.watchText = watchText;
    }
}
