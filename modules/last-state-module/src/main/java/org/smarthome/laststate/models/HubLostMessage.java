package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.laststate.entities.HubState;

import java.util.List;

@Getter
public class HubLostMessage {

    private final HubState hubState;

    private final List<DeviceStateDTO> devices;


    public HubLostMessage(HubState hubState, List<DeviceStateDTO> devices) {
        this.hubState = hubState;
        this.devices = devices;
    }
}
