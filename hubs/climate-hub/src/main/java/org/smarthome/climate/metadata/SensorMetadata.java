package org.smarthome.climate.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class SensorMetadata {

    private final String type;

    public SensorMetadata(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
