package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import java.util.List;


@Value public class HubLostMessage {

    @NonNull HubStateDTO hubState;

    /**
     * List of devices which were associated with hub now marked as inactive. <br>
     * Lost (disconnected) hubs cannot have active devices
     */
    List<DeviceStateDTO> devices;
}
