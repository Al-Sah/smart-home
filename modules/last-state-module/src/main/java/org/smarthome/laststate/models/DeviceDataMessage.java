package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.DeviceMessage;

@Value public class DeviceDataMessage {

    @NonNull DeviceMessage message;
    @NonNull DeviceStateDTO state;
}
