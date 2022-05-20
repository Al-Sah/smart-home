package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class JsonHubMessage {

    private final String hub;
    private final String action;
    private final List<JsonDeviceData> messages; //TODO replace with JsonDeviceData
    private final String data;


    @JsonCreator
    public JsonHubMessage(
            @JsonProperty("hub") String hub,
            @JsonProperty("action") String action,
            @JsonProperty("messages") List<JsonDeviceData> messages,
            @JsonProperty("data") String data){

        this.hub = hub;
        this.action = action;
        this.messages = messages;
        this.data = data;
    }

    public String getHub() {
        return hub;
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
