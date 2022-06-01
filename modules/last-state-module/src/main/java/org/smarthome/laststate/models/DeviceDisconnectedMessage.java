package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;



public class DeviceDisconnectedMessage {

    private final DeviceDisconnectionDetails details;
    private final DeviceStateDetails deviceStateDetails;

    public DeviceDisconnectedMessage(DeviceDisconnectionDetails details, DeviceStateDetails deviceStateDetails) {
        this.details = details;
        this.deviceStateDetails = deviceStateDetails;
    }


    public DeviceDisconnectionDetails getDetails() {
        return details;
    }

    public DeviceStateDetails getDeviceStateDetails() {
        return deviceStateDetails;
    }
}
