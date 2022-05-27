package org.smarthome.sdk.hub.device;


/**
 * @author Al-Sah
 */
public class DeviceComponent {

	/**
	 * Component id
	 */
	protected final String id;

	/**
	 * Component name
	 */
	protected final String name;

	/**
	 * Datatype that main property produce
	 */
	protected final String datatype;

	/**
	 * Data that this component sends
	 */
	protected final WritableDeviceProperty<?> mainProperty;

	/**
	 * Component properties that cannot be changed
	 */
	protected final ConstantDeviceProperty<?>[] constProperties;

	/**
	 * Component properties that can be changed by user
	 */
	protected final WritableDeviceProperty<?>[] writableProperties;


	public DeviceComponent(
			String id,
			String name,
			String datatype,
			WritableDeviceProperty<?> mainProperty,
			ConstantDeviceProperty<?>[] constProperties,
			WritableDeviceProperty<?>[] writableProperties) {

		this.id = id;
		this.name = name;
		this.datatype = datatype;
		this.mainProperty = mainProperty;
		this.constProperties = constProperties;
		this.writableProperties = writableProperties;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDatatype() {
		return datatype;
	}

	public WritableDeviceProperty<?> getMainProperty() {
		return mainProperty;
	}

	public ConstantDeviceProperty<?>[] getConstProperties() {
		return constProperties;
	}

	public WritableDeviceProperty<?>[] getWritableProperties() {
		return writableProperties;
	}

}