package org.smarthome.sdk.hub.device;

/**
 * @author Al-Sah
 */
public class ConstantDeviceProperty<T> extends DevicePropertyBase{

    /**
     * Property value
     */
    private final T value;

    /**
     * @param name property name
     * @param unit unit of measure for a specific type of data
     * @param description additional description to the property
     * @param value constant property value
     */
    public ConstantDeviceProperty(String name, String unit, String description, T value) {
        super(name, unit, description);
        this.value = value;
    }


    public T getValue() {
        return value;
    }

}
