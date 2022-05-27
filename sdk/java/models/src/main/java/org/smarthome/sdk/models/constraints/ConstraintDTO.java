package org.smarthome.sdk.models.constraints;

public abstract class ConstraintDTO {

    /**
     * Constraint type
     */
    private final String type;

    protected ConstraintDTO(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
