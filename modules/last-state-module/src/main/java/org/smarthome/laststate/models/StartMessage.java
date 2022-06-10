package org.smarthome.laststate.models;

import lombok.Value;
import org.smarthome.sdk.models.DeviceMessage;
import org.smarthome.sdk.models.DeviceMetadata;
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
    List<DeviceMetadata> devices;
    List<DeviceMessage> errors;
    List<DeviceStateDTO> devicesState;
    List<HubStateDTO> hubsState;
}