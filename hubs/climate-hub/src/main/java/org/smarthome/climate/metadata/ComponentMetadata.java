package org.smarthome.climate.metadata;

public class ComponentMetadata {

    private final String type;
    private final String unit;

    public ComponentMetadata(String type, String unit) {
        this.type = type;
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }
}
