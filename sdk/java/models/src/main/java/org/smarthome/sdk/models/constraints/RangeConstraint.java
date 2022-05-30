package org.smarthome.sdk.models.constraints;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RangeConstraint implements ConstraintDTO{


    private final String type;

    /**
     * Minimum acceptable value
     */
    private final String min;

    /**
     * Maximum acceptable value
     */
    private final String max;


    /**
     * @param type type of constraint (integer or floating point)
     * @param min string that contains minimum acceptable value
     * @param max string that contains maximum acceptable value
     */
    @JsonCreator
    public RangeConstraint(
            @JsonProperty("type") String type,
            @JsonProperty("min") String min,
            @JsonProperty("max") String max) {
        this.type = type;
        this.min = min;
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    @Override
    public String getType() {
        return type;
    }
}
