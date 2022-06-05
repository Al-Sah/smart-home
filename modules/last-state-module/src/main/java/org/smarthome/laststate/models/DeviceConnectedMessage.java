package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.DeviceMetadata;

@Value public class DeviceConnectedMessage {

    @NonNull DeviceMetadata metadata;
    @NonNull DeviceStateDTO state;
}
