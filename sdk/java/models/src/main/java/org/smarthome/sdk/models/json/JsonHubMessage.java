package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.DeviceData;

import java.util.List;

public class JsonHubMessage {

    private final String action;
    private final List<JsonDeviceData> messages;
    private final String data;


    @JsonCreator
    public JsonHubMessage(@JsonProperty("action") String action, @JsonProperty("messages") List<JsonDeviceData> messages, @JsonProperty("data") String data){
        this.action = action;
        this.messages = messages;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public List<JsonDeviceData> getMessages() {
        return messages;
    }

    public String getData() {
        return data;
    }

}
