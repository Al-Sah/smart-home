package org.smarthome.sdk.module.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;
import org.smarthome.sdk.module.consumer.impl.DefaultHubMessagesHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;

@Component
public class ModuleConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ModuleConsumer.class);

    /**
     * Default handler {@link DefaultHubMessagesHandler} can be used
     */
    private final HubMessagesHandler handler;

    public ModuleConsumer(HubMessagesHandler messagesHandler) {
        this.handler = messagesHandler;
    }

    /**
     * Listen messages from topic
     * @param message HubMessage
     * @param ts producer record creation timestamp
     */
    @KafkaListener(topics = "#{listenerTopics.get()}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload HubMessage<?> message, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

        var action = DeserializationUtils.getMessageAction(message.getAction());
        if(action == null){
            logger.error("Received undefined hub-message; action: " + message.getAction());
            return;
        }

        handleMessage(action, message, new Date(ts));
    }

    /**
     * Send received messages to the handler
     * @param message HubMessage
     * @param date producer record creation time
     */
    @SuppressWarnings("unchecked cast")
    private void handleMessage(MessageAction action, HubMessage<?> message, Date date){
        try {
            switch (action){
                case HUB_OFF -> handler.onHubOff(
                        new HubMessage<>(
                                message.getHub(),
                                message.getAction(),
                                DeserializationUtils.getHubShutdownDetails((LinkedHashMap<String, Object>) message.getData())),
                        date
                );
                case HUB_START -> handler.onHubStart(
                        new HubMessage<>(
                                message.getHub(),
                                message.getAction(),
                                DeserializationUtils.getHubProperties((LinkedHashMap<String, Object>) message.getData())),
                        date
                );
                case HEART_BEAT -> handler.onHeartBeat((HubMessage<String>)message, date);
                case HUB_MESSAGE -> handler.onHubMessage((HubMessage<String>)message, date);
                case DEVICE_CONNECTED -> handler.onDevicesConnected(
                        new HubMessage<>(
                                message.getHub(),
                                message.getAction(),
                                DeserializationUtils.getDeviceMetadata((LinkedHashMap<String, Object>) message.getData())),
                        date
                );
                case DEVICE_DISCONNECTED -> handler.onDevicesDisconnected(
                        new HubMessage<>(
                                message.getHub(),
                                message.getAction(),
                                DeserializationUtils.getDeviceDisconnectionDetails(
                                        (LinkedHashMap<String, Object>) message.getData())
                        ),
                        date
                );
                case DEVICE_MESSAGE -> handler.onDeviceMessage(
                        new HubMessage<>(
                                message.getHub(),
                                message.getAction(),
                                DeserializationUtils.getDeviceMessage((LinkedHashMap<String, Object>) message.getData())),
                        date
                );
            }
        } catch(ClassCastException e) {
            logger.error("failed to cast message: " + e.getMessage());
        }
    }

}
