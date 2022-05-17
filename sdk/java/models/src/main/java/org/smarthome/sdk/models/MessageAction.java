package org.smarthome.sdk.models;

public enum MessageAction {

    HUB_START("start"),
    HUB_OFF("off"),
    HUB_MESSAGE("hub-msg"),
    HEART_BEAT("alive"),
    DEVICE_MESSAGE("msg"),
    DEVICES_CONNECTED("devices-connected"),
    DEVICES_DISCONNECTED("devices-disconnected");

    public final String name;
    MessageAction(String name) {
        this.name = name;
    }

}
