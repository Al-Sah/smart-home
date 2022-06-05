package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HubProperties {

    private final String id;
    private final String name;

    private final Integer period;

    private final String unit;

    @JsonCreator
    public HubProperties(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("period")Integer period,
            @JsonProperty("unit") String unit) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getUnit() {
        return unit;
    }
}
