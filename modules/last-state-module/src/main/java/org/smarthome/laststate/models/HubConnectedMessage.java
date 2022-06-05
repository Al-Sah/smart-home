package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.HubProperties;

/**
 * {@code HubConnectedMessage} is a DTO
 * that contains hub properties and current state.
 *
 * @author Al-Sah
 * @see org.smarthome.sdk.models.HubProperties
 * @see org.smarthome.laststate.models.HubStateDTO
 */
@Value public class HubConnectedMessage {

    @NonNull HubProperties properties;
    @NonNull HubStateDTO state;
}
