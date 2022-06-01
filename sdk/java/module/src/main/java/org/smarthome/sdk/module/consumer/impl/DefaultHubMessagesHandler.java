package org.smarthome.sdk.module.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.*;
import org.smarthome.sdk.module.consumer.HubMessagesHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DefaultHubMessagesHandler implements HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHubMessagesHandler.class);

    @Override
    public void onHubStart(HubMessage<HubProperties> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onHubOff(HubMessage<HubShutdownDetails> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onHeartBeat(HubMessage<HubHeartBeatData> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onHubMessage(HubMessage<String> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onDeviceMessage(HubMessage<DeviceMessage> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onDevicesConnected(HubMessage<DeviceMetadata> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }

    @Override
    public void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> message, Date timestamp) {
        logger.info(message.toString() + " | " + timestamp.toString());
    }
}
