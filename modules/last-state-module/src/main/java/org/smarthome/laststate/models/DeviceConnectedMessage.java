package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.DeviceMetadata;

@Getter
public class DeviceConnectedMessage {

    private final DeviceMetadata metadata;
    private final DeviceStateDTO state;

    public DeviceConnectedMessage(DeviceMetadata metadata, DeviceStateDTO state) {
        this.metadata = metadata;
        this.state = state;
    }
}
