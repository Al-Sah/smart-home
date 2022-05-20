package org.smarthome.climate;

public class SwitchableSensor<D> extends Sensor {

    private final String[] dataTypes;
    private String active;
    private D data;



    public SwitchableSensor(String uuid, String type, String[] dataTypes, String setup) {
        super(uuid, type);
        this.dataTypes = dataTypes;
        this.active = setup;
    }


    public String[] getDataTypes() {
        return dataTypes;
    }


    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }



    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
