package org.smarthome.laststate.models;

import org.smarthome.sdk.models.DeviceMessage;

public class DeviceDataMessage {

    private final DeviceMessage data;
    private final DeviceStateDetailsDTO stateDetails;

    public DeviceDataMessage(DeviceMessage data, DeviceStateDetailsDTO stateDetails) {
        this.data = data;
        this.stateDetails = stateDetails;
    }


    public DeviceMessage getData() {
        return data;
    }

    public DeviceStateDetailsDTO getStateDetails() {
        return stateDetails;
    }
}
