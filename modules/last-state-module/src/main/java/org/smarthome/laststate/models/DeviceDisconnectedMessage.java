package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;

@Getter
public class DeviceDisconnectedMessage {

    private final DeviceDisconnectionDetails details;
    private final DeviceStateDTO state;

    public DeviceDisconnectedMessage(DeviceDisconnectionDetails details, DeviceStateDTO state) {
        this.details = details;
        this.state = state;
    }
}
