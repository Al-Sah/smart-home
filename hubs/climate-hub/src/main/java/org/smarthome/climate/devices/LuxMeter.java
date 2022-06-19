package org.smarthome.climate.devices;

import org.smarthome.climate.DataImitationTask;
import org.smarthome.climate.ImitationPattern;
import org.smarthome.sdk.hub.device.ConstantDeviceProperty;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.device.constraints.FloatingPointConstraint;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceType;

import java.util.Random;
import java.util.Timer;

public class LuxMeter extends Device {

    private final WritableDeviceProperty<Float> tempProperty;
    private final WritableDeviceProperty<Integer> illuminationProperty;
    private final ConstantDeviceProperty<Integer> illuminationSensitivity;
    private final float tempSensitivity;

    private final Timer timer = new Timer();
    private final Random random = new Random();


    @SuppressWarnings("unchecked cast")
    public LuxMeter(String id, ImitationPattern pattern, DeviceCallback callback) {
        super(
                id,
                DeviceType.SENSOR,
                "WINTACT digital light meter",
                new DeviceComponent[]{
                        new DeviceComponent(id+"-child1", "lux-meter", "illumination",
                                new WritableDeviceProperty<Integer>(
                                        "illumination", "Lux", "current illumination",
                                        new IntegerNumberConstraint(0, 200000)
                        ),
                        new ConstantDeviceProperty[]{
                                new ConstantDeviceProperty<>(
                                        "sensitivity", TemperatureUnit.celsius.name, "illumination sensitivity", 1),
                                new ConstantDeviceProperty<>("deviation1",
                                        "%", "in the range up to 10000 Lux", 3),
                                new ConstantDeviceProperty<>("deviation2",
                                        "%", "in the range of more than 10000 Lux", 4)
                        }, null),
                        new DeviceComponent(
                                id+"-child2", "plain-thermometer", "temperature",
                                new WritableDeviceProperty<Float>(
                                        "temperature", TemperatureUnit.celsius.name, "current temperature",
                                        new FloatingPointConstraint(-9.9, 49.9)
                                ),
                                new ConstantDeviceProperty[]{
                                        new ConstantDeviceProperty<>("deltaT", TemperatureUnit.celsius.name, "sensor sensitivity", 0.1F)
                                }, null)
                },
                callback);


        this.tempSensitivity = 0.1F;
        this.illuminationProperty = (WritableDeviceProperty<Integer>) components[0].getMainProperty();
        this.illuminationSensitivity = (ConstantDeviceProperty<Integer>) components[0].getConstProperties()[0];
        this.tempProperty = (WritableDeviceProperty<Float>) components[1].getMainProperty();

        tempProperty.setValue(17 + random.nextFloat() * 7); // Setting up initial temperature value
        illuminationProperty.setValue(400 + random.nextInt(200));  // Setting up initial illumination value

        timer.schedule(new DataImitationTask(timer, this::generateAndSendData, pattern), pattern.newTime());
    }

    public void generateAndSendData(){
        if(random.nextBoolean()){
            var current = tempProperty.getValue();
            var newValue = random.nextBoolean() ? current - tempSensitivity : current + tempSensitivity;
            tempProperty.setValue(newValue);
            callback.send(id, components[1].getId(), tempProperty.getName(), Double.toString(newValue), null);
        }

        var current = illuminationProperty.getValue();
        var newValue = random.nextBoolean() ?
                current - illuminationSensitivity.getValue() :
                current + illuminationSensitivity.getValue();
        illuminationProperty.setValue(newValue);
        callback.send(id, components[0].getId(), illuminationProperty.getName(), Double.toString(newValue), null);
    }

    @Override
    public void execute(Command command) {
        callback.send(id, null, null, null, "No writable properties");
    }

    @Override
    public void stop() {
        timer.cancel();
    }
}
