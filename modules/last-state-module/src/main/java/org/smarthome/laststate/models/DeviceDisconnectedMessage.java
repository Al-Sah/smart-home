package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;

@Value public class DeviceDisconnectedMessage {

    @NonNull DeviceDisconnectionDetails details;
    @NonNull DeviceStateDTO state;
}
