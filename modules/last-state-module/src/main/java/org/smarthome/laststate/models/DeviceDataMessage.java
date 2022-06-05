package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.DeviceMessage;

/**
 * {@code DeviceDataMessage} is a DTO
 * that contains last device message and current state.
 *
 * @author Al-Sah
 * @see org.smarthome.sdk.models.DeviceMessage
 * @see org.smarthome.laststate.models.DeviceStateDTO
 */
@Value public class DeviceDataMessage {

    @NonNull DeviceMessage message;
    @NonNull DeviceStateDTO state;
}
