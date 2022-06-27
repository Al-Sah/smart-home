package org.smarthome.laststate.exceptions;

public class DeviceNotFoundException extends RuntimeException{

    public DeviceNotFoundException(String id) {
        super(String.format("Device with id '%s' was not found", id));
    }
}
