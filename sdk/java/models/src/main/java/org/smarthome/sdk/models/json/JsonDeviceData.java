package org.smarthome.sdk.models.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;

/**
 * Model {@code DeviceData} is used to describe device message.
 * When actuator or sensor produces some data it must be described by {@code DeviceData}
 *
 * @see HubMessage
 * @see MessageAction
 * @see DeviceData
 * @author  Al-Sah
 */
public class JsonDeviceData {

    private final String id;
    private final String type;
    private final String name;
    private final String data;

    @JsonCreator
    public JsonDeviceData(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("name") String name,
            @JsonProperty("data") String data) {

        this.id = id;
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public JsonDeviceData(DeviceData data) {
        this.id = data.getId();
        this.type = data.getType();
        this.name = data.getName();
        this.data = data.getData();
    }


    public String getType() {
        return type;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getData() {
        return data;
    }
}
