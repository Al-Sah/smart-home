package org.smarthome.hub.example;

import java.util.Random;

public class SensorImitator {

    private final String id;
    private final String type;

    private final String name;

    private final static Random random = new Random();

    public SensorImitator(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }


    public String getData() {
        return String.format("{\"value\": %d}", random.nextInt(50) -25 );
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }


}
