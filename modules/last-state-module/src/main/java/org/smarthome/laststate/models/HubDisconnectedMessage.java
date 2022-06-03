package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.entities.HubStateDetails;
import org.smarthome.sdk.models.HubShutdownDetails;

import java.util.List;

public class HubDisconnectedMessage {

    private final HubShutdownDetails shutdownDetails;

    private final HubStateDetailsDTO stateDetails;

    private final List<DeviceStateDetails> devices;

    public HubDisconnectedMessage(HubShutdownDetails shutdownDetails, HubStateDetailsDTO stateDetails, List<DeviceStateDetails> devices) {
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

    public List<DeviceStateDetails> getDevices() {
        return devices;
    }
}
