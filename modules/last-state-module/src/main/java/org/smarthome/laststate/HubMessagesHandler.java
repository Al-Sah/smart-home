package org.smarthome.laststate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.laststate.entities.DeviceStateDetails;
import org.smarthome.laststate.models.*;
import org.smarthome.sdk.models.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Primary
@Service
public class HubMessagesHandler implements org.smarthome.sdk.module.consumer.HubMessagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(HubMessagesHandler.class);

    private final Integer HEART_BEAT_MAX_LATENCY = 2000; // 2 seconds in milliseconds
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
            clientWebSocketHandler.sendMessage(new HubStateDetailsDTO(res));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        var prop = hubMessage.getData();
        var period = TimeUnit.MILLISECONDS.convert(prop.getPeriod(), TimeUnit.valueOf(prop.getUnit()));
        var nextHB = System.currentTimeMillis() + period + HEART_BEAT_MAX_LATENCY;
        heartbeats.put(hubMessage.getHub(), new HeartBeatDetails(period, System.currentTimeMillis(), nextHB));
    }

    @Override
    public void onHubOff(HubMessage<HubShutdownDetails> hubMessage, Date date) {
        var hub = dataBaseManager.setHubStateDisconnected(hubMessage.getHub());
        var details = dataBaseManager.removeActiveDevices(hubMessage.getHub());
        heartbeats.remove(hubMessage.getHub());
        try {
            clientWebSocketHandler.sendMessage(
                    new HubDisconnectedMessage(hubMessage.getData(), new HubStateDetailsDTO(hub), details)
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
            clientWebSocketHandler.sendMessage(res); //TODO
        }

        hb.setLastHeatBeat(date.getTime());
        hb.setNextHeatBeat(System.currentTimeMillis() + hb.getHeatBeatPeriod() + HEART_BEAT_MAX_LATENCY);
        heartbeats.replace(hubMessage.getHub(), hb);


    }

    @Override
    public void onHubMessage(HubMessage<String> hubMessage, Date date) {
        var res = dataBaseManager.updateHubState(hubMessage.getData(), hubMessage.getHub());
        try {
            clientWebSocketHandler.sendMessage(new HubStateDetailsDTO(res));
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


    private void checkHeartbeats(){
        List<DeviceStateDetails> devices = new ArrayList<>();
        for (ConcurrentMap.Entry<String, HeartBeatDetails> entry: heartbeats.entrySet()) {
            var hb = entry.getValue();
            if(hb.getLastHeatBeat() > hb.getNextHeatBeat()){
                devices.addAll(dataBaseManager.removeActiveDevices(entry.getKey()));
            }
        }
        if(!devices.isEmpty()){
            clientWebSocketHandler.sendMessage(devices);
        }
    }
}
