package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.sdk.models.HubProperties;

@Getter
public class HubConnected {

    private final HubProperties properties;
    private final HubStateDTO state;

    public HubConnected(HubProperties properties, HubStateDTO state) {
        this.state = state;
        this.properties = properties;
    }
}
