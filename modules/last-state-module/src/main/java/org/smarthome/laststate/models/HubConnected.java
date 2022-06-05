package org.smarthome.laststate.models;

import org.smarthome.sdk.models.HubProperties;

public class HubConnected {

    private final HubStateDetailsDTO hubState;

    private final HubProperties properties;

    public HubConnected(HubStateDetailsDTO hubState, HubProperties properties) {
        this.hubState = hubState;
        this.properties = properties;
    }
}
