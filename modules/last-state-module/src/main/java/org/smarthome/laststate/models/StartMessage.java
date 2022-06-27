package org.smarthome.laststate.models;

import lombok.Value;

import java.util.List;

/**
 * {@code HubLostMessage} is a DTO
 * that contains all information about devices and hubs.
 *
 * @author Al-Sah
 * @see org.smarthome.laststate.models.HubStateDTO
 * @see org.smarthome.laststate.models.DeviceStateDTO
 * @see org.smarthome.sdk.models.DeviceMetadata
 * @see org.smarthome.sdk.models.DeviceMessage
 */
@Value public class StartMessage {

    /**
     * All registered devices
     */
    List<FullDeviceDescription> devices;
    List<HubStateDTO> hubsState;
}
