package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.sdk.models.DeviceMessage;

public class DeviceDataMessage {

    private final DeviceMessage data;
    private final DeviceStateDetails stateDetails;

    public DeviceDataMessage(DeviceMessage data, DeviceStateDetails stateDetails) {
        this.data = data;
        this.stateDetails = stateDetails;
    }


    public DeviceMessage getData() {
        return data;
    }

    public DeviceStateDetails getStateDetails() {
        return stateDetails;
    }
}
