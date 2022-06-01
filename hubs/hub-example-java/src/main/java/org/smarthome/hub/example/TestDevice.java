package org.smarthome.hub.example;

import org.smarthome.sdk.hub.device.ConstantDeviceProperty;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceType;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestDevice extends Device {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Random random = new Random();
    public TestDevice(String id, DeviceCallback callback) {
        super(
                id,
                DeviceType.SENSOR,
                "test-device",
                new DeviceComponent[]{new DeviceComponent(
                        id+"-child1",
                        "test-component1",
                        "bananas",
                        new WritableDeviceProperty<Integer>("bananas", "(._.)", "bananas count", null),
                        new ConstantDeviceProperty[]{
                                new ConstantDeviceProperty<>("test-ro-prop", "(._.)", "", "(._.)")
                        },
                        null)
                }, callback);

        var value = random.nextInt(100);
        ((WritableDeviceProperty<Integer>)components[0].getMainProperty()).setValue(value);

        scheduler.scheduleAtFixedRate(this::imitateWork, 5, 80, TimeUnit.SECONDS);
    }

    private void imitateWork(){
        var value = random.nextInt(100);
        ((WritableDeviceProperty<Integer>)components[0].getMainProperty()).setValue(value);
        callback.send(id, components[0].getId(), "bananas", Integer.toString(value), null);
    }

    @Override
    public void execute(Command command) {}

    @Override
    public void stop() {
        scheduler.shutdown();
    }
}
