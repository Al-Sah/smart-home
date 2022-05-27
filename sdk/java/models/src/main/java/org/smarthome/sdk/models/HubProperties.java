package org.smarthome.sdk.models;

public class HubProperties {

    private final String name;

    private final Integer period;

    private final String unit;

    public HubProperties(String name, Integer period, String unit) {
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
