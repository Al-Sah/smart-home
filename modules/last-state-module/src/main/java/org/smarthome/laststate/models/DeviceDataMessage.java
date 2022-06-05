package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.DeviceMessage;
@Getter
public class DeviceDataMessage {

    private final DeviceMessage message;
    private final DeviceStateDTO state;

    public DeviceDataMessage(DeviceMessage message, DeviceStateDTO state) {
        this.message = message;
        this.state = state;
    }
}
