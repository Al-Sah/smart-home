package org.smarthome.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.module.consumer.HubMessagesHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MessagesHandler implements HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessagesHandler.class);
    @Override
    public void onHubStart(HubMessage<String> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onHubOff(HubMessage<String> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onHeartBeat(HubMessage<String> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onHubMessage(HubMessage<String> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onDeviceMessage(HubMessage<List<DeviceData>> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onDevicesConnected(HubMessage<List<DeviceData>> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }

    @Override
    public void onDevicesDisconnected(HubMessage<List<DeviceData>> message, Date date, String hub) {
        logger.info(String.format(" %s, %s, %s ", message.toString(), date.toString(), hub));
    }
}
