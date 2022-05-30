package org.smarthome.climate;

import org.smarthome.sdk.hub.device.*;
import org.smarthome.sdk.hub.device.constraints.IntegerNumberConstraint;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceType;

import java.util.Random;
import java.util.Timer;

public class PlainThermometer extends Device {

    private final ImitationPattern pattern;

    private final WritableDeviceProperty<Float> sensorTemperature;
    private final IntegerNumberConstraint sensorValueBorders;
    private final Random random = new Random();
    private final Timer timer = new Timer();

    private final float sensitivity;
    private float lastSentValue;

    @SuppressWarnings("unchecked cast")
    public PlainThermometer(
            String id,
            TemperatureUnit unit,
            int min,
            int max,
            double sensitivity,
            DeviceCallback callback,
            ImitationPattern pattern) {

        super(
                id,
                DeviceType.SENSOR,
                "plain-thermometer",
                new DeviceComponent[]{new DeviceComponent(
                        id+"-child1",
                        "plain-thermometer",
                        "temperature",
                        new WritableDeviceProperty<Float>(
                                "temperature",
                                unit.name,
                                "current temperature",
                                new IntegerNumberConstraint(min, max)
                        ),
                        new ConstantDeviceProperty[]{
                                new ConstantDeviceProperty<>(
                                        "deltaT",
                                        unit.name,
                                        "sensor sensitivity",
                                        sensitivity
                                )
                        },
                        null)
                },
                callback);

        this.sensitivity = (float)sensitivity;
        this.sensorValueBorders = (IntegerNumberConstraint) components[0].getMainProperty().getConstraint();
        this.sensorTemperature = (WritableDeviceProperty<Float>) components[0].getMainProperty();
        this.pattern = pattern;

        lastSentValue = unit == TemperatureUnit.celsius ? 17 + random.nextFloat() * 7 : 63 + random.nextFloat() * 10;
        sensorTemperature.setValue(lastSentValue);

        timer.schedule(new DataImitationTask(timer, this::generateAndSendData, pattern), pattern.newTime());
    }


    private void generateAndSendData(){

        float temp = sensorTemperature.getValue();
        float max = temp + pattern.getMaxDifference()/2;
        if(max > sensorValueBorders.getMax()){
            max = sensorValueBorders.getMax();
        }

        float min = temp - pattern.getMaxDifference()/2;
        if(max < sensorValueBorders.getMin()){
            max = sensorValueBorders.getMin();
        }

        float res = min + random.nextFloat() * (max - min);
        sensorTemperature.setValue(res);

        if(Math.abs(res - temp) > sensitivity){
            callback.send(id, components[0].getId(), sensorTemperature.getName(), Float.toString(res), null);
            lastSentValue = res;
        }
    }

    @Override
    public void execute(Command command) {
        callback.send(id, null, null, null, "No writable properties");
    }

    public void stop() {
        timer.cancel();
    }

}
