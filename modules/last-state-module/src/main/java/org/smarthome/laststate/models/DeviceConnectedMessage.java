package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.sdk.models.DeviceMetadata;

public class DeviceConnectedMessage {

    private final DeviceMetadata metadata;
    private final DeviceStateDetails stateDetails;

    public DeviceConnectedMessage(DeviceMetadata metadata, DeviceStateDetails stateDetails) {
        this.metadata = metadata;
        this.stateDetails = stateDetails;
    }

    public DeviceMetadata getMetadata() {
        return metadata;
    }

    public DeviceStateDetails getStateDetails() {
        return stateDetails;
    }
}
