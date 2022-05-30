package org.smarthome.sdk.hub.device.constraints;

import org.smarthome.sdk.hub.device.PropertyConstraint;

/**
 * @author Al-Sah
 */
public class IntegerNumberConstraint implements PropertyConstraint {

    /**
     * Minimum acceptable value
     */
    private final long min;

    /**
     * Maximum acceptable value
     */
    private final long max;

    /**
     * Default constructor
     */
    public IntegerNumberConstraint(long min, long max) {
        this.min = min;
        this.max = max;
    }



    @Override
    public String getType() {
        return "range-long";
    }

    @Override
    public Boolean isValid(Object value) {
        try {
            long number = (long) value;
            return number >= min && number <= max;
        } catch (Exception e){
            return false; // TODO log ?
        }
    }


    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

}
