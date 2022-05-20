package org.smarthome.climate.metadata;

import org.smarthome.climate.SimpleSensor;

public class SimpleSensorMetadata extends SensorMetadata{

    private final String unit;

    public SimpleSensorMetadata(SimpleSensor<?> sensor) {
        super("elementary");
        this.unit = sensor.getDatatype();
    }

    public String getUnit() {
        return unit;
    }
}
