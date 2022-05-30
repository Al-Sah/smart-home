package org.smarthome.sdk.hub.device.constraints;

import org.smarthome.sdk.hub.device.PropertyConstraint;

/**
 * @author Al-Sah
 */
public class EnumValuesConstraint<T> implements PropertyConstraint {

	/**
	 * Sequence of acceptable values
	 */
	private final T[] values;

	/**
	 * @param values sequence of acceptable values
	 */
	public EnumValuesConstraint(T[] values) {
		this.values = values;
	}


	public T[] getValues() {
		return values;
	}

	@Override
	public String getType() {
		return "enum";
	}

	@Override
	public Boolean isValid(Object value) {
		return null;
	}
}