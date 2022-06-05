package org.smarthome.laststate.models;

import lombok.Getter;
import org.smarthome.laststate.entities.DeviceState;

@Getter
public class DeviceStateDTO {

    private final String id;
    private final String owner;

    private final Boolean active;

    private final Long lastConnection;

    private final Long lastDisconnection;

    private final Long lastUpdate;

    public DeviceStateDTO(DeviceState entity) {
        this.owner = entity.getOwner();
        this.id = entity.getId();
        this.active = entity.getActive();
        this.lastConnection = entity.getLastConnection();
        this.lastDisconnection = entity.getLastDisconnection();
        this.lastUpdate = entity.getLastUpdate();
    }
}
