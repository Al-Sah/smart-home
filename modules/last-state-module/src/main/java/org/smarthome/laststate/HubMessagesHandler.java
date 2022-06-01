package org.smarthome.laststate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Primary
@Service
public class HubMessagesHandler implements org.smarthome.sdk.module.consumer.HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(HubMessagesHandler.class);
    private final DataBaseManager dataBaseManager;

    public HubMessagesHandler(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    @Override
    public void onHubStart(HubMessage<HubProperties> hubMessage, Date date) {}

    @Override
    public void onHubOff(HubMessage<HubShutdownDetails> hubMessage, Date date) {}

    @Override
    public void onHeartBeat(HubMessage<String> hubMessage, Date date) {}

    @Override
    public void onHubMessage(HubMessage<String> hubMessage, Date date) {}

    @Override
    public void onDeviceMessage(HubMessage<DeviceMessage> hubMessage, Date date) {
        var msg = hubMessage.getData();
        if(msg.getError() != null){
            dataBaseManager.saveDeviceError(msg);
            // TODO send
            return;
        }

        try {
            dataBaseManager.updateDevice(hubMessage.getData());
            // TODO send
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onDevicesConnected(HubMessage<DeviceMetadata> hubMessage, Date date) {
        dataBaseManager.saveDevice(hubMessage.getData());
        // TODO send
    }

    @Override
    public void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> hubMessage, Date date) {}
}
