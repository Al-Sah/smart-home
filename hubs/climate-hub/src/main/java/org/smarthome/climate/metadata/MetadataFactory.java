package org.smarthome.climate.metadata;

import org.smarthome.climate.CombinedSensor;
import org.smarthome.climate.Sensor;
import org.smarthome.climate.SimpleSensor;
import org.smarthome.climate.SwitchableSensor;

public class MetadataFactory {

    public static SensorMetadata get(Sensor sensor){

        if(sensor instanceof SimpleSensor){
            return new SimpleSensorMetadata((SimpleSensor<?>) sensor);
        }
        if(sensor instanceof SwitchableSensor){
            return new SwitchableSensorMetadata((SwitchableSensor<?>) sensor);
        }

        if(sensor instanceof CombinedSensor){
            return new CombinedSensorMetadata((CombinedSensor) sensor);
        }

        throw new RuntimeException("Undefined sensor");
    }
}
