package org.smarthome.laststate.models;

import org.smarthome.sdk.models.HubShutdownDetails;

import java.util.List;

public class HubDisconnectedMessage {

    private final HubShutdownDetails shutdownDetails;

    private final HubStateDetailsDTO stateDetails;

    private final List<DeviceStateDetailsDTO> devices;

    public HubDisconnectedMessage(
            HubShutdownDetails shutdownDetails,
            HubStateDetailsDTO stateDetails,
            List<DeviceStateDetailsDTO> devices) {
        this.shutdownDetails = shutdownDetails;
        this.stateDetails = stateDetails;
        this.devices = devices;
    }


    public HubShutdownDetails getShutdownDetails() {
        return shutdownDetails;
    }

    public HubStateDetailsDTO getStateDetails() {
        return stateDetails;
    }

    public List<DeviceStateDetailsDTO> getDevices() {
        return devices;
    }
}
