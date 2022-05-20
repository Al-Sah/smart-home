package org.smarthome.climate.metadata;

import org.smarthome.climate.SwitchableSensor;

public class SwitchableSensorMetadata extends SensorMetadata {

    private final String[] units;

    public SwitchableSensorMetadata(SwitchableSensor<?> sensor) {
        super("switchable");
        this.units = sensor.getDataTypes();
    }

    public String[] getUnits() {
        return units;
    }
}
