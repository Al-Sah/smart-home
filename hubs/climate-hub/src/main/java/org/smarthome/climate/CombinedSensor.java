package org.smarthome.climate;

import java.util.ArrayList;

public class CombinedSensor extends Sensor {

    private final ArrayList<SimpleSensor<?>> components;

    private String[] active;

    public CombinedSensor(String uuid, String type,  ArrayList<SimpleSensor<?>> components) {
        super(uuid, type);
        this.components = components;
    }

    public ArrayList<SimpleSensor<?>> getComponents(){
        return components;
    }


    public String[] getActive() {
        return active;
    }


    public void setActive(String[] active) {
        this.active = active;
    }

}
