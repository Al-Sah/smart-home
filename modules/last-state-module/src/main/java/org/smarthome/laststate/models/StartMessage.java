package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;

import java.util.List;

@Getter
public class StartMessage {

    private final List<DeviceMetadata> devices;
    private final List<DeviceMessage> errors;
    private final List<DeviceStateDTO> devicesState;
    private final List<HubStateDTO> hubsState;

    public StartMessage(
            List<DeviceMetadata> devices,
            List<DeviceMessage> errors,
            List<DeviceStateDTO> devicesState,
            List<HubStateDTO> hubsState) {
        this.devices = devices;
        this.errors = errors;
        this.devicesState = devicesState;
        this.hubsState = hubsState;
    }
}
