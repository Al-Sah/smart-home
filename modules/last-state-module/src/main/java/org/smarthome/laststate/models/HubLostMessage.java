package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.laststate.entities.HubState;

import java.util.List;

@Value
public class HubLostMessage {

    @NonNull HubState hubState;

    // can be null
    List<DeviceStateDTO> devices;
}
