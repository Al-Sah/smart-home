package org.smarthome.sdk.models;

public class DeviceMessage {

    private final String device;
    private final String component;
    private final String property;
    private final String value;
    private final String error;


    public DeviceMessage(String device, String component, String property, String value, String error) {
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
