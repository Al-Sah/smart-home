package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Al-Sah
 */
public class ComponentMetadata {

	/**
	 * Component id
	 */
	private final String id;

	/**
	 * Component name
	 */
	private final String name;

	/**
	 * Datatype that main property produce
	 */
	private final String datatype;

	/**
	 * Data that this component sends
	 */
	private final DeviceProperty mainProperty;

	/**
	 * Component properties that cannot be changed
	 */
	private final DeviceProperty[] constProperties;

	/**
	 * Component properties that can be changed by user
	 */
	private final DeviceProperty[] writableProperties;

	/**
	 * @param id component id
	 * @param name component name
	 * @param datatype datatype that main property produce
	 * @param mainProperty common data that this component sends
	 * @param constProperties component properties that cannot be changed
	 * @param writableProperties component properties that can be changed by user
	 */
	@JsonCreator
	public ComponentMetadata(
			@JsonProperty("id") String id,
			@JsonProperty("name") String name,
			@JsonProperty("datatype") String datatype,
			@JsonProperty("mainProperty") DeviceProperty mainProperty,
			@JsonProperty("constProperties") DeviceProperty[]  constProperties,
			@JsonProperty("writableProperties") DeviceProperty[]  writableProperties) {

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

	public DeviceProperty getMainProperty() {
		return mainProperty;
	}

	public DeviceProperty[]  getConstProperties() {
		return constProperties;
	}

	public DeviceProperty[]  getWritableProperties() {
		return writableProperties;
	}
}