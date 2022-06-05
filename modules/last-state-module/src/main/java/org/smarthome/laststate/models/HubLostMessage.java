package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import java.util.List;

/**
 * {@code HubLostMessage} is a DTO
 * that contains current state of hub and list of devices which belong to this hub.
 *
 * This model is used to describe a situation when the heartbeat message
 * of a specified hub was not received in expected time.
 * @author Al-Sah
 * @see org.smarthome.laststate.models.HubStateDTO
 * @see org.smarthome.laststate.models.DeviceStateDTO
 */
@Value public class HubLostMessage {

    @NonNull HubStateDTO hubState;

    /**
     * List of devices which were associated with hub now marked as inactive. <br>
     * Lost (disconnected) hubs cannot have active devices
     */
    List<DeviceStateDTO> devices;
}
