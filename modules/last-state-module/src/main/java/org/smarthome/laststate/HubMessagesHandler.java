package org.smarthome.laststate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.models.DeviceConnectedMessage;
import org.smarthome.laststate.models.DeviceDataMessage;
import org.smarthome.laststate.models.DeviceDisconnectedMessage;
import org.smarthome.sdk.models.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Primary
@Service
public class HubMessagesHandler implements org.smarthome.sdk.module.consumer.HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(HubMessagesHandler.class);
    private final DataBaseManager dataBaseManager;

    private final ClientWebSocketHandler clientWebSocketHandler;

    public HubMessagesHandler(DataBaseManager dataBaseManager, ClientWebSocketHandler clientWebSocketHandler) {
        this.dataBaseManager = dataBaseManager;
        this.clientWebSocketHandler = clientWebSocketHandler;
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
        DeviceStateDetails state = null;
        if(msg.getError() != null){
            state = dataBaseManager.saveDeviceError(msg);
        }else{
            try {
                state = dataBaseManager.updateDevice(hubMessage.getData());
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
            }
        }

        try {
            clientWebSocketHandler.sendMessage(new DeviceDataMessage(msg, state));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onDevicesConnected(HubMessage<DeviceMetadata> hubMessage, Date date) {
        try {
            var res = dataBaseManager.saveDevice(hubMessage.getData(), hubMessage.getHub());
            clientWebSocketHandler.sendMessage(new DeviceConnectedMessage(hubMessage.getData(), res));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> hubMessage, Date date) {
        try {
            clientWebSocketHandler.sendMessage(
                    new DeviceDisconnectedMessage(
                            hubMessage.getData(),
                            dataBaseManager.updateDeviceState(hubMessage.getData())
                    )
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

    }
}
