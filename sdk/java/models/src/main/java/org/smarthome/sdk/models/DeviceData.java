package org.smarthome.sdk.models;

/**
 * Model {@code DeviceData} is used to describe each variation of data that hub can produce.
 *
 * @see HubMessage
 * @see HubMessage.Action
 * @author  Al-Sah
 */
public class DeviceData {

    private String id;
    private String type;
    private String name;
    private String data;

    /**
     * Use this constructor to describe {@link HubMessage.Action} actions:
     *  <ul>
     *      <li>DEVICES_CONNECTED</li>
     *      <li>HUB_START</li>
     *  </ul>
     *
     *  @param id
     *      <p>Case <b>DEVICES_CONNECTED</b>: device (sensor or actuator) uuid</p>
     *      <p>Case <b>HUB_START</b>:  Hub uuid </p>
     *  @param type
     *      <p>Case <b>DEVICES_CONNECTED</b>: name of device given by manufacturer</p>
     *      <p>Case <b>HUB_START</b>: hub type (application, microcontroller, smart-device-name .....)</p>
     *  @param name
     *      <p>Case <b>DEVICES_CONNECTED</b>: custom name to describe device</p>
     *      <p>Case <b>HUB_START</b>: custom name to describe hub</p>
     */
    public DeviceData(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    /**
     * Use this constructor to describe {@link HubMessage.Action} actions:
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
    public DeviceData(String id, String data){
        this.id = id;
        this.data = data;
    }


    /**
     * Default constructor, DO NOT USE IT !!!!
     * Used by jackson (json Serialization/Deserialization)
     */
    public DeviceData(){}


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
