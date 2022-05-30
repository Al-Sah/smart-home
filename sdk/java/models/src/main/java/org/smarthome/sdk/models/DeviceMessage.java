package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceMessage {

    private final String device;
    private final String component;
    private final String property;
    private final String value;
    private final String error;


    @JsonCreator
    public DeviceMessage(
            @JsonProperty("device") String device,
            @JsonProperty("component") String component,
            @JsonProperty("property") String property,
            @JsonProperty("value") String value,
            @JsonProperty("error") String error) {
        this.device = device;
        this.component = component;
        this.property = property;
        this.value = value;
        this.error = error;
    }

    public String getDevice() {
        return device;
    }

    public String getComponent() {
        return component;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
    
}
