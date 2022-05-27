package org.smarthome.sdk.hub.device;

/**
 * @author Al-Sah
 */
public class WritableDeviceProperty<D> extends DevicePropertyBase{

	/**
	 * Property constraint
	 */
	private final PropertyConstraint constraint;

	/**
	 * Property value
	 */
	private D value;


	/**
	 * @param name property name
	 * @param unit unit of measure for a specific type of data
	 * @param description additional description to the property
	 * @param constraint property constraint
	 */
	public WritableDeviceProperty(String name, String unit, String description, PropertyConstraint constraint) {
		super(name, unit, description);
		this.constraint = constraint;
	}


	public void setValue(D value) {
		this.value = value;
	}

	public D getValue() {
		return value;
	}

	public PropertyConstraint getConstraint() {
		return constraint;
	}


}