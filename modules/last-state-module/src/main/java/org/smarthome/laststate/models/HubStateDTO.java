package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.laststate.entities.HubState;

@Getter
public class HubStateDTO {

    private final String id;
    private final Boolean active;
    private final Long lastConnection;
    private final Long lastDisconnection;
    private final Long lastUpdate;
    private final String lastMessage;


    public HubStateDTO(HubState hubState) {
        this.id = hubState.getId();
        this.active = hubState.getActive();
        this.lastConnection = hubState.getLastConnection();
        this.lastDisconnection = hubState.getLastDisconnection();
        this.lastUpdate = hubState.getLastUpdate();
        this.lastMessage = hubState.getLastMessage();
    }
}
