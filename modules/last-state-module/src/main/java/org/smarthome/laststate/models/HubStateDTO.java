package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.laststate.entities.HubState;

@Value public class HubStateDTO {

    @NonNull String id;
    @NonNull Boolean active;
    @NonNull String lastMessage;

    Long lastConnection;
    Long lastDisconnection;
    Long lastUpdate;

    public HubStateDTO(HubState hubState) {
        this.id = hubState.getId();
        this.active = hubState.getActive();
        this.lastConnection = hubState.getLastConnection();
        this.lastDisconnection = hubState.getLastDisconnection();
        this.lastUpdate = hubState.getLastUpdate();
        this.lastMessage = hubState.getLastMessage();
    }
}
