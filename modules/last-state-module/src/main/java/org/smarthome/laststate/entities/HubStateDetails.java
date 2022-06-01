package org.smarthome.laststate.entities;

public class HubStateDetails {

    private String id;

    private Boolean active;

    // ts
    private Long lastConnection;

    // ts
    private Long lastDisconnection;

    // ts (last any info: data or error)
    private Long lastUpdate;

    private String lastMessage;


    public HubStateDetails(String id, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate, String lastMessage) {
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public Long getLastDisconnection() {
        return lastDisconnection;
    }

    public void setLastDisconnection(Long lastDisconnection) {
        this.lastDisconnection = lastDisconnection;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
