package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Al-Sah
 */
public class DeviceMetadata {

	/**
	 * Const uuid / device serial number
	 */
	private final String id;

	/**
	 * Device type (sensor or actuator)
	 */
	private final DeviceType type;

	/**
	 * Name of device provided by manufacturer
	 */
	private final String name;

	/**
	 * Device components
	 */
	private final ComponentMetadata[] components;

	/**
	 *
	 * @param id const uuid / device serial number
	 * @param type device type (sensor or actuator)
	 * @param name device name provided by manufacturer
	 * @param components list of component
	 */
	@JsonCreator
	public DeviceMetadata(
			@JsonProperty("id") String id,
			@JsonProperty("type") DeviceType type,
			@JsonProperty("name") String name,
			@JsonProperty("components") ComponentMetadata[] components) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.components = components;
	}


	public String getId() {
		return id;
	}

	public DeviceType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ComponentMetadata[] getComponents() {
		return components;
	}

}