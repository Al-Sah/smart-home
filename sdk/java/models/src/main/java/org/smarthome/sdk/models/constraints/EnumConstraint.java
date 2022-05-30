package org.smarthome.sdk.models.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Al-Sah
 */
public class EnumConstraint implements ConstraintDTO{


	private final String type;

	/**
	 * Sequence of acceptable values
	 */
	private final String[] values;


	/**
	 * @param type type of constraint (enum)
     * @param values sequence of acceptable values
	 */
	@JsonCreator
	public EnumConstraint(
			@JsonProperty("type") String type,
			@JsonProperty("values") String[] values) {
		this.type = type;
		this.values = values;
	}


	public String[] getValues() {
		return values;
	}

	@Override
	public String getType() {
		return type;
	}

}