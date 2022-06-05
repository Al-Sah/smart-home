package org.smarthome.laststate.models;

import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;

import java.util.List;


public class ClientStartMessage {

    private final List<DeviceMetadata> devices;
    private final List<DeviceMessage> errors;
    private final List<DeviceStateDetailsDTO> devicesState;
    private final List<HubStateDetailsDTO> hubsState;

    public ClientStartMessage(
            List<DeviceMetadata> devices,
            List<DeviceMessage> errors,
            List<DeviceStateDetailsDTO> devicesState,
            List<HubStateDetailsDTO> hubsState) {
        this.devices = devices;
        this.errors = errors;
        this.devicesState = devicesState;
        this.hubsState = hubsState;
    }


    public List<DeviceMetadata> getDevices() {
        return devices;
    }

    public List<DeviceMessage> getErrors() {
        return errors;
    }

    public List<DeviceStateDetailsDTO> getDevicesState() {
        return devicesState;
    }

    public List<HubStateDetailsDTO> getHubsState() {
        return hubsState;
    }
}
