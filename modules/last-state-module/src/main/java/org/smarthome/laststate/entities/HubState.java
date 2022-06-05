package org.smarthome.laststate.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="hubs")
@Data
public class HubState {

    @Id
    private String id;

    private Boolean active;

    // ts
    private Long lastConnection;

    // ts
    private Long lastDisconnection;

    // ts (last any info: data or error)
    private Long lastUpdate;

    private String lastMessage;

    @PersistenceCreator
    public HubState(String id, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate, String lastMessage) {
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
        this.lastMessage = lastMessage;
    }

    public HubState(String id) {
        this.id = id;
        this.active = null;
        this.lastConnection = null;
        this.lastDisconnection = null;
        this.lastUpdate = null;
        this.lastMessage = null;
    }
}
