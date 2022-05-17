package org.smarthome.module;

import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.json.HubMessageMapper;
import org.smarthome.sdk.models.json.JsonHubMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ModuleConsumer {

    private final HubMessagesHandler handler;

    public ModuleConsumer(HubMessagesHandler messagesHandler) {
        this.handler = messagesHandler;
    }

    @KafkaListener(topics = "#{topics.get()}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(
            @Payload JsonHubMessage jsonHubMessage,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
            @Header("hub-id") String hub) {
        handleMessage(HubMessageMapper.getMessage(jsonHubMessage), ts, hub);
    }

    private void handleMessage(HubMessage<?> message, long timestamp, String hubId){

        var date = new Date(timestamp);
        switch (message.getAction()){
            case HUB_OFF -> handler.onHubOff((HubMessage<String>) message, date, hubId);
            case HUB_START -> handler.onHubStart((HubMessage<String>)message, date, hubId);
            case HEART_BEAT -> handler.onHeartBeat((HubMessage<String>)message, date, hubId);
            case HUB_MESSAGE -> handler.onHubMessage((HubMessage<String>)message, date, hubId);
            case DEVICES_CONNECTED -> handler.onDevicesConnected((HubMessage<List<DeviceData>>)message, date, hubId);
            case DEVICES_DISCONNECTED -> handler.onDevicesDisconnected((HubMessage<List<DeviceData>>)message, date, hubId);
            case DEVICE_MESSAGE -> handler.onDeviceMessage((HubMessage<List<DeviceData>>)message, date, hubId);
        }
    }

}
