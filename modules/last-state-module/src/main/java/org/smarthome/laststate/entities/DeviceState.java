package org.smarthome.laststate.entities;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="devices")
@Data
public class DeviceState {

    // hub-id
    @Id
    private String id;
    private String owner;
    private Boolean active;

    // ts
    private Long lastConnection;

    // ts
    private Long lastDisconnection;

    // ts (last any info: data or error)
    private Long lastUpdate;

    @PersistenceCreator
    public DeviceState(String id, String owner, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate) {
        this.owner = owner;
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
    }

    public DeviceState(String id, String owner) {
        this.id = id;
        this.owner = owner;
        this.active = null;
        this.lastConnection = null;
        this.lastDisconnection = null;
        this.lastUpdate = null;
    }
}
