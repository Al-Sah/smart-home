package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.HubStateDetails;

public class HubStateDetailsDTO {

    private final String id;
    private final Boolean active;
    private final Long lastConnection;
    private final Long lastDisconnection;

    private final Long lastUpdate;

    private final String lastMessage;


    public HubStateDetailsDTO(HubStateDetails hubStateDetails) {
        this.id = hubStateDetails.getId();
        this.active = hubStateDetails.getActive();
        this.lastConnection = hubStateDetails.getLastConnection();
        this.lastDisconnection = hubStateDetails.getLastDisconnection();
        this.lastUpdate = hubStateDetails.getLastUpdate();
        this.lastMessage = hubStateDetails.getLastMessage();
    }


    public String getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getLastConnection() {
        return lastConnection;
    }

    public Long getLastDisconnection() {
        return lastDisconnection;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
