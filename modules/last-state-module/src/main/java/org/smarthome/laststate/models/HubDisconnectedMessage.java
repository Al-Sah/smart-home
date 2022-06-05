package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.HubShutdownDetails;

import java.util.List;

@Getter
public class HubDisconnectedMessage {

    private final HubShutdownDetails shutdownDetails;

    private final HubStateDTO hubState;

    private final List<DeviceStateDTO> devices;

    public HubDisconnectedMessage(
            HubShutdownDetails shutdownDetails,
            HubStateDTO hubState,
            List<DeviceStateDTO> devices) {
        this.shutdownDetails = shutdownDetails;
        this.hubState = hubState;
        this.devices = devices;
    }
}
