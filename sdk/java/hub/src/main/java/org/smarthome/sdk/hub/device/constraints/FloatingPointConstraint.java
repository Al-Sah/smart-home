package org.smarthome.sdk.hub.device.constraints;

import org.smarthome.sdk.hub.device.PropertyConstraint;

/**
 * @author Al-Sah
 */
public class FloatingPointConstraint implements PropertyConstraint {


	/**
	 * Minimum acceptable value
	 */
	private final double min;

	/**
	 * Maximum acceptable value
	 */
	private final double max;



	public FloatingPointConstraint(double min, double max) {
		this.min = min;
		this.max = max;
	}



	@Override
	public String getType() {
		return "range-float";
	}

	@Override
	public Boolean isValid(Object value) {
		try {
			double number = (double) value;
			return number >= min && number <= max;
		} catch (Exception e){
			return false; // TODO log ?
		}
	}


	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

}