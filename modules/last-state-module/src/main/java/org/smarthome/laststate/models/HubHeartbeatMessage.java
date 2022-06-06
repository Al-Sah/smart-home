package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

/**
 * {@code HubHeartbeatMessage} is a DTO
 * that contains current state of hub and list of devices which belong to this hub.
 *
 * This model is used in 2 different situations:
 * <ul>
 *     <li>Heartbeat message received</li>
 *     <li>Previously lost hub reconnected</li>
 * </ul>
 * @author Al-Sah
 * @see org.smarthome.laststate.models.HubStateDTO
 * @see org.smarthome.laststate.models.DeviceStateDTO
 */
@Value public class HubHeartbeatMessage {

    @NonNull HubStateDTO hub;
    /**
     * List of devices which are associated with the specified hub
     */
    List<DeviceStateDTO> devices;
}
