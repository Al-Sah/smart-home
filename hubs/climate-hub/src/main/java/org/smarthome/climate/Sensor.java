package org.smarthome.climate;

public abstract class Sensor {

    private final String uuid;
    private final String type;

    protected Sensor(String uuid, String type) {
        this.uuid = uuid;
        this.type = type;
    }


    public String getId() {
        return uuid;
    }


    public String getType() {
        return type;
    }

}
