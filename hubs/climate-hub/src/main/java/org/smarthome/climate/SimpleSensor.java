package org.smarthome.climate;

public class SimpleSensor<D> extends Sensor {

    private final String datatype;
    private D data;

    public SimpleSensor(String uuid, String type, String datatype) {
        super(uuid, type);
        this.data = null;
        this.datatype = datatype;
    }


    public String getDatatype() {
        return datatype;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }


}
