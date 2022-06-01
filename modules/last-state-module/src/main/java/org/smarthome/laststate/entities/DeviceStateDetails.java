package org.smarthome.laststate.entities;

public class DeviceStateDetails {

    // hub-id
    private String owner;

    private String id;

    private Boolean active;

    // ts
    private Long lastConnection;

    // ts
    private Long lastDisconnection;

    // ts (last any info: data or error)
    private Long lastUpdate;


    public DeviceStateDetails(String owner, String id, Boolean active, Long lastConnection, Long lastDisconnection, Long lastUpdate) {
        this.owner = owner;
        this.id = id;
        this.active = active;
        this.lastConnection = lastConnection;
        this.lastDisconnection = lastDisconnection;
        this.lastUpdate = lastUpdate;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
}
