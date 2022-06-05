package org.smarthome.laststate.models;

import org.smarthome.laststate.entities.DeviceStateDetails;

public class DeviceStateDetailsDTO {

    private final String id;
    private final String owner;

    private final Boolean active;

    private final Long lastConnection;

    private final Long lastDisconnection;

    private final Long lastUpdate;

    public DeviceStateDetailsDTO(DeviceStateDetails entity) {
        this.owner = entity.getOwner();
        this.id = entity.getId();
        this.active = entity.getActive();
        this.lastConnection = entity.getLastConnection();
        this.lastDisconnection = entity.getLastDisconnection();
        this.lastUpdate = entity.getLastUpdate();
    }


    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
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
}
