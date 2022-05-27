package org.smarthome.sdk.models;

/**
 * @author Al-Sah
 * @see HubMessage
 */
public enum MessageAction {

    HUB_START("on"),
    HUB_OFF("off"),
    HUB_MESSAGE("hub-msg"),
    HEART_BEAT("hb"),
    DEVICE_MESSAGE("d-msg"),
    DEVICE_CONNECTED("dc"),
    DEVICE_DISCONNECTED("ddc");

    public final String name;
    MessageAction(String name) {
        this.name = name;
    }

}
