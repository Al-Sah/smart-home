package org.smarthome.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

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

    /**
     * action = Action.name
     */
    private String action;

    private List<DeviceData> data;

    public enum Action {
        HUB_START("start"),
        HUB_OFF("off"),
        HUB_MESSAGE("hub-msg"),
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
        if( !(action == Action.HEART_BEAT || action == Action.HUB_OFF) && (data == null || data.isEmpty())){ // TODO validation regex ??
            throw new InvalidModelParams("filed 'data' is empty or not set");
        }

        this.action = action.name;
        this.data = data;
    }


    /**
     * Default constructor, DO NOT USE IT !!!!
     * Used by jackson (json Serialization/Deserialization)
     */
    public HubMessage() {}


    public void setAction(Action action) {
        this.action = action.name;
    }
    public void setData(List<DeviceData> data) {
        this.data = data;
    }
    public List<DeviceData> getData() {
        return data;
    }

    public String getAction() {
        return this.action;
    }
    @JsonIgnore
    public Action getActionAsEnum() {
        for (Action action : Action.values()) {
            if (Objects.equals(action.name, this.action)) {
                return action;
            }
        }
        throw new InvalidModelParams("Undefined action");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("HubMessage{action=='%s'",getAction()));
        if(data != null){
            sb.append(String.format(", data='%s'",data));
        }
        sb.append('}');
        return sb.toString();
    }
}