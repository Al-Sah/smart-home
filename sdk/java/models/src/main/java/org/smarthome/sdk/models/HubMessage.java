package org.smarthome.sdk.models;

import java.util.List;

/**
 * Model {@code HubMessage} is used to describe messages hub can produce
 * according to the {@link HubMessage.Action}.
 * <br/>
 * Enum {@code HubMessage.Action} contains all possible cases to send message
 *
 * @see DeviceData
 * @author  Al-Sah
 */
public class HubMessage {

    private Action action;

    private List<DeviceData> data;

    public enum Action {
        HUB_START("start"),
        HUB_OFF("off"),
        HEART_BEAT("alive"),
        DEVICE_MESSAGE("msg"),
        DEVICES_CONNECTED("devices-connected"),
        DEVICES_DISCONNECTED("devices-disconnected");

        public final String name;
        Action(String name) {
            this.name = name;
        }
    }


    /**
     * Creates a message to be sent
     *
     * @param action describes purpose of sending message
     * @param data is used to provide all information according to the specified action
     *
     * @throws InvalidModelParams if action was not set
     */

    public HubMessage(Action action, List<DeviceData> data) throws InvalidModelParams {

        if(action == null){
            throw new InvalidModelParams("filed 'action' is not set");
        }
        if( !(action == Action.HEART_BEAT || action == Action.HUB_OFF) && (data == null || data.isEmpty())){
            throw new InvalidModelParams("filed 'data' is empty or not set");
        }

        this.action = action;
        this.data = data;
    }


    /**
     * Default constructor, DO NOT USE IT !!!!
     * Used by jackson (json Serialization/Deserialization)
     */
    public HubMessage() {}


    public void setAction(Action action) {
        this.action = action;
    }
    public void setData(List<DeviceData> data) {
        this.data = data;
    }
    public List<DeviceData> getData() {
        return data;
    }
    public Action getAction() {
        return action;
    }
}