package org.smarthome.sdk.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.smarthome.sdk.models.constraints.ConstraintDTO;

public class DeviceProperty {

    /**
     * Property name
     */
    private final String name;

    /**
     * Unit of measure for a specific type of data
     */
    private final String unit;

    /**
     * Additional description to the property
     */
    private final String description;

    /**
     * Property constraint
     */
    private final ConstraintDTO constraint;

    /**
     * Property value
     */
    private final String value;


    /**
     * @param name property name
     * @param unit unit of measure for a specific type of data
     * @param description additional description to the property
     * @param constraint property constraint
     * @param value property value
     */
    @JsonCreator
    public DeviceProperty(
            @JsonProperty("name") String name,
            @JsonProperty("unit") String unit,
            @JsonProperty("description") String description,
            @JsonProperty("constraint") ConstraintDTO constraint,
            @JsonProperty("value") String value) {
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.constraint = constraint;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public ConstraintDTO getConstraint() {
        return constraint;
    }

}
