package org.smarthome.sdk.models;

/**
 * Model {@code DeviceData} is used to describe device message.
 * When actuator or sensor produces some data it must be described by {@code DeviceData}
 *
 * @see HubMessage
 * @see MessageAction
 * @author  Al-Sah
 */
public class DeviceData {

    private String id;
    private String type;
    private String name;
    private String data;

    /**
     * Use this constructor to describe {@link MessageAction}.DEVICES_CONNECTED action
     *  @param id device (sensor or actuator) uuid
     *  @param type name of device given by manufacturer
     *  @param name custom name to describe device
     *  @param data additional field where device configuration/properties or something else can be described
     */
    public DeviceData(String id, String type, String name, String data) { // TODO validation ??
        this.id = id;
        this.type = type;
        this.name = name;
        this.data = data;
    }

    /**
     * Use this constructor to describe {@link MessageAction} actions:
     *  <ul>
     *      <li>DEVICES_DISCONNECTED</li>
     *      <li>DEVICE_MESSAGE</li>
     *  </ul>
     *
     *  @param id device uuid
     *  @param data
     *      <p>Case <b>DEVICES_DISCONNECTED</b>: reason </p>
     *      <p>Case <b>DEVICE_MESSAGE</b>: device message </p>
     */
    public DeviceData(String id, String data){ // TODO validation ??
        this.id = id;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("DeviceData{id='%s'",id));
        if(type != null){
            sb.append(String.format(", type='%s'",type));
        }
        if(name != null){
            sb.append(String.format(", name='%s'",name));
        }
        if(data != null){
            sb.append(String.format(", data='%s'",data));
        }
        sb.append('}');
        return sb.toString();
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
