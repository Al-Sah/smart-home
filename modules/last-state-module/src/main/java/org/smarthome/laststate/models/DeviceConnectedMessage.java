package org.smarthome.laststate.models;

import org.smarthome.sdk.models.DeviceMetadata;

public class DeviceConnectedMessage {

    private final DeviceMetadata metadata;
    private final DeviceStateDetailsDTO stateDetails;

    public DeviceConnectedMessage(DeviceMetadata metadata, DeviceStateDetailsDTO stateDetails) {
        this.metadata = metadata;
        this.stateDetails = stateDetails;
    }

    public DeviceMetadata getMetadata() {
        return metadata;
    }

    public DeviceStateDetailsDTO getStateDetails() {
        return stateDetails;
    }
}
