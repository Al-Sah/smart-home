package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Command {

    private final String hub;
    private final String device;
    private final String component;
    private final String property;
    private final String options;
    private final long expiration;


    @JsonCreator
    public Command(
            @JsonProperty("hub") String hub,
            @JsonProperty("device") String device,
            @JsonProperty("component") String component,
            @JsonProperty("property") String property,
            @JsonProperty("options") String options,
            @JsonProperty("expire") long expiration) {
        this.hub = hub;
        this.device = device;
        this.component = component;
        this.property = property;
        this.options = options;
        this.expiration = expiration;
    }


    public String getOptions() {
        return options;
    }

    public String getComponent() {
        return component;
    }

    public String getHub() {
        return hub;
    }

    public String getDevice() {
        return device;
    }

    public String getProperty() {
        return property;
    }

    public long getExpiration() {
        return expiration;
    }

}
