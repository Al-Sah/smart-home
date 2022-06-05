package org.smarthome.laststate.models;

/**
 * {@code ModuleMessageAction} contains all possible events to be used in {@link ModuleMessage}
 *
 * @author Al-Sah
 * @see org.smarthome.laststate.models.ModuleMessage
 */
public enum ModuleMessageAction {

    START,
    DEVICE_CONNECTED,
    DEVICE_MESSAGE,
    DEVICE_DISCONNECTED,

    HUB_CONNECTED,
    HUB_DISCONNECTED,
    HUB_MESSAGE,

    HUB_LOST,
    HUB_RECONNECTED,

}
