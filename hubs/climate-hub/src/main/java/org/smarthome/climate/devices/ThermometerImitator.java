package org.smarthome.climate.devices;

import org.smarthome.climate.ImitationPattern;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.DeviceType;

import java.util.Random;
import java.util.Timer;

public abstract class ThermometerImitator extends Device {

    protected final ImitationPattern pattern;
    protected final WritableDeviceProperty<Float> temperatureProperty;
    protected final IntegerNumberConstraint temperatureLimits;

    
    protected final Timer timer = new Timer();
    protected final Random random = new Random();

    @SuppressWarnings("unchecked cast")
    public ThermometerImitator(
            String id,
            String name,
            TemperatureUnit unit,
            ImitationPattern pattern,
            DeviceComponent[] components,
            DeviceCallback callback) {
        super(id, DeviceType.SENSOR, name, components, callback);
        
        this.pattern = pattern;
        this.temperatureLimits = (IntegerNumberConstraint) components[0].getMainProperty().getConstraint();
        this.temperatureProperty = (WritableDeviceProperty<Float>) components[0].getMainProperty();

        temperatureProperty.setValue(unit == TemperatureUnit.celsius ?
                17 + random.nextFloat() * 7 :
                63 + random.nextFloat() * 10
        ); // Setting up initial temperature value
    }

    public void stop() {
        timer.cancel();
    }
}
