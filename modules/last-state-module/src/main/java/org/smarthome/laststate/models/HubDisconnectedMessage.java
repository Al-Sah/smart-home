package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.HubShutdownDetails;

import java.util.List;

@Value public class HubDisconnectedMessage {

    @NonNull HubShutdownDetails shutdownDetails;
    @NonNull HubStateDTO hubState;

    // can be null
    List<DeviceStateDTO> devices;
}
