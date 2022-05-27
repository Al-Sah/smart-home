package org.smarthome.sdk.models;

public class HubShutdownDetails {

    private final String reason;

    private final String additionalInfo;


    public HubShutdownDetails(String reason, String additionalInfo) {
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
