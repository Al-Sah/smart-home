package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.Command;

public class JsonCommand {

    private final String hub;
    private final String device;
    private final String task;
    private final long expiration;


    @JsonCreator
    public JsonCommand(
            @JsonProperty("hub") String hub,
            @JsonProperty("device") String device,
            @JsonProperty("task") String task,
            @JsonProperty("expire") long expiration) {
        this.hub = hub;
        this.device = device;
        this.task = task;
        this.expiration = expiration;
    }

    public JsonCommand(Command data) {
        this.hub = data.getHub();
        this.device = data.getDevice();
        this.task = data.getTask();
        this.expiration = data.getExpiration();
    }


    public String getHub() {
        return hub;
    }

    public String getDevice() {
        return device;
    }

    public String getTask() {
        return task;
    }

    public long getExpiration() {
        return expiration;
    }

}
