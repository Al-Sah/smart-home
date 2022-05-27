package org.smarthome.sdk.models;


public class ErrorDeviceMessage {

    private final String device;
    private final String error;


    public ErrorDeviceMessage(String device, String error) {
        this.device = device;
        this.error = error;
    }


    public String getDevice() {
        return device;
    }

    public String getError() {
        return error;
    }
}
