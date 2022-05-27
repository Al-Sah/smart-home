package org.smarthome.sdk.hub.device;


public abstract class DevicePropertyBase {

    /**
     * Property name
     */
    private final String name;

    /**
     * Unit of measure for a specific type of data
     */
    private final String unit;

    /**
     * Additional description to the property
     */
    private final String description;

    protected DevicePropertyBase(String name, String unit, String description) {
        this.name = name;
        this.unit = unit;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

}
