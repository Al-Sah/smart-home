package org.smarthome.laststate.models;

import lombok.NonNull;
import lombok.Value;

/**
 * {@code ModuleMessage} is an object that is used to notify the client of
 * this server (last-state module) about any changes of any device in
 * the smart-home system.
 *
 *
 * @author Al-Sah
 * @see org.smarthome.laststate.models.ModuleMessageAction
 */
@Value public class ModuleMessage<T> {

    /**
     * Message action (event)
     */
    @NonNull ModuleMessageAction action;

    /**
     * Each action has a corresponding DTO
     *
     * Here is all cases: (presented as a pair of 'message action - data type' )
     * <ol>
     *     <li> START: StartMessage </li>
     *
     *     <li> DEVICE_CONNECTED: DeviceConnectedMessage </li>
     *     <li> DEVICE_MESSAGE: DeviceDataMessage </li>
     *     <li> DEVICE_DISCONNECTED: DeviceDisconnectedMessage </li>
     *
     *     <li> HUB_CONNECTED: HubConnectedMessage </li>
     *     <li> HUB_MESSAGE: HubStateDTO </li>
     *     <li> HUB_DISCONNECTED: HubDisconnectedMessage </li>
     *
     *     <li> HUB_LOST: HubLostMessage </li>
     *     <li> HUB_RECONNECTED: ???? </li>
     * </ol>
     */
    @NonNull T data;
}
