package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;



public class DeviceDisconnectedMessage {

    private final DeviceDisconnectionDetails details;
    private final DeviceStateDetailsDTO deviceStateDetails;

    public DeviceDisconnectedMessage(DeviceDisconnectionDetails details, DeviceStateDetailsDTO deviceStateDetails) {
        this.details = details;
        this.deviceStateDetails = deviceStateDetails;
    }


    public DeviceDisconnectionDetails getDetails() {
        return details;
    }

    public DeviceStateDetailsDTO getDeviceStateDetails() {
        return deviceStateDetails;
    }
}
