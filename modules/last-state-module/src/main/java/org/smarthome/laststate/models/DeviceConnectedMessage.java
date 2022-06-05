package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.DeviceMetadata;

/**
 * {@code DeviceConnectedMessage} is a DTO
 * that contains device metadata and current state.
 *
 * @author Al-Sah
 * @see org.smarthome.sdk.models.DeviceMetadata
 * @see org.smarthome.laststate.models.DeviceStateDTO
 */
@Value public class DeviceConnectedMessage {

    @NonNull DeviceMetadata metadata;
    @NonNull DeviceStateDTO state;
}
