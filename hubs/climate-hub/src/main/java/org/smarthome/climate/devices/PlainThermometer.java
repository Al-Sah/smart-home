package org.smarthome.climate.devices;

import org.smarthome.climate.DataImitationTask;
import org.smarthome.climate.ImitationPattern;
import org.smarthome.sdk.hub.device.ConstantDeviceProperty;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;

public class PlainThermometer extends ThermometerImitator {

    private final float sensitivity;

    public PlainThermometer(
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
                        new ConstantDeviceProperty[]{
                                new ConstantDeviceProperty<>(
                                        "deltaT",
                                        settings.getUnit().name,
                                        "sensor sensitivity",
                                        settings.getInitialSensitivity()
                                )
                        },
                        null)
                },
                callback);

        this.sensitivity = settings.getInitialSensitivity();

        timer.schedule(new DataImitationTask(timer, () ->{
            var current = temperatureProperty.getValue();
            var newValue = random.nextBoolean() ? current - sensitivity : current + sensitivity;
            temperatureProperty.setValue(newValue);
            callback.send(id, components[0].getId(), temperatureProperty.getName(), Double.toString(newValue), null);
        }, pattern), pattern.newTime());
    }

    @Override
    public void execute(Command command) {
        callback.send(id, null, null, null, "No writable properties");
    }
}
