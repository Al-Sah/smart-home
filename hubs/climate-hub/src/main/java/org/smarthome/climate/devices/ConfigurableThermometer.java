package org.smarthome.climate.devices;

import org.smarthome.climate.DataImitationTask;
import org.smarthome.climate.ImitationPattern;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.device.constraints.FloatingPointConstraint;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;

import java.util.Objects;

public class ConfigurableThermometer extends ThermometerImitator {

    private final WritableDeviceProperty<Float> sensitivity;

    @SuppressWarnings("unchecked cast")
    public ConfigurableThermometer(
            String id, ThermometerSettings settings, ImitationPattern pattern, DeviceCallback callback) {
        super(
                id,
                settings.getName(),
                settings.getUnit(),
                pattern,
                new DeviceComponent[]{new DeviceComponent(
                        id+"-child1",
                        "plain-thermometer",
                        "temperature",
                        new WritableDeviceProperty<Float>(
                                "temperature",
                                settings.getUnit().name,
                                "current temperature",
                                new IntegerNumberConstraint(settings.getMinTemperature(), settings.getMaxTemperature())
                        ),
                        null,
                        new WritableDeviceProperty[]{
                                new WritableDeviceProperty<Float>(
                                        "deltaT",
                                        settings.getUnit().name,
                                        "sensor sensitivity",
                                        new FloatingPointConstraint(0.1, 2.0)
                                )
                        })
                },
                callback);


        this.sensitivity = (WritableDeviceProperty<Float>)components[0].getWritableProperties()[0];
        this.sensitivity.setValue(settings.getInitialSensitivity());


        timer.schedule(new DataImitationTask(timer, () ->{
            var current = temperatureProperty.getValue();
            var newValue = random.nextBoolean() ? current - sensitivity.getValue() : current + sensitivity.getValue();
            temperatureProperty.setValue(newValue);
            callback.send(id, components[0].getId(), temperatureProperty.getName(), Double.toString(newValue), null);
        }, pattern), pattern.newTime());
    }


    @Override
    public void execute(Command command) {
        if(!Objects.equals(command.getProperty(), sensitivity.getName())){
            callback.send(id, components[0].getId(), null, null, "Property not found");
            return;
        }

        try {
            this.sensitivity.setValue(Double.valueOf(command.getProperty()).floatValue());
            callback.send(id, components[0].getId(), sensitivity.getName(), null, sensitivity.getValue().toString());
        } catch (NumberFormatException exception){
            callback.send(id, components[0].getId(), sensitivity.getName(), null, "invalid property value (parsing fail)");
        }
    }


}
