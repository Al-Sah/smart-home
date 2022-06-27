package org.smarthome.laststate.models;

import lombok.Value;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;

@Value
public class FullDeviceDescription {

    DeviceMetadata metadata;
    DeviceStateDTO state;
    DeviceMessage lastError;
}
