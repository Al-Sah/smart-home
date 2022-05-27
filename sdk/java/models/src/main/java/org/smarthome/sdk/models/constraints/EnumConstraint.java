package org.smarthome.sdk.models.constraints;

import java.util.Set;

/**
 * @author Al-Sah
 */
public class EnumConstraint extends ConstraintDTO {

	/**
	 * Sequence of acceptable values
	 */
	private final String[] values;


	/**
	 * @param type type of constraint (enum)
     * @param values sequence of acceptable values
	 */
	public EnumConstraint(String type, String[] values) {
		super(type);
		this.values = values;
	}


	public String[] getValues() {
		return values;
	}
}