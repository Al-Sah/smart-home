package org.smarthome.laststate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.*;
import org.smarthome.sdk.module.consumer.HubMessagesHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Primary
@Service
public class HubStateMessagesHandler implements HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(HubStateMessagesHandler.class);
    private final DevicesRepository devicesRepository;

    public HubStateMessagesHandler(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
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
            return;
        }
        var id = hubMessage.getData().getDevice();
        var device =  devicesRepository.findById(id).orElse(null);

        if(device == null){
            logger.error(String.format("Cannot find device '%s' !!!!", id));
        }
    }

    @Override
    public void onDevicesConnected(HubMessage<DeviceMetadata> hubMessage, Date date) {
        devicesRepository.save(hubMessage.getData());
    }

    @Override
    public void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> hubMessage, Date date) {}
}
