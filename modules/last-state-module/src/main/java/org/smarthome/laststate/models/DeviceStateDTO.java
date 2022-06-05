package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;
import org.smarthome.laststate.entities.DeviceState;

@Value public class DeviceStateDTO {

    @NonNull String id;
    @NonNull String owner;
    @NonNull Boolean active;

    Long lastConnection;
    Long lastDisconnection;
    Long lastUpdate;

    public DeviceStateDTO(DeviceState entity) {
        this.owner = entity.getOwner();
        this.id = entity.getId();
        this.active = entity.getActive();
        this.lastConnection = entity.getLastConnection();
        this.lastDisconnection = entity.getLastDisconnection();
        this.lastUpdate = entity.getLastUpdate();
    }
}
