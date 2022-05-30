package org.smarthome.sdk.hub.device;

import org.smarthome.sdk.hub.producer.DeviceCallback;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.DeviceType;

/**
 * @author Al-Sah
 */
public abstract class Device {

	/**
	 * Const uuid / device serial number
	 */
	protected final String id;

	/**
	 * Device type (sensor or actuator)
	 */
	protected final DeviceType type;

	/**
	 * Name of device provided by manufacturer
	 */
	protected final String name;

	/**
	 * Device components
	 */
	protected final DeviceComponent[] components;

	/**
	 * Functions to send information
	 */
	protected final DeviceCallback callback;

	/**
	 * @param id const uuid / device serial number
	 * @param type device type (sensor or actuator)
	 * @param name name of device provided by manufacturer
	 * @param components device components
	 * @param callback functions to send information
	 */
	public Device(String id, DeviceType type, String name, DeviceComponent[] components, DeviceCallback callback) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.components = components;
		this.callback = callback;
	}

	/**
	 * @param command received command that will be executed
	 */
	public abstract void execute(Command command);

	/**
	 * Stop receiving data from device. Use it to safely close connection between hub and device
	 */
	public abstract void stop();


	public String getId() {
		return id;
	}

	public DeviceType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public DeviceComponent[] getComponents() {
		return components;
	}
}