package org.smarthome.sdk.module;

import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;

import java.util.Date;
import java.util.List;

public interface HubMessagesHandler {

    void onHubStart(HubMessage<String> message, Date timestamp, String hub);
    void onHubOff(HubMessage<String> message, Date timestamp, String hub);
    void onHeartBeat(HubMessage<String> message, Date timestamp, String hub);
    void onHubMessage(HubMessage<String> message, Date timestamp, String hub);
    void onDeviceMessage(HubMessage<List<DeviceData>> message, Date timestamp, String hub);
    void onDevicesConnected(HubMessage<List<DeviceData>> message, Date timestamp, String hub);
    void onDevicesDisconnected(HubMessage<List<DeviceData>> message, Date timestamp, String hub);
}
