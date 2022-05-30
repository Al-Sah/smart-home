package org.smarthome.sdk.module.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smarthome.sdk.models.*;
import org.smarthome.sdk.models.constraints.EnumConstraint;
import org.smarthome.sdk.models.constraints.RangeConstraint;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

@SuppressWarnings("unchecked cast")
public class DeserializationUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static MessageAction getMessageAction(String alias){
        for (MessageAction value : MessageAction.values()) {
            if (Objects.equals(value.name, alias)) {
                return value;
            }
        }
        return null;
    }

    public static HubShutdownDetails getHubShutdownDetails(LinkedHashMap<String, Object> map){
        return mapper.convertValue(map, new TypeReference<>() {});
    }


    public static HubProperties getHubProperties(LinkedHashMap<String, Object> map){
        return mapper.convertValue(map, new TypeReference<>() {});
    }

    public static DeviceMetadata getDeviceMetadata(LinkedHashMap<String, Object> map){

        var res = mapper.convertValue(map, new TypeReference<DeviceMetadata>() {});

        for (var component: res.getComponents()) {
            updateProperty(component.getMainProperty());
            if(component.getConstProperties() != null){
                for (var constProperty : component.getConstProperties()) {
                    updateProperty(constProperty);
                }
            }
            if(component.getWritableProperties() != null){
                for (var constProperty : component.getWritableProperties()) {
                    updateProperty(constProperty);
                }
            }
        }
        return res;
    }

    private static void updateProperty(DeviceProperty property){
        if(property == null || property.getConstraint() == null){
            return;
        }

        var type = (String)((LinkedHashMap<String, Object>)property.getConstraint()).get("type");

        if (type.contains("enum")) {
            property.setConstraint(mapper.convertValue(
                    property.getConstraint(), new TypeReference<EnumConstraint>() {})
            );
        } else {
            property.setConstraint(mapper.convertValue(
                    property.getConstraint(), new TypeReference<RangeConstraint>() {})
            );
        }
    }

    public static DeviceMessage getDeviceMessage(LinkedHashMap<String, Object> map){
        return mapper.convertValue(map, new TypeReference<>() {});
    }



    public static DeviceDisconnectionDetails getDeviceDisconnectionDetails(LinkedHashMap<String, Object> map){
        return mapper.convertValue(map, new TypeReference<>() {});
    }
}
