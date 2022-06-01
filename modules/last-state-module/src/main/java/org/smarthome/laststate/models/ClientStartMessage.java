package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.entities.HubStateDetails;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;
import java.util.List;


public class ClientStartMessage {

    private final List<DeviceMetadata> devices;
    private final List<DeviceMessage> errors;
    private final List<DeviceStateDetails> devicesState;
    private final List<HubStateDetails> hubsState;

    public ClientStartMessage(List<DeviceMetadata> devices, List<DeviceMessage> errors, List<DeviceStateDetails> devicesState, List<HubStateDetails> hubsState) {
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

    public List<DeviceStateDetails> getDevicesState() {
        return devicesState;
    }

    public List<HubStateDetails> getHubsState() {
        return hubsState;
    }
}
