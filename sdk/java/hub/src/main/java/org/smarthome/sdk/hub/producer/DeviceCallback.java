package org.smarthome.sdk.hub.producer;

/**
 * @author Al-Sah
 */
public interface DeviceCallback {

    void send(String device, String component, String property, String value, String error);

}
