import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.producer.DTOFactory;
import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceType;

public class DeviceTests {

    private static class TestDevice extends Device{

        public TestDevice(String id, DeviceType type, String name, DeviceComponent[] components, DeviceCallback callback) {
            super(id, type, name, components, callback);
        }

        @Override
        public void execute(Command command) {}
        @Override
        public void stop() {}
    }

    @Test
    public void testIncorrectDeviceCreation(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> new TestDevice(
                "test-id", DeviceType.SENSOR, "test-name", null, null)
        );
    }

    @Test
    public void testCorrectDeviceComponentCreation(){
        Assertions.assertDoesNotThrow(()-> new DeviceComponent(
                "test-id-child1", "test", "test",
                new WritableDeviceProperty<>("test", "test", null, null), null, null)
        );
    }

    @Test
    public void testCorrectDeviceCreation(){
        var c = new DeviceComponent(
                "test-id-child1", "test", "test",
                new WritableDeviceProperty<>("test", "test", null, null),
                null, null
        );
        Assertions.assertDoesNotThrow(()-> new TestDevice(
                "test-id", DeviceType.SENSOR, "test-name", new DeviceComponent[]{c}, null)
        );
    }

    @Test
    public void testFactory(){
        var device = new TestDevice("test-id", DeviceType.SENSOR, "test-name", new DeviceComponent[]{
                new DeviceComponent(
                        "test-id-child1", "test", "test",
                        new WritableDeviceProperty<>("test", "test", null, null),
                        null, null)
        }, null);
        Assertions.assertThrows(NullPointerException.class, ()-> DTOFactory.getDeviceMetadata(device));
    }
}
