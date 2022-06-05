package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.sdk.models.HubProperties;

@Value public class HubConnected {

    @NonNull HubProperties properties;
    @NonNull HubStateDTO state;
}
