package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Model {@code HubMessage} is used to describe messages hub can produce according to the {@code MessageAction}.
 *
 * @see MessageAction
 * @author  Al-Sah
 */
public class HubMessage<T> {

    private final String hub;
    private final String action;
    private final T data;

    @JsonCreator
    public HubMessage(
            @JsonProperty("hub") String hub,
            @JsonProperty("action") String action,
            @JsonProperty("data") T data){
        this.hub = hub;
        this.action = action;
        this.data = data;
    }

    public String getHub() {
        return hub;
    }
    public String getAction() {
        return action;
    }
    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("HubMessage{action=='%s'",getAction()));
        if(data != null){
            sb.append(String.format(", data='%s'", data));
        }
        sb.append('}');
        return sb.toString();
    }

}