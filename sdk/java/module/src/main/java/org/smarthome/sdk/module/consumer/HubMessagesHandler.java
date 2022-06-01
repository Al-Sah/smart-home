package org.smarthome.sdk.module.consumer;

import org.smarthome.sdk.models.*;

import java.util.Date;

public interface HubMessagesHandler {

    void onHubStart(HubMessage<HubProperties> message, Date timestamp);
    void onHubOff(HubMessage<HubShutdownDetails> message, Date timestamp);
    void onHeartBeat(HubMessage<HubHeartBeatData> message, Date timestamp);
    void onHubMessage(HubMessage<String> message, Date timestamp);
    void onDeviceMessage(HubMessage<DeviceMessage> message, Date timestamp);
    void onDevicesConnected(HubMessage<DeviceMetadata> message, Date timestamp);
    void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> message, Date timestamp);
}
