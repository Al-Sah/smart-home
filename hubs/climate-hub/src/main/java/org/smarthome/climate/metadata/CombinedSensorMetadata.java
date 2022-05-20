package org.smarthome.climate.metadata;

import org.smarthome.climate.CombinedSensor;

import java.util.HashMap;

public class CombinedSensorMetadata extends SensorMetadata {


    private final HashMap<String, ComponentMetadata> components;

    public CombinedSensorMetadata(CombinedSensor sensor) {
        super("combined");
        components = new HashMap<>();

        for (var component: sensor.getComponents()) {
            components.put(component.getId(), new ComponentMetadata(component.getType(), component.getDatatype()));
        }
    }

    public HashMap<String, ComponentMetadata> getComponents() {
        return components;
    }


}
