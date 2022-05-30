package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HubShutdownDetails {

    private final String reason;

    @JsonProperty("details")
    private final String additionalInfo;

    @JsonCreator
    public HubShutdownDetails(
            @JsonProperty("reason") String reason,
            @JsonProperty("details") String additionalInfo) {
        this.reason = reason;
        this.additionalInfo = additionalInfo;
    }


    public String getReason() {
        return reason;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
