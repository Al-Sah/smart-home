package org.smarthome.sdk.hub.device;

/**
 * @author Al-Sah
 */
public interface PropertyConstraint {

	/**
	 * @return type of constraint
	 */
	String getType();

	/**
	 * Check if property value valid according to the specific realization
	 * @param value value to check
	 * @return true if value is valid, otherwise false
	 */
	Boolean isValid(Object value);
}