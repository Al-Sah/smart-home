package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.HubStateDetails;

import java.util.List;

public class HubLostMessage {

    private final HubStateDetails hubStateDetails;

    private final List<DeviceStateDetailsDTO> devices;


    public HubLostMessage(HubStateDetails hubStateDetails, List<DeviceStateDetailsDTO> devices) {
        this.hubStateDetails = hubStateDetails;
        this.devices = devices;
    }


    public HubStateDetails getHubStateDetails() {
        return hubStateDetails;
    }

    public List<DeviceStateDetailsDTO> getDevices() {
        return devices;
    }
}
