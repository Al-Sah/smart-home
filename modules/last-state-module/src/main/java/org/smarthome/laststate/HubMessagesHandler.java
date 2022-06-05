package org.smarthome.laststate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.models.*;
import org.smarthome.sdk.models.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Primary
@Service
public class HubMessagesHandler implements org.smarthome.sdk.module.consumer.HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(HubMessagesHandler.class);
    private final DataBaseManager dataBaseManager;
    private final ClientWebSocketHandler clientWebSocketHandler;

    private final ConcurrentHashMap<String, HeartBeatDetails> heartbeats = new ConcurrentHashMap<>();

    public HubMessagesHandler(DataBaseManager dataBaseManager, ClientWebSocketHandler clientWebSocketHandler) {
        this.dataBaseManager = dataBaseManager;
        this.clientWebSocketHandler = clientWebSocketHandler;

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::checkHeartbeats, 500,500, TimeUnit.MICROSECONDS);
    }

    @Override
    public void onHubStart(HubMessage<HubProperties> hubMessage, Date date) {
        var res = dataBaseManager.setHubStateConnected(hubMessage.getHub());
        try {
            clientWebSocketHandler.sendMessage(
                    ModuleMessageAction.HUB_CONNECTED,
                    new HubConnected(new HubStateDetailsDTO(res), hubMessage.getData())
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

        var prop = hubMessage.getData();
        var period = TimeUnit.MILLISECONDS.convert(prop.getPeriod(), TimeUnit.valueOf(prop.getUnit()));
        heartbeats.put(hubMessage.getHub(), new HeartBeatDetails(period, date.getTime()));
    }

    @Override
    public void onHubOff(HubMessage<HubShutdownDetails> hubMessage, Date date) {
        var hub = dataBaseManager.setHubStateDisconnected(hubMessage.getHub());
        var removedDevices = dataBaseManager.removeActiveDevices(hubMessage.getHub());
        heartbeats.remove(hubMessage.getHub());
        try {
            clientWebSocketHandler.sendMessage(
                    ModuleMessageAction.HUB_DISCONNECTED,
                    new HubDisconnectedMessage(
                            hubMessage.getData(),
                            new HubStateDetailsDTO(hub),
                            removedDevices.stream().map(DeviceStateDetailsDTO::new).collect(Collectors.toList())
                    )
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onHeartBeat(HubMessage<HubHeartBeatData> hubMessage, Date date) {
        var hb = heartbeats.get(hubMessage.getHub());
        if(hb == null){
            logger.error("failed to handle heartbeat; hub with id {} not found", hubMessage.getHub());
            return;
        }

        if(hb.getLastHeatBeat() > hb.getNextHeatBeat()){
            var res = dataBaseManager.updateActiveDevices(
                    hubMessage.getHub(),
                    hubMessage.getData().getActive()
            );
            clientWebSocketHandler.sendMessage(ModuleMessageAction.HUB_RECONNECTED, res);
        }
        hb.moveToNextPeriod(date.getTime());
        heartbeats.replace(hubMessage.getHub(), hb);
    }

    @Override
    public void onHubMessage(HubMessage<String> hubMessage, Date date) {
        var res = dataBaseManager.updateHubState(hubMessage.getData(), hubMessage.getHub());
        try {
            clientWebSocketHandler.sendMessage(ModuleMessageAction.HUB_MESSAGE, new HubStateDetailsDTO(res));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

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
                return;
            }
        }

        try {
            clientWebSocketHandler.sendMessage(
                    ModuleMessageAction.DEVICE_MESSAGE,
                    new DeviceDataMessage(msg, new DeviceStateDetailsDTO(state))
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onDevicesConnected(HubMessage<DeviceMetadata> hubMessage, Date date) {
        try {
            var res = dataBaseManager.saveDevice(hubMessage.getData(), hubMessage.getHub());
            clientWebSocketHandler.sendMessage(
                    ModuleMessageAction.DEVICE_CONNECTED,
                    new DeviceConnectedMessage(hubMessage.getData(), new DeviceStateDetailsDTO(res))
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onDevicesDisconnected(HubMessage<DeviceDisconnectionDetails> hubMessage, Date date) {
        try {
            var res = dataBaseManager.updateDeviceState(hubMessage.getData());
            clientWebSocketHandler.sendMessage(
                    ModuleMessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectedMessage(
                            hubMessage.getData(),
                            new DeviceStateDetailsDTO(res)
                    )
            );
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

    }


    private void checkHeartbeats(){

        for (ConcurrentMap.Entry<String, HeartBeatDetails> entry: heartbeats.entrySet()) {
            var hb = entry.getValue();
            var id = entry.getKey();
            if(hb.getLastHeatBeat() > hb.getNextHeatBeat()){
                var hub= dataBaseManager.setHubStateLost(id);
                var details = dataBaseManager.removeActiveDevices(id)
                        .stream().map(DeviceStateDetailsDTO::new).collect(Collectors.toList());
                clientWebSocketHandler.sendMessage(ModuleMessageAction.HUB_LOST, new HubLostMessage(hub, details));
            }
        }
    }
}
