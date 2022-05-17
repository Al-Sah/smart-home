package org.smarthome.sdk.models.json;

import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;

import java.util.ArrayList;
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

        return new HubMessage<>(action, castJsonDeviceMessages(message.getMessages()));
    }

    public static JsonHubMessage getMessage(HubMessage<?> message) throws RuntimeException{

        if(message.getAction() == MessageAction.HEART_BEAT){
            return new JsonHubMessage(message.getAction().name, null, null);
        }
        if(message.getData().getClass() == String.class){
            return new JsonHubMessage(message.getAction().name, null, (String)message.getData());
        }

        if(message.getData() instanceof List){
            // TODO fix warning Unchecked cast: 'capture<?>' to 'java.util.List<org.smarthome.sdk.models.DeviceData>'
            return new JsonHubMessage(message.getAction().name, castDeviceMessages((List<DeviceData>)message.getData()), null);
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

    private static List<JsonDeviceData> castDeviceMessages(List<DeviceData> messages){
        ArrayList<JsonDeviceData> result = new ArrayList<>(messages.size());
        for (DeviceData message : messages) {
            result.add(new JsonDeviceData(message));
        }
        return result;
    }

    private static List<DeviceData> castJsonDeviceMessages(List<JsonDeviceData> messages){
        ArrayList<DeviceData> result = new ArrayList<>(messages.size());
        for (JsonDeviceData message : messages) {
            result.add(new DeviceData(message.getId(), message.getType(),  message.getName(), message.getData()));
        }
        return result;
    }

}
