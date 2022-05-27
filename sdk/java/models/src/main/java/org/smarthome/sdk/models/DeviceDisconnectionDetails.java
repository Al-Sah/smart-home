package org.smarthome.sdk.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceDisconnectionDetails {

    @JsonProperty("id")
    private final String deviceId;

    @JsonProperty("reason")
    private final String reason;

    @JsonCreator
    public DeviceDisconnectionDetails(
            @JsonProperty("id") String deviceId,
            @JsonProperty("reason") String reason) {
        this.deviceId = deviceId;
        this.reason = reason;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public String getReason() {
        return reason;
    }
}
