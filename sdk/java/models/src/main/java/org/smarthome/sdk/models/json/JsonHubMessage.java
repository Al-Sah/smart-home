package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.DeviceData;

import java.util.List;

public class JsonHubMessage {

    private final String action;
    private final List<DeviceData> messages;
    private final String data;


    @JsonCreator
    public JsonHubMessage(@JsonProperty("action") String action, @JsonProperty("messages") List<DeviceData> messages){
        this.action = action;
        this.messages = messages;
        this.data = null;
    }

    @JsonCreator
    public JsonHubMessage(@JsonProperty("action") String action, @JsonProperty("data") String data){
        this.action = action;
        this.data = data;
        this.messages = null;
    }

    @JsonCreator
    public JsonHubMessage(@JsonProperty("action") String action){
        this.action = action;
        this.messages = null;
        this.data = null;
    }

    public String getAction() {
        return action;
    }

    public List<DeviceData> getMessages() {
        return messages;
    }

    public String getData() {
        return data;
    }

}
