package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.HubShutdownDetails;

import java.util.List;

/**
 * {@code HubDisconnectedMessage} is a DTO
 * that contains information about hub shutdown and current state of hub.
 *
 * @author Al-Sah
 * @see org.smarthome.sdk.models.HubShutdownDetails
 * @see org.smarthome.laststate.models.HubStateDTO
 * @see org.smarthome.laststate.models.DeviceStateDTO
 */
@Value public class HubDisconnectedMessage {

    @NonNull HubShutdownDetails shutdownDetails;
    @NonNull HubStateDTO hubState;

    /**
     * If the hub shuts down, but some devices are still active they will be marked as inactive and passed here.
     *
     * In ideal situation this field will be null
     */
    List<DeviceStateDTO> devices;
}
