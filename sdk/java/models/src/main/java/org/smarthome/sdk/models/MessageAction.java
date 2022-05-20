package org.smarthome.sdk.models;

public enum MessageAction {

    HUB_START("start"), //on
    HUB_OFF("off"), //off
    HUB_MESSAGE("hub-msg"), // hub-msg
    HEART_BEAT("alive"), // hb
    DEVICE_MESSAGE("msg"), // d-msg
    DEVICES_CONNECTED("devices-connected"), // dc
    DEVICES_DISCONNECTED("devices-disconnected"); // dc

    public final String name;
    MessageAction(String name) {
        this.name = name;
    }

}
