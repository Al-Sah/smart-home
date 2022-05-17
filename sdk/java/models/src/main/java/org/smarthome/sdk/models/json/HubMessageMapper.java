package org.smarthome.sdk.models.json;

import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;

import java.util.List;
import java.util.Objects;


public class HubMessageMapper {

    public static HubMessage<?> getMessage(JsonHubMessage message) throws RuntimeException{

        MessageAction action = castAction(message.getAction());

        if(message.getMessages() != null && message.getData() != null){
            throw new RuntimeException("Cannot create HubMessage. 2 Data types are available");
        }

        if (action == MessageAction.HEART_BEAT || message.getData() != null) {
            return new HubMessage<>(action, message.getData());
        }

        return new HubMessage<>(action, message.getMessages());
    }

    public static JsonHubMessage getMessage(HubMessage<?> message) throws RuntimeException{

        if(message.getAction() == MessageAction.HEART_BEAT){
            return new JsonHubMessage(message.getAction().name, (String)null);
        }
        if(message.getData().getClass() == String.class){
            return new JsonHubMessage(message.getAction().name, (String)message.getData());
        }

        if(message.getData() instanceof List){
            // TODO fix warning Unchecked cast: 'capture<?>' to 'java.util.List<org.smarthome.sdk.models.DeviceData>'
            return new JsonHubMessage(message.getAction().name, (List<DeviceData>) message.getData());
        }

        throw new RuntimeException("Failed to recognize HubMessage data type: " + message.getData().getClass());
    }

    private static MessageAction castAction(String action){
        for (MessageAction value : MessageAction.values()) {
            if (Objects.equals(value.name, action)) {
                return value;
            }
        }
        throw new RuntimeException("Failed to recognize message action");
    }
}
