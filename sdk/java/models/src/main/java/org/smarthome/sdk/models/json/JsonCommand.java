package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceData;

public class JsonCommand {

    private final String actuator;
    private final String task;
    private final long expiration;


    @JsonCreator
    public JsonCommand(
            @JsonProperty("id") String actuator,
            @JsonProperty("task") String task,
            @JsonProperty("expire") long expiration) {
        this.actuator = actuator;
        this.task = task;
        this.expiration = expiration;
    }

    public JsonCommand(Command data) {
        this.actuator = data.getActuator();
        this.task = data.getTask();
        this.expiration = data.getExpiration();
    }


    public String getActuator() {
        return actuator;
    }

    public String getTask() {
        return task;
    }

    public long getExpiration() {
        return expiration;
    }

}
