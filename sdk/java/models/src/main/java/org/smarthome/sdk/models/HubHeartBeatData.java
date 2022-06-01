package org.smarthome.sdk.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HubHeartBeatData {

    public final String[] active;
    public final String[] inactive;

    @JsonCreator
    public HubHeartBeatData(
            @JsonProperty("active") String[] active,
            @JsonProperty("inactive") String[] inactive) {
        this.active = active;
        this.inactive = inactive;
    }


    public String[] getActive() {
        return active;
    }

    public String[] getInactive() {
        return inactive;
    }
}
